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


    TextView tvWifiStatus;
    Intent intent;
    Context context;
    Activity activity;

    InternetConnectionReceiver internetConnectionReceiver;
    IntentFilter intentConnectionFilter;

    private FirebaseHandler firebaseHandler;
    private Button btn;
    private EditText password;
    private EditText email;

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

    private void initElements() {
        activity = MainActivity.this;
        context = MainActivity.this;
        tvWifiStatus = findViewById(R.id.textView);
        internetConnectionReceiver = new InternetConnectionReceiver();
        intentConnectionFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(internetConnectionReceiver, intentConnectionFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(internetConnectionReceiver);
    }


}
