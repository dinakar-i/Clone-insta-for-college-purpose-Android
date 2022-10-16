package com.spoof.MyMethods;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.spoof.Model.Message;

import java.io.IOException;
import java.io.InputStream;

public class RequestNotification extends AsyncTask<String,Void, Bitmap> {
    private Context mContext;
    private String username,userid,message;

    public RequestNotification(Context mContext, String userid, String username,String message) {
        this.mContext = mContext;
        this.userid=userid;
        this.username = username;
        this.message=message;
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
      notification.Request_notification(userid,username,message,bitmap);
    }

}
