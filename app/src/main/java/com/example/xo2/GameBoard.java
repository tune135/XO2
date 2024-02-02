package com.example.xo2;

public class GameBoard {
    private String turn; //מי משחק
    private int[][] bigBoard; //מערך דו ממדי של הלוח הגדול
    private int[][][][] smallBoard; //מערך דו ממדי של מערכים דו ממדיים של הלוחות הקטנים

    public GameBoard(){
        this.turn = "X"; //X מתחיל את המשחק
        this.bigBoard = new int[3][3]; //3*3 גודל של לוח רגיל
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                this.bigBoard[i][j] = 0; //הכנסת 0 לאיפוס
            }
        }
        this.smallBoard = new int[3][3][3][3]; //גודל 3*3 בשביל 9 הלוחות
        for(int a = 0; a < 3; a++){
            for (int b = 0; b < 3; b++){
                for(int i = 0; i < 3; i++){
                    for (int j = 0; j < 3; j++){
                        this.smallBoard[a][b][i][j] = 0; //הכנסת מערך 3*3 עם ערכים של 0 לאיפוס
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
                smallBoard[playOnB / 10][playOnB % 10][playOnS / 10][playOnS % 10] = 1;

                // Check for a win in the small board
                if (WinCheck(smallBoard[playOnB / 10][playOnB % 10], 1)) {
                    // Update the corresponding cell in the big board
                    bigBoard[playOnB / 10][playOnB % 10] = 1;
                    for(int i = 0; i < 3; i++){
                        for(int j = 0; j < 3; j++){
                            smallBoard[playOnB / 10][playOnB % 10][i][j] = 1;
                        }
                    }

                    // Check for a win in the big board
                    if (WinCheck(bigBoard, 1)) {
                        return "X";
                    }
                }

                // Check for a draw in the big board or no more play options in the small boards
                if (BigBoardDrawCheck(bigBoard) || NoMorePlayOptionsDrawCheck(smallBoard)) {
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
                smallBoard[playOnB / 10][playOnB % 10][playOnS / 10][playOnS % 10] = -1;

                // Check for a win in the small board
                if (WinCheck(smallBoard[playOnB / 10][playOnB % 10], -1)) {
                    // Update the corresponding cell in the big board
                    bigBoard[playOnB / 10][playOnB % 10] = -1;

                    for(int i = 0; i < 3; i++){
                        for(int j = 0; j < 3; j++){
                            smallBoard[playOnB / 10][playOnB % 10][i][j] = -1;
                        }
                    }

                    // Check for a win in the big board
                    if (WinCheck(bigBoard, -1)) {
                        return "O";
                    }
                }

                // Check for a draw in the big board or no more play options in the small boards
                if (BigBoardDrawCheck(bigBoard) || NoMorePlayOptionsDrawCheck(smallBoard)) {
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
        // בדיקת שורות וטורים
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true; // שורה
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true; // טור
            }
        }

        // בדיקת אלכסונים
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true; // אלכסון שמאל למעלה עד ימין למטה
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true; // אלכסון ימין למעלה עד שמאל למטה
        }

        return false; // No win
    }
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
