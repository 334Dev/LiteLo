package com.dev334.litelo.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.dev334.litelo.Database.TinyDB;
import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        TinyDB tinyDB=new TinyDB(mCtx);
        ArrayList<Object> notification=new ArrayList<>();
        notification=tinyDB.getListObject("Notification");
        Map<String, String> map=new HashMap<>();
        map.put("title", title);
        map.put("body", body);
        notification.add(map);
        tinyDB.putListObject("Notification", notification);

        PendingIntent pendingIntent=PendingIntent.getActivity(mCtx,0,intent,0);

        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if(mNotificationManager!=null)
        {
           mNotificationManager.notify(1,mBuilder.build());
        }

    }

}
