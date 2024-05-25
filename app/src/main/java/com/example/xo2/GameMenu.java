package com.example.xo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import com.example.xo2.R;


public class GameMenu extends AppCompatActivity {

    // Context of the current state of the application or an object within it
    Context context;

    // Button to display current time or a similar quick action
    Button bTimeNow;

    // Button to initiate the start of the game
    Button bStartGame;

    // Handler for managing Firebase operations related to the game
    FirebaseHandler firebaseHandler;

    // Intent for managing transitions between activities
    Intent intent;

    // Class variable to hold the reference to an activity class, used with intents to start new activities
    Class aClass;

    // TextView that displays information about the game type or rules
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
        setDailyNotification(16, 0, 0);

//        bTimeNow = findViewById(R.id.bTimeNow);
//        bTimeNow.setOnClickListener(new View.OnClickListener() { //button for testing the notification
//            @Override
//            public void onClick(View v) {
//                triggerNotificationNow();
//            }
//        });

        //on click button bStartGame to start the chosen game
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
        if (isWifiConnected()) {
            getMenuInflater().inflate(R.menu.total, menu);
        } else {
            getMenuInflater().inflate(R.menu.no_wifi_menu, menu);
        }
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
            if (aClass == GameABot20.class) {
                aClass = OnlineCode.class;
                bStartGame.setText("Start The Game Against Another Player");
                whatGame.setText("When clicking on the button the game against another player will start");
            } else if (aClass == OnlineCode.class) {
                aClass = GameABot20.class;
                bStartGame.setText("Start The Game Against A Bot");
                whatGame.setText("When clicking on the button the game against the Bot will start");
            }
            return true;
        } else if (id == R.id.Exit) {
            // Log out the user using FirebaseHandler class
            firebaseHandler.signOut();
            return true;
        }
        else if (id == R.id.ExitNoWifi) {
            intent = new Intent(GameMenu.this, MainActivity.class);
            startActivity(intent); // return to sign up
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to check if the device is connected to Wi-Fi
    public boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isConnected();
    }


    // sets the daily notification for given time
    private void setDailyNotification(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);


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

    //triggers a notification now
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
