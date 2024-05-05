package com.example.xo2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;


public class AppNotificationChannel {
    public static final String CHANNEL_ID = "notification_channel";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";  // Name of the channel
            String description = "Channel for App Notifications";  // Description of what the channel is used for

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            // Set the description for the channel
            channel.setDescription(description);

            // Enable vibration for this notification channel
            channel.enableVibration(true);


            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

