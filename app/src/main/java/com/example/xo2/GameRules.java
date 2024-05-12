package com.example.xo2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameRules extends AppCompatActivity {

    // Button to handle returning to the previous screen or activity
    Button bReturn;

    // Context of the current state of the application or an object within it
    Context context;

    // Intent for managing transitions between activities
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rules);

        context = this;
        bReturn = findViewById(R.id.returnButton);

        //on click bReturn return to the game menu class
        bReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, GameMenu.class);
                context.startActivity(intent);
            }
        });

    }
}