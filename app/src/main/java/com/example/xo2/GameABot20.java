package com.example.xo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

public class GameABot20 extends AppCompatActivity {

    GridLayout[][] bigBoardGrids;
    ImageButton[][] smallBoardButtons;
    GameBoard board;
    Bot bot;
    TextView turnView;
    TextView timerTextView;
    int playS;
    int playB;
    String nextTurnPlayString;
    int nextTurnPlayNumberBBoard;
    int nextTurnPlayNumberSBoard;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_abot20);

        // Initialize the GameBoard
        board = new GameBoard();

        bigBoardGrids = new GridLayout[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                bigBoardGrids[i][j] = (GridLayout) FindViewGrid(i, j);
            }
        }

        smallBoardButtons = new ImageButton[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                smallBoardButtons[i][j] = (ImageButton) FindViewImageButton(i, j);
            }
        }

        Random random = new Random();
        // Generate a random integer that is either 0 or 1
        int randomValue = random.nextInt(2);
        // Adjust the result to be either 1 or -1
        int playerNumber = (randomValue == 0) ? -1 : 1;

        turnView = findViewById(R.id.turn2);

        if(playerNumber == 1){
            bot = new Bot(board, -1);
            turnView.setText("Your Turn (X)");
        } else if (playerNumber == -1) {
            bot = new Bot(board, 1);
            BotClickButton(bot.playBot(-1));
            turnView.setText("Your Turn (O)");
        }
    }

    public View FindViewImageButton(int i, int j) {
        String buttonID = "imageButton" + Integer.toString(i) + Integer.toString(j);
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        return findViewById(resID);
    }

    public View FindViewGrid(int i, int j) {
        String gridID = "grid" + Integer.toString(i) + Integer.toString(j);
        int resID = getResources().getIdentifier(gridID, "id", getPackageName());
        return findViewById(resID);
    }

    public void CheckPlay(){
        if(Objects.equals(board.getTurn(), "X") && bot.getBotNum() == 1 || Objects.equals(board.getTurn(), "O") && bot.getBotNum() == -1){
            BotClickButton(bot.playBot(nextTurnPlayNumberSBoard));
        }
        else{
            ApplyTimer();
        }
    }

    public void ApplyTimer(){
        // Initialize the TextView
        timerTextView = findViewById(R.id.timerTextView);

        // Set the timer for 120 seconds (2 minutes)
        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                // Update the TextView with the remaining time
                long seconds = millisUntilFinished / 1000;
                String timeString = String.format("Timer: %02d:%02d", seconds / 60, seconds % 60);
                timerTextView.setText(timeString);
            }

            public void onFinish() {
                // Timer finished, apply RandomPlay class
                RandomPlay();
            }
        }.start();
    }

    public void RandomPlay(){
        Random random = new Random();
        int randomNum1 = random.nextInt(9); // Generates numbers from 0 to 8
        int randomNum2 = random.nextInt(9);
        while(!smallBoardButtons[randomNum1 / 10][randomNum2 % 10].isClickable()){
            randomNum1 = random.nextInt(9); // Generates numbers from 0 to 8
            randomNum2 = random.nextInt(9);
        }
        smallBoardButtons[randomNum1 / 10][randomNum2 % 10].performClick();
    }



    public void Play(View view) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (smallBoardButtons[i][j].getId() == view.getId()) {
                    playS = i * 10 + j;
                    switch (i) {
                        case 0:
                        case 1:
                        case 2:
                            playB = 0;
                            break;
                        case 3:
                        case 4:
                        case 5:
                            playB = 10;
                            break;
                        case 6:
                        case 7:
                        case 8:
                            playB = 20;
                            break;
                    }
                    switch (j) {
                        //for 0, 1, 2 i add 0 so it has no need
                        case 3:
                        case 4:
                        case 5:
                            playB += 1;
                            break;
                        case 6:
                        case 7:
                        case 8:
                            playB += 2;
                            break;
                    }
                }
            }
        }

        nextTurnPlayString = board.gamePlay(playS, playB);
        if(Objects.equals(board.getTurn(), "X")){
            smallBoardButtons[playS / 10][playS % 10].setImageResource(R.drawable.ic_x);
        }
        else if(Objects.equals(board.getTurn(), "O")){
            smallBoardButtons[playS / 10][playS % 10].setImageResource(R.drawable.ic_o);
        }


        if(Objects.equals(nextTurnPlayString, "X")){
            //end game
        }
        else if(Objects.equals(nextTurnPlayString, "O")){
            //end game
        }
        else {
            NextTurn();
            CheckPlay();
        }
    }

    public void NextTurn(){
        ResetButtons();
        if(Objects.equals(nextTurnPlayString, "any")) {
            nextTurnPlayNumberSBoard = -1;
            for (int a = 0; a < 3; a++) {
                for (int b = 0; b < 3; b++) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            if(board.getSmallBoard()[a][b][i][j] == 0){
                                smallBoardButtons[i + a * 3][j + a * 3].setImageResource(R.drawable.ic_yellow);
                                smallBoardButtons[i  + a * 3][j  + a * 3].setClickable(true);
                            }
                        }
                    }
                }
            }
        }
        else {
            nextTurnPlayNumberSBoard = Integer.parseInt(nextTurnPlayString);
            switch (nextTurnPlayNumberSBoard / 10) {
                case 0:
                case 1:
                case 2:
                    nextTurnPlayNumberBBoard = 0;
                    break;
                case 3:
                case 4:
                case 5:
                    nextTurnPlayNumberBBoard = 10;
                    break;
                case 6:
                case 7:
                case 8:
                    nextTurnPlayNumberBBoard = 20;
                    break;
            }
            switch (nextTurnPlayNumberSBoard % 10) {
                //for 0, 1, 2 i add 0 so it has no need
                case 3:
                case 4:
                case 5:
                    nextTurnPlayNumberBBoard += 1;
                    break;
                case 6:
                case 7:
                case 8:
                    nextTurnPlayNumberBBoard += 2;
                    break;
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if(board.getSmallBoard()[nextTurnPlayNumberSBoard / 10][nextTurnPlayNumberSBoard % 10][i][j] == 0){
                        smallBoardButtons[i + nextTurnPlayNumberSBoard / 10 * 3][j + nextTurnPlayNumberSBoard % 10 * 3].setImageResource(R.drawable.ic_yellow);
                        smallBoardButtons[i + nextTurnPlayNumberSBoard / 10 * 3][j + nextTurnPlayNumberSBoard % 10 * 3].setClickable(true);
                    }
                }
            }
        }
        return;
    }

    public void ResetButtons(){
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if(board.getSmallBoard()[a][b][i][j] == 0){
                            smallBoardButtons[i][j].setImageResource(R.drawable.ic_empty);
                            smallBoardButtons[i][j].setClickable(false);
                        }
                    }
                }
            }
        }
    }

    public void BotClickButton(int play) {
        if (smallBoardButtons[play / 10][play % 10].isClickable()) {
            smallBoardButtons[play / 10][play % 10].performClick();
        } else {
            //the player wins
        }
    }
}