package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spoof.Model.User;
import com.spoof.MyMethods.Methods;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private EditText username,email,password,confirmPassword,fullname,reg_no;
    private String UserName,Email,Password,ConfirmPassword,UserId,Fullname;
    private Button Signup_btn;
    private FirebaseAuth Auth;
    private DatabaseReference reference;
    private String[] departmentsList,CastList;
    private Spinner spinner_f_debt,spinner_f_cast;
    private ProgressDialog pd;
    private String Cast,dept,RegNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_sign_up);
        //findObjects
        username=findViewById(R.id.username_f_s);
        email=findViewById(R.id.email_f_s);
        password=findViewById(R.id.password_f_s);
        confirmPassword=findViewById(R.id.confirm_password_f_s);
        Signup_btn=findViewById(R.id.SignUp_btn_f_s);
        spinner_f_debt=findViewById(R.id.spinner_f_debt);
        spinner_f_cast=findViewById(R.id.spinner_f_cast);
        reg_no=findViewById(R.id.reg_no);
        fullname=findViewById(R.id.fullname_f_s);
        Auth=FirebaseAuth.getInstance();

        //spinner for dept
        departmentsList=getResources().getStringArray(R.array.Departments);
        ArrayAdapter adapter_for_dept=new ArrayAdapter(SignUpActivity.this,android.R.layout.simple_spinner_item,departmentsList);
        adapter_for_dept.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_f_debt.setAdapter(adapter_for_dept);

        //spinner for cast
        CastList=getResources().getStringArray(R.array.CastList);
        ArrayAdapter adapter_for_cast=new ArrayAdapter(SignUpActivity.this,android.R.layout.simple_spinner_item,CastList);
        adapter_for_cast.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_f_cast.setAdapter(adapter_for_cast);

        //function calling
        GetData();
        getSpinnerData();
    }
private void getSpinnerData()
{
   spinner_f_cast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       @Override
       public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         Cast=spinner_f_cast.getSelectedItem().toString();
           if(Cast.equals("Student"))
           {
               spinner_f_debt.setVisibility(View.VISIBLE);
               reg_no.setVisibility(View.VISIBLE);
           }else
           {
               spinner_f_debt.setVisibility(View.GONE);
               reg_no.setVisibility(View.GONE);
           }
       }

       @Override
       public void onNothingSelected(AdapterView<?> adapterView) {

       }
   });
}
    private void validateData()
    {
        if(!TextUtils.isEmpty(UserName) && !TextUtils.isEmpty(Fullname) && !TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password) && !TextUtils.isEmpty(ConfirmPassword))
        {

              if(UserName.length()<=23 && UserName.length()>=4)
              {
                  if(Fullname.length()<=40 && UserName.length()>=5)
                  {
                      if(Password.equals(ConfirmPassword))
                      {
                          if(Password.length()<8)
                          {
                              Toast.makeText(SignUpActivity.this, "Password must be up 8 Characters.", Toast.LENGTH_SHORT).show();
                          }else
                          {
                              SignUp();

                          }
                      }else
                      {
                          Toast.makeText(SignUpActivity.this, "Confirm password does not match!.", Toast.LENGTH_SHORT).show();
                      }
                  }else
                  {
                      Toast.makeText(SignUpActivity.this, "Full Name up to 5 Characters to 40 Characters.", Toast.LENGTH_SHORT).show();
                  }
              }else
              {
                  Toast.makeText(SignUpActivity.this, "Username up to 4 Characters to 23 Characters.", Toast.LENGTH_SHORT).show();
              }
        }else
        {
            Toast.makeText(this, "Make use all fields is not empty.", Toast.LENGTH_SHORT).show();
        }
    }
    private void GetData()
    {
      Signup_btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              UserName=username.getText().toString().toLowerCase(Locale.ROOT).replace(" ","");
              Email=email.getText().toString().replace(" ","");
              Password=password.getText().toString().replace(" ","");
              ConfirmPassword=confirmPassword.getText().toString().replace(" ","");
              Fullname=fullname.getText().toString();
              if(Cast!=null && !Cast.equals("Cast"))
              {
                  if(Cast.equals("Student"))
                  {
                      dept=spinner_f_debt.getSelectedItem().toString();
                      RegNo=reg_no.getText().toString();

                  }else
                  {
                      dept="null";
                  }
                  if(Cast.equals("Student"))
                  {
                      if(!dept.equals("DEPARTMENT"))
                      {
                         if(!TextUtils.isEmpty(RegNo) && RegNo.length()==12)
                         {
                             validateData();
                         }else
                         {
                             Toast.makeText(SignUpActivity.this, "Please Check the Register Number.", Toast.LENGTH_SHORT).show();
                         }
                      }else
                      {
                          Toast.makeText(SignUpActivity.this, "Please Select Department.", Toast.LENGTH_SHORT).show();
                      }
                  }else if(!Cast.equals("Student"))
                  {
                      validateData();
                  }
              }
              else
              {
                  Toast.makeText(SignUpActivity.this, "Please Select Cast.", Toast.LENGTH_SHORT).show();

              }

          }
      });
    }
    private void SignUp()
    {
        pd=new ProgressDialog(this);
        pd.setMessage("Creating account...");
        pd.show();
        Auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser firebaseUser=Auth.getCurrentUser();
                    UserId=firebaseUser.getUid();
                    StoreData();
                }else if(!task.isSuccessful())
                {
                    pd.dismiss();
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void StoreData()
    {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss a zzzz", Locale.getDefault()).format(new Date());
        //for genral
        HashMap<String,Object>General=new HashMap<>();
        General.put("Username",UserName);
        General.put("Fullname",Fullname);
        General.put("Email",Email);
        General.put("UserId",UserId);
        General.put("ImageUrl","https://firebasestorage.googleapis.com/v0/b/cbot-11436.appspot.com/o/default-user.png?alt=media&token=89c43db3-968f-497a-bd39-a526b13c1ea1");
        General.put("Password",Password);
        General.put("Bio","Hey I am in.");
        General.put("FirstAppearance",currentDate+"/"+currentTime);
        General.put("Time",currentTime);
        General.put("Date",currentDate);
        General.put("RegisterNumber",RegNo);
        General.put("Cast",Cast);
        if(Cast.equals("Student"))
        {
            General.put("Dept",dept);
        }
        //for root
        HashMap<String,Object>Userinfo=new HashMap<>();
        Userinfo.put("Username",UserName);
        Userinfo.put("Fullname",Fullname);
        Userinfo.put("Email",Email);
        Userinfo.put("UserId",UserId);
        Userinfo.put("RegisterNumber",RegNo);
        Userinfo.put("ImageUrl","https://firebasestorage.googleapis.com/v0/b/cbot-11436.appspot.com/o/default-user.png?alt=media&token=89c43db3-968f-497a-bd39-a526b13c1ea1");
        Userinfo.put("Bio","Hey I am in.");
        Userinfo.put("Cast",Cast);
        if(Cast.equals("Student"))
        {
            Userinfo.put("Dept",dept);
        }

        reference=FirebaseDatabase.getInstance().getReference().child("Users").child(UserId).child("General");
        DatabaseReference reference2=FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);
        reference2.setValue(Userinfo);
        reference.setValue(General).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
             public void onSuccess(Void unused) {
                Toast.makeText(SignUpActivity.this, "Account create successfully!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}