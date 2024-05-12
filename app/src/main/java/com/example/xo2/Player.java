package com.example.xo2;

import java.util.Random;

// Class representing a player in the game
public class Player {
    // Public variable to hold the player's nickname
    public String nickname;

    // Default constructor, necessary for Firebase to deserialize data into Player objects
    public Player() {
    }

    // Method to generate and assign a random nickname for a new user
    public void writeNewUser() {
        // Create a Random object to generate random numbers
        Random rnd = new Random();
        // Generate a random six-digit number and prepend it with "player" to create a nickname
        this.nickname = "player" + (100000 + rnd.nextInt(900000)); // Ensures a 6-digit number
    }
}
