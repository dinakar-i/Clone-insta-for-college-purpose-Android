package com.spoof.Adapter;



import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.spoof.Model.MemorieImage;
import com.spoof.lm.R;
import java.util.List;

public class LoadGalleryAdapter extends RecyclerView.Adapter<LoadGalleryAdapter.ViewHolder> {
     private Context mContext;
     private List<MemorieImage> memorieImageList;

    public LoadGalleryAdapter(Context mContext, List<MemorieImage> memorieImageList) {
        this.mContext = mContext;
        this.memorieImageList = memorieImageList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.gallery_layout,parent,false);
        return new LoadGalleryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemorieImage memorieImage=memorieImageList.get(position);
        Glide.with(mContext).load(memorieImage.getImageUrl()).into(holder.gallery_image);
        openimg(holder.gallery_image,memorieImage.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return memorieImageList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder
    {
          ImageView gallery_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gallery_image=itemView.findViewById(R.id.gallery_image);
        }
    }
    void openimg(ImageView imageView,String url)
    {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater myLayout = LayoutInflater.from(mContext);
                final View dialogView = myLayout.inflate(R.layout.popimage_layout, null);
                ImageView myphotos_f_p=dialogView.findViewById(R.id.pop_img_f_pop_layout);
                Glide.with(mContext).load(url).into(myphotos_f_p);
                Dialog dialog =new Dialog(mContext);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                dialog.setCancelable(true);
                dialog.setContentView(dialogView);
                dialog.show();
            }
        });
    }

}
