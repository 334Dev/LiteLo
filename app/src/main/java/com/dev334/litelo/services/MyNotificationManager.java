package com.dev334.litelo.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;

public class MyNotificationManager {

    private Context mCtx;
    private static MyNotificationManager mInstance;

    private MyNotificationManager(Context context){
        mCtx=context;
    }

    public static synchronized MyNotificationManager getInstance(Context context)
    {
        if(mInstance==null){
            mInstance=new MyNotificationManager(context);
        }
        return  mInstance;
    }

    public void displayNotification(String title, String body){

        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(mCtx,Constants.Channel_ID)
                .setSmallIcon(R.drawable.ic_app_logo)
                .setContentTitle(title)
                .setContentText(body);

        Intent intent=new Intent(mCtx, HomeActivity.class);
       //  Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/"));
       // String u="https://www.google.com/";



        PendingIntent pendingIntent=PendingIntent.getActivity(mCtx,0,intent,0);

        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if(mNotificationManager!=null)
        {
           mNotificationManager.notify(1,mBuilder.build());
        }

    }

}
