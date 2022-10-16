package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.CommentAdapter;
import com.spoof.Model.Comment;
import com.spoof.Model.User;
import com.spoof.MyMethods.Methods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CommentsActivity extends AppCompatActivity {

    private String postid,publisherid,PostImage;
    private EditText commenttext;
    private TextView addCommentbtn;
    private ImageView profilepic,closebtn;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_comments);
        commenttext=findViewById(R.id.comments_text_f_comments);
        profilepic=findViewById(R.id.comment_profile_img);
        addCommentbtn=findViewById(R.id.addcomments);
        closebtn=findViewById(R.id.close_comments);
        databaseReference=FirebaseDatabase.getInstance().getReference();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=findViewById(R.id.recycle_f_comment);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList=new ArrayList<>();
        commentAdapter=new CommentAdapter(this,commentList);
        recyclerView.setAdapter(commentAdapter);
        go_back();
        getintent();
        ReadComments();
        getProfile();
        addComments();
    }

 private void getintent()
 {
     Intent intent=getIntent();
     postid=intent.getStringExtra("postid");
     publisherid=intent.getStringExtra("publisherid");
     PostImage=intent.getStringExtra("postImage");
 }

 private void addComments()
 {
     addCommentbtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if(!TextUtils.isEmpty(commenttext.getText()))
             {
                 String key=databaseReference.child("Comments").child(postid).push().getKey();
                 String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                 String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                 HashMap<String,Object>hashMap=new HashMap<>();
                 hashMap.put("comment",commenttext.getText().toString());
                 hashMap.put("commander",firebaseUser.getUid());
                 hashMap.put("Date",currentDate);
                 hashMap.put("Time",currentTime);
                 hashMap.put("Key",key);
                 hashMap.put("CommentPostid",postid);
                 assert key != null;
                 databaseReference.child("Comments").child(postid).child(key).setValue(hashMap);
                 if(!TextUtils.isEmpty(PostImage))
                 {
                     databaseReference.child("Commend_notify").child(publisherid).child(firebaseUser.getUid()).setValue(PostImage);
                 }
                 commenttext.setText("");
             }
         }
     });
 }
 private void getProfile()
 {
     databaseReference.child("Users").child(firebaseUser.getUid()).child("General").addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             User user=snapshot.getValue(User.class);
             Glide.with(CommentsActivity.this).load(user.getImageUrl()).into(profilepic);
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });
 }

    private void go_back()
    {
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CommentsActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
    private void ReadComments()
    {
        databaseReference.child("Comments").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Comment comment=dataSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Methods.status("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Methods.status("Offline");
    }
}