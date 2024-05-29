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

    // 2D array representing each small grid within the big boards
    GridLayout[][] bigBoardGrids;

    // 4D array of ImageButtons for each cell in the small boards
    ImageButton[][][][] smallBoardButtons;

    // Object representing the state of the game
    GameBoard board;

    // TextView for displaying whose turn it is
    TextView turnView;

    // TextView for displaying the countdown timer
    TextView timerTextView;

    // The small board a player wants to play on
    int playS;

    // The big board a player wants to play on
    int playB;

    // String indicating who's turn is next
    String nextTurnPlayString;

    // The number of the next turn's small board
    int nextTurnPlayNumberSBoard;

    // Intent to manage transitions between components
    Intent intent;

    // Timer for managing countdown during a game
    CountDownTimer countDownTimer;

    // Flag to check if the timer is running
    boolean timerRunning = false;

    // Handler for Firebase operations
    private FirebaseHandler firebaseHandler;

    // Unique code for the game session
    private String gameCode;




    // Override the onCreate method to initialize the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_aplayer);

        firebaseHandler = new FirebaseHandler(this);
        gameCode = Constants.code;

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

        ChildEventListener gameEventListener = new ChildEventListener() {
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
                // Handle changes
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Handle removal
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle moves
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle cancellation
            }

        };

        firebaseHandler.addGameEventListener(gameCode, gameEventListener);
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

    //Method to add the move the first player made on the second player game board
    public void MoveOnline(String data, Boolean move){
        int cellOnline;
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
                intent.putExtra("result", "Well Played \nPlayer X Won The Game");
                startActivity(intent); // Draw

            } else if (Objects.equals(nextTurnPlayString, "O")) {
                intent = new Intent(this, EndGame.class);
                intent.putExtra("result", "Well Played \nPlayer X Won The Game");
                startActivity(intent); // Draw

            } else if (Objects.equals(nextTurnPlayString, "Draw")) {
                intent = new Intent(this, EndGame.class);
                intent.putExtra("result", "The Game Ended As A Draw");
                startActivity(intent); // Draw
            } else {
                NextTurn();
                ApplyTimer();
            }

        }
    }


    // Clean up and remove event listeners when the activity is destroyed
    protected void onDestroy() {
        super.onDestroy();
        firebaseHandler.removeGameEventListener(gameCode); // Removes the game event listener for this game code
    }

    // Updates the database with the move made by the player at the specified cell
    public void updateDatabase(int cellId) {
        firebaseHandler.pushGameMove(gameCode, cellId); // Pushes the game move to Firebase under the current game code
    }

    // Clears game data from Firebase if the user is the game's code maker
    public void removeCode() {
        if (Constants.isCodeMaker) {
            firebaseHandler.clearGameData(gameCode); // Clear game data only if the user created the game code
        }
    }

    // Overrides the onBackPressed method to add custom behavior
    public void onBackPressed() {
        super.onBackPressed();
        removeCode(); // Calls removeCode to clear data if necessary
        if (Constants.isCodeMaker) {
            FirebaseDatabase.getInstance().getReference().child("data").child(Constants.code).removeValue(); // Additional cleanup specific to game creators
        }
        System.exit(0); // Exits the app completely after backing out
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
                smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setClickable(false);
                turnView.setText("Player O's turn");
            }else if (Objects.equals(board.getTurn(), "O")) {
                smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setImageResource(R.drawable.ic_o);
                smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setClickable(false);
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
                intent.putExtra("result", "Well Played \nPlayer X Won The Game");
                startActivity(intent); // Draw

            } else if (Objects.equals(nextTurnPlayString, "O")) {
                intent = new Intent(this, EndGame.class);
                intent.putExtra("result", "Well Played \nPlayer X Won The Game");
                startActivity(intent); // Draw

            } else if (Objects.equals(nextTurnPlayString, "Draw")) {
                intent = new Intent(this, EndGame.class);
                intent.putExtra("result", "The Game Ended As A Draw");
                startActivity(intent); // Draw
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
                            smallBoardButtons[a][b][i][j].setClickable(false);

                        }
                    }
                }
            }
        }
    }


    //method to reset the buttons when a small board was captured
    public void ResetAllButtonsForOneSBoard(int sBoard) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                smallBoardButtons[sBoard / 10][sBoard % 10][i][j].setVisibility(View.INVISIBLE);
                smallBoardButtons[sBoard / 10][sBoard % 10][i][j].setClickable(false);
            }
        }
    }


}