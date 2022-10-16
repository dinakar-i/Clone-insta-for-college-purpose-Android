package com.spoof.MyMethods;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Methods {
    public  boolean connected = false;
    private Context mContext;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public Methods(Context context) {
        firebaseUser=firebaseAuth.getCurrentUser();
        this.mContext=context;
        network();
    }
    private void network()
    {

        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
    }

    public static void status(String status)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser;
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
       DatabaseReference reference=databaseReference.child("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Status",status);
        reference.updateChildren(hashMap);
    }

}
