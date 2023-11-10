package com.example.xo2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Intent intent; // Intent for navigating to another activity

    private FirebaseHandler firebaseHandler; // Reference to FirebaseHandler for authentication
    private Button btn; // Button for user registration
    private EditText password; // EditText field for password input
    private EditText email; // EditText field for email input

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseHandler with the FirebaseAuth instance and this activity's context
        firebaseHandler = new FirebaseHandler(FirebaseAuth.getInstance(), this);

        // Initialize UI components
        btn = findViewById(R.id.button); // Find and reference the registration button from the layout
        password = findViewById(R.id.editPassword); // Find and reference the password input field from the layout
        email = findViewById(R.id.editEmail); // Find and reference the email input field from the layout

        // Set an onClickListener for the registration button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the button is clicked, call the register method of FirebaseHandler
                // with the email and password entered by the user
                firebaseHandler.register(email.getText().toString(), password.getText().toString());
            }
        });
    }

    // Method for navigating to the SignIn1 activity
    public void Sign(View view) {
        intent = new Intent(this, SignIn1.class);
        startActivity(intent);
    }
}
