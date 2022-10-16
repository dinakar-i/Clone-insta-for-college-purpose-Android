package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.PostAdapter;
import com.spoof.Model.Post;
import com.spoof.MyMethods.Methods;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostDetail extends AppCompatActivity {

    private RecyclerView recycle_f_post_detail;
    List<Post> posts;
    PostAdapter postAdapter;
    String postid,publisher,currentUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            actionBar.setTitle("Post detail");
            ColorDrawable cd=new ColorDrawable(Color.BLACK);
            actionBar.setBackgroundDrawable(cd);
        }
        recycle_f_post_detail=findViewById(R.id.recycle_f_post_detail);
        recycle_f_post_detail.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplication());
        recycle_f_post_detail.setLayoutManager(linearLayoutManager);
        posts=new ArrayList<>();
        postAdapter=new PostAdapter(this,posts);
        recycle_f_post_detail.setAdapter(postAdapter);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        ReadPost();
    }
    void Goback()
    {
                Intent intent=new Intent(getApplication(),StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);
    }
    void ReadPost()
    {
        Intent intent=getIntent();
        postid=intent.getStringExtra("postid");
            databaseReference.child("Posts").child(postid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    posts.clear();
                    Post post=snapshot.getValue(Post.class);
                    assert post != null;
                    publisher=post.getPublisher();
                    posts.add(post);
                    postAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
}

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                Goback();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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