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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.UserAdapter;
import com.spoof.Adapter.messageUserAdapter;
import com.spoof.Model.User;
import com.spoof.MyMethods.Methods;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recycle_view_f_message;
    private List<User> userList;
    private messageUserAdapter messageUserAdapter;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private LottieAnimationView empty_animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            actionBar.setTitle("Inbox");
            ColorDrawable cd=new ColorDrawable(Color.parseColor("#000000"));
            actionBar.setBackgroundDrawable(cd);
        }
        setContentView(R.layout.activity_message);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        empty_animation=findViewById(R.id.empty_animation);
        recycle_view_f_message=findViewById(R.id.recycle_view_f_message);
        recycle_view_f_message.setHasFixedSize(true);
        recycle_view_f_message.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        userList=new ArrayList<>();
        messageUserAdapter=new messageUserAdapter(userList,this);
        recycle_view_f_message.setAdapter(messageUserAdapter);
        readUsers();
    }
 private void readUsers()
 {
         empty_animation.setVisibility(View.VISIBLE);
         databaseReference.child("PROPOSES").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 userList.clear();
                 for (DataSnapshot dataSnapshot:snapshot.getChildren())
                 {
                     String userid=dataSnapshot.getKey();
                     assert userid!=null;
                     String type=dataSnapshot.getValue(String.class);
                     assert type != null;
                     if(type.equals("MATCHED"))
                        {
                            databaseReference.child("Users").child(userid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    userList.add(user);
                                    empty_animation.setVisibility(View.GONE);
                                    messageUserAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                     }
                 messageUserAdapter.notifyDataSetChanged();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
 }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case 16908332:
                goBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void goBack()
    {
        Intent intent=new Intent(MessageActivity.this,StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
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