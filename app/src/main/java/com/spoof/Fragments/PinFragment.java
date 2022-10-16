package com.spoof.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.MyphotoAdapter;
import com.spoof.Model.Post;
import com.spoof.lm.R;

import java.util.ArrayList;
import java.util.List;

public class PinFragment extends Fragment {
    RecyclerView recycle_view_f_pin_post;
    MyphotoAdapter myphotoAdapter;
    List<Post> posts;
    List<String> Pins;
    String User_id;
    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pin, container, false);
        recycle_view_f_pin_post=view.findViewById(R.id.recycle_view_f_pin_post);
        recycle_view_f_pin_post.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager=new GridLayoutManager(getContext(),3);
        recycle_view_f_pin_post.setLayoutManager(linearLayoutManager);
        posts=new ArrayList<>();
        myphotoAdapter=new MyphotoAdapter(getContext(),posts);
        recycle_view_f_pin_post.setAdapter(myphotoAdapter);
        databaseReference=FirebaseDatabase.getInstance().getReference();
        User_id=FirebaseAuth.getInstance().getUid();
        myPins();
        return view;
    }

    void myPins()
    {
        Pins=new ArrayList<>();
        databaseReference.child("Pinned").child(User_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Pins.add(dataSnapshot.getKey());
                }
                readPins();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
   void readPins()
    {
        databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    final Post post=dataSnapshot.getValue(Post.class);
                    for(String id:Pins)
                    {
                        if(id!=null)
                        {
                            if(post.getPostId().equals(id))
                            {
                                posts.add(post);
                            }
                        }
                    }
                }
                myphotoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}