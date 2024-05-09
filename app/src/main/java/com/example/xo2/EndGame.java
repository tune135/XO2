package com.example.xo2;

// Import necessary Android and Java classes
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Define the EndGame activity class, inheriting from AppCompatActivity
public class EndGame extends AppCompatActivity {
    // Declare UI components
    private TextView resultTextView;
    private Button playAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity
        setContentView(R.layout.activity_end_game);

        // Initialize the TextView and Button by finding them by their ID
        resultTextView = findViewById(R.id.resultTextView);
        playAgainButton = findViewById(R.id.playAgainButton);

        // Retrieve the game result passed via intent from the previous activity
        String result = getIntent().getStringExtra("result");
        // Display the game result in the TextView
        resultTextView.setText(result);

        // Set an OnClickListener to the 'Play Again' button
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the GameMenu activity
                Intent intent = new Intent(EndGame.this, GameMenu.class);
                startActivity(intent); // Start the GameMenu activity
                finish(); // Close this activity to clear it from the back stack
            }
        });
    }
}
