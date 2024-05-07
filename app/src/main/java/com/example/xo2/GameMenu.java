package com.example.xo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import com.example.xo2.R;


public class GameMenu extends AppCompatActivity {

    Context context;
    Button bTimeNow, bStartGame;
    FirebaseHandler firebaseHandler;
    Intent intent;
    Class aClass;
    TextView whatGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu); // Update the layout file to match the new activity

        aClass = GameABot20.class;
        context = this;
        bStartGame = findViewById(R.id.start);
        whatGame = findViewById(R.id.whatGame);


        // Automatically set the daily notification for 4 PM
        setDailyNotification();

//        bTimeNow = findViewById(R.id.bTimeNow);
//        bTimeNow.setOnClickListener(new View.OnClickListener() { //button for testing the notification
//            @Override
//            public void onClick(View v) {
//                triggerNotificationNow();
//            }
//        });


        bStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, aClass);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.total, menu); // Ensure the menu resource name matches your XML file name
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Rules) {
            intent = new Intent(this, GameRules.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.Difficulty) {
            if(aClass == GameABot20.class){
                aClass = GameAPlayer.class;
                bStartGame.setText("Start The Game Against Another Player");
                whatGame.setText("when clicking on the button the game against the another player will start");
            } else if (aClass == GameAPlayer.class) {
                aClass = GameABot20.class;
                bStartGame.setText("Start The Game Against A Bot");
                whatGame.setText("when clicking on the button the game against the Bot will start");
            }
            return true;
        } else if (id == R.id.Exit) {
            // Log out the user using FirebaseHandler class
            firebaseHandler.signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void setDailyNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // If it's already past 4 PM today, set it for the next day
        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent after = new Intent(context, NotificationService.class);
        PendingIntent afterIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            afterIntent = PendingIntent.getForegroundService(context, 2, after, PendingIntent.FLAG_IMMUTABLE);
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, afterIntent);
    }

    private void triggerNotificationNow() {
        Intent now = new Intent(context, NotificationService.class);
        PendingIntent nowIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            nowIntent = PendingIntent.getForegroundService(context, 0, now, PendingIntent.FLAG_IMMUTABLE);
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), nowIntent);
    }


}
