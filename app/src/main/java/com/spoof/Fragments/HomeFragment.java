package com.spoof.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.PostAdapter;
import com.spoof.Model.Post;
import com.spoof.lm.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    //for posts
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> posts;
    private ProgressBar post_progressBar;
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        post_progressBar= view.findViewById(R.id.post_progressBar);
        databaseReference=FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        //for posts
        recyclerView=view.findViewById(R.id.recycle_view_post);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        posts=new ArrayList<>();
        postAdapter=new PostAdapter(getContext(),posts);
        recyclerView.setAdapter(postAdapter);
        //
        ReadPosts();
        return view;
    }
    private void ReadPosts()
    {
        databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren())
                {
                   final Post post=dataSnapshot.getValue(Post.class);
                    posts.add(post);
                }
                postAdapter.notifyDataSetChanged();
                post_progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}