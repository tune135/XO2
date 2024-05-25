package com.example.xo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;

/**
 * The GameABot20 class handles the interactive gameplay between a human player and an AI bot
 * in a tic-tac-toe game with a complex board structure.
 */
public class GameABot20 extends AppCompatActivity {

    // Game components
    GridLayout[][] bigBoardGrids; // GridLayout arrays for big tic-tac-toe boards
    ImageButton[][][][] smallBoardButtons; // ImageButtons for the small tic-tac-toe boards within each big board cell
    GameBoard board; // GameBoard object to manage the game state
    Bot bot; // Bot object for AI logic
    TextView turnView; // TextView to display whose turn it is
    TextView timerTextView; // TextView for displaying the countdown timer
    int playS; // Index for small board play
    int playB; // Index for big board play
    String nextTurnPlayString; // Stores string value of the next turn
    int nextTurnPlayNumberSBoard = -1; // Next turn play index for small boards, initialized to -1
    Intent intent; // Intent for navigating to different activities
    CountDownTimer countDownTimer; // Timer object for turn timing
    boolean timerRunning = false; // Flag to check if the timer is running
    Button ret; //Button to return to menu


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_abot20);

        // Initialize game board and AI components
        board = new GameBoard();
        initializeGridLayouts();
        initializeImageButtons();

        ret = findViewById(R.id.returnTo);
        turnView = findViewById(R.id.turn2);
        setupInitialPlayer();
    }

    //Initializes GridLayouts for the big board cells.
    private void initializeGridLayouts() {
        bigBoardGrids = new GridLayout[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                bigBoardGrids[i][j] = (GridLayout) FindViewGrid(i, j);
            }
        }
    }

    //Initializes ImageButtons for each cell within the small boards.
    private void initializeImageButtons() {
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
    }

    //Determines initial player and initializes bot based on random selection.
    private void setupInitialPlayer() {
        Random random = new Random();
        int playerNumber = random.nextInt(2) * 2 - 1; // Randomly assigns 1 or -1
        bot = new Bot(board, -playerNumber);
        String playerSymbol = playerNumber == 1 ? "X" : "O";
        turnView.setText("Your Turn (" + playerSymbol + ")");
        if (playerNumber == -1) {
            BotClickButton(11, 11); // Let bot make the first move if it starts
        } else {
            CheckPlay(); // Check the play if the player starts
        }
    }

    //Finds an ImageButton by its ID constructed from its grid coordinates.
    public View FindViewImageButton(int a, int b, int i, int j) {
        String buttonID = "imageButton" + a + b + i + j;
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        return findViewById(resID);
    }

    //Finds a GridLayout by its ID constructed from its coordinates.
    public View FindViewGrid(int i, int j) {
        String gridID = "grid" + i + j;
        int resID = getResources().getIdentifier(gridID, "id", getPackageName());
        return findViewById(resID);
    }


    // Check if it's bot's turn and let it play or start the timer for the human player
    public void CheckPlay() {
        if ((Objects.equals(bot.getGb().getTurn(), "X") && bot.getBotNum() == 1) || (Objects.equals(bot.getGb().getTurn(), "O") && bot.getBotNum() == -1)) {
            BotClickButton(bot.playBot(nextTurnPlayNumberSBoard), nextTurnPlayNumberSBoard);
        } else {
            ApplyTimer();
        }
    }

    // method to apply a countdown timer for the human player's turn:
    public void ApplyTimer() {
        timerTextView = findViewById(R.id.timerTextView);
        if (timerRunning) {
            stopTimer();
        }
        countDownTimer = new CountDownTimer(120000, 1000) { // 2 minutes timer
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                timerTextView.setText(String.format("Timer: %02d:%02d", seconds / 60, seconds % 60));
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
            smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setClickable(false);
        } else if (Objects.equals(bot.getGb().getTurn(), "O")) {
            smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setImageResource(R.drawable.ic_o);
            smallBoardButtons[playB / 10][playB % 10][playS / 10][playS % 10].setClickable(false);
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
                GameEnd("You Lost \nNo Worries You Can Play Again");
            } else {
                GameEnd("You won! \nWell Played You Defeated The Bot");
            }
        } else if (Objects.equals(nextTurnPlayString, "O")) {
            if (bot.getBotNum() == 1) {
                GameEnd("You won! \nWell Played You Defeated The Bot");
            } else {
                GameEnd("You Lost \nNo Worries You Can Play Again");
            }
        } else if (Objects.equals(nextTurnPlayString, "Draw")) {
            GameEnd("The Game Ended As A Draw");
        } else {
            NextTurn();
            CheckPlay();
        }
    }

    //Method for when the game ended
    public void GameEnd(String result) {
        turnView.setText(result);
        turnView.setTextSize(20);
        stopTimer();
        ResetButtons();
        ret.setVisibility(View.VISIBLE); // Make the button visible
        ret.setEnabled(true); // Make the button clickable
    }

    //Button to return to the Game Menu
    public void returnToMenu(View view) {
        intent = new Intent(GameABot20.this, GameMenu.class);
        startActivity(intent); // return to menu
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
        int boardIndexI = playBBot / 10;
        int boardIndexJ = playBBot % 10;
        int buttonIndexI = playSBot / 10;
        int buttonIndexJ = playSBot % 10;

        // Check if the indices are within bounds
        if (boardIndexI >= 0 && boardIndexI < 3 &&
                boardIndexJ >= 0 && boardIndexJ < 3 &&
                buttonIndexI >= 0 && buttonIndexI < 3 &&
                buttonIndexJ >= 0 && buttonIndexJ < 3) {

            if (smallBoardButtons[boardIndexI][boardIndexJ][buttonIndexI][buttonIndexJ].isClickable()) {
                smallBoardButtons[boardIndexI][boardIndexJ][buttonIndexI][buttonIndexJ].performClick();
            } else {

                Toast.makeText(GameABot20.this, "Bot Not Working", Toast.LENGTH_SHORT).show();
                GameEnd("You won! \nThere Was A Problem In The Bot And We Will Fix It Soon");
            }
        } else {
            Toast.makeText(GameABot20.this, "Bot Not Working", Toast.LENGTH_SHORT).show();
            GameEnd("You won! \nThere Was A Problem In The Bot And We Will Fix It Soon");
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.Quit) {
            intent = new Intent(GameABot20.this, GameMenu.class);
            startActivity(intent); // Quit the game
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
