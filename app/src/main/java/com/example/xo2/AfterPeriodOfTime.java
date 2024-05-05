package com.example.xo2;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AfterPeriodOfTime extends AppCompatActivity {

    Context context;
    Button bTimeAfter;
    EditText etTimeAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_period_of_time);

        initComponents();

        bTimeAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stPeriod=etTimeAfter.getText().toString();
                int t=Integer.parseInt(stPeriod);
                long afterT=System.currentTimeMillis()+1000*t;

                Intent after=new Intent(context,NotificationService.class);
                PendingIntent afterIntent= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    afterIntent = PendingIntent.getForegroundService(context,
                            2,after,PendingIntent.FLAG_IMMUTABLE);
                }

                AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,afterT,afterIntent);

            }
        });
    }

    private void initComponents() {
        context=this;
        bTimeAfter=findViewById(R.id.bTimeAfter);
        etTimeAfter=findViewById(R.id.etTimeAfter);
    }
}
