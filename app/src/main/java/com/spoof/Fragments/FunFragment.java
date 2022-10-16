package com.spoof.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.spoof.lm.FlameActivity;
import com.spoof.lm.R;

public class FunFragment extends Fragment {
    private CardView flames_fun;
    private RelativeLayout relative;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fun, container, false);
        flames_fun=view.findViewById(R.id.flames_fun);
        relative=view.findViewById(R.id.relative);
        goFlames();
        return view;
    }
    private void goFlames()
    {
     flames_fun.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent=new Intent(getContext(), FlameActivity.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent);
         }
     });
    }
}