package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
public class PostIncident extends AppCompatActivity {
    private Uri imageUri;
    private ImageView selected_image;
    private String myUrl;
    private StorageReference storageReference;
    private String CurrentUser;
    private EditText caption_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_post_incident);
        CurrentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        //find Objects
        ImageView close_btn=findViewById(R.id.close_post_btn);
        TextView post_btn=findViewById(R.id.Upload_incident_btn);
        caption_text=findViewById(R.id.Caption_Edit_text);
        selected_image=findViewById(R.id.selected_image);
        //create Storage reference
        String UserIDMy=FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference("Users").child(UserIDMy).child("Posts");
        //close button to close upload screen.
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PostIncident.this,StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadIncidents();
            }
        });
       CropImage.activity().setAspectRatio(10,11).start(this);
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
    private void uploadIncidents()
    {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Posting...");
        pd.show();
        if(imageUri!=null)
        {

            final StorageReference reference=storageReference.child(System.currentTimeMillis()+"_"+CurrentUser+getFileExtension(imageUri));
            StorageTask uploadTask=reference.putFile(imageUri);
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
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Posts");

                        String currentDate = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(new Date());
                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()); String postId=reference.push().getKey();
                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("PostId",postId);
                        hashMap.put("PostImage",myUrl);
                        hashMap.put("Caption",caption_text.getText().toString());
                        hashMap.put("Publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("PostTime",currentTime);
                        hashMap.put("PostDate",currentDate);
                        reference.child(postId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pd.dismiss();
                                Intent intent=new Intent(PostIncident.this,StartActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }else
                    {
                        Toast.makeText(PostIncident.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostIncident.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            pd.dismiss();
            Toast.makeText(PostIncident.this, "No Incidents selected!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            selected_image.setImageURI(imageUri);
        }else
        {
            startActivity(new Intent(PostIncident.this, StartActivity.class));
        }
    }
}