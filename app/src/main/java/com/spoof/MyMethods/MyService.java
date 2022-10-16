package com.spoof.MyMethods;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Model.Message;
import com.spoof.Model.User;

public class MyService extends Service {

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    @Override
    public void onCreate() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        new Thread(new Runnable() {
            @Override
            public void run() {
                MessageNotify();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestNotify();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                MatchNotify();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                LikeNotify();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommendNotify();
            }
        }).start();

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void MessageNotify()
    {
        databaseReference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);

                    //Listening New Messages create notification
                    if (message.getReceiver().equals(firebaseUser.getUid()) && message.getStatus().equals("send")) {

                        //change status to received
                        databaseReference.child("Chats").child(message.getMessageId()).child("status").setValue("received");

                        //get sender name
                        databaseReference.child("Users").child(message.getSender()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user=snapshot.getValue(User.class);

                               if(user!=null)
                               {
                                   ChatNotification chatNotification =new ChatNotification(getApplicationContext(),message,user.getUsername());
                                   chatNotification.execute(user.getImageUrl());
                               }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void RequestNotify()
    {
        databaseReference.child("PROPOSES").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                   String key=dataSnapshot.getValue(String.class);
                    assert key != null;
                    if(key.equals("NOTIFY"))
                   {
                       String RequesterID=dataSnapshot.getKey();
                       assert  RequesterID!=null;
                       databaseReference.child("Users").child(RequesterID).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               User user=snapshot.getValue(User.class);
                               if(user!=null)
                               {
                                   RequestNotification requestNotification=new RequestNotification(getApplicationContext(),user.getUserId(),user.getUsername(),user.getUsername()+" has request Us!.");
                                   requestNotification.execute(user.getImageUrl());
                                   databaseReference.child("PROPOSES").child(firebaseUser.getUid()).child(user.getUserId()).setValue("REQUEST");
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void MatchNotify()
    {
        databaseReference.child("Propose_notify").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    String key=dataSnapshot.getValue(String.class);
                    assert key != null;
                    if(key.equals("notify"))
                    {
                        String MatcherID=dataSnapshot.getKey();
                        assert  MatcherID!=null;
                        databaseReference.child("Users").child(MatcherID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user=snapshot.getValue(User.class);
                                if(user!=null)
                                {
                                    RequestNotification requestNotification=new RequestNotification(getApplicationContext(),user.getUserId(),user.getUsername(),user.getUsername()+" has accept you request😀.");
                                    requestNotification.execute(user.getImageUrl());
                                    databaseReference.child("Propose_notify").child(firebaseUser.getUid()).child(user.getUserId()).removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void LikeNotify()
    {
        databaseReference.child("Liked_notify").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                       String imagurl=dataSnapshot.getValue(String.class);
                       assert imagurl != null;
                        String MatcherID=dataSnapshot.getKey();
                        assert  MatcherID!=null;
                        databaseReference.child("Users").child(MatcherID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user=snapshot.getValue(User.class);
                                if(user!=null)
                                {
                                    LikeNotification likeNotification=new LikeNotification(getApplicationContext(),user.getUsername(),user.getUsername()+" has Liked Your Post👍.");
                                    likeNotification.execute(imagurl);
                                    databaseReference.child("Liked_notify").child(firebaseUser.getUid()).child(user.getUserId()).removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void CommendNotify()
    {
        databaseReference.child("Commend_notify").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    String imagurl=dataSnapshot.getValue(String.class);
                    assert imagurl != null;
                    String MatcherID=dataSnapshot.getKey();
                    assert  MatcherID!=null;
                    databaseReference.child("Users").child(MatcherID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user=snapshot.getValue(User.class);
                            if(user!=null)
                            {
                                CommendNotification commendNotification=new CommendNotification(getApplicationContext(),user.getUserId(),user.getUsername(),user.getUsername()+" is Commented on your Post.");
                                commendNotification.execute(imagurl);
                                databaseReference.child("Commend_notify").child(firebaseUser.getUid()).child(user.getUserId()).removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
