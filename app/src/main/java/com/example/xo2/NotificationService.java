package com.example.xo2;

// Static import of the notification channel ID
import static com.example.xo2.AppNotificationChannel.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

// Service class to manage notifications related to the app
public class NotificationService extends Service {
    // Method required for classes extending Service; not used here as this service is not for binding
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Binding not provided
    }

    // Called every time the service is started using startService()
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel(); // Create a notification channel
        startForeground(1, buildNotification()); // Start this service in the foreground using the custom notification
        return START_STICKY; // Service is restarted if terminated by the system
    }

    // Helper method to create a notification channel for foreground service notification
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Service Channel"; // Channel name
            String description = "Channel for background service"; // Channel description
            int importance = NotificationManager.IMPORTANCE_DEFAULT; // Default importance
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true); // Enable lights for notifications
            channel.setLightColor(Color.RED); // Set the color of the notification light
            channel.enableVibration(true); // Enable vibration
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel); // Register the channel with the system
        }
    }

    // Builds the notification displayed when the service is running in the foreground
    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, GameMenu.class);

        // PendingIntent flag setup
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE; // Ensure immutability from Android 12 onwards
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flags);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Let's Play") // Title of the notification
                .setContentText("You didn't connected to the app for a long time") // Text content of the notification
                .setSmallIcon(android.R.drawable.dark_header) // Icon displayed on the notification
                .setContentIntent(pendingIntent) // Intent that will start when the notification is tapped
                .build();
    }
}
