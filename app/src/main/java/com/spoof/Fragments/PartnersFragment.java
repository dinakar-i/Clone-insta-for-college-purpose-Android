package com.spoof.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.UserAdapter;
import com.spoof.Model.User;
import com.spoof.lm.R;

import java.util.ArrayList;
import java.util.List;

public class PartnersFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_partners, container, false);

        recyclerView=view.findViewById(R.id.recycle_view_from_partners);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mUsers=new ArrayList<>();

        userAdapter=new UserAdapter(getContext(),mUsers);
        recyclerView.setAdapter(userAdapter);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        readUsers();
        return view;
    }
    private void readUsers()
    {
        databaseReference.child("PROPOSES").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                    mUsers.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                       if(dataSnapshot.getValue(String.class).equals("MATCHED"))
                       {
                           databaseReference.child("Users").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   final User user =snapshot.getValue(User.class);
                                   mUsers.add(user);
                                   userAdapter.notifyDataSetChanged();
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });

                       }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}