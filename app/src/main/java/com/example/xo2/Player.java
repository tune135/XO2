package com.example.xo2;

import java.util.Random;

public class Player {
    public String nickname;

    // Default constructor required for DataSnapshot.getValue(Player.class)
    public Player() {
    }


    public void writeNewUser() {
        // Generate a random nickname for the user
        Random rnd = new Random();
        this.nickname = "player" + (100000 + rnd.nextInt(900000));
    }


}
