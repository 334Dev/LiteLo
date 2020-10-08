package com.example.litelo;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived (RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        generateMessage(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());

    }

    private void generateMessage(String body, String title) {

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),"LiteLoNotify")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager =NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }
}
