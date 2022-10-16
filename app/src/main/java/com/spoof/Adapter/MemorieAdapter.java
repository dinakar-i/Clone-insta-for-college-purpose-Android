package com.spoof.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spoof.Model.Memories;
import com.spoof.lm.GalleryActivity;
import com.spoof.lm.R;

import java.util.List;

public class MemorieAdapter extends RecyclerView.Adapter<MemorieAdapter.ViewHolder> {

    private Context mContext;
    private List<Memories> memoriesList;

    public MemorieAdapter(Context mContext, List<Memories> memoriesList) {
        this.mContext = mContext;
        this.memoriesList = memoriesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.memories_item,parent,false);
        return new MemorieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Memories memories=memoriesList.get(position);
        Glide.with(mContext).load(memories.getImageUrl()).into(holder.memories_box_img);
        holder.memories_box_title.setText(memories.getTITLE());
        goGallery(holder.memories_box_img,memories.getTITLE());
    }

    @Override
    public int getItemCount() {
        return memoriesList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView memories_box_img;
        private TextView memories_box_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memories_box_img=itemView.findViewById(R.id.memories_box_img);
            memories_box_title=itemView.findViewById(R.id.memories_box_title);
        }
    }
    private void goGallery(ImageView img,String TITLE)
    {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, GalleryActivity.class);
                intent.putExtra("mCat",TITLE);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }
}
