package com.example.xo2;

public class GameBoard {
    private String turn; // Who's turn it is to play
    private int[][] bigBoard; // Two-dimensional array of the big board
    private int[][][][] smallBoard; // Two-dimensional array of two-dimensional arrays for the small boards

    //Constructor for the GameBoard class initializes the game with player X starting and sets all board positions to zero.
    public GameBoard(){
        this.turn = "X"; // X starts the game
        this.bigBoard = new int[3][3]; // Standard 3x3 board size
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                this.bigBoard[i][j] = 0; // Setting all values to 0 to reset
            }
        }
        this.smallBoard = new int[3][3][3][3]; // Size 3x3 for the 9 small boards
        for(int a = 0; a < 3; a++){
            for (int b = 0; b < 3; b++){
                for(int i = 0; i < 3; i++){
                    for (int j = 0; j < 3; j++){
                        this.smallBoard[a][b][i][j] = 0; // Inserting a 3x3 array with 0 values to reset
                    }
                }
            }
        }
    }


    public String getTurn() {
        return turn;
    }
    // set and get turn
    public void setTurn(String turn) {
        this.turn = turn;
    }

    public int[][] getBigBoard() {
        return bigBoard;
    }
    // set and get BigBoard
    public void setBigBoard(int[][] bigBoard) {
        this.bigBoard = bigBoard;
    }

    public int[][][][] getSmallBoard() {
        return smallBoard;
    }
    // set and get smallBoard
    public void setSmallBoard(int[][][][] smallBoard) {
        this.smallBoard = smallBoard;
    }

    /**
     * Manages the gameplay logic for a player's move.
     *
     * @param playOnS Coordinates for the small board (e.g., 22 for the center)
     * @param playOnB Coordinates for the big board (e.g., 11 for the top-left)
     * @return Result of the move - "X", "O", "Draw", "any", or "-1000"
     */
    public String gamePlay(int playOnS, int playOnB) {
        switch (this.turn) {
            case "X": {
                // Perform X's move on the small board
                this.smallBoard[playOnB / 10][playOnB % 10][playOnS / 10][playOnS % 10] = 1;

                // Check for a win in the small board
                if (WinCheck(this.smallBoard[playOnB / 10][playOnB % 10], 1)) {
                    // Update the corresponding cell in the big board
                    bigBoard[playOnB / 10][playOnB % 10] = 1;
                    for(int i = 0; i < 3; i++){
                        for(int j = 0; j < 3; j++){
                            this.smallBoard[playOnB / 10][playOnB % 10][i][j] = 1;
                        }
                    }

                    // Check for a win in the big board
                    if (WinCheck(bigBoard, 1)) {
                        return "X";
                    }
                }

                // Check for a draw in the big board or no more play options in the small boards
                if (BigBoardDrawCheck(bigBoard) || NoMorePlayOptionsDrawCheck(this.smallBoard)) {
                    return "Draw";
                }

                // Switch turn to "O" and continue the game
                this.turn = "O";

                // Check if the selected cell in the big board is empty
                if (bigBoard[playOnS / 10][playOnS % 10] == 0) {
                    return playOnS + "";
                } else {
                    return "any"; // Cell is already occupied
                }
            }
            case "O": {
                // Perform O's move on the small board
                this.smallBoard[playOnB / 10][playOnB % 10][playOnS / 10][playOnS % 10] = -1;

                // Check for a win in the small board
                if (WinCheck(this.smallBoard[playOnB / 10][playOnB % 10], -1)) {
                    // Update the corresponding cell in the big board
                    bigBoard[playOnB / 10][playOnB % 10] = -1;

                    for(int i = 0; i < 3; i++){
                        for(int j = 0; j < 3; j++){
                            this.smallBoard[playOnB / 10][playOnB % 10][i][j] = -1;
                        }
                    }

                    // Check for a win in the big board
                    if (WinCheck(bigBoard, -1)) {
                        return "O";
                    }
                }

                // Check for a draw in the big board or no more play options in the small boards
                if (BigBoardDrawCheck(bigBoard) || NoMorePlayOptionsDrawCheck(this.smallBoard)) {
                    return "Draw";
                }

                // Switch turn to "X" and continue the game
                this.turn = "X";

                // Check if the selected cell in the big board is empty
                if (bigBoard[playOnS / 10][playOnS % 10] == 0) {
                    return playOnS + "";
                } else {
                    return "any"; // Cell is already occupied
                }
            }
            default:
                return "-1000"; // Invalid turn indicator
        }
    }


    public Boolean WinCheck(int[][] board, int player){
        // Checking rows and columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true; // שורה
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true; // טור
            }
        }

        // Checking diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true; // אלכסון שמאל למעלה עד ימין למטה
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true; // אלכסון ימין למעלה עד שמאל למטה
        }

        return false; // No win
    }

    //check the big board for a possible draw
    public boolean BigBoardDrawCheck(int[][] board){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false; // There is an empty cell, not a draw
                }
            }
        }
        return true; // All cells are filled, it's a draw
    }

    //Check if there is still a playable cell
    public boolean NoMorePlayOptionsDrawCheck(int[][][][] smallBoard){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        if (smallBoard[i][j][k][l] == 0) {
                            return false; // There is an empty cell, not a draw
                        }
                    }
                }
            }
        }
        return true; // All cells are filled, it's a draw
    }


}
