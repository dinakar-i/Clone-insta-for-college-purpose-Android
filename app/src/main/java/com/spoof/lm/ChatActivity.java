package com.spoof.lm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Adapter.MessageAdapter;
import com.spoof.Model.Message;
import com.spoof.Model.Msg_notify;
import com.spoof.Model.User;
import com.spoof.MyMethods.Methods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    private String User_id;
    private TextView username_f_chat_activity,chat_status;
    private EditText message_text_f_chat_activity;
    private Button send_message_btn;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView_for_message;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private ImageView go_back_btn_f_chat_activity;
    private boolean isPost=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_chat);

        username_f_chat_activity=findViewById(R.id.username_f_chat_activity);
        chat_status=findViewById(R.id.chat_status);
        message_text_f_chat_activity=findViewById(R.id.message_text_f_chat_activity);
        send_message_btn=findViewById(R.id.send_message_btn);
        go_back_btn_f_chat_activity=findViewById(R.id.go_back_btn_f_chat_activity);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        recyclerView_for_message=findViewById(R.id.recyclerView_for_message);
        recyclerView_for_message.setHasFixedSize(true);

          ScrollBottom();

        messageList=new ArrayList<>();
        messageAdapter=new MessageAdapter(ChatActivity.this,messageList);
        recyclerView_for_message.setAdapter(messageAdapter);
        GetIntent();
        GetMessageFromNotification();
        Button_listener();
        GetUserInfo();
        readMessage();
        Listener();
        ListeningMessages();
    }

    private void GetIntent()
    {
        Intent intent=getIntent();
        User_id=intent.getStringExtra("profiled");
    }
    private void Button_listener()
    {
        username_f_chat_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("profiled",User_id);
                startActivity(intent);
            }
        });
        go_back_btn_f_chat_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatActivity.this,MessageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
    private void ScrollBottom()
    {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView_for_message.setLayoutManager(linearLayoutManager);
    }
    private void Listener()
    {
      message_text_f_chat_activity.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 String data;
                 data=""+charSequence.toString().replace(" ","");
                 if(data.length()>0)
                 {
                     isPost=false;
                     send_message_btn.setText("Send");
                 }
                 else
                 {
                     isPost=true;
                     send_message_btn.setText("Send Image");
                 }
          }

          @Override
          public void afterTextChanged(Editable editable) {

          }
      });
    }
    private void GetUserInfo()
    {
        databaseReference.child("Users").child(User_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                username_f_chat_activity.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendImage()
    {
        Intent intent=new Intent(ChatActivity.this,chatImage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("receiver",User_id);
        startActivity(intent);
    }
    private void ListeningMessages()
    {
      send_message_btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
             if(isPost)
             {
                sendImage();
             }else
             {
                 String msg=message_text_f_chat_activity.getText().toString();
                 message_text_f_chat_activity.setText("");
                 if(!TextUtils.isEmpty(msg) && msg.replace(" ","").length()!=0)
                 {
                     sendMessage(msg,firebaseUser.getUid(),User_id);
                 }else
                 {
                     Toast.makeText(ChatActivity.this, "box can't empty!", Toast.LENGTH_SHORT).show();
                 }
             }
          }
      });
    }

    public void sendMessage(String msg,String Current_user,String receiverId)
    {
        String key= databaseReference.child("Chats").push().getKey();
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm a", Locale.getDefault()).format(new Date());
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("message",msg);
        hashMap.put("sender",Current_user);
        hashMap.put("receiver",receiverId);
        hashMap.put("status","send");
        hashMap.put("messageId",key);
        hashMap.put("type","text");
        hashMap.put("time",currentTime);
        hashMap.put("date",currentDate);
        assert key != null;
        databaseReference.child("Chats").child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete())
                {
                    HashMap<String,Object>msgn=new HashMap<>();
                    msgn.put("sender",firebaseUser.getUid());
                    msgn.put("msg",msg);
                    msgn.put("notify_key",key);
                    assert key != null;
                    databaseReference.child("Msg_notify").child(User_id).child(key).setValue(msgn);
                    ScrollBottom();
                }
            }
        });
    }
    private void readMessage()
    {
        databaseReference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Message message=dataSnapshot.getValue(Message.class);
                    assert message != null;
                    if(!message.getStatus().equals("deleted"))
                    {
                        if(message.getReceiver().equals(User_id)&&message.getSender().equals(firebaseUser.getUid())||message.getSender().equals(User_id)&&message.getReceiver().equals(firebaseUser.getUid()))
                        {
                            messageList.add(message);
                        }
                    }
                   messageAdapter.notifyDataSetChanged();
                }
               ScrollBottom();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void GetMessageFromNotification()
    {
        Intent receivedIntent=getIntent();
        Bundle input= RemoteInput.getResultsFromIntent(receivedIntent);
        if(input!=null)
        {
            int notify_id=receivedIntent.getIntExtra("notify_id",0);
            String CH_ID=receivedIntent.getStringExtra("CH_ID");
            String msg=String.valueOf(input.getCharSequence("key_replay"));
            if(msg!=null)
            {
                sendMessage(msg,firebaseUser.getUid(),User_id);

                //builder for notification
                Notification builder=new NotificationCompat.Builder(getApplicationContext(),CH_ID)
                        .setColor(Color.BLACK)
                        .setSmallIcon(R.drawable.ic_baseline_chat_24)
                        .setContentTitle("Message send").setContentText(msg)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setAutoCancel(true).build();

                NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(getApplicationContext());
                notificationManagerCompat.notify(notify_id,builder);
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