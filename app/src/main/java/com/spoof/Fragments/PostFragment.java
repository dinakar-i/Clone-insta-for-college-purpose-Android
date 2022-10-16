package com.spoof.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.MyphotoAdapter;
import com.spoof.Model.Post;
import com.spoof.Model.User;
import com.spoof.lm.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PostFragment extends Fragment {
    private View holder;
    private RecyclerView recyclerView;
    private MyphotoAdapter myphotoAdapter;
    private List<Post> Posts;
    private String Userid;

    public PostFragment(String userid) {
        Userid = userid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        holder=inflater.inflate(R.layout.fragment_post, container, false);
        recyclerView=holder.findViewById(R.id.recycle_view_f_Profile_my_photo);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager=new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);
        Posts=new ArrayList<>();
        myphotoAdapter=new MyphotoAdapter(getContext(),Posts);
        recyclerView.setAdapter(myphotoAdapter);
        ReadMyPhotos();
        return holder;
    }
    private void ReadMyPhotos()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Posts.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                      final Post post=dataSnapshot.getValue(Post.class);
                    if(post!=null)
                    {
                        if(post.getPublisher().equals(Userid))
                        {
                            Posts.add(post);

                        }
                    }

                    Collections.reverse(Posts);
                    myphotoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}