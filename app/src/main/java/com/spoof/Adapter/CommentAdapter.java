package com.spoof.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Fragments.ProfileFragment;
import com.spoof.Model.Comment;
import com.spoof.Model.Post;
import com.spoof.Model.User;
import com.spoof.lm.R;
import com.spoof.lm.StartActivity;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context mContext;
    private List<Comment> commentList;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    public CommentAdapter(Context mContext, List<Comment> commentList) {
        this.mContext = mContext;
        this.commentList = commentList;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        View view= LayoutInflater.from(mContext).inflate(R.layout.comments,parent,false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment=commentList.get(position);
        holder.comment_text.setText(comment.getComment());
        holder.datetime.setText(comment.getDate()+"/"+comment.getTime());
        getUserinfo(holder,comment.getCommander());
        goUser(comment.getCommander(),holder.username);
        deleteComment(holder.comment_text,comment.getCommander(),comment.getKey(),comment.getComment(),comment.getCommentPostid());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
       private ImageView profileimg;
       private TextView username,comment_text,datetime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimg=itemView.findViewById(R.id.profile_f_comment_display);
            username=itemView.findViewById(R.id.username_f_comment_display);
            comment_text=itemView.findViewById(R.id.comment_f_comment_display);
            datetime=itemView.findViewById(R.id.date_time_f_comment);
        }
    }
    private void getUserinfo(ViewHolder holder,String userid)
    {
        databaseReference.child("Users").child(userid).child("General").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                holder.username.setText(user.getUsername());
                Glide.with(mContext).load(user.getImageUrl()).into(holder.profileimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void goUser(String userid,TextView username)
    {
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, StartActivity.class);
                intent.putExtra("profiled",userid);
                mContext.startActivity(intent);
            }
        });
    }
    private void deleteComment(TextView Commendtextview,String Commender,String commentId,String comment,String commentPostid)
    {
        Commendtextview.setOnLongClickListener(new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View view) {
                  databaseReference.child("Posts").child(commentPostid).addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          Post post=snapshot.getValue(Post.class);
                          assert post != null;

                          if(firebaseUser.getUid().equals(Commender)||post.getPublisher().equals(firebaseUser.getUid()))
                          {
                              new AlertDialog.Builder(mContext).setTitle("Delete Comments").setMessage(comment)
                                      .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                          public void onClick(DialogInterface dialog, int which) {
                                              DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
                                              databaseReference.child("Comments").child(commentPostid).child(commentId).removeValue();
                                          }
                                      }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialogInterface, int i) {

                                  }
                              }).setIcon(android.R.drawable.ic_dialog_alert).show();
                          }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                  });
                return true;
              }
          });
    }
}
