package com.example.xo2;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class FirebaseHandler {
    private FirebaseAuth auth;
    private Context context;
    Intent intent;
    private String selectedDifficulty;
    private DatabaseReference mDatabase;

    // Constructor to initialize FirebaseHandler with FirebaseAuth and Context
    public FirebaseHandler(FirebaseAuth auth, Context context) {
        this.auth = auth;
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // Method to sign in with the provided email and password
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Show a short toast message indicating successful sign-in
                Toast.makeText(context, "Sign in Successful", Toast.LENGTH_SHORT).show();

                // Navigate to ChooseGame activity upon successful sign-in
                Intent intent = new Intent(context, GameAPlayer.class);
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
                                    Intent intent = new Intent(context, SignIn1.class);
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

    public void setSelectedDifficulty(String difficulty) {
        this.selectedDifficulty = difficulty;
    }

    // Method to get the selected difficulty
    public String getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public void signOut() {
        auth.signOut();
        // Show a short toast message indicating successful sign-out
        Toast.makeText(context, "Sign out Successful", Toast.LENGTH_SHORT).show();

        // Navigate to SignIn1 activity upon successful sign-out
        Intent intent = new Intent(context, SignIn1.class);
        context.startActivity(intent);
    }

    // Method to get the current player's data from the database
    public void getPlayerData() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference playerRef = mDatabase.child("players").child(currentUser.getUid());
            playerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // This method is called when the data at the specified database reference changes
                    Player player = dataSnapshot.getValue(Player.class);
                    if (player != null) {
                        // Handle the retrieved player data
                        // For example, update UI or perform any necessary actions
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors in reading data
                    Toast.makeText(context, "Failed to retrieve player data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Method to update the player's nickname in the database
    public void updatePlayerNickname(String newNickname) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            mDatabase.child("players").child(currentUser.getUid()).child("nickname").setValue(newNickname)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Nickname updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to update nickname", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Method to update the player's photo in the database
    public void updateUserPhoto(byte[] imageData) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            mDatabase.child("players").child(currentUser.getUid()).child("photo").setValue(imageData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Photo updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to update photo", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    // Getter method for the FirebaseAuth instance
    public FirebaseAuth getAuth() {
        return auth;
    }

    // Setter method to update the FirebaseAuth instance
    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }

    // Method to create a new game room
    public void createGameRoom() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // Generate a unique game ID
            String gameId = generateGameId();

            // Create a new game room with initial data
            DatabaseReference gameRef = mDatabase.child("games").child(gameId);
            gameRef.child("player1").setValue(currentUser.getUid());
            gameRef.child("player2").setValue(""); // Player2 will be set when the second player joins
            gameRef.child("turn").setValue("player1"); // Set the initial turn to player1
            gameRef.child("board").setValue(""); // Initialize the game board

            // Navigate to the game activity with the generated game ID
            Intent intent = new Intent(context, GameAPlayer.class);
            intent.putExtra("gameId", gameId);
            context.startActivity(intent);
        }
    }

    // Method to join an existing game room
    public void joinGameRoom(String gameId) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference gameRef = mDatabase.child("games").child(gameId);

            // Check if the game room exists and is not full
            gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String player1 = dataSnapshot.child("player1").getValue(String.class);
                    String player2 = dataSnapshot.child("player2").getValue(String.class);

                    if (player1 != null && player2.isEmpty()) {
                        // Join the game room as player2
                        gameRef.child("player2").setValue(currentUser.getUid());
                        // Navigate to the game activity with the provided game ID
                        Intent intent = new Intent(context, GameAPlayer.class);
                        intent.putExtra("gameId", gameId);
                        context.startActivity(intent);
                    } else {
                        // The game room is full or does not exist
                        Toast.makeText(context, "Game room is full or does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }

    // Method to generate a unique game ID
    private String generateGameId() {
        // Implement your logic to generate a unique ID, for example, using a timestamp or random string
        return "game_" + System.currentTimeMillis();
    }

}
