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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.LoadGalleryAdapter;
import com.spoof.Model.MemorieImage;
import com.spoof.MyMethods.Methods;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleryActivity extends AppCompatActivity {
    private ImageView goback_btn;
    private DatabaseReference databaseReference;
    private RecyclerView recycle_view_f_gallery;
    private LoadGalleryAdapter loadGalleryAdapter;
    private TextView gallery_title;

    private String key;
    private List<MemorieImage> memorieImageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_gallery);
        goback_btn=findViewById(R.id.goback_btn_f_gallery);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        //for images
        recycle_view_f_gallery=findViewById(R.id.recycle_view_f_gallery);
        gallery_title=findViewById(R.id.gallery_title);
        recycle_view_f_gallery.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager=new GridLayoutManager(getApplicationContext(),3);
        recycle_view_f_gallery.setLayoutManager(linearLayoutManager);
        memorieImageList=new ArrayList<>();
        loadGalleryAdapter=new LoadGalleryAdapter(this,memorieImageList);
        recycle_view_f_gallery.setAdapter(loadGalleryAdapter);
        //
        get_Intent();
        goBack();
        readImages();
    }
    void get_Intent()
    {
        Intent intent=getIntent();
        key=intent.getStringExtra("mCat");
        if(key!=null)
        {
            gallery_title.setText(String.valueOf(key+" - Gallery"));
        }
    }
    void goBack()
    {
     goback_btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent=new Intent(GalleryActivity.this,StartActivity.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
             startActivity(intent);
         }
     });
    }
    void readImages()
    {
        databaseReference.child("Memories").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                memorieImageList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                      MemorieImage memorieImage=dataSnapshot.getValue(MemorieImage.class);
                    memorieImageList.add(memorieImage);
                }
                loadGalleryAdapter.notifyDataSetChanged();
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