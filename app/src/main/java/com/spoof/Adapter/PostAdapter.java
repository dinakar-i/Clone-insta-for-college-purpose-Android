package com.spoof.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.rtp.AudioStream;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Fragments.ProfileFragment;
import com.spoof.Model.Post;
import com.spoof.Model.User;
import com.spoof.lm.CommentsActivity;
import com.spoof.lm.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
     private Context mContext;
     private List<Post> mPost;
     private FirebaseUser firebaseUser;
     private DatabaseReference databaseReference;
    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.postiteam,parent,false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=mPost.get(position);
        PublisherInfo(holder,post.getPublisher(),post.getPostImage(),post.getCaption(),post.getPostTime(),post.getPostDate());
       isLike(post.getPostId(),holder.like_f_post);
       likeCount(holder.likes_count_f_post,post.getPostId());
       hitLike(holder.like_f_post,post.getPostId(),post.getPublisher(),post.getPostImage());
       getanSetCommentCount(post.getPostId(),holder.Comment_count_f_post);
       goComment(holder.comment_f_post,post.getPostId(),post.getPublisher(),post.getPostImage());
       ChecKpin(holder.pin_post_btn,post.getPostId());
       pinPost(holder.pin_post_btn,post.getPostId());
       ReportPost(holder.post_f_post,post.getPostId());
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView username_f_post,fullname_f_post,likes_count_f_post,Comment_count_f_post,caption_username_f_post,caption_f_post,date_f_post;
        private ImageView like_f_post,comment_f_post,post_f_post,profile_f_post,pin_post_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //text view
            username_f_post=itemView.findViewById(R.id.username_f_post);
            fullname_f_post=itemView.findViewById(R.id.fullname_f_post);
            likes_count_f_post=itemView.findViewById(R.id.likes_count_f_post);
            Comment_count_f_post=itemView.findViewById(R.id.Comment_count_f_post);
            caption_username_f_post=itemView.findViewById(R.id.caption_username_f_post);
            caption_f_post=itemView.findViewById(R.id.caption_f_post);
            date_f_post=itemView.findViewById(R.id.date_f_post);
            //imgaeview
            like_f_post=itemView.findViewById(R.id.like_f_post);
            comment_f_post=itemView.findViewById(R.id.comment_f_post);
            post_f_post=itemView.findViewById(R.id.post_f_post);
            profile_f_post=itemView.findViewById(R.id.profile_f_post);
            pin_post_btn=itemView.findViewById(R.id.pin_f_post_item);
            like_f_post.setColorFilter(Color.BLACK);
            comment_f_post.setColorFilter(Color.BLACK);
        }
    }
    private void goUser(String userid,TextView username)
    {
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer,new ProfileFragment(userid,mContext)).commit();
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
    private void ReportPost(ImageView post,String postid)
    {
        checkReportStatus(postid,post);
        post.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setTitle("What you want? 🤬");
                builder.setMessage("Report post or Go back 😡");
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
                builder.setCancelable(true);
                builder.show();
                return false;
            }
        });
    }
    private void goComment(ImageView btn,String postid,String publisherid,String PostImage)
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid",postid);
                intent.putExtra("publisherid",publisherid);
                intent.putExtra("postImage",PostImage);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }
    private void pinPost(ImageView pin,String postid)
    {
       pin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(pin.getTag().equals("pin"))
               {
                   databaseReference.child("Pinned").child(firebaseUser.getUid()).child(postid).setValue("PINNED");
                   Toast.makeText(mContext, "Post pinned📌.", Toast.LENGTH_SHORT).show();
               }else
               {
                   databaseReference.child("Pinned").child(firebaseUser.getUid()).child(postid).removeValue();
                   Toast.makeText(mContext, "Post Unpinned📌.", Toast.LENGTH_SHORT).show();
               }
           }
       });
    }
    private void ChecKpin(ImageView pin,String postid)
    {
        databaseReference.child("Pinned").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postid).exists())
                {
                    pin.setTag("pinned");
                    pin.setColorFilter(Color.BLACK);
                }else
                {
                    pin.setTag("pin");
                    pin.setColorFilter(Color.parseColor("#8A8A8A"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getanSetCommentCount(String postid,TextView commentsCount)
    {
        databaseReference.child("Comments").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentsCount.setText(snapshot.getChildrenCount()+" Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
   private void isLike(String postid,final ImageView imageView)
   {
       databaseReference.child("Likes").child(postid).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.child(firebaseUser.getUid()).exists())
               {
                   imageView.setImageResource(R.drawable.ic_baseline_thumb_up_fill);
                   imageView.setTag("Liked");
               }else
               {
                   imageView.setImageResource(R.drawable.ic_baseline_thumb_up_outline);
                   imageView.setTag("Like");
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
   }
   private void hitLike(ImageView likebtn,String postid,String User_id,String imageUrl)
   {
       likebtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(likebtn.getTag().equals("Like"))
              {
                  databaseReference.child("Likes").child(postid).child(firebaseUser.getUid()).setValue("Liked");
                  databaseReference.child("Liked_notify").child(User_id).child(firebaseUser.getUid()).setValue(imageUrl);
              }else
              {
                  databaseReference.child("Likes").child(postid).child(firebaseUser.getUid()).removeValue();
              }
           }
       });
   }
   private void likeCount(TextView likecount,String postid)
   {
       databaseReference.child("Likes").child(postid).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               likecount.setText(snapshot.getChildrenCount()+" Likes");
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
   }
    private void PublisherInfo(ViewHolder holder, String userid,String postUrl,String Caption,String Time,String Date)
    {
        databaseReference.child("Users").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final User user=snapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageUrl()).into(holder.profile_f_post);
                holder.username_f_post.setText(user.getUsername());
                holder.fullname_f_post.setText(user.getFullname());
                Glide.with(mContext).load(postUrl).into(holder.post_f_post);
                holder.date_f_post.setText(Time+"/"+Date);
                goUser(user.getUserId(),holder.username_f_post);
                if(TextUtils.isEmpty(Caption))
                {
                    holder.caption_f_post.setVisibility(View.GONE);
                    holder.caption_username_f_post.setVisibility(View.GONE);
                }else
                {
                    holder.caption_f_post.setText(Caption);
                    holder.caption_username_f_post.setText(user.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
