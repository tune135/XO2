package com.example.xo2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Player {
    public String nickname;
    public GameStatistics statistics;
    public byte[] photo; // Change int to byte[]

    // Default constructor required for DataSnapshot.getValue(Player.class)
    public Player() {
    }

    public Player(String nickname, GameStatistics statistics, byte[] photo) {
        this.nickname = nickname;
        this.statistics = statistics;
        this.photo = photo;
    }

    public void writeNewUser() {
        // Generate a random nickname for the user
        Random rnd = new Random();
        this.nickname = "player" + (100000 + rnd.nextInt(900000));

        // Set default values for other properties
        this.statistics = new GameStatistics();
        this.photo = null; // Set to null initially
    }

    // Method to update player data in the database
    public void updatePlayerData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference playerRef = FirebaseDatabase.getInstance().getReference()
                    .child("players").child(currentUser.getUid());
            playerRef.setValue(this)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Handle the successful update
                        } else {
                            // Handle the failed update
                        }
                    });
        }
    }

}
