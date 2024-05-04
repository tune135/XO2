package com.example.xo2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameRules extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rules);

        // Creating Layout programmatically
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add TextView for the rules
        TextView rulesTextView = new TextView(this);
        rulesTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rulesTextView.setText("Ultimate Tic Tac Toe Rules:\n\n" +
                "1. Each turn, you mark one of the small squares.\n" +
                "2. When you get three in a row on a small board, you’ve won that board.\n" +
                "3. To win the game, you need to win three small boards in a row.\n" +
                "4. You may only play in the big field that corresponds to the last small field your opponent played.\n" +
                "5. When you are sent to a field that is already decided, you can choose freely.");
        layout.addView(rulesTextView);

        // Add a Return Button
        Button returnButton = new Button(this);
        returnButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        returnButton.setText("Return to Game Selection");
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start ChooseGame Activity
                Intent intent = new Intent(GameRules.this, GameABot20.class);
                startActivity(intent);
            }
        });
        layout.addView(returnButton);

        // Set the layout as the content view
        setContentView(layout);

    }
}