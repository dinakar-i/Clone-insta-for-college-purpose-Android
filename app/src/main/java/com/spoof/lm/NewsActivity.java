package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.NewsAdapter;
import com.spoof.Adapter.UserAdapter;
import com.spoof.Model.News;
import com.spoof.Model.User;
import com.spoof.MyMethods.Methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NewsActivity extends AppCompatActivity {

    private ImageView arrow_back;
    private RecyclerView news_recycle_view;
    private Button news_send_btn;
    private boolean isPost=true;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private List<News> newsList;
    private NewsAdapter newsAdapter;
    private TextView notPermissionText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_news);
        //
        arrow_back=findViewById(R.id.arrow_back);
        news_recycle_view=findViewById(R.id.news_recycle_view);
        news_send_btn=findViewById(R.id.news_send_btn);
        notPermissionText=findViewById(R.id.notPermissionText);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        news_recycle_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(NewsActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        news_recycle_view.setLayoutManager(linearLayoutManager);
        newsList=new ArrayList<>();

        newsAdapter=new NewsAdapter(NewsActivity.this,newsList);
        news_recycle_view.setAdapter(newsAdapter);
        //
        whoIsComing();
        ButtonListener();
        readNews();
        insert_notify();
    }

    private void ButtonListener() {
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NewsActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        news_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NewsActivity.this,UploadNewsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void readNews()
    {
        databaseReference.child("News").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    News news=dataSnapshot.getValue(News.class);
                    if(news!=null && news.getWhoSee().equals("All"))
                    {
                        newsList.add(news);
                    }else
                    {
                      databaseReference.child("Users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              User user=snapshot.getValue(User.class);
                              if(news.getWhoSee().equals(user.getCast()))
                              {
                                  newsList.add(news);
                              }
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {

                          }
                      });
                    }
                    newsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void whoIsComing()
    {
          databaseReference.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if(snapshot!=null)
                  {
                      final User user=snapshot.getValue(User.class);
                      if(!user.getCast().equals("Student"))
                      {
                         notPermissionText.setVisibility(View.GONE);
                         news_send_btn.setVisibility(View.VISIBLE);
                      }else
                      {
                          notPermissionText.setVisibility(View.VISIBLE);
                          news_send_btn.setVisibility(View.GONE);
                      }
                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
    }

    private void insert_notify()
    {

        DatabaseReference reference=databaseReference.child("News_notify").child(firebaseUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    databaseReference.child("Users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user=snapshot.getValue(User.class);
                            reference.setValue(user.getCast());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
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