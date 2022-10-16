package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.spoof.MyMethods.Methods;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class chatImage extends AppCompatActivity {

    private ImageView imageView,go_back_btn;
    private TextView send_img_btn;
    private Uri uri;
    private StorageReference storageReference;
    private String CurrentUser;
    private String myUrl,User_id;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_image);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        imageView=findViewById(R.id.imageView);
        go_back_btn=findViewById(R.id.go_back_btn_to_chat);
        send_img_btn=findViewById(R.id.send_img_btn);

        CurrentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference().child("Chats").child(CurrentUser);


        databaseReference=FirebaseDatabase.getInstance().getReference();
        Intent intent=getIntent();
        User_id=intent.getStringExtra("receiver");
        selectImage();
        Button_listener();
    }
    private String getFileExtension(Uri uri)
    {
        String  u=uri.toString();
        if(u.contains(".")) {
            String extension = u.substring(u.lastIndexOf("."));
            return extension;
        }else
        {
            return null;
        }
    }
    private void Button_listener()
    {
        go_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(chatImage.this,ChatActivity.class);
                intent.putExtra("profiled",User_id);
                startActivity(intent);
            }
        });

        send_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog pd=new ProgressDialog(chatImage.this);
                pd.setMessage("Uploading...");
                pd.show();
                if(uri!=null)
                {
                    final StorageReference reference=storageReference.child(System.currentTimeMillis()+""+CurrentUser+getFileExtension(uri));
                    StorageTask uploadTask=reference.putFile(uri);
                    uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if(!task.isSuccessful())
                            {
                                pd.dismiss();
                                throw task.getException();
                            }
                            return reference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {
                                Uri DownloadUri=task.getResult();
                                myUrl=DownloadUri.toString();

                                String key= databaseReference.child("Chats").push().getKey();

                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                String currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault()).format(new Date());
                                HashMap<String,Object>hashMap=new HashMap<>();
                                hashMap.put("message",myUrl);
                                hashMap.put("sender",CurrentUser);
                                hashMap.put("receiver",User_id);
                                hashMap.put("status","send");
                                hashMap.put("messageId",key);
                                hashMap.put("type","img");
                                hashMap.put("time",currentTime);
                                hashMap.put("date",currentDate);
                                databaseReference.child("Chats").child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pd.dismiss();
                                        Intent intent=new Intent(chatImage.this,ChatActivity.class);
                                        intent.putExtra("profiled",User_id);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }else
                            {
                                Toast.makeText(chatImage.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(chatImage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                {
                    pd.dismiss();
                    Toast.makeText(chatImage.this, "No Incidents selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void selectImage()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==1)
        {
            uri=data.getData();
            if(uri!=null)
            {
            imageView.setImageURI(uri);
            }else
            {
                Intent intent=new Intent(chatImage.this,ChatActivity.class);
                intent.putExtra("profiled",User_id);
                startActivity(intent);
            }
        }else
        {
            Intent intent=new Intent(chatImage.this,ChatActivity.class);
            intent.putExtra("profiled",User_id);
            startActivity(intent);
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