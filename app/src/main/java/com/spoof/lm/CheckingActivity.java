package com.spoof.lm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

public class CheckingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        timing();
    }

    private void timing()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Check_login();
            }
        },4100);

    }
    private void Check_login()
    {
        if(getLogin())
        {
            Intent intent=new Intent(CheckingActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }else if(!getLogin())
        {
            Intent intent=new Intent(CheckingActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private boolean getLogin()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("PREF",MODE_PRIVATE);
        String v=sharedPreferences.getString("Login",null);
        if(v!=null)
        {
            return true;
        }else
        {
            return false;
        }

    }

}