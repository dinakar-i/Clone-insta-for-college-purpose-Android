package com.spoof.MyMethods;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.spoof.Model.Message;
import com.spoof.lm.R;

import java.io.IOException;
import java.io.InputStream;

public class ChatNotification extends AsyncTask<String,Void, Bitmap> {
    private Context mContext;
    private Message message;
    private String username;

    public ChatNotification(Context mContext, Message message, String username) {
        this.mContext = mContext;
        this.message = message;
        this.username = username;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlLink=strings[0];
        Bitmap bitmap=null;
        try {
            InputStream inputStream=new java.net.URL(urlLink).openStream();
            bitmap= BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
      Notification notification=new Notification(mContext);
      notification.Chat_notification(message,username,bitmap);
    }

}
