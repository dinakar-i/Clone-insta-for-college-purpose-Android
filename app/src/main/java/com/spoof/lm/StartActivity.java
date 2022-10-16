package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Fragments.ExploreFragment;
import com.spoof.Fragments.FunFragment;
import com.spoof.Fragments.HomeFragment;
import com.spoof.Fragments.MemoriesFragment;
import com.spoof.Fragments.ProfileFragment;
import com.spoof.Model.User;
import com.spoof.Model.Version;
import com.spoof.MyMethods.Methods;
import com.spoof.MyMethods.MyService;

public class StartActivity extends AppCompatActivity {

    private NavigationView Nv;
    private DrawerLayout drawer_layout;
    private boolean isOpen=false;
    private BottomNavigationView bottomNavigationView;
    private Fragment FinalFragment;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private TextView Username_from_Navigation,email_from_Navigation;
    private ImageView profile_img_f_navigation;
    private String Email,Name,imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ActionBar actionBar=getSupportActionBar();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
            actionBar.setTitle(null);
            ColorDrawable cd=new ColorDrawable(Color.parseColor("#000000"));
            actionBar.setBackgroundDrawable(cd);
        }
        //find objects
        Nv=findViewById(R.id.navigation_view_side_bar);
        drawer_layout=findViewById(R.id.drawer_layout);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        //functioncalling
        StartService();
        versionCheck();
        GetUserinfo();
        OpenNavigation();
        ManageBottomNavigation();
        checkVisitProfile();
    }

    private void StartService()
    {
       Intent intent=new Intent(StartActivity.this,MyService.class);
       startService(intent);
    }
    private void checkVisitProfile()
    {
        Intent intent = getIntent();
        String profiledid=intent.getStringExtra("profiled");
        if(profiledid!=null)
        {
            getIntent().removeExtra("profiled");
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer,new ProfileFragment(profiledid,StartActivity.this)).commit();
        }else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer,new HomeFragment()).commit();
        }
    }
    private void ManageBottomNavigation()
    {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home_id_menu_b:
                        FinalFragment=new HomeFragment();
                        break;
                    case R.id.explore_id_menu_b:
                        FinalFragment=new ExploreFragment();
                        break;
                    case R.id.makefun:
                        FinalFragment=new FunFragment();
                        break;
                    case R.id.memories:
                        FinalFragment= new MemoriesFragment();
                        break;
                    case R.id.profile_id_menu_b:
                        FinalFragment=new ProfileFragment(firebaseUser.getUid(),StartActivity.this);
                        break;
                }
                if(FinalFragment!=null)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer,FinalFragment).commit();
                }
                return true;
            }
        });
    }
    private void GetUserinfo()
    {
        databaseReference.child("Users").child(firebaseUser.getUid()).child("Status").setValue("Online");
        databaseReference.child("Users").child(firebaseUser.getUid()).child("General").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               try {
                   User user=snapshot.getValue(User.class);
                   Name=user.getUsername();
                   Email=user.getEmail();
                   imgUrl=user.getImageUrl();
               }catch (Exception e)
               {
                   setLogOut();
               }
                SetUserInfoOnNavigation();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void SetUserInfoOnNavigation()
    {
        View view=Nv.getHeaderView(0);
        Username_from_Navigation=view.findViewById(R.id.Username_from_Navigation);
        email_from_Navigation=view.findViewById(R.id.email_from_Navigation);
        profile_img_f_navigation=view.findViewById(R.id.profile_img_f_navigation);
        Username_from_Navigation.setText(Name);
        email_from_Navigation.setText(Email);
        Glide.with(getApplicationContext()).load(imgUrl).into(profile_img_f_navigation);

    }
    private void OpenNavigation()
    {
        Nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.AU:
                        goAU();
                        break;
                    case R.id.partners_menu:
                        goPartners();
                        break;
                    case R.id.log_out:
                        logout();
                        break;
                }
                return true;
            }

        });
    }
    private void goPartners()
    {
        Intent intent=new Intent(StartActivity.this,ShowPartnersActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void NavigationController()
    {
        if(isOpen)
        {
            drawer_layout.openDrawer(GravityCompat.END);
        }else
        {
            drawer_layout.closeDrawers();
        }
    }

    //implement notification badger
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_in_actionbar,menu);
        MenuItem menuItem=menu.findItem(R.id.message);
        MenuItem news=menu.findItem(R.id.news);

        databaseReference.child("News_notify").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    news.setActionView(R.layout.news_notification_badge);
                    View view=news.getActionView();
                    ImageView imageView=view.findViewById(R.id.news_after_badge);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goNews();
                        }
                    });
                }else if(snapshot.exists())
                {
                    news.setActionView(null);
                }
                else
                {
                    news.setActionView(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Msg_notify").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()<=0)
                {
                    menuItem.setActionView(null);
                }else
                {
                    menuItem.setActionView(R.layout.notification_badge_layout);
                    View view=menuItem.getActionView();
                    TextView counter=view.findViewById(R.id.msg_count);
                    ImageView message_after_badge=view.findViewById(R.id.message_after_badge);
                    counter.setText(String.valueOf(snapshot.getChildrenCount()));
                    message_after_badge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goMessage();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.create:
                UploadTask();
                break;
            case R.id.message:
                goMessage();
                break;
            case R.id.news:
                goNews();
                break;
            case 16908332:
                if(!isOpen)
                {
                    isOpen=true;
                }else
                {
                    isOpen=false;
                }
                NavigationController();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void UploadTask()
    {
        Intent intent=new Intent(StartActivity.this,PostIncident.class);
        startActivity(intent);
        finish();
    }

    public void logout()
    {
        AlertDialog alertDialog=new AlertDialog.Builder(StartActivity.this).create();
        alertDialog.setTitle("Logout");
        alertDialog.setMessage("Are you sure want to logout?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setLogOut();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }
    private void setLogOut()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("PREF",MODE_PRIVATE);
        SharedPreferences.Editor v=sharedPreferences.edit();
        v.putString("Login",null);
        v.apply();
        Intent intent=new Intent(StartActivity.this, CheckingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void goMessage()
    {
           Intent intent=new Intent(StartActivity.this,MessageActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
    }
    private void goNews() {
        Intent intent=new Intent(StartActivity.this,NewsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void goAU()
    {
        Intent intent=new Intent(StartActivity.this,AUActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void versionCheck()
    {
        databaseReference.child("Version").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Version version=snapshot.getValue(Version.class);
                assert version!=null;
                long New_version=version.getCurrent();
                long v_code=BuildConfig.VERSION_CODE;
                if(v_code!=New_version)
                {
                    AlertDialog.Builder alertdialog=new AlertDialog.Builder(StartActivity.this);
                    alertdialog.setTitle("Update Available");
                    alertdialog.setMessage("Hey good news for you new Update available.We add some new features an some bug fixing,Your Current version is "+v_code+" but new version is "+New_version+" I hope you Understand.just do it.Thank you.");
                    alertdialog.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String url = version.getLink();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    });
                    alertdialog.setNeutralButton("Update Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertdialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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