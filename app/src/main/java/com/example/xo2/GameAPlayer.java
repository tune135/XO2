package com.example.xo2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Random;

public class GameAPlayer extends AppCompatActivity {


    // Declare member variables
    GridLayout[][] bigBoardGrids;
    ImageButton[][][][] smallBoardButtons;
    GameBoard board;
    TextView turnView;
    TextView timerTextView;
    int playS;
    int playB;
    String nextTurnPlayString;
    int nextTurnPlayNumberSBoard;
    Intent intent;
    CountDownTimer countDownTimer;
    boolean timerRunning = false;

    int activeUser = 1;


    // Override the onCreate method to initialize the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_aplayer);

        turnView = findViewById(R.id.turn2);
        // Initialize the GameBoard
        board = new GameBoard();

        // Initialize GridLayouts for big boards
        bigBoardGrids = new GridLayout[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                bigBoardGrids[i][j] = (GridLayout) FindViewGrid(i, j);
            }
        }

        // Initialize ImageButtons for small boards
        smallBoardButtons = new ImageButton[3][3][3][3];
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        smallBoardButtons[a][b][i][j] = (ImageButton) FindViewImageButton(a, b, i, j);
                    }
                }
            }
        }

        FirebaseDatabase.getInstance().getReference().child("data").child(Constants.code).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Object data = snapshot.getValue();
                if (IsMyMove.isMyMove) {
                    IsMyMove.isMyMove = false;
                    MoveOnline(data.toString(), IsMyMove.isMyMove);
                } else {
                    IsMyMove.isMyMove = true;
                    MoveOnline(data.toString(), IsMyMove.isMyMove);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Your code for handling child changed event
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Your code for handling child removed event
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Your code for handling child moved event
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Your code for handling database error
            }
        });


        // Generate a random player number (1 or -1) and initialize the turnView
        Random random = new Random();
        int randomValue = random.nextInt(2);
        int playerNumber = (randomValue == 0) ? -1 : 1;

        turnView.setText("Player X's turn");

        ApplyTimer();
    }

    // Helper method to find an ImageButton by coordinates
    public View FindViewImageButton(int a, int b, int i, int j) {
        String buttonID = "imageButton" + Integer.toString(a) + Integer.toString(b) + Integer.toString(i) + Integer.toString(j);
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        return findViewById(resID);
    }

    // Helper method to find a GridLayout by coordinates
    public View FindViewGrid(int i, int j) {
        String gridID = "grid" + Integer.toString(i) + Integer.toString(j);
        int resID = getResources().getIdentifier(gridID, "id", getPackageName());
        return findViewById(resID);
    }

    public void MoveOnline(String data, Boolean move){
        int cellOnline = 0;
        if(move) {
            for (int a = 0; a < 3; a++) {
                for (int b = 0; b < 3; b++) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            if (Integer.parseInt(data) / 100 == a * 10 + b && Integer.parseInt(data) % 100 == i * 10 + j) {
                                playS = i * 10 + j;
                                playB = a * 10 + b;
                                cellOnline = (a * 10 + b) * 100 + i * 10 + j;
                                break;
                            }
                        }
                    }
                }
            }

            // Set the image resource based on the player's turn
            if (Objects.equals(board.getTurn(), "X")) {
                smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setImageResource(R.drawable.ic_x);
                turnView.setText("Player O's turn");
            }else if (Objects.equals(board.getTurn(), "O")) {
                smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setImageResource(R.drawable.ic_o);
                turnView.setText("Player X's turn");
            }




            // Get the result of the game play
            nextTurnPlayString = board.gamePlay(playS, playB);


            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(board.getBigBoard()[i][j] == 1){
                        ResetAllButtonsForOneSBoard(i * 10 + j);
                        bigBoardGrids[i][j].setBackgroundResource(R.drawable.ic_x);
                        //updateDatabaseBigBoard(i * 10 + j);
                    }
                    else if(board.getBigBoard()[i][j] == -1){
                        ResetAllButtonsForOneSBoard(i * 10 + j);
                        bigBoardGrids[i][j].setBackgroundResource(R.drawable.ic_o);
                        //updateDatabaseBigBoard(i * 10 + j);
                    }
                }
            }

            // Check the result of the game play and navigate to EndGame activity accordingly
            if (Objects.equals(nextTurnPlayString, "X")) {
                intent = new Intent(this, EndGame.class);
                startActivity(intent); // player X win

            } else if (Objects.equals(nextTurnPlayString, "O")) {
                intent = new Intent(this, EndGame.class);
                startActivity(intent); // player O win

            } else {
                NextTurn();
                ApplyTimer();
            }

        }
    }

    public void updateDatabase(int cellId) {
        FirebaseDatabase.getInstance().getReference().child("data").child(Constants.code).push().setValue(cellId);
    }

    public void updateDatabaseBigBoard(int cellId){
        FirebaseDatabase.getInstance().getReference().child("data").child(Constants.code).push().setValue(cellId);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                FirebaseDatabase.getInstance().getReference().child("data").child(Constants.code).push().setValue(cellId * 100 + i * 10 + j);
            }
        }
    }


    void removeCode() {
        if (Constants.isCodeMaker) {
            FirebaseDatabase.getInstance().getReference().child("codes").child(Constants.keyValue).removeValue();
        }
    }


    public void onBackPressed() {
        super.onBackPressed();
        removeCode();
        if (Constants.isCodeMaker) {
            FirebaseDatabase.getInstance().getReference().child("data").child(Constants.code).removeValue();
        }
        System.exit(0);
    }

    // Method to apply a countdown timer
    public void ApplyTimer() {
        timerTextView = findViewById(R.id.timerTextView);

        // Check if a timer is already running
        if (timerRunning) {
            // Stop the existing timer
            stopTimer();
        }

        // Start a new timer
        countDownTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                String timeString = String.format("Timer: %02d:%02d", seconds / 60, seconds % 60);
                timerTextView.setText(timeString);
            }

            public void onFinish() {
                RandomPlay();
            }
        }.start();

        timerRunning = true;
    }

    // Method to stop the timer
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            timerRunning = false;
        }
    }


    // Method to perform a random play
    public void RandomPlay() {
        Random random = new Random();
        int randomNumB1 = random.nextInt(3);
        int randomNumB2 = random.nextInt(3);
        int randomNumS1 = random.nextInt(3);
        int randomNumS2 = random.nextInt(3);

        while (!smallBoardButtons[randomNumB1][randomNumB2][randomNumS1][randomNumS2].isClickable()) {
            randomNumB1 = random.nextInt(3);
            randomNumB2 = random.nextInt(3);
            randomNumS1 = random.nextInt(3);
            randomNumS2 = random.nextInt(3);
        }

        smallBoardButtons[randomNumB1][randomNumB2][randomNumS1][randomNumS2].performClick();
    }

    // Method to handle player's move
    public void Play(View view) {
        if(IsMyMove.isMyMove){
            // Identify the clicked ImageButton
            int cellOnline = 0;
            for (int a = 0; a < 3; a++) {
                for (int b = 0; b < 3; b++) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            if (smallBoardButtons[a][b][i][j].getId() == view.getId()) {
                                playS = i * 10 + j;
                                playB = a * 10 + b;
                                cellOnline = (a * 10 + b) * 100 + i * 10 + j;
                                break;
                            }
                        }
                    }
                }
            }


            // Set the image resource based on the player's turn
            if (Objects.equals(board.getTurn(), "X")) {
                smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setImageResource(R.drawable.ic_x);
                turnView.setText("Player O's turn");
            }else if (Objects.equals(board.getTurn(), "O")) {
                smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setImageResource(R.drawable.ic_o);
                turnView.setText("Player X's turn");
            }

            updateDatabase(cellOnline);
            //Get the result of the game play
            nextTurnPlayString = board.gamePlay(playS, playB);

            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(board.getBigBoard()[i][j] == 1){
                        ResetAllButtonsForOneSBoard(i * 10 + j);
                        bigBoardGrids[i][j].setBackgroundResource(R.drawable.ic_x);
                    }
                    else if(board.getBigBoard()[i][j] == -1){
                        ResetAllButtonsForOneSBoard(i * 10 + j);
                        bigBoardGrids[i][j].setBackgroundResource(R.drawable.ic_o);
                    }
                }
            }


            // Check the result of the game play and navigate to EndGame activity accordingly
            if (Objects.equals(nextTurnPlayString, "X")) {
                intent = new Intent(this, EndGame.class);
                startActivity(intent); // player X win

            } else if (Objects.equals(nextTurnPlayString, "O")) {
                intent = new Intent(this, EndGame.class);
                startActivity(intent); // player O win

            } else {
                NextTurn();
                ApplyTimer();
            }

        }
    }

    // Method to handle the next turn and update clickable buttons
    public void NextTurn() {
        ResetButtons();
        if (Objects.equals(nextTurnPlayString, "any")) {
            nextTurnPlayNumberSBoard = -1;
            for (int a = 0; a < 3; a++) {
                for (int b = 0; b < 3; b++) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            if (board.getSmallBoard()[a][b][i][j] == 0) {
                                smallBoardButtons[a][b][i][j].setImageResource(R.drawable.ic_yellow);
                                smallBoardButtons[a][b][i][j].setClickable(true);
                            }
                        }
                    }
                }
            }
        } else {
            nextTurnPlayNumberSBoard = Integer.parseInt(nextTurnPlayString);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board.getSmallBoard()[nextTurnPlayNumberSBoard / 10][nextTurnPlayNumberSBoard % 10][i][j] == 0) {
                        smallBoardButtons[nextTurnPlayNumberSBoard / 10][nextTurnPlayNumberSBoard % 10][i][j].setImageResource(R.drawable.ic_yellow);
                        smallBoardButtons[nextTurnPlayNumberSBoard / 10][nextTurnPlayNumberSBoard % 10][i][j].setClickable(true);
                    }
                }
            }
        }
        return;
    }

    // Method to reset buttons to initial state
    public void ResetButtons() {
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        smallBoardButtons[a][b][i][j].setClickable(false);
                        if (board.getSmallBoard()[a][b][i][j] == 0) {
                            smallBoardButtons[a][b][i][j].setImageResource(R.drawable.ic_empty);
                        }
                    }
                }
            }
        }
    }

    public void ResetAllButtonsForOneSBoard(int sBoard) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                smallBoardButtons[sBoard / 10][sBoard % 10][i][j].setVisibility(View.INVISIBLE);
                smallBoardButtons[sBoard / 10][sBoard % 10][i][j].setClickable(false);
            }
        }
    }


}