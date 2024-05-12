package com.example.xo2;

//The Bot class represents the logic for an AI player in the ultimate tic-tac-toe game.
public class Bot {

    private GameBoard gb; //Game Board class
    private int[][] playPriority; // Where the bot canceled the play
    private int botPlay; // Where the bot plays
    private int botNum; //if bot plays X it will get 1 if bot plays O it will get -1


    //Constructor for the Bot class.
    public Bot(GameBoard gb, int botNum) {
        this.gb = gb;
        this.botNum = botNum;
        playPriority = new int[3][3];
        // Initialize playPriority
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.playPriority[i][j] = 0;
            }
        }
    }

    //Gets the bots number (1 for X, -1 for O).
    public int getBotNum() {
        return this.botNum;
    }

    //Gets the current game board associated with the bot.
    public GameBoard getGb() {
        return gb;
    }

    //check the best option for the bot to play
    public int playBot(int boardPlay) {
        // Initialize play
        this.botPlay = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.playPriority[i][j] = 0;
            }
        }
        if (boardPlay == -1) { //-1 for any play and return the full id of the button small board and big board
            int bestPlayPrio = 0; //Best Play Priority for the Bot
            int nowPlayPrio; //the play priority that is now in check
            int nowBotPlay; //the bot move that is now in check
            int bestBotPlay = 0; //the best move for the bot
            int bestPlayBigBoard = 0; //the big board for the best move for the bot
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(this.gb.getBigBoard()[i][j] == 0)
                    { //check for the best move of every playable board
                        nowBotPlay = playBot(i * 10 + j);
                        nowPlayPrio = this.playPriority[nowBotPlay / 10][nowBotPlay % 10];
                        if(bestPlayPrio <= nowPlayPrio){
                            bestBotPlay = nowBotPlay;
                            bestPlayBigBoard = i * 10 + j;
                            bestPlayPrio = nowPlayPrio;
                        }
                    }
                }
            }
            this.botPlay = bestPlayBigBoard * 100 + bestBotPlay;
        } else {
            for(int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if(canWinSBoard(boardPlay, i ,j)) {
                        if (canWinBBoard()) {
                            this.playPriority[this.botPlay / 10][this.botPlay % 10] = 10;//A number that isn't 1, -1, or 0 to show the how much this option is good
                            return this.botPlay;
                        } else if (otherPlayerWinSBoardNextTurn(boardPlay)) {
                            // Mark the position as not allowing the opponent to win
                            if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 5){
                                this.playPriority[this.botPlay / 10][this.botPlay % 10] = 5; //A number that isn't 1, -1, or 0 to show the how much this option is good
                            }
                        } else if (otherPlayerCanPlayAnywhere()) {
                            // Mark the position as available for the opponent to play
                            if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 5){
                                this.playPriority[this.botPlay / 10][this.botPlay % 10] = 5;//A number that isn't 1, -1, or 0 to show the how much this option is good
                            }
                        } else {
                            return this.botPlay;
                        }
                    }
                }
            }
            //if the other player can catch this board
            if(this.botNum == 1){
                for(int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if(canOtherPlayerWinThisSmallBoard(this.gb.getSmallBoard()[boardPlay / 10][boardPlay % 10], -1, i, j)){
                            if (otherPlayerWinSBoardNextTurn(boardPlay)) {
                                // Mark the position as not allowing the opponent to win after catching the middle
                                if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 4){
                                    this.playPriority[this.botPlay / 10][this.botPlay % 10] = 4;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                                }
                            } else if (otherPlayerCanPlayAnywhere()) {
                                // Mark the position as available for the opponent to play after catching the middle
                                if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 4){
                                    this.playPriority[this.botPlay / 10][this.botPlay % 10] = 4;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                                }
                            } else {  // The numbers are different, so you can tell if he wins the board or not at this play
                                this.playPriority[this.botPlay / 10][this.botPlay % 10] = 9;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                                return this.botPlay;
                            }
                        }
                    }
                }

            } else{
                for(int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if(canOtherPlayerWinThisSmallBoard(this.gb.getSmallBoard()[boardPlay / 10][boardPlay % 10], 1, i, j)){
                            if (otherPlayerWinSBoardNextTurn(boardPlay)) {
                                // Mark the position as not allowing the opponent to win after catching the middle
                                if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 4){
                                    this.playPriority[this.botPlay / 10][this.botPlay % 10] = 4;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                                }
                            } else if (otherPlayerCanPlayAnywhere()) {
                                // Mark the position as available for the opponent to play after catching the middle
                                if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 4){
                                    this.playPriority[this.botPlay / 10][this.botPlay % 10] = 4;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                                }
                            } else {  // The numbers are different, so you can tell if he wins the board or not at this play
                                this.playPriority[this.botPlay / 10][this.botPlay % 10] = 9;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                                return this.botPlay;
                            }
                        }
                    }
                }

            }
            if (isMiddleAvailableForCatching(boardPlay)) {
                if (otherPlayerWinSBoardNextTurn(boardPlay)) {
                    // Mark the position as not allowing the opponent to win after catching the middle
                    if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 3){
                        this.playPriority[this.botPlay / 10][this.botPlay % 10] = 3;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                    }
                } else if (otherPlayerCanPlayAnywhere()) {
                    // Mark the position as available for the opponent to play after catching the middle
                    if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 3){
                        this.playPriority[this.botPlay / 10][this.botPlay % 10] = 3;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                    }
                } else {  // The numbers are different, so you can tell if he wins the board or not at this play
                    this.playPriority[this.botPlay / 10][this.botPlay % 10] = 8;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                    return this.botPlay;
                }
            }

            for (int i = 0; i <= 2; i += 2) {
                for (int j = 0; j <= 2; j += 2){
                    // 00 02 20 22
                    if(canCatchIt(boardPlay, i, j)){
                        if (otherPlayerWinSBoardNextTurn(boardPlay)) {
                            // Mark the position as available for the opponent to play after catching a random corner
                            if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 2){
                                this.playPriority[this.botPlay / 10][this.botPlay % 10] = 2;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                            }
                        } else if (otherPlayerCanPlayAnywhere()) {
                            // Mark the position as available for the opponent to play after catching a random corner
                            if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 2){
                                this.playPriority[this.botPlay / 10][this.botPlay % 10] = 2;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                            }
                        } else {
                            this.playPriority[this.botPlay / 10][this.botPlay % 10] = 7;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                            return this.botPlay;
                        }
                    }
                }
            }
            for (int i = 0; i <= 2; i++) {
                for (int j = 0; j <= 2; j++) {
                    if ((i == 0 && j == 1) || (i == 1 && j == 0) || (i == 1 && j == 2) || (i == 2 && j == 1)) {
                        //0(i) 1(j), 1(i) 0(j), 1(i) 2(j), 2(i) 1(j)
                        if(canCatchIt(boardPlay, i, j)){
                            if (otherPlayerWinSBoardNextTurn(boardPlay)) {
                                // Mark the position as available for the opponent to play after catching a random middle side
                                if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 1){
                                    this.playPriority[this.botPlay / 10][this.botPlay % 10] = 1;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                                }
                            } else if (otherPlayerCanPlayAnywhere()) {
                                // Mark the position as available for the opponent to play after catching a random middle side
                                if(this.playPriority[this.botPlay / 10][this.botPlay % 10] < 1){
                                    this.playPriority[this.botPlay / 10][this.botPlay % 10] = 1;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                                }
                            } else {
                                this.playPriority[this.botPlay / 10][this.botPlay % 10] = 6;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                                return this.botPlay;
                            }
                        }
                    }
                }
            }


            // Check positions where the bot can win the small board, or the player next turn can win a small board or play anywhere
            for (int priority = 5; priority >= 1; priority--) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (this.playPriority[i][j] == priority) {
                            this.botPlay = i * 10 + j;
                            return this.botPlay;
                        }
                    }
                }
            }

        }
        return this.botPlay;
    }

    //Check if the bot can win the current small board in the specified position.
    public Boolean canWinSBoard(int boardPlay ,int i , int j) {
        int[][] board = this.gb.getSmallBoard()[boardPlay / 10][boardPlay % 10];
        // Check each empty spot on the board
        if (board[i][j] == 0) {
            // Make the move and check if it results in a win
            board[i][j] = this.botNum;
            if (this.gb.WinCheck(board, this.botNum)) {
                // Undo the move
                this.botPlay = i * 10 + j;
                board[i][j] = 0;
                return true; // Player can win on the next move
            }
            // Undo the move
            board[i][j] = 0;
        }
        return false; // Player cannot win on the next move
    }

    //Check if the bot can win the big board
    public Boolean canWinBBoard() {
        int[][] board = this.gb.getBigBoard();
        if (board[this.botPlay / 10][this.botPlay % 10] == 0) {
            // Make the move and check if it results in a win
            board[this.botPlay / 10][this.botPlay % 10] = this.botNum;
            if (this.gb.WinCheck(board, this.botNum)) {
                // Undo the move
                board[this.botPlay / 10][this.botPlay % 10] = 0;
                return true; // Player can win on the next move
            }
            // Undo the move
            board[this.botPlay / 10][this.botPlay % 10] = 0;
        }
        return false; // Player cannot win on the next move
    }

    //Check if the other player can win the next small board on the next turn.
    public Boolean otherPlayerWinSBoardNextTurn(int board) {
        if(board == this.botPlay) {
            if (this.botNum == 1) {
                return canWinNextMoveSmallBoardNew(this.gb.getSmallBoard()[this.botPlay / 10][this.botPlay % 10], -1);
            } else if (this.botNum == -1) {
                return canWinNextMoveSmallBoardNew(this.gb.getSmallBoard()[this.botPlay / 10][this.botPlay % 10], 1);
            } else {
                //error
                return null;
            }
        }
        else{
            if (this.botNum == 1) {
                return canWinNextMoveSmallBoard(this.gb.getSmallBoard()[this.botPlay / 10][this.botPlay % 10], -1);
            } else if (this.botNum == -1) {
                return canWinNextMoveSmallBoard(this.gb.getSmallBoard()[this.botPlay / 10][this.botPlay % 10], 1);
            } else {
                //error
                return null;
            }
        }

    }

    //Check if the other player can play anywhere on the current big board.
    public Boolean otherPlayerCanPlayAnywhere() {
        if (gb.getBigBoard()[this.botPlay / 10][this.botPlay % 10] == 0) {
            return false;
        } else if (gb.getBigBoard()[this.botPlay / 10][this.botPlay % 10] == 1 || gb.getBigBoard()[this.botPlay / 10][this.botPlay % 10] == -1) {
            return true;
        } else {
            return null;
        }
    }

    //Check if the middle position is available for catching on the current small board.
    public Boolean isMiddleAvailableForCatching(int boardPlay) {
        if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][1] == 1 || gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][1] == -1 || playPriority[1][1] != 0) {
            return false;
        } else if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][1] == 0) {
            this.botPlay = 11;
            return true;
        } else {
            return null;
        }
    }

    //Check if the bot can catch a place that isn't the middle on the current small board.
    public Boolean canCatchIt(int boardPlay, int I, int J) {
        if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][I][J] == 1 || gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][I][J] == -1 || playPriority[I][J] != 0) {
            return false;
        } else if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][I][J] == 0) {
            this.botPlay = I * 10 + J;
            return true;
        } else {
            return null;
        }
    }


    // Checks if the given player can win on the next move in a small board (for other player)
    public boolean canWinNextMoveSmallBoard(int[][] board, int player) {
        // Check each empty spot on the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    // Make the move and check if it results in a win
                    board[i][j] = player;
                    if (this.gb.WinCheck(board, player)) {
                        // Undo the move
                        board[i][j] = 0;
                        return true; // Player can win on the next move
                    }
                    // Undo the move
                    board[i][j] = 0;
                }
            }
        }
        return false; // Player cannot win on the next move
    }

    //Checks if the given player can win on the next move in a small board
    //if this is the board the bot plays in now
    public boolean canWinNextMoveSmallBoardNew(int[][] board, int player) {
        // Check each empty spot on the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0 && (i * 10 + j != this.botPlay)) {
                    // Make the move and check if it results in a win
                    board[i][j] = player;
                    if (this.gb.WinCheck(board, player)) {
                        // Undo the move
                        board[i][j] = 0;
                        return true; // Player can win on the next move
                    }
                    // Undo the move
                    board[i][j] = 0;
                }
            }
        }
        return false; // Player cannot win on the next move
    }

    //Checks if the given player can win on the next move in a small board (for bot)
    public boolean canOtherPlayerWinThisSmallBoard(int[][] board, int player, int i, int j) {
        // Check an empty spot on the board
        if (board[i][j] == 0) {
            // Make the move and check if it results in a win
            board[i][j] = player;
            if (this.gb.WinCheck(board, player)) {
                // Undo the move
                this.botPlay = i * 10 + j;
                board[i][j] = 0;
                return true; // Player can win on the next move
            }
            // Undo the move
            board[i][j] = 0;
        }
        return false; // Player cannot win on the next move
    }

}