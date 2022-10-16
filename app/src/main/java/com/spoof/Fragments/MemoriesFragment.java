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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.MemorieAdapter;
import com.spoof.Model.Memories;
import com.spoof.Model.Post;
import com.spoof.lm.R;

import java.util.ArrayList;
import java.util.List;

public class MemoriesFragment extends Fragment {
    private RecyclerView memories_recycle_view;
    private MemorieAdapter memorieAdapter;
    private List<Memories> memoriesList;
    private DatabaseReference databaseReference;
    @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_memories, container, false);
        //for box memories
        memories_recycle_view=view.findViewById(R.id.memories_recycle_view);
        memories_recycle_view.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager=new GridLayoutManager(getContext(),3);
        memories_recycle_view.setLayoutManager(linearLayoutManager);
        memoriesList=new ArrayList<>();
        memorieAdapter=new MemorieAdapter(getContext(),memoriesList);
        memories_recycle_view.setAdapter(memorieAdapter);
        //
        databaseReference= FirebaseDatabase.getInstance().getReference();
        readMemories();
        return view;
    }

    void readMemories()
    {
        databaseReference.child("Memories_Cat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                memoriesList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Memories memories=dataSnapshot.getValue(Memories.class);
                    memoriesList.add(memories);

                }
                memorieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}