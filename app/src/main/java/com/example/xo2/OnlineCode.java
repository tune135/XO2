package com.example.xo2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class OnlineCode extends AppCompatActivity {

    // Handler for Firebase operations, managing database interactions and real-time updates
    private FirebaseHandler firebaseHandler;

    // ValueEventListener to listen for changes to data at a Firebase database path
    private ValueEventListener valueEventListener;

    // Flag to track whether the ValueEventListener has been added to a database reference
    private boolean isListenerAdded = false;

    // TextView for displaying the header or title information on the screen
    TextView headTV;

    // EditText for users to input or edit a game code
    EditText codeEdt;

    // Button to trigger the creation of a new game code
    Button createCodeBtn;

    // Button to allow users to join a game using an existing code
    Button joinCodeBtn;

    // ProgressBar to indicate a loading process, typically used while waiting for network responses
    ProgressBar loadingPB;

    // Intent for managing transitions between activities or components
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_code);
        firebaseHandler = new FirebaseHandler(this);

        // Set up UI components from the layout
        setupViews();

        // Set up button listeners for creating and joining game codes
        createCodeBtn.setOnClickListener(view -> handleCreateCode());
        joinCodeBtn.setOnClickListener(view -> handleJoinCode());
    }

    // Initialize UI components and assign them from the layout
    private void setupViews() {
        headTV = findViewById(R.id.idTVHead);
        codeEdt = findViewById(R.id.idEdtCode);
        createCodeBtn = findViewById(R.id.idBtnCreate);
        joinCodeBtn = findViewById(R.id.idBtnJoin);
        loadingPB = findViewById(R.id.idPBLoading);
    }

    // Handle the logic for creating a game code
    private void handleCreateCode() {
        String code = codeEdt.getText().toString().trim();
        if (!code.isEmpty()) {
            toggleLoading(true);
            valueEventListener = createValueEventListener(code, true);
            firebaseHandler.createOrJoinCode(code, true, valueEventListener);
            isListenerAdded = true;
        } else {
            Toast.makeText(this, "Please enter a valid code", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the logic for joining a game with a code
    private void handleJoinCode() {
        String code = codeEdt.getText().toString().trim();
        if (!code.isEmpty()) {
            toggleLoading(true);
            valueEventListener = createValueEventListener(code, false);
            firebaseHandler.createOrJoinCode(code, false, valueEventListener);
            isListenerAdded = true;
        } else {
            Toast.makeText(this, "Please enter a valid code", Toast.LENGTH_SHORT).show();
        }
    }

    // Create a ValueEventListener that responds to data changes in Firebase
    private ValueEventListener createValueEventListener(String code, boolean isCreator) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean codeExists = isValueAvaliable(snapshot, code);
                if (isCreator && !codeExists) {
                    firebaseHandler.pushCode(code, () -> Accepted());
                } else if (!isCreator && codeExists) {
                    Accepted();
                } else {
                    Toast.makeText(OnlineCode.this, isCreator ? "Code already exists" : "Invalid Code", Toast.LENGTH_SHORT).show();
                }
                toggleLoading(false);
                if (isListenerAdded) {
                    firebaseHandler.removeEventListener(this);
                    isListenerAdded = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                toggleLoading(false);
            }
        };
    }

    // Utility method to toggle loading UI elements
    private void toggleLoading(boolean show) {
        loadingPB.setVisibility(show ? View.VISIBLE : View.GONE);
        headTV.setVisibility(show ? View.GONE : View.VISIBLE);
        codeEdt.setVisibility(show ? View.GONE : View.VISIBLE);
        createCodeBtn.setVisibility(show ? View.GONE : View.VISIBLE);
        joinCodeBtn.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    // Overrides the onDestroy method to ensure cleanup when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy(); // Always call the superclass method first
        // Checks if the ValueEventListener is added and not null before removing it
        if (valueEventListener != null && isListenerAdded) {
            firebaseHandler.removeEventListener(valueEventListener); // Remove the event listener from Firebase to prevent memory leaks
        }
    }


    // Method called when a code is accepted and the game screen needs to be launched
    public void Accepted() {
        intent = new Intent(this, GameAPlayer.class);
        startActivity(intent);
        toggleLoading(false);
    }

    // Check if a specific game code is available in the snapshot from Firebase
    public boolean isValueAvaliable(DataSnapshot snapshot, String code) {
        for (DataSnapshot it : snapshot.getChildren()) {
            String value = it.getValue(String.class);
            if (code.equals(value)) {
                Constants.keyValue = it.getKey();
                return true;
            }
        }
        return false;
    }
}
