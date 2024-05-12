package com.example.xo2;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    // TextView for displaying the Wi-Fi connection status
    TextView tvWifiStatus;

    // Intent for managing transitions and actions between components or activities
    Intent intent;

    // Context of the current state of the application or an object within it
    Context context;

    // Reference to the current activity to manage UI and lifecycle
    Activity activity;

    // BroadcastReceiver to handle changes in internet connectivity
    InternetConnectionReceiver internetConnectionReceiver;

    // IntentFilter to specify the type of intents a receiver can handle, here for connectivity changes
    IntentFilter intentConnectionFilter;

    // Handler for Firebase operations, managing database interactions
    private FirebaseHandler firebaseHandler;

    // Button typically used for submitting forms or triggering actions
    private Button btn;

    // EditText for user to enter their password
    private EditText password;

    // EditText for user to enter their email
    private EditText email;

    // Constant to listen for changes in network connectivity
    private static final String CONNECTIVITY_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initElements();

        // Initialize UI components
        btn = findViewById(R.id.button);
        password = findViewById(R.id.editPassword);
        email = findViewById(R.id.editEmail);

        // Set an onClickListener for the registration button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseHandler = new FirebaseHandler(FirebaseAuth.getInstance(), MainActivity.this);

                if (isValidEmail(email.getText().toString()) && isValidPassword(password.getText().toString())) {
                    firebaseHandler.register(email.getText().toString(), password.getText().toString());
                } else {
                    if (!isValidEmail(email.getText().toString())) {
                        showToast("Invalid email address");
                    } else {
                        showToast("Password must be at least 8 characters");
                    }
                }
            }
        });
    }

    // Method for navigating to the SignIn1 activity
    public void Sign(View view) {
        intent = new Intent(this, SignIn1.class);
        startActivity(intent);
    }

    // Validate email format
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Validate password length
    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    // Display toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //initialize elements
    private void initElements() {
        activity = MainActivity.this;
        context = MainActivity.this;
        tvWifiStatus = findViewById(R.id.textView);
        internetConnectionReceiver = new InternetConnectionReceiver();
        intentConnectionFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    // Called when the activity is starting, after creation or after being stopped
    @Override
    protected void onStart() {
        super.onStart(); // Always call the superclass method first
        // Registers a BroadcastReceiver to listen for changes in internet connectivity
        registerReceiver(internetConnectionReceiver, intentConnectionFilter);
    }

    // Called when the activity is no longer visible to the user
    @Override
    protected void onStop() {
        super.onStop(); // Always call the superclass method first
        // Unregisters the previously registered BroadcastReceiver
        unregisterReceiver(internetConnectionReceiver);
    }



}
