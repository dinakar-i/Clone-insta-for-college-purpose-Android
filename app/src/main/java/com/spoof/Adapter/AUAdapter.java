package com.spoof.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spoof.Model.AU;
import com.spoof.lm.R;
import com.spoof.lm.WebActivity;

import java.util.List;

public class AUAdapter extends RecyclerView.Adapter<AUAdapter.ViewHolder> {
    private Context mContext;
    private List<AU> auList;

    public AUAdapter(Context mContext, List<AU> auList) {
        this.mContext = mContext;
        this.auList = auList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.site_au,parent,false);
        return new AUAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
               AU au=auList.get(position);
               holder.au_site_box_title.setText(au.getTITLE());
               holder.au_site_box_title.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if(au.getTYPE().equals("IN"))
                       {
                           Intent intent=new Intent(mContext, WebActivity.class);
                           intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                           intent.putExtra("url",au.getURL());
                           mContext.startActivity(intent);
                       }else
                       {
                           Intent intent=new Intent(Intent.ACTION_VIEW);
                           intent.setData(Uri.parse(au.getURL()));
                           mContext.startActivity(intent);
                       }

                   }
               });
    }

    @Override
    public int getItemCount() {
        return auList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
           private TextView au_site_box_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            au_site_box_title=itemView.findViewById(R.id.au_site_box_title);
        }
    }
}
