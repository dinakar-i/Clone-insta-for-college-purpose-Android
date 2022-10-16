package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.AUAdapter;
import com.spoof.Model.AU;
import com.spoof.MyMethods.Methods;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AUActivity extends AppCompatActivity {

    private ImageView go_back_btn_from_AU;
    private RecyclerView recycle_view_f_AUActivity;
    private List<AU> auList;
    private AUAdapter auAdapter;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_auactivity);
        go_back_btn_from_AU=findViewById(R.id.go_back_btn_from_AU);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        recycle_view_f_AUActivity=findViewById(R.id.recycle_view_f_AUActivity);
        recycle_view_f_AUActivity.setHasFixedSize(false);
        auList=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new GridLayoutManager(this,2);
        recycle_view_f_AUActivity.setLayoutManager(linearLayoutManager);
        auAdapter=new AUAdapter(this,auList);
        recycle_view_f_AUActivity.setAdapter(auAdapter);
        goBack();
        readAUSites();
    }
    private void goBack()
    {
        go_back_btn_from_AU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AUActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void readAUSites()
    {
        databaseReference.child("AUSITE").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                auList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    AU au=dataSnapshot.getValue(AU.class);
                    assert au != null;
                    if(au.getSTATUS().equals("approve"))
                    {
                        auList.add(au);
                    }
                    auAdapter.notifyDataSetChanged();
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