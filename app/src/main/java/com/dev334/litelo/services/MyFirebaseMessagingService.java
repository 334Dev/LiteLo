package com.dev334.litelo.services;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

       String title=remoteMessage.getNotification().getTitle();
       String body=remoteMessage.getNotification().getBody();
       //String aas=remoteMessage.getNotification().getClickAction();

       MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title,body);
    }
}
