package com.example.xo2;


import static com.example.xo2.AppNotificationChannel.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class MyForegroundService extends Service {
    private Notification notification;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AppNotificationChannel.createNotificationChannel(this);
        }

        build_and_view_Notification(this, intent);
        startForeground(1, notification);

        return START_STICKY;
    }

    private void build_and_view_Notification(Context context, Intent intent) {
        String title = "IMPORTANT!", message = "Something";
        Intent goInfo = new Intent(context, NotificationAndDB.class);
        PendingIntent go = PendingIntent.getActivities(context,
                100, new Intent[]{goInfo}, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.noti)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .addAction(R.drawable.noti, "Go to NotificationAndDB", go)
                .setColor(Color.RED)
                .build();

        if (ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "NO", Toast.LENGTH_SHORT).show();
            return;
        }
        NotificationManager nm= (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        nm.notify(1, notification);
        Toast.makeText(context, "Notify", Toast.LENGTH_SHORT).show();
    }
}
