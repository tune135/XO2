package com.example.xo2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

/**
 * Class to manage notification channels for Android Oreo and above.
 */
public class AppNotificationChannel {
    // Unique identifier for the notification channel
    public static final String CHANNEL_ID = "notification_channel";

    /**
     * Creates a notification channel if the device is running Android Oreo or above.
     * Notification channels are necessary to deliver notifications on Android 8.0 and later.
     *
     * @param context The application context, used to access system services.
     */
    public static void createNotificationChannel(Context context) {
        // Check for the Android version to ensure the code is only executed on Oreo and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // The user-visible name of the channel.
            CharSequence name = "Notification Channel";
            // The user-visible description of the channel.
            String description = "Channel for App Notifications";

            // Define the importance level of notifications sent in this channel.
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            // Create the NotificationChannel object with the defined ID, name, and importance.
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            // Set the description of the channel.
            channel.setDescription(description);

            // Enable vibration for notifications sent through this channel.
            channel.enableVibration(true);

            // Register the channel with the system. Note: You can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
