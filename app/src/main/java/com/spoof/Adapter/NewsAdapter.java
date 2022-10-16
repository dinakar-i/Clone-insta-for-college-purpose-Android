package com.spoof.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Model.News;
import com.spoof.Model.User;
import com.spoof.lm.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context mContext;
    private List<News> newsList;

    public NewsAdapter(Context mContext, List<News> newsList) {
        this.mContext = mContext;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.news_item,parent,false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      final News news=newsList.get(position);
      if(news!=null)
      {
          holder.newsHeading.setText(news.getNewsTitle());
          holder.newsText.setText(news.getNewsText());
         if(news.getImageUrl()!=null)
         {
             holder.news_Image.setVisibility(View.VISIBLE);
             Glide.with(mContext).load(news.getImageUrl()).into(holder.news_Image);
         }else
         {
             holder.news_Image.setVisibility(View.GONE);
         }
          DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
          databaseReference.child("Users").child(news.getPublisher()).addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  User user=snapshot.getValue(User.class);
                  holder.news_author.setText(user.getUsername());
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
      }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
       private TextView newsHeading,newsText,news_author;
       private ImageView news_Image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsHeading=itemView.findViewById(R.id.news_title_from_new_item);
            newsText=itemView.findViewById(R.id.news_msg_text);
            news_author=itemView.findViewById(R.id.news_creator_name);
            news_Image=itemView.findViewById(R.id.news_banner_img);
        }
    }
}
