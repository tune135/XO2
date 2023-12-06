package com.example.xo2;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

    public class FirebaseHandler {
    private FirebaseAuth auth;
    private Context context;
    Intent intent;

    // Constructor to initialize FirebaseHandler with FirebaseAuth and Context
    public FirebaseHandler(FirebaseAuth auth, Context context) {
        this.auth = auth;
        this.context = context;
    }

    // Method to sign in with the provided email and password
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Show a short toast message indicating successful sign-in
                Toast.makeText(context, "Sign in Successful", Toast.LENGTH_SHORT).show();

                // Navigate to ChooseGame activity upon successful sign-in
                Intent intent = new Intent(context, ChooseGame.class);
                context.startActivity(intent);
            } else {
                // Show a short toast message indicating unsuccessful sign-in
                Toast.makeText(context, "Sign in is not Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            // If sign-in fails, show a toast message with a generic error message
            Toast.makeText(context, "Sign in failed. Please try again.", Toast.LENGTH_LONG).show();
        });
    }

    // Method to register a user with the provided email and password
    public void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Show a short toast message indicating successful registration
                Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show();

                // Navigate to SignIn1 activity upon successful registration
                Intent intent = new Intent(context, SignIn1.class);
                context.startActivity(intent);
            } else {
                // Show a short toast message indicating unsuccessful registration
                Toast.makeText(context, "Registration is not Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            // If sign-in or registration fails, show a toast message with the specific error message
            String errorMessage = e.getMessage(); // Get the specific error message from the exception
            Toast.makeText(context, "Authentication failed: " + errorMessage, Toast.LENGTH_LONG).show();
        });

    }

    // Getter method for the FirebaseAuth instance
    public FirebaseAuth getAuth() {
        return auth;
    }

    // Setter method to update the FirebaseAuth instance
    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }
}
