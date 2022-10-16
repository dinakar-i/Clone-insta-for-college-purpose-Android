package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.spoof.Model.User;
import com.spoof.MyMethods.Methods;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private TextView save_text_f_edit_profile;
    private ImageView close_btn_f_edit_profile,profile_img_f_edit_profile;
    private EditText username_f_edit_profile,fullname_f_edit_profile,bio_f_edit_profile,email_f_edit_profile;
    private String User_id,username,fullname,bio,email;
    private Button save_btn_f_edit_profile;
    private DatabaseReference databaseReference;
    Uri mImageUri;
    StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_edit_profile);
        save_text_f_edit_profile=findViewById(R.id.save_text_f_edit_profile);
        save_btn_f_edit_profile=findViewById(R.id.save_btn_f_edit_profile);
        close_btn_f_edit_profile=findViewById(R.id.close_btn_f_edit_profile);
        username_f_edit_profile=findViewById(R.id.username_f_edit_profile);
        fullname_f_edit_profile=findViewById(R.id.fullname_f_edit_profile);
        bio_f_edit_profile=findViewById(R.id.bio_f_edit_profile);
        email_f_edit_profile=findViewById(R.id.email_f_edit_profile);
        profile_img_f_edit_profile=findViewById(R.id.profile_img_f_edit_profile);

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        getIntend();
        get_an_set_details();
        ListeningButtons();
        ChangeProfile();
    }
    void getIntend()
    {
        Intent intent=getIntent();
        User_id=intent.getStringExtra("userid");
        if(User_id==null)
        {
            startActivity(new Intent(EditProfileActivity.this,StartActivity.class));
            finish();
        }
    }
    void ListeningButtons()
    {
        save_btn_f_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update_details();
            }
        });

        save_text_f_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update_details();
            }
        });
        close_btn_f_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close_activity();
            }
        });
    }
    void Update_details()
    {
        getDetails();
        if(!TextUtils.isEmpty(username) && username.length()<=23 && username.length()>=4)
        {
            if(!TextUtils.isEmpty(fullname) && fullname.length()<=40 && fullname.length()>=5)
            {
                if(bio.length()<=199)
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("Username",username);
                    hashMap.put("Fullname",fullname);
                    hashMap.put("Bio",bio);
                    hashMap.put("Cmail",email);
                    databaseReference.child(User_id).child("General").updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete())
                            {
                                databaseReference.child(User_id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete())
                                        {
                                            UpdateProfile();
                                            Toast.makeText(EditProfileActivity.this, "Profile Successfully Changed!.", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(EditProfileActivity.this,StartActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }else
                {
                    Toast.makeText(EditProfileActivity.this, "Bio must be bellow 200 character.", Toast.LENGTH_SHORT).show();
                }
            }else
            {
                Toast.makeText(EditProfileActivity.this, "Full Name up to 5 Characters to 40 Characters.", Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(EditProfileActivity.this, "Username up to 4 Characters to 23 Characters.", Toast.LENGTH_SHORT).show();
        }
    }
    void getDetails()
    {
        username=username_f_edit_profile.getText().toString().toLowerCase(Locale.ROOT).replace(" ","");
        fullname=fullname_f_edit_profile.getText().toString();
        bio=bio_f_edit_profile.getText().toString();
        email=email_f_edit_profile.getText().toString().replace(" ","");
    }
    void get_an_set_details()
    {
        databaseReference.child(User_id).child("General").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                username_f_edit_profile.setText(user.getUsername());
                fullname_f_edit_profile.setText(user.getFullname());
                bio_f_edit_profile.setText(user.getBio());
                email_f_edit_profile.setText(user.getCmail());
                Glide.with(getApplication()).load(user.getImageUrl()).into(profile_img_f_edit_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ChangeProfile()
    {
        profile_img_f_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setAspectRatio(1,1).setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
            }
        });
    }
    private String getFileExtension(Uri uri)
    {
        String  u=uri.toString();
        if(u.contains(".")) {
            String extension = u.substring(u.lastIndexOf("."));
            return extension;
        }else
        {
            return "png";
        }
    }
    private void UpdateProfile()
    {
        if(mImageUri!=null)
        {
            ProgressDialog pd=new ProgressDialog(this);
            pd.setMessage("updating");
            pd.show();
            String imgName=System.currentTimeMillis()+getFileExtension(mImageUri);
            final StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Users").child(User_id).child("Profile").child(imgName);
            uploadTask=storageReference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUri=task.getResult();
                        String myUri=downloadUri.toString();
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("ImageUrl",myUri);
                        databaseReference.child(User_id).child("General").updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete())
                                {
                                    databaseReference.child(User_id).updateChildren(hashMap);
                                }
                            }
                        });
                        pd.dismiss();
                    }else{
                        pd.dismiss();
                    }
                }
            });
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            mImageUri=result.getUri();
            Glide.with(getApplicationContext()).load(mImageUri).into(profile_img_f_edit_profile);
        }
    }
    void close_activity()
    {
        Intent intent = new Intent(EditProfileActivity.this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
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