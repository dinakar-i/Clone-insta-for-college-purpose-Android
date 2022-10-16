package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.spoof.MyMethods.Methods;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class UploadNewsActivity extends AppCompatActivity {
   private DatabaseReference databaseReference;
   private FirebaseUser firebaseUser;
   private TextView upload_news_btn;
   private EditText news_title,news_text;
   private CardView attach_button_container;
   private RelativeLayout news_banner_container;
   private ImageView news_banner,discard_image,arrow_back_to_news;
   private Spinner spinner_for_who_see;
   private Uri ImageUri;
   private String title,whoSee,news;
   private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_upload_news);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        //
        upload_news_btn=findViewById(R.id.upload_news_btn);
        news_title=findViewById(R.id.news_title);
        news_text=findViewById(R.id.news_text);
        attach_button_container=findViewById(R.id.attach_button_container);
        news_banner_container=findViewById(R.id.news_banner_container);
        news_banner=findViewById(R.id.news_banner);
        discard_image=findViewById(R.id.discard_image);
        arrow_back_to_news=findViewById(R.id.arrow_back_to_news);
        spinner_for_who_see=findViewById(R.id.spinner_for_who_see);
        //
        ButtonListener();
    }
    private void ButtonListener()
    {
        String[] whoseelist=getResources().getStringArray(R.array.WhoSee);
        ArrayAdapter adapter_for_see=new ArrayAdapter(UploadNewsActivity.this,android.R.layout.simple_spinner_item,whoseelist);
        adapter_for_see.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_for_who_see.setAdapter(adapter_for_see);

        arrow_back_to_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UploadNewsActivity.this,NewsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        upload_news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData();
            }
        });

        attach_button_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),2);
            }
        });

        discard_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUri=null;
                news_banner_container.setVisibility(View.GONE);
                attach_button_container.setVisibility(View.VISIBLE);
            }
        });
    }
    private  void  GetData()
    {
        title=news_title.getText().toString();
        news=news_text.getText().toString();
        whoSee=spinner_for_who_see.getSelectedItem().toString();
        if(title.replace(" ","").length()>0 && news.replace(" ","").length()>0)
        {
            if(title.replace(" ","").length()>=4)
            {
                if(news.replace(" ","").length()>=10)
                {
                           if(!TextUtils.isEmpty(whoSee))
                           {
                               UploadNews();
                           }
                }else
                {
                    Toast.makeText(UploadNewsActivity.this, "News length must be large than 9.", Toast.LENGTH_SHORT).show();
                }
            }else
            {
                Toast.makeText(UploadNewsActivity.this, "Title length must be large than 3.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(UploadNewsActivity.this, "Make sure all field are not Empty!.", Toast.LENGTH_SHORT).show();
        }
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
    private void UploadNews()
    {
       if(ImageUri!=null)
       {
           ProgressDialog pd=new ProgressDialog(this);
           pd.setMessage("updating");
           pd.show();
           String imgName=System.currentTimeMillis()+""+firebaseUser.getUid()+""+getFileExtension(ImageUri);
           final StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("News").child(firebaseUser.getUid()).child(imgName);
           uploadTask=storageReference.putFile(ImageUri);
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
               public void onComplete(@NonNull Task <Uri>task) {
                   if(task.isSuccessful())
                   {
                       Uri downloadUri=task.getResult();
                       String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                       String currentTime = new SimpleDateFormat("HH:mm a", Locale.getDefault()).format(new Date());
                       String NewsKey=databaseReference.child("News").push().getKey();
                       HashMap<String,Object> hashMap=new HashMap<>();
                       hashMap.put("newsText",news);
                       hashMap.put("currentDate",currentDate);
                       hashMap.put("currentTime",currentTime);
                       hashMap.put("Publisher",firebaseUser.getUid());
                       hashMap.put("whoSee",whoSee);
                       hashMap.put("newsKey",NewsKey);
                       if(downloadUri!=null)
                       {
                           hashMap.put("ImageUrl",downloadUri.toString());
                       }
                       hashMap.put("newsTitle",title);
                       databaseReference.child("News").child(NewsKey).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful())
                               {
                                   news_title.setText("");
                                   news_text.setText("");
                                   attach_button_container.setVisibility(View.VISIBLE);
                                   news_banner_container.setVisibility(View.GONE);
                                   deleteNotifyList();
                                   Toast.makeText(UploadNewsActivity.this, "News Successfully uploaded", Toast.LENGTH_SHORT).show();
                                   pd.dismiss();
                                   Intent intent=new Intent(UploadNewsActivity.this,NewsActivity.class);
                                   intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                                   startActivity(intent);
                               }
                           }
                       });
                   }
                   else
                   {
                       Toast.makeText(UploadNewsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                       pd.dismiss();
                   }
               }
           });
       }else
       {
           String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
           String currentTime = new SimpleDateFormat("HH:mm a", Locale.getDefault()).format(new Date());
           String NewsKey=databaseReference.child("News").push().getKey();
           HashMap<String,Object> hashMap=new HashMap<>();
           hashMap.put("newsText",news);
           hashMap.put("currentDate",currentDate);
           hashMap.put("currentTime",currentTime);
           hashMap.put("Publisher",firebaseUser.getUid());
           hashMap.put("whoSee",whoSee);
           hashMap.put("newsKey",NewsKey);
           hashMap.put("newsTitle",title);
           databaseReference.child("News").child(NewsKey).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful())
                   {
                       deleteNotifyList();
                       Toast.makeText(UploadNewsActivity.this, "News Successfully uploaded", Toast.LENGTH_SHORT).show();
                       Intent intent=new Intent(UploadNewsActivity.this,NewsActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(intent);
                   }
               }
           });
       }
    }

    private void deleteNotifyList()
    {
        databaseReference.child("News_notify").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    String key=dataSnapshot.getKey();
                    String cast=dataSnapshot.getValue(String.class);
                    Log.d("work1", key+" "+cast+"  whosee"+whoSee);
                    assert key!=null;
                   if(!whoSee.equals("All"))
                   {
                       if(cast.equals(whoSee))
                       {
                           databaseReference.child("News_notify").child(key).removeValue();
                       }
                   }else
                   {
                       databaseReference.child("News_notify").child(key).removeValue();
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK)
        {
            assert data != null;
            ImageUri=data.getData();
            if(ImageUri!=null)
            {
                news_banner_container.setVisibility(View.VISIBLE);
                Glide.with(UploadNewsActivity.this).load(ImageUri).into(news_banner);
                attach_button_container.setVisibility(View.GONE);
            }
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