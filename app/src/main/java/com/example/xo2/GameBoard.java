package com.example.xo2;

public class GameBoard {
    private String turn; //מי משחק
    private int[][] bigBoard; //מערך דו ממדי של הלוח הגדול
    private int[][] [][] smallBoard; //מערך דו ממדי של מערכים דו ממדיים של הלוחות הקטנים

    public GameBoard(){
        this.turn = "X"; //X מתחיל את המשחק
        this.bigBoard = new int[3][3]; //3*3 גודל של לוח רגיל
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                this.bigBoard[i][j] = 0; //הכנסת 0 לאיפוס
            }
        }
        this.smallBoard = new int[3][3] [][]; //גודל 3*3 בשביל 9 הלוחות
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                this.smallBoard[i][j] = this.bigBoard; //הכנסת מערך 3*3 עם ערכים של 0 לאיפוס
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

    public String gamePlay(int playOnS, int playOnB){
        switch (this.turn)  {
            case "X":{
                smallBoard[playOnB / 10][playOnB % 10] [playOnS / 10][playOnS % 10] = 1;
                if(WinCheck(smallBoard[playOnB / 10][playOnB % 10], 1)) { //בדיקה אם שחקן ניצח בלוח הקטן
                    bigBoard[playOnB / 10][playOnB % 10] = 1;
                    if(WinCheck(bigBoard, 1)) { //בדיקה אם שחקן ניצח בלוח הגדול
                        return "X";
                    }
                }
                this.turn = "O";
                if(bigBoard[playOnS / 10][playOnS % 10] == 0) { //בדיקה אם הלוח הקטן בו השחקן השני בתור הבא אמור לשחק אם ניצחו בו
                    return playOnS + "";
                }
                else{
                    return "any";
                }
            }
            case "O":{
                smallBoard[playOnB / 10][playOnB % 10] [playOnS / 10][playOnS % 10] = -1;
                if(WinCheck(smallBoard[playOnB / 10][playOnB % 10], -1)) { //בדיקה אם שחקן ניצח בלוח הקטן
                    bigBoard[playOnB / 10][playOnB % 10] = -1;
                    if(WinCheck(bigBoard, -1)) { //בדיקה אם שחקן ניצח בלוח הגדול
                        return "O";
                    }
                }
                this.turn = "X";
                if(bigBoard[playOnS / 10][playOnS % 10] == 0) {//בדיקה אם הלוח הקטן בו השחקן השני בתור הבא אמור לשחק אם ניצחו בו
                    return playOnS + "";
                }
                else{
                    return "any";
                }
            }
        }

        return "-1000";
        /*
        swich (gamePlay(sBoard, bBoard))
        case "X" - X won
        case "O" - O won
        case "any" - play next turn anywhere
        case "00" / "01" / "02" / "10" / "11" / "12" / "20" / "21" / "22" - play next turn int that place on big board
        case "-1000" - error
         */
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

}
