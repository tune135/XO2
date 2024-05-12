package com.example.xo2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Random;

public class FirebaseHandler {
    // Instance of FirebaseAuth; used for handling user authentication
    private FirebaseAuth auth;

    // Context from which this instance is operating (e.g., an Activity or Application context)
    private Context context;

    // Intent object to carry data or trigger activities or services
    Intent intent;

    // DatabaseReference to interact with Firebase Realtime Database
    private DatabaseReference mDatabase;

    // ChildEventListener to handle real-time changes to data at a specific database reference
    private ChildEventListener gameEventListener;


    // Constructor to initialize FirebaseHandler with FirebaseAuth and Context
    public FirebaseHandler(FirebaseAuth auth, Context context) {
        this.auth = auth;
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    // Constructor to initialize FirebaseHandler with Context
    public FirebaseHandler(Context context) {
        this.auth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }



    // Method to sign in with the provided email and password
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Show a short toast message indicating successful sign-in
                Toast.makeText(context, "Sign in Successful", Toast.LENGTH_SHORT).show();

                // Navigate to ChooseGame activity upon successful sign-in
                intent = new Intent(context, GameMenu.class); //OnlineCode
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
                // Get the current authenticated user
                FirebaseUser currentUser = auth.getCurrentUser();

                if (currentUser != null) {
                    // Create a new Player object for the user
                    Player newPlayer = new Player();
                    newPlayer.writeNewUser(); // Generate random nickname and set default values

                    // Store the new Player object in the Firebase Realtime Database under the user's ID
                    mDatabase.child("players").child(currentUser.getUid()).setValue(newPlayer)
                            .addOnCompleteListener(databaseTask -> {
                                if (databaseTask.isSuccessful()) {
                                    // Show a short toast message indicating successful registration
                                    Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                    // Navigate to SignIn1 activity upon successful registration
                                    intent = new Intent(context, SignIn1.class);
                                    context.startActivity(intent);
                                } else {
                                    // Show a short toast message indicating unsuccessful registration in database
                                    Log.e("ERROR", databaseTask.getException().getMessage());
                                }
                            });
                } else {
                    // Handle the case where the current user is unexpectedly null
                    Toast.makeText(context, "User not found after registration", Toast.LENGTH_SHORT).show();
                }

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

    //Sign out of player
    public void signOut() {
        auth.signOut();
        // Show a short toast message indicating successful sign-out
        Toast.makeText(context, "Sign out Successful", Toast.LENGTH_SHORT).show();

        // Navigate to SignIn1 activity upon successful sign-out
        intent = new Intent(context, SignIn1.class);
        context.startActivity(intent);
    }


    //Attaches a ChildEventListener to a specific game in the database.
    public void addGameEventListener(String gameCode, ChildEventListener listener) {
        mDatabase.child("data").child(gameCode).addChildEventListener(listener);
    }

    //Removes the ChildEventListener from a specific game in the database.
    public void removeGameEventListener(String gameCode) {
        if (gameEventListener != null) {
            mDatabase.child("data").child(gameCode).removeEventListener(gameEventListener);
        }
    }

    //Pushes a new move to the game's node in the database.
    public void pushGameMove(String gameCode, Object move) {
        mDatabase.child("data").child(gameCode).push().setValue(move);
    }

    //Clears all data related to a specific game in the database.
    public void clearGameData(String gameCode) {
        mDatabase.child("data").child(gameCode).removeValue();
    }

    //Updates the big board state for a specific game in the database and pushes detailed cell updates.
    public void updateDatabaseBigBoard(String gameCode, int cellId) {
        mDatabase.child("data").child(gameCode).push().setValue(cellId);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int detailedCellId = cellId * 100 + i * 10 + j;
                mDatabase.child("data").child(gameCode).push().setValue(detailedCellId);
            }
        }
    }

    // Method to handle the creation or joining of a game code with a ValueEventListener
    public void createOrJoinCode(String code, boolean isCodeMaker, ValueEventListener listener) {
        Constants.isCodeMaker = isCodeMaker;
        mDatabase.child("codes").addValueEventListener(listener);
    }

    // Method to push a new game code to Firebase and execute a callback on success
    public void pushCode(String code, Runnable onSuccess) {
        mDatabase.child("codes").push().setValue(code).addOnSuccessListener(aVoid -> {
            onSuccess.run();
        });
    }

    // Method to remove a ValueEventListener from the database reference
    public void removeEventListener(ValueEventListener listener) {
        mDatabase.child("codes").removeEventListener(listener);
    }


}
