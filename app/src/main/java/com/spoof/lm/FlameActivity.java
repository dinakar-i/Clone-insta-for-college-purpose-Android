package com.spoof.lm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spoof.MyMethods.Methods;

public class FlameActivity extends AppCompatActivity {
    private EditText first_name,second_name;
    private TextView output_text;
    private Button Calulate_btn;
    private ImageView go_back_from_main;
    private String FirstName,SecondName;
    private FirebaseUser firebaseUser;
    boolean connected = false;
    private LottieAnimationView flam_anim_love,flam_anim_enemy,flam_anim_friend,flam_anim_siblings,flam_anim_marriage,flam_anim_Affection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_flame);
        first_name=findViewById(R.id.first_name_f);
        second_name=findViewById(R.id.second_name_f);
        Calulate_btn=findViewById(R.id.Calculate_btn);

        flam_anim_love=findViewById(R.id.flam_anim_love);
        flam_anim_enemy=findViewById(R.id.flam_anim_enemy);
        flam_anim_friend=findViewById(R.id.flam_anim_friend);
        flam_anim_siblings=findViewById(R.id.flam_anim_siblings);
        flam_anim_marriage=findViewById(R.id.flam_anim_marriage);
        flam_anim_Affection=findViewById(R.id.flam_anim_Affection);

        output_text=findViewById(R.id.Output);

        go_back_from_main=findViewById(R.id.go_back_from_main);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        Check_connection();
        Calculation_Listener();
        goBack();
    }
    private void goBack()
    {
        go_back_from_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FlameActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    private void Calculation_Listener()
    {
        Calulate_btn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  first_name.onEditorAction(EditorInfo.IME_ACTION_DONE);
                  second_name.onEditorAction(EditorInfo.IME_ACTION_DONE);
                  FirstName=first_name.getText().toString();
                  SecondName=second_name.getText().toString();
                  first_name.setText(null);
                  second_name.setText(null);
                  Calculation(FirstName,SecondName);
              }
          });
    }
    private void Calculation(String name1,String name2)
    {
        if(!TextUtils.isEmpty(name1) && !TextUtils.isEmpty(name2)) {
            name1 = name1.toLowerCase();
            name2 = name2.toLowerCase();

            StringBuilder sb1 = new StringBuilder(name1);// converting to string builder
            StringBuilder sb2 = new StringBuilder(name2);

            int m = sb1.length();
            int n = sb2.length();
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (sb1.charAt(i) == sb2.charAt(j)) {
                        sb1.replace(i, i + 1, "0"); // replacing matching characters into "0"
                        sb2.replace(j, j + 1, "0");
                    }
                }
            }
            int x1 = 0;
            int y1 = 0;
            String s1 = "";
            String s2 = "";
            s1 = sb1.toString();
            s2 = sb2.toString();
            for (int i = 0; i < s1.length(); i++) { //length of string to remove 0 and find the length
                if (s1.charAt(i) != '0') {
                    x1 += 1;
                }
            }
            for (int i = 0; i < s2.length(); i++) {
                if (s2.charAt(i) != '0') {
                    y1 += 1;

                }
            }
            int x = x1 + y1; // total length of remaining characters in both the strings
            String flames = "flames";
            StringBuilder sb3 = new StringBuilder(flames);

            char flameResult = 0;

            while (sb3.length() != 1) {
                int y = x % sb3.length();
                String temp;

                if (y != 0) {
                    temp = sb3.substring(y) + sb3.substring(0, y - 1); // taking substring (counting purpose)

                } else {
                    temp = sb3.substring(0, sb3.length() - 1); // taking substring (counting purpose)

                }
                sb3 = new StringBuilder(temp);
                flameResult = sb3.charAt(0);

            }
            String d;
            switch (flameResult) {
                case 'f':
                    d="Friends";
                    MaintainAnim(d);
                    break;
                case 'l':
                    d="Love";
                    MaintainAnim(d);
                    break;
                case 'a':
                    d="Affection";
                    MaintainAnim(d);
                    break;
                case 'm':
                    d="Marriage";
                    MaintainAnim(d);
                    break;
                case 'e':
                    d="Enemies";
                    MaintainAnim(d);
                    break;
                case 's':
                    d="Sibling";
                    MaintainAnim(d);
                    break;

            }
        }else
        {
            AlertDialog.Builder ad=new AlertDialog.Builder(FlameActivity.this);
            ad.setMessage("Are you sure!. You enter names.");
            ad.show();
        }
    }
    private void MaintainAnim(String Data)
    {
        StoreDataWhoPair(FirstName,SecondName,Data);
     switch (Data)
     {
         case "Friends":
             output_text.setText(Data);
             output_text.setTextColor(Color.parseColor("#ffb7cf"));
             flam_anim_Affection.setVisibility(View.GONE);
             flam_anim_love.setVisibility(View.GONE);
             flam_anim_marriage.setVisibility(View.GONE);
             flam_anim_friend.setVisibility(View.VISIBLE);
             flam_anim_enemy.setVisibility(View.GONE);
             flam_anim_siblings.setVisibility(View.GONE);
             break;
         case "Love":
             output_text.setText(Data);
             output_text.setTextColor(Color.RED);
             flam_anim_Affection.setVisibility(View.GONE);
             flam_anim_love.setVisibility(View.VISIBLE);
             flam_anim_marriage.setVisibility(View.GONE);
             flam_anim_friend.setVisibility(View.GONE);
             flam_anim_enemy.setVisibility(View.GONE);
             flam_anim_siblings.setVisibility(View.GONE);
             break;
         case "Affection":
             output_text.setText(Data);
             output_text.setTextColor(Color.GRAY);
             flam_anim_Affection.setVisibility(View.VISIBLE);
             flam_anim_love.setVisibility(View.GONE);
             flam_anim_marriage.setVisibility(View.GONE);
             flam_anim_friend.setVisibility(View.GONE);
             flam_anim_enemy.setVisibility(View.GONE);
             flam_anim_siblings.setVisibility(View.GONE);
             break;
         case "Marriage":
             output_text.setText(Data);
             output_text.setTextColor(Color.GREEN);
             flam_anim_Affection.setVisibility(View.GONE);
             flam_anim_love.setVisibility(View.GONE);
             flam_anim_marriage.setVisibility(View.VISIBLE);
             flam_anim_friend.setVisibility(View.GONE);
             flam_anim_enemy.setVisibility(View.GONE);
             flam_anim_siblings.setVisibility(View.GONE);
             break;
         case "Enemies":
             output_text.setText(Data);
             output_text.setTextColor(Color.BLUE);
             flam_anim_Affection.setVisibility(View.GONE);
             flam_anim_love.setVisibility(View.GONE);
             flam_anim_marriage.setVisibility(View.GONE);
             flam_anim_friend.setVisibility(View.GONE);
             flam_anim_enemy.setVisibility(View.VISIBLE);
             flam_anim_siblings.setVisibility(View.GONE);
             break;
         case "Sibling":
             output_text.setText(Data);
             output_text.setTextColor(Color.YELLOW);
             flam_anim_Affection.setVisibility(View.GONE);
             flam_anim_love.setVisibility(View.GONE);
             flam_anim_marriage.setVisibility(View.GONE);
             flam_anim_friend.setVisibility(View.GONE);
             flam_anim_enemy.setVisibility(View.GONE);
             flam_anim_siblings.setVisibility(View.VISIBLE);
             break;
         default:
             break;

     }
    }

    private void StoreDataWhoPair(String name1,String name2,String R_status)
    {
        if(connected)
        {
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault()).format(new Date());
            String device= Build.MODEL;
            int V_id=BuildConfig.VERSION_CODE;
            HashMap<String,Object>hashMap=new HashMap<>();
            hashMap.put("First Name",name1);
            hashMap.put("SecondName",name2);
            hashMap.put("RelationStatus",R_status);
            hashMap.put("DeviceModel",device);
            hashMap.put("Apk_V",V_id);
            hashMap.put("Time",currentTime);
            hashMap.put("Date",currentDate);
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Flames").child(firebaseUser.getUid()).push();
            reference.setValue(hashMap);
        }
    }
    private void Check_connection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
        {
            connected = false;
        }
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