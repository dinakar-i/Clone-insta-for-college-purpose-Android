package com.spoof.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Model.Msg_notify;
import com.spoof.Model.User;
import com.spoof.lm.ChatActivity;
import com.spoof.lm.R;

import java.util.List;

public class messageUserAdapter extends RecyclerView.Adapter<messageUserAdapter.ViewHolder> {
    private List<User> mUsers;
    private Context mContext;
    private View view;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    public messageUserAdapter(List<User> mUsers, Context mContext) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    public messageUserAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       view= LayoutInflater.from(mContext).inflate(R.layout.message_user_list,parent,false);
        return new messageUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user=mUsers.get(position);
        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getFullname());
        Glide.with(mContext).load(user.getImageUrl()).into(holder.profilepic);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visit_profile(user.getUserId());
            }
        });
        readMessageCount(user.getUserId(), holder.msg_count);
    }

    private void readMessageCount(String User_id,TextView msg_count)
    {
        databaseReference.child("Msg_notify").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Msg_notify msg_notify=dataSnapshot.getValue(Msg_notify.class);
                    if(msg_notify.getSender().equals(User_id))
                    {
                        i++;
                    }
                }
                if(i>0)
                {
                    msg_count.setVisibility(View.VISIBLE);
                    msg_count.setText(String.valueOf(i));
                }else
                {
                    msg_count.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        private TextView username,fullname,msg_count;
        private ImageView profilepic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.message_user_item_username);
            fullname=itemView.findViewById(R.id.message_user_item_full_name);
            profilepic=itemView.findViewById(R.id.message_user_item_profile);
            msg_count=itemView.findViewById(R.id.msg_count);
        }
    }
    private  void visit_profile(String user_id)
    {
            Intent i=new Intent(mContext, ChatActivity.class);
            i.putExtra("profiled",user_id);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
    }
}
