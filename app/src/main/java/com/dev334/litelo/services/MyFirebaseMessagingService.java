package com.dev334.litelo.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

       String title=remoteMessage.getData().get("title");
       String body=remoteMessage.getData().get("body");
        Log.i("Notif", title + " " + body);
      // String click_action=remoteMessage.getNotification().getClickAction();

       MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title,body);

    }
}
