package com.example.xo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;

public class GameABot20 extends AppCompatActivity {

    // Declare member variables
    GridLayout[][] bigBoardGrids;
    ImageButton[][][][] smallBoardButtons;
    GameBoard board;
    Bot bot;
    TextView turnView;
    TextView timerTextView;
    int playS;
    int playB;
    String nextTurnPlayString;
    int nextTurnPlayNumberBBoard = -1;
    int nextTurnPlayNumberSBoard = -1;
    Intent intent;
    CountDownTimer countDownTimer;
    boolean timerRunning = false;


    // Override the onCreate method to initialize the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_abot20);

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

        turnView = findViewById(R.id.turn2);

        // Generate a random player number (1 or -1) and initialize the turnView
        Random random = new Random();
        int randomValue = random.nextInt(2);
        int playerNumber;
        if(randomValue == 1){
            playerNumber = 1;
            // Initialize the Bot based on the random player number
            bot = new Bot(board, -1);
            turnView.setText("Your Turn (X)");
        } else{
            playerNumber = -1;
            // Initialize the Bot based on the random player number
            bot = new Bot(board, 1);
            turnView.setText("Your Turn (O)");
        }

        CheckPlay();
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

    // Method to check the play and call BotClickButton or ApplyTimer accordingly
    public void CheckPlay() {
        if ((Objects.equals(bot.getGb().getTurn(), "X") && bot.getBotNum() == 1) || (Objects.equals(bot.getGb().getTurn(), "O") && bot.getBotNum() == -1)) {
            BotClickButton(bot.playBot(nextTurnPlayNumberSBoard), nextTurnPlayNumberBBoard);
        } else {
            ApplyTimer();
        }
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
        // Identify the clicked ImageButton
        boolean found = false;
        for (int a = 0; a < 3 && !found; a++) {
            for (int b = 0; b < 3 && !found; b++) {
                for (int i = 0; i < 3 && !found; i++) {
                    for (int j = 0; j < 3 && !found; j++) {
                        if (smallBoardButtons[a][b][i][j].getId() == view.getId()) {
                            playS = i * 10 + j;
                            playB = a * 10 + b;
                            found = true; // Set the flag to true to break out of loops
                        }
                    }
                }
            }
        }

        // Set the image resource based on the player's turn
        if (Objects.equals(bot.getGb().getTurn(), "X")) {
            smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setImageResource(R.drawable.ic_x);
        } else if (Objects.equals(bot.getGb().getTurn(), "O")) {
            smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setImageResource(R.drawable.ic_o);
        }

        // Get the result of the game play
        nextTurnPlayString = bot.getGb().gamePlay(playS, playB);

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
            if (bot.getBotNum() == 1) {
                intent = new Intent(this, EndGame.class);
                startActivity(intent); // player lose
            } else {
                intent = new Intent(this, EndGame.class);
                startActivity(intent); // player win
            }
        } else if (Objects.equals(nextTurnPlayString, "O")) {
            if (bot.getBotNum() == 1) {
                intent = new Intent(this, EndGame.class);
                startActivity(intent); // player win
            } else {
                intent = new Intent(this, EndGame.class);
                startActivity(intent); // player lose
            }
        } else {
            NextTurn();
            CheckPlay();
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
                            if (bot.getGb().getSmallBoard()[a][b][i][j] == 0) {
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
                    if (bot.getGb().getSmallBoard()[nextTurnPlayNumberSBoard / 10][nextTurnPlayNumberSBoard % 10][i][j] == 0) {
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
                        if (bot.getGb().getSmallBoard()[a][b][i][j] == 0) {
                            smallBoardButtons[a][b][i][j].setImageResource(R.drawable.ic_empty);
                            smallBoardButtons[a][b][i][j].setClickable(false);
                        }
                    }
                }
            }
        }
    }

    // Method to simulate bot click on a button
    public void BotClickButton(int playSBot, int playBBot) {
        if(playBBot == -1){
            playBBot = playSBot / 100;
            playSBot = playSBot % 100;
        }
        int boardIndexB = playBBot / 10;
        int boardIndexS = playBBot % 10;
        int buttonIndexB = playSBot / 10;
        int buttonIndexS = playSBot % 10;

        // Check if the indices are within bounds
        if (boardIndexB >= 0 && boardIndexB < 3 &&
                boardIndexS >= 0 && boardIndexS < 3 &&
                buttonIndexB >= 0 && buttonIndexB < 3 &&
                buttonIndexS >= 0 && buttonIndexS < 3) {

            //if (smallBoardButtons[boardIndexB][boardIndexS][buttonIndexB][buttonIndexS].isClickable()) {
                smallBoardButtons[boardIndexB][boardIndexS][buttonIndexB][buttonIndexS].performClick();
//            } else {
////                intent = new Intent(this, EndGame.class);
////                startActivity(intent); // player win
//
//                Toast.makeText(GameABot20.this, "Bot Not Working", Toast.LENGTH_SHORT).show();
//            }
        } else {
            intent = new Intent(this, EndGame.class);
            startActivity(intent); // player win
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
