package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private String Email,Password,UserId;
    private Button Login_btn;
    private TextView Forgot_text_btn;
    private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);
        //find Objects
        email=findViewById(R.id.email_f_l);
        password=findViewById(R.id.password_f_l);
        Login_btn=findViewById(R.id.Login_btn_f_s);
        Forgot_text_btn=findViewById(R.id.forgot_password_txt_f_l);
        //
        GetData();
        forgot_password();
    }
    private void GetData()
    {
        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email=email.getText().toString().replace(" ","");;
                Password=password.getText().toString().replace(" ","");;
                if(!TextUtils.isEmpty(Email)&&!TextUtils.isEmpty(Password))
                {
                    Login();
                }
            }
        });
    }
    private void Login()
    {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Login...");
        pd.show();
        Auth=FirebaseAuth.getInstance();
        Auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser firebaseUser=Auth.getCurrentUser();
                    UserId=firebaseUser.getUid();
                    write_login_data_on_local_storage();
                    pd.dismiss();
                    Intent intent=new Intent(LoginActivity.this, StartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else
                {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private  void write_login_data_on_local_storage()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("PREF",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Login",UserId);
        editor.apply();
    }
    private void forgot_password()
    {
       Forgot_text_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String f_mail=email.getText().toString().replace(" ","");
               if(!TextUtils.isEmpty(f_mail))
               {
                   email.setText("");
                   FirebaseAuth.getInstance().sendPasswordResetEmail(f_mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful())
                           {
                               Toast.makeText(LoginActivity.this, "we will send forgot email to your mail id.so check the mail.Thank you!☺.", Toast.LENGTH_SHORT).show();
                           }else
                           {
                               Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                           }
                       }
                   });
               }else
               {
                   Toast.makeText(LoginActivity.this, "Enter your mail", Toast.LENGTH_SHORT).show();
               }
           }
       });

    }
}