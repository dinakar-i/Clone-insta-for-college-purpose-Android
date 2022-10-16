package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spoof.Fragments.ExploreFragment;
import com.spoof.Fragments.HomeFragment;
import com.spoof.Fragments.MemoriesFragment;
import com.spoof.Fragments.PartnersFragment;
import com.spoof.Fragments.ProfileFragment;
import com.spoof.Fragments.RequestFragment;
import com.spoof.MyMethods.Methods;

import java.util.Objects;

public class ShowPartnersActivity extends AppCompatActivity {
    private Fragment FinalFragment;
    private BottomNavigationView bottomNavigationView;
    private ImageView go_btn_from_show_partners;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_show_partners);
        bottomNavigationView=findViewById(R.id.bottomNavigationView_f_show_partners);
        go_btn_from_show_partners=findViewById(R.id.go_btn_from_show_partners);
        ManageBottomNavigation();
        goBack();

    }
    private void goBack()
    {
        go_btn_from_show_partners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShowPartnersActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    private void ManageBottomNavigation()
    {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.partners_menu:
                        FinalFragment=new PartnersFragment();
                        break;
                    case R.id.request_menu:
                        FinalFragment=new RequestFragment();
                        break;
                }
                if(FinalFragment!=null)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_f_show_partners,FinalFragment).commit();
                }
                return true;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Methods.status("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Methods.status("Offline");
    }
}