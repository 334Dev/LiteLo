package com.example.litelo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class Notification_receiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {


        // NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
       // NotificationChannel channel=new NotificationChannel("default","Channel Name",NotificationManager.IMPORTANCE_DEFAULT);
       //  channel.setDescription("Channel Description");
       //  notificationManager.createNotificationChannel(channel);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context, HomeActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(context,100,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);






        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"LiteLoNotify")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Hello mnnitians")
                .setContentText("Build your resume nowwwwwww")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        if (intent.getAction().equals("MY_NOTIFICATION_MESSAGE")) {
            notificationManager.notify(100,builder.build());
//            Log.i("Notify", "Alarm"); //Optional, used for debuging.
        }




    }


}
