/*
 * Created by $user on 23/1/21 3:58 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 23/1/21 3:58 PM
 */

package com.androhub.fcmdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    /**
     * * There are two scenarios when onNewToken is called:
     * * 1) When a new token is generated on initial app startup
     * * 2) Whenever an existing token is changed
     * * Under #2, there are three scenarios when the existing token is changed:
     * * A) App is restored to a new device
     * * B) User uninstalls/re-installs the app
     * * C) User clears app data
     * <p>
     * method where token changes will received
     *
     * @param token device token
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.d(TAG, "Token: " + token);

        //upload the token to your server
        //you have to store in preferences
    }

    /**
     * method to receive the incoming push notifications
     *
     * @param remoteMessage contains notification and data payload
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "Notification: " + remoteMessage.getFrom());

        //create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        //check if push notification has notification payload or not
        if (remoteMessage.getNotification() != null) {

            //get the title and body
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Log.d(TAG, "Notification Title: " + title + " - Body: " + body);

            //show notification
            showNotification(title, body);
        }

        //check if push notification has data payload or not
        if (remoteMessage.getData().size() > 0) {

            //iterate the map to get key and value
            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                Log.d(TAG, "Key: " + entry.getKey() + " - Value: " + entry.getValue());
            }

            //show notification if required
            //showNotification(title, body);
        }

    }

    /**
     * method to create the notification channel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        //channel_id should be unique for every notification channel
        NotificationChannel notificationChannel = new NotificationChannel("channel_id", "Test Notification Channel",
                NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("My test notification channel");

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    /**
     * method to show the notification
     *
     * @param title of the notification
     * @param body  of the notification
     */
    private void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);

        //pass the same channel_id which we created in previous method
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        notificationManager.notify(1, builder.build());
    }
}
