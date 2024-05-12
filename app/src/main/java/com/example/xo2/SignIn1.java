package com.example.xo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn1 extends AppCompatActivity {
    // Reference to FirebaseHandler for managing Firebase authentication and database operations
    private FirebaseHandler firebaseHandler;

    // Button for initiating the sign-in process
    private Button btn;

    // EditText field for user to enter their password
    private EditText password;

    // EditText field for user to enter their email address
    private EditText email;

    // Intent for managing transitions between activities or passing data between components
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in1);

        // Initialize FirebaseHandler with the FirebaseAuth instance and this activity's context
        firebaseHandler = new FirebaseHandler(FirebaseAuth.getInstance(), this);

        // Initialize UI components
        btn = findViewById(R.id.button); // Find and reference the sign-in button from the layout
        password = findViewById(R.id.editPassword); // Find and reference the password input field from the layout
        email = findViewById(R.id.editEmail); // Find and reference the email input field from the layout

        // Set an onClickListener for the sign-in button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the button is clicked, call the signIn method of FirebaseHandler
                // with the email and password entered by the user
                firebaseHandler.signIn(email.getText().toString(), password.getText().toString());
            }
        });
    }

    // Called when the signup button is clicked, handling the user sign-up process
    public void Signup(View view) {
        // Create a new Intent to transition from the current Activity to MainActivity
        intent = new Intent(this, MainActivity.class);
        // Start MainActivity, navigating the user to the main screen after signing up
        startActivity(intent);
    }

}
