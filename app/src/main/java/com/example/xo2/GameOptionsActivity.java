package com.example.xo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;

public class GameOptionsActivity extends AppCompatActivity {

    private FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);

        // Initialize FirebaseHandler (replace with your actual initialization)
        firebaseHandler = new FirebaseHandler(FirebaseAuth.getInstance(), this);

        // Get the selected difficulty from the intent
        String selectedDifficulty = getIntent().getStringExtra("SELECTED_DIFFICULTY");

        // Find the radio group and radio buttons
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButtonHard = findViewById(R.id.Hard);
        RadioButton radioButtonChanging = findViewById(R.id.Changing);

        // Set the default radio button based on the selected difficulty
        if ("Difficulty Hard - Play against a bot".equals(selectedDifficulty)) {
            radioButtonHard.setChecked(true);
        } else if ("Difficulty Changing - Play against a real player".equals(selectedDifficulty)) {
            radioButtonChanging.setChecked(true);
        }
    }

    // Method called when the "Start Game" button is clicked
    public void startNewGame(View view) {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        // Get the selected difficulty
        String difficulty = radioButton.getText().toString();

        // Set the selected difficulty in FirebaseHandler
        firebaseHandler.setSelectedDifficulty(difficulty);

        // Return the chosen difficulty to Choose Game Activity
        Intent intent = new Intent();
        intent.putExtra("DIFFICULTY", difficulty);
        setResult(RESULT_OK, intent);
        finish();
    }
}