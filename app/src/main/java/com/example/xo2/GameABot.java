package com.example.xo2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

public class GameABot extends AppCompatActivity {

    GridLayout[][] bigBoardGrids;
    ImageButton[][] smallBoardButtons;
    GameBoard board;
    Bot botPlay;
    TextView turnView;
    int gridId;
    int buttonId;
    int playS;
    int playB;
    String nextTurnPlayString;
    int nextTurnPlayNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_abot2);
        turnView = findViewById(R.id.turn);

        Random random = new Random();
        // Generate a random integer that is either 0 or 1
        int randomValue = random.nextInt(2);
        // Adjust the result to be either 1 or -1
        int playerNumber = (randomValue == 0) ? -1 : 1;

        if(playerNumber == 1){
            botPlay = new Bot(board, -1);
            turnView.setText("Your Turn (X)");
        } else if (playerNumber == -1) {
            botPlay = new Bot(board, 1);
            turnView.setText("bot's Turn (X)");
        }

        bigBoardGrids = new GridLayout[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gridId = getResources().getIdentifier("grid" + i + j, "id", getPackageName());
                bigBoardGrids[i][j] = findViewById(gridId);
            }
        }

        smallBoardButtons = new ImageButton[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                buttonId = getResources().getIdentifier("imageButton" + i + j, "id", getPackageName());
                smallBoardButtons[i][j] = findViewById(buttonId);
            }
        }

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
                        case 0:
                        case 1:
                        case 2:
                            playB += 0;
                            break;
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

        String nextTurnPlayString = board.gamePlay(playS, playB);
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
            NextTurn(nextTurnPlayString);
        }
    }

    public void NextTurn(String where){
        if(Objects.equals(where, "any")) {

        }
    }
}