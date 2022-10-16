package com.spoof.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spoof.Model.Post;
import com.spoof.Model.User;
import com.spoof.lm.ChatActivity;
import com.spoof.lm.CheckingActivity;
import com.spoof.lm.EditProfileActivity;
import com.spoof.lm.R;
import com.spoof.lm.SignUpActivity;
import com.spoof.lm.StartActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class ProfileFragment extends Fragment {
    private BottomNavigationView bottom_nv_f_profile;
    private String User_id,PostCount;
    private Fragment FinalFragment;
    private TextView username_f_profile,bio_f_profile,fullname_f_profile,cmail_f_profile,post_count_f_profile,profile_text_for_dept,profile_text_for_Cast,profile_text_for_RegNO;
    private ImageView profile_img_f_profile,go_message;
    private Button propose_btn_f_profile;
    private CardView addUsers;
    private User user;
    private boolean isMe,partner;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private Context mContext;
    private ProgressBar progressBar;
    public ProfileFragment(String user_id,Context mContext) {
        this.User_id=user_id;
        this.mContext=mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        //find objects
        username_f_profile=view.findViewById(R.id.username_f_profile);
        bio_f_profile=view.findViewById(R.id.bio_f_profile);
        profile_img_f_profile=view.findViewById(R.id.profile_img_f_profile);
        propose_btn_f_profile=view.findViewById(R.id.propose_btn_f_profile);
        bottom_nv_f_profile=view.findViewById(R.id.bottom_nv_f_profile);
        fullname_f_profile=view.findViewById(R.id.fullname_f_profile);
        cmail_f_profile=view.findViewById(R.id.cmail_f_profile);
        post_count_f_profile=view.findViewById(R.id.post_count_f_profile);
        profile_text_for_dept=view.findViewById(R.id.profile_text_for_dept);
        profile_text_for_Cast=view.findViewById(R.id.profile_text_for_Cast);
        profile_text_for_RegNO=view.findViewById(R.id.profile_text_for_RegNO);
        progressBar=view.findViewById(R.id.progressBar_f_Profile);
        go_message=view.findViewById(R.id.go_message);
        addUsers=view.findViewById(R.id.addUsers);
        //
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_f_profile,new PostFragment(User_id)).commit();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        checkWhois();
        getPostCount();
        GetUserinfo();
        changeFragment();
        return view;
    }
    private void changeFragment()
    {
        bottom_nv_f_profile.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.post_f_profile:
                        FinalFragment=new PostFragment(User_id);
                        break;
                    case R.id.pin_f_profile:
                        FinalFragment=new PinFragment();
                        break;
                }
               getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_f_profile,FinalFragment).commit();
                return true;
            }
        });
    }
    private void GetUserinfo()
    {
        databaseReference.child("Users").child(User_id).child("General").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user=snapshot.getValue(User.class);
                assert user != null;
                setDatas(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getPostCount()
    {
        databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List <Post> postList=new ArrayList<>();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Post post=dataSnapshot.getValue(Post.class);
                    assert post != null;
                    if(post.getPublisher().equals(User_id))
                    {
                        postList.add(post);
                    }
                }
                PostCount=String.valueOf(postList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void checkWhois()
    {
        isMe= User_id.equals(firebaseUser.getUid());
    }
    private void setDatas(User user)
    {
        progressBar.setVisibility(View.GONE);
        propose_btn_f_profile.setVisibility(View.VISIBLE);
        username_f_profile.setText(user.getUsername());
        fullname_f_profile.setText(user.getFullname());
        bio_f_profile.setText(user.getBio()+" ");
        post_count_f_profile.setText(PostCount);
        if(user.getCast()!=null)
        {
            profile_text_for_Cast.setVisibility(View.VISIBLE);
            profile_text_for_Cast.setText(user.getCast());
        }else
        {
            profile_text_for_Cast.setVisibility(View.GONE);
        }
        if (user.getRegisterNumber()!=null)
        {
            profile_text_for_RegNO.setVisibility(View.VISIBLE);
            profile_text_for_RegNO.setText(user.getRegisterNumber());
        }else
        {
            profile_text_for_RegNO.setVisibility(View.GONE);
        }

        if(user.getDept()!=null)
        {
            profile_text_for_dept.setVisibility(View.VISIBLE);
            profile_text_for_dept.setText(user.getDept());
        }else
        {
            profile_text_for_dept.setVisibility(View.GONE);
        }
        Glide.with(mContext).load(user.getImageUrl()).into(profile_img_f_profile);
        if(!TextUtils.isEmpty(user.getCmail()))
        {
            cmail_f_profile.setVisibility(View.VISIBLE);
            cmail_f_profile.setText(user.getCmail());
            openGmail(user.getCmail());
        }else
        {
            cmail_f_profile.setVisibility(View.GONE);
        }
       if(isMe)
       {
           EditProfile();
           SignUpPage();
       }
       else
       {
           checkButtonTag();
       }
        bottom_nv_f_profile.setVisibility(View.VISIBLE);
    }
    private void SignUpPage()
    {
        if(!user.getCast().equals("Student"))
        {
            addUsers.setVisibility(View.VISIBLE);
        }
        addUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);
            }
        });
    }
    private void openGmail(String email)
    {
        cmail_f_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                startActivity(Intent.createChooser(emailIntent, null));
            }
        });
    }
    private void checkButtonTag()
    {

        databaseReference.child("PROPOSES").child(firebaseUser.getUid()).child(User_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tag;
                if(snapshot.exists())
                {
                    tag=snapshot.getValue(String.class);
                }else
                {
                    tag="";
                }
                assert tag != null;
                if(tag.equals("REQUEST"))
                {
                    AcceptRequest();
                }else
                {
                    databaseReference.child("PROPOSES").child(User_id).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String tag=snapshot.getValue(String.class);
                            setButtonRoll(tag);
                            //check if the profile is partner already enabling message button other wise Gone
                            if(partner)
                            {
                                go_message.setVisibility(View.VISIBLE);
                                go_message.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(mContext, ChatActivity.class);
                                        intent.putExtra("profiled",User_id);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });
                            }else
                            {
                                go_message.setVisibility(View.GONE);
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
    private void EditProfile()
    {
        propose_btn_f_profile.setText("Edit Profile");
        propose_btn_f_profile.setBackgroundColor(Color.BLACK);
        propose_btn_f_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), EditProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                intent.putExtra("userid",User_id);
                startActivity(intent);
            }
        });
    }
    private void setButtonRoll(String tag)
    {
        partner=false;
        if(tag==null)
        {
            tag="null";
        }
        if(tag.equals("REQUEST") || tag.equals("NOTIFY"))
        {
            partner=false;
            CancelPropose();
        }else if(tag.equals("MATCHED"))
        {
            partner=true;
            Remove();
        }else {
            partner=false;
            Request();
        }
    }

    private void AcceptRequest()
    {
        propose_btn_f_profile.setText("Accept Request");
        propose_btn_f_profile.setBackgroundColor(Color.BLUE);
        propose_btn_f_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("PROPOSES").child(User_id).child(firebaseUser.getUid()).setValue("MATCHED");
                databaseReference.child("PROPOSES").child(firebaseUser.getUid()).child(User_id).setValue("MATCHED");
                databaseReference.child("Propose_notify").child(User_id).child(firebaseUser.getUid()).setValue("notify");
            }
        });
    }
    private void Request()
    {
        propose_btn_f_profile.setText("SEND PARTNER REQUEST");
        propose_btn_f_profile.setBackgroundColor(Color.MAGENTA);
        propose_btn_f_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object>hashMap=new HashMap<>();
                hashMap.put(firebaseUser.getUid(),"NOTIFY");
                databaseReference.child("PROPOSES").child(User_id).updateChildren(hashMap);
            }
        });
    }

    private void Remove()
    {
        propose_btn_f_profile.setText("Remove Partner");
        propose_btn_f_profile.setBackgroundColor(Color.parseColor("#FF3700B3"));
        propose_btn_f_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("PROPOSES").child(firebaseUser.getUid()).child(User_id).removeValue();
                databaseReference.child("PROPOSES").child(User_id).child(firebaseUser.getUid()).removeValue();
            }
        });
    }
    private void CancelPropose()
    {
        propose_btn_f_profile.setText("Cancel Propose");
        propose_btn_f_profile.setBackgroundColor(Color.GRAY);
        propose_btn_f_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("PROPOSES").child(User_id).child(firebaseUser.getUid()).removeValue();
            }
        });
    }

}