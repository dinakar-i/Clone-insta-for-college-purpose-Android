package com.spoof.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spoof.Model.User;

import com.spoof.lm.ChatActivity;
import com.spoof.lm.R;
import com.spoof.lm.StartActivity;

import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>
{
    private Context mContext;
    private List<User> mUsers;
    private View view;
    private String TO;

    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(mContext).inflate(R.layout.useriteam,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final User user=mUsers.get(position);
            holder.Username.setText(user.getUsername());
            holder.Fullname.setText(user.getFullname());
            Glide.with(mContext).load(user.getImageUrl()).into(holder.ProfileImg);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    visit_profile(user.getUserId());
                }
            });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView Username,Fullname;
        private CircleImageView ProfileImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Username=itemView.findViewById(R.id.useriteam_username);
            Fullname=itemView.findViewById(R.id.useriteam_fullname);
            ProfileImg=itemView.findViewById(R.id.useriteam_profile);
        }
    }
    private  void visit_profile(String user_id)
    {
            Intent i=new Intent(mContext, StartActivity.class);
            i.putExtra("profiled",user_id);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
    }
}
