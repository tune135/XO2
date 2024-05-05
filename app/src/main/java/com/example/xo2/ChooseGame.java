package com.example.xo2;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class ChooseGame extends AppCompatActivity {


    private Context context = this;
    private Button bNow, bAfterPeriod;
    private Button startGameButton;
    private TextView winsTextView, lossesTextView, drawsTextView;
    private FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);

        // Request necessary permissions
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.POST_NOTIFICATIONS,
                        Manifest.permission.FOREGROUND_SERVICE},
                100);

        // Button to trigger a notification service immediately
        bNow = findViewById(R.id.bNow);
        bNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent to start the NotificationService
                Intent serviceIntent = new Intent(context, NotificationService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                }
            }
        });

        // Button to navigate to the activity that schedules notifications after a period
        bAfterPeriod = findViewById(R.id.bAfterPeriod);
        bAfterPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(context, AfterPeriodOfTime.class);
                startActivity(go);
            }
        });



    }

}
