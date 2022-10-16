package com.spoof.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Model.Post;
import com.spoof.lm.PostDetail;
import com.spoof.lm.R;
import com.spoof.lm.StartActivity;

import java.util.List;

public class MyphotoAdapter extends RecyclerView.Adapter<MyphotoAdapter.ViewHolder> {
    private Context mContext;
    private List<Post> Posts;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    public MyphotoAdapter(Context mContext, List<Post> posts) {
        this.mContext = mContext;
        Posts = posts;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.myphoto_iteam,parent,false);
        return new MyphotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
              Post post=Posts.get(position);
              Glide.with(mContext).load(post.getPostImage()).into(holder.imageView);
              goPostdetail(holder.imageView, post.getPostId());
              deletePost(post.getPostId(), post.getPublisher(), holder.imageView);
    }

    private void goPostdetail(ImageView holder,String postid)
    {
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(mContext, PostDetail.class);
               intent.putExtra("postid",postid);
               intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
               mContext.startActivity(intent);
            }
        });
    }

    private void deletePost(String postid,String publisher,ImageView post)
    {
        checkReportStatus(postid,post);
        post.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setTitle("What you want? 🤬");
                builder.setMessage("Report or Delete post then go back.😡");
                builder.setPositiveButton("Report :)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(post.getTag().equals("Report"))
                        {
                            databaseReference.child("PReports").child(postid).child(firebaseUser.getUid()).setValue("Report").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(mContext, "Successfully reported!.🖐", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else
                        {
                            Toast.makeText(mContext, "You already report this post!.🤬", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNeutralButton("Delete post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         if(firebaseUser.getUid().equals(publisher))
                         {
                             databaseReference.child("Posts").child(postid).removeValue();
                         }else
                         {
                             Toast.makeText(mContext, "Don't have permission to delete this post!.🤣", Toast.LENGTH_SHORT).show();
                         }
                    }
                });
                builder.setCancelable(true);
                builder.show();
                return false;
            }
        });
    }
    private void checkReportStatus(String postid,ImageView post)
    {
        databaseReference.child("PReports").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists())
                {
                    post.setTag("Reported");
                }else
                {
                    post.setTag("Report");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return Posts.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
       private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.myphotos_f_p);
        }
    }
}
