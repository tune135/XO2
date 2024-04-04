package com.example.xo2;

//The Bot class represents the logic for an AI player in the ultimate tic-tac-toe game.
public class Bot {

    private GameBoard gb; //Game Board class
    private int[][] playPossibility; // Where the bot canceled the play
    private int botPlay; // Where the bot plays
    private int botNum; //if bot plays X it will get 1 if bot plays O it will get -1


    /**
     * Constructor for the Bot class.
     *
     * @param gb     The GameBoard instance representing the game state.
     * @param botNum The player number assigned to the bot (1 for X, -1 for O).
     */
    public Bot(GameBoard gb, int botNum) {
        this.gb = gb;
        this.botNum = botNum;
        playPossibility = new int[3][3];
        // Initialize playPossibility
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.playPossibility[i][j] = 0;
            }
        }
    }

    /**
     * Gets the bots number (1 for X, -1 for O).
     *
     * @return The bots number.
     */
    public int getBotNum() {
        return this.botNum;
    }
    /**
     * Sets the player number for the bot (1 for X, -1 for O).
     *
     * @param botNum The player number to set for the bot.
     */
    public void setBotNum(int botNum) {
        this.botNum = botNum;
    }

    /**
     * Gets the last chosen move by the bot.
     *
     * @return The last move chosen by the bot.
     */
    public int getBotPlay() {
        return botPlay;
    }

    /**
     * Sets the last chosen move by the bot.
     *
     * @param botPlay The move to set as the last chosen move by the bot.
     */
    public void setBotPlay(int botPlay) {
        this.botPlay = botPlay;
    }

    /**
     * Gets the current game board associated with the bot.
     *
     * @return The current game board.
     */
    public GameBoard getGb() {
        return gb;
    }

    /**
     * Sets the game board for the bot.
     *
     * @param gb The game board to set for the bot.
     */
    public void setGb(GameBoard gb) {
        this.gb = gb;
    }


    public int playBot(int boardPlay) {
        // Initialize play
        this.botPlay = 0;
        if (boardPlay == -1) { //-1 for any play and return the full id of the button small board and big board
            int bestPlayProp = 0;
            int nowPlayProp;
            int nowBotPlay;
            int bestPlayBigBoard = 0;
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(this.gb.getBigBoard()[i][j] == 0)
                    {
                        nowBotPlay = playBot(i * 10 + j);
                        nowPlayProp = this.playPossibility[this.botPlay /10][this.botPlay % 10];
                        if(bestPlayProp < nowPlayProp){
                            this.botPlay = nowBotPlay;
                            bestPlayBigBoard = i * 10 + j;
                            bestPlayProp = nowPlayProp;
                        }
                    }
                }
            }
            this.botPlay = bestPlayBigBoard * 100 + this.botPlay;
        } else {
            // Initialize play
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    this.playPossibility[i][j] = this.gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][i][j];
                }
            }
            while (canWinSBoard(boardPlay)) {
                // Copy the small board state to playPossibility
                if (canWinBBoard()) {
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 10;//A number that isn't 1, -1, or 0 to show the how much this option is good
                    return this.botPlay;
                } else if (otherPlayerWinSBoardNextTurn()) {
                    // Mark the position as not allowing the opponent to win
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 6; //A number that isn't 1, -1, or 0 to show the how much this option is good
                } else if (otherPlayerCanPlayAnywhere()) {
                    // Mark the position as available for the opponent to play
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 6;//A number that isn't 1, -1, or 0 to show the how much this option is good
                } else {
                    return this.botPlay;
                }
            }
            if (isMiddleAvailableForCatching(boardPlay)) {
                if (otherPlayerWinSBoardNextTurn()) {
                    // Mark the position as not allowing the opponent to win after catching the middle
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 5;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                } else if (otherPlayerCanPlayAnywhere()) {
                    // Mark the position as available for the opponent to play after catching the middle
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 5;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                } else {  // The numbers are different, so you can tell if he wins the board or not at this play
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 9;     //A number that isn't 1, -1, or 0 to show the how much this option is good
                    return this.botPlay;
                }
            }
            while (canCatchRandomCorner(boardPlay)) {
                if (otherPlayerWinSBoardNextTurn()) {
                    // Mark the position as available for the opponent to play after catching a random corner
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 4;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                } else if (otherPlayerCanPlayAnywhere()) {
                    // Mark the position as available for the opponent to play after catching a random corner
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 4;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                } else {
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 8;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                    return this.botPlay;
                }
            }
            while (canCatchRandomMiddleSide(boardPlay)) {
                if (otherPlayerWinSBoardNextTurn()) {
                    // Mark the position as available for the opponent to play after catching a random middle side
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 3;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                } else if (otherPlayerCanPlayAnywhere()) {
                    // Mark the position as available for the opponent to play after catching a random middle side
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 3;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                } else {
                    this.playPossibility[this.botPlay / 10][this.botPlay % 10] = 7;    //A number that isn't 1, -1, or 0 to show the how much this option is good
                    return this.botPlay;
                }
            }


            // Check positions where the bot can win the small board, but the player next turn can win a small board or play anywhere
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (this.playPossibility[i][j] == 6){
                        this.botPlay = i * 10 + j;
                        return this.botPlay;
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (this.playPossibility[i][j] == 5){
                        this.botPlay = i * 10 + j;
                        return this.botPlay;
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (this.playPossibility[i][j] == 4){
                        this.botPlay = i * 10 + j;
                        return this.botPlay;
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (this.playPossibility[i][j] == 3){
                        this.botPlay = i * 10 + j;
                        return this.botPlay;
                    }
                }
            }

        }
        return this.botPlay;
    }

    /**
     * Check if the bot can win the current small board in the specified position.
     *
     * @param boardPlay The position of the small board where the bot is making a move.
     * @return True if the bot can win the small board on the next move, false otherwise.
     */
    public Boolean canWinSBoard(int boardPlay) {
        return canWinNextMoveSmallBoard(this.gb.getSmallBoard()[boardPlay / 10][boardPlay % 10], this.botNum);
    }

    /**
     * Check if the bot can win the big board.
     *
     * @return True if the bot can win the big board on the next move, false otherwise.
     */
    public Boolean canWinBBoard() {
        return canWinNextMoveBigBoard(this.gb.getBigBoard(), this.botNum);
    }

    /**
     * Check if the other player can win the next small board on the next turn.
     *
     * @return True if the other player can win the small board on the next turn, false otherwise.
     */
    public Boolean otherPlayerWinSBoardNextTurn() {
        if (this.botNum == 1) {
            return canWinNextMoveSmallBoard(this.gb.getSmallBoard()[this.botPlay / 10][this.botPlay % 10], -1);
        } else if (this.botNum == -1) {
            return canWinNextMoveSmallBoard(this.gb.getSmallBoard()[this.botPlay / 10][this.botPlay % 10], 1);
        } else {
            //error
            return null;
        }
    }

    /**
     * Check if the other player can play anywhere on the current big board.
     *
     * @return True if the other player can play anywhere on the big board, false otherwise.
     */
    public Boolean otherPlayerCanPlayAnywhere() {
        if (gb.getBigBoard()[this.botPlay / 10][this.botPlay % 10] == 0) {
            return false;
        } else if (gb.getBigBoard()[this.botPlay / 10][this.botPlay % 10] == 1 || gb.getBigBoard()[this.botPlay / 10][this.botPlay % 10] == -1) {
            return true;
        } else {
            return null;
        }
    }

    /**
     * Check if the middle position is available for catching on the current small board.
     *
     * @param boardPlay The position of the small board where the bot is making a move.
     * @return True if the middle position is available for catching, false otherwise.
     */
    public Boolean isMiddleAvailableForCatching(int boardPlay) {
        if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][1] == 1 || gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][1] == -1 || playPossibility[1][1] != 0) {
            return false;
        } else if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][1] == 0) {
            return true;
        } else {
            return null;
        }
    }

    /**
     * Check if the bot can catch a random corner on the current small board.
     *
     * @param boardPlay The position of the small board where the bot is making a move.
     * @return True if the bot can catch a random corner, false otherwise.
     */
    public Boolean canCatchRandomCorner(int boardPlay) {
        //#1
        if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][0][0] == 1 || gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][0][0] == -1 || playPossibility[0][0] != 0) {
            return false;
        } else if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][0][0] == 0) {
            return true;
        }

        //#2
        if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][0][2] == 1 || gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][0][2] == -1 || playPossibility[0][2] != 0) {
            return false;
        } else if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][0][2] == 0) {
            return true;
        }

        //#3
        if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][2][0] == 1 || gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][2][0] == -1 || playPossibility[2][0] != 0) {
            return false;
        } else if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][2][0] == 0) {
            return true;
        }

        //#4
        if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][2][2] == 1 || gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][2][2] == -1 || playPossibility[2][2] != 0) {
            return false;
        } else if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][2][2] == 0) {
            return true;
        } else {
            return null;
        }
    }

    /**
     * Check if the bot can catch a random middle side on the current small board.
     *
     * @param boardPlay The position of the small board where the bot is making a move.
     * @return True if the bot can catch a random middle side, false otherwise.
     */
    public Boolean canCatchRandomMiddleSide(int boardPlay) {
        //#1
        if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][0][1] == 1 || gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][0][1] == -1 || playPossibility[0][1] != 0) {
            return false;
        } else if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][0][1] == 0) {
            return true;
        }

        //#2
        if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][2] == 1 || gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][2] == -1 || playPossibility[1][2] != 0) {
            return false;
        } else if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][2] == 0) {
            return true;
        }

        //#3
        if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][2][1] == 1 || gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][2][1] == -1 || playPossibility[2][1] != 0) {
            return false;
        } else if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][2][1] == 0) {
            return true;
        }

        //#4
        if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][0] == 1 || gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][0] == -1 || playPossibility[1][0] != 0) {
            return false;
        } else if (gb.getSmallBoard()[boardPlay / 10][boardPlay % 10][1][0] == 0) {
            return true;
        } else {
            return null;
        }
    }


    /**
     * Checks if the given player can win on the next move in a small tic-tac-toe board.
     *
     * @param board  The small tic-tac-toe board to check.
     * @param player The player (X or O) for whom to check the winning possibility.
     * @return true if the player can win on the next move, false otherwise.
     */
    public boolean canWinNextMoveSmallBoard(int[][] board, int player) {
        // Check each empty spot on the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
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
            }
        }

        return false; // Player cannot win on the next move
    }

    /**
     * Checks if the given player can win on the next move in a big tic-tac-toe board.
     *
     * @param board  The big tic-tac-toe board to check.
     * @param player The player (X or O) for whom to check the winning possibility.
     * @return true if the player can win on the next move, false otherwise.
     */
    public boolean canWinNextMoveBigBoard(int[][] board, int player) {
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
}