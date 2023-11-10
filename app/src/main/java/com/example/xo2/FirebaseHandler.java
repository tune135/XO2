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

    // Constructor to initialize FirebaseHandler with FirebaseAuth and Context
    public FirebaseHandler(FirebaseAuth auth, Context context) {
        this.auth = auth;
        this.context = context;
    }

    // Method to sign in with the provided email and password
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // When sign-in is successful, navigate to the ChooseGame activity
                if(task.isSuccessful()) {
                    Intent intent = new Intent(context.getApplicationContext(), ChooseGame.class);
                    context.startActivity(intent);

                    // Show a short toast message indicating successful sign-in
                    Toast.makeText(context, "Sign in Successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Show a short toast message indicating unsuccessful sign-in
                    Toast.makeText(context, "Sign in is not Successful", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // If sign-in fails, show a toast message with the error message
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Method to register a user with the provided email and password
    public void register(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // When registration is successful, navigate to the SignIn1 activity
                Intent intent = new Intent(context.getApplicationContext(), SignIn1.class);
                context.startActivity(intent);

                // Show a short toast message indicating successful registration
                Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // If registration fails, show a toast message with the error message
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
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
