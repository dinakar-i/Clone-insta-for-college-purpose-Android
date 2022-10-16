package com.spoof.MyMethods;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import com.spoof.Model.Message;
import com.spoof.lm.ChatActivity;
import com.spoof.lm.PostDetail;
import com.spoof.lm.R;
import com.spoof.lm.StartActivity;

public class Notification {
   private Context mContext;
    private final static String CH_ID="BOT_";
    public Notification(Context mContext) {
        this.mContext=mContext;
        CreateNotificationChannel();
    }

    public void Chat_notification(Message message, String username, Bitmap bitmap)
   {
       //create intent
       Intent intent=new Intent(mContext,ChatActivity.class);
       intent.putExtra("profiled",message.getSender());
       PendingIntent pendingIntent=PendingIntent.getActivity(mContext,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

       //generate random notification key
       int min,max;
       min=1;
       max=1000;
       int id = (int)(Math.random()*(max-min+1)+min);

       //builder for notification
       NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext,CH_ID);
       builder.setColor(Color.BLACK);
       builder.setSmallIcon(R.drawable.ic_baseline_smart_toy_24);
       builder.setLargeIcon(bitmap);
       builder.setContentTitle(username).setContentText(message.getMessage());
       builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
       builder.addAction(action(message.getSender(),id));
       builder.setAutoCancel(true).setContentIntent(pendingIntent).build();
       NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(mContext);
       notificationManagerCompat.notify(id,builder.build());
   }

    public void Request_notification(String userid, String username,String msg, Bitmap bitmap)
    {

       Intent intent=new Intent(mContext, StartActivity.class);
        intent.putExtra("profiled",userid);
       PendingIntent pendingIntent=PendingIntent.getActivity(mContext,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //generate random notification key
        int min,max;
        min=1;
        max=1000;
        int id = (int)(Math.random()*(max-min+1)+min);

        //builder for notification
        NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext,CH_ID);
        builder.setColor(Color.BLACK);
        builder.setSmallIcon(R.drawable.ic_baseline_smart_toy_24);
        builder.setLargeIcon(bitmap);
        builder.setContentTitle(username).setContentText(msg);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setAutoCancel(true).setContentIntent(pendingIntent).build();
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(mContext);
        notificationManagerCompat.notify(id,builder.build());
    }

    public void Commend_notification(String username,String msg, Bitmap bitmap)
    {
        //generate random notification key
        int min,max;
        min=1;
        max=1000;
        int id = (int)(Math.random()*(max-min+1)+min);

        //builder for notification
        NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext,CH_ID);
        builder.setColor(Color.BLACK);
        builder.setSmallIcon(R.drawable.ic_baseline_smart_toy_24);
        builder.setLargeIcon(bitmap);
        builder.setContentTitle(username).setContentText(msg);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setAutoCancel(true).build();
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(mContext);
        notificationManagerCompat.notify(id,builder.build());
    }

    public void Like_notification(String username,String msg, Bitmap bitmap)
    {

        //generate random notification key
        int min,max;
        min=1;
        max=1000;
        int id = (int)(Math.random()*(max-min+1)+min);

        //builder for notification
        NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext,CH_ID);
        builder.setColor(Color.BLACK);
        builder.setSmallIcon(R.drawable.ic_baseline_smart_toy_24);
        builder.setLargeIcon(bitmap);
        builder.setContentTitle(username).setContentText(msg);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setAutoCancel(true).build();
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(mContext);
        notificationManagerCompat.notify(id,builder.build());
    }
    private void CreateNotificationChannel()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(CH_ID,"Message", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("forCBOT");

            NotificationManager notificationManager=(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager!=null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Action action(String userid, int id)
    {
        RemoteInput remoteInput=new RemoteInput.Builder("key_replay").setLabel("Replay text...").build();
        Intent intent=new Intent(mContext, ChatActivity.class);
        intent.putExtra("profiled",userid);
        intent.putExtra("CH_ID",CH_ID);
        intent.putExtra("notify_id",id);
        PendingIntent pendingIntent=PendingIntent.getActivity(mContext,3,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Action replayAction=new NotificationCompat.Action.Builder(R.drawable.ic_baseline_keyboard_double_arrow_up_24,"Replay Partner",pendingIntent).addRemoteInput(remoteInput).build();
        return replayAction;
    }
}
