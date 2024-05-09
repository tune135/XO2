package com.example.xo2;

import android.widget.Toast;

// The Bot class represents the AI logic for an Ultimate Tic-Tac-Toe game.
public class Bot {
    private GameBoard gb; // GameBoard instance to manage the board state.
    private int[][] playPriority; // Priority matrix for moves.
    private int botPlay; // The last move played by the bot.
    private int botNum; // Bot number: 1 for X, -1 for O.

    /**
     * Constructor to initialize the Bot with a game board and bot number.
     *
     * @param gb     The GameBoard instance representing the game state.
     * @param botNum The player number assigned to the bot (1 for X, -1 for O).
     */
    public Bot(GameBoard gb, int botNum) {
        this.gb = gb;
        this.botNum = botNum;
        this.playPriority = new int[3][3]; // Initialize priority grid.
        // Set all priority values to zero.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.playPriority[i][j] = 0;
            }
        }
    }

    // Getter and setter methods for Bot properties.
    public int getBotNum() { return this.botNum; }
    public void setBotNum(int botNum) { this.botNum = botNum; }
    public int getBotPlay() { return botPlay; }
    public void setBotPlay(int botPlay) { this.botPlay = botPlay; }
    public GameBoard getGb() { return gb; }
    public void setGb(GameBoard gb) { this.gb = gb; }

    /**
     * Main logic for bot to decide its next move based on the current state of the game.
     * @param boardPlay Position of the current board in play.
     * @return The calculated best move for the bot.
     */
    public int playBot(int boardPlay) {
        // Reset botPlay and playPriority for new calculation.
        this.botPlay = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.playPriority[i][j] = 0;
            }
        }
        // Strategy implementation, e.g., check for immediate win or block opponent.
        // This method needs to be filled with appropriate logic for choosing moves.
        // Currently placeholders suggest logic areas to implement, like evaluating best move based on priority.
        return this.botPlay;
    }

    /**
     * Check if a move can win a small board game.
     * @param boardPlay Index of the board to play on.
     * @param i Row of the move.
     * @param j Column of the move.
     * @return True if the move can win the small board.
     */
    public boolean canWinSBoard(int boardPlay, int i, int j) {
        return canWinNextMoveSmallBoardIJ(this.gb.getSmallBoard()[boardPlay / 10][boardPlay % 10], this.botNum, i, j);
    }

    /**
     * Check for a winning move on a specified small board for a given player.
     * @param board The small board to check.
     * @param player The player number.
     * @param i Row index of the move.
     * @param j Column index of the move.
     * @return True if the move results in a win.
     */
    public boolean canWinNextMoveSmallBoardIJ(int[][] board, int player, int i, int j) {
        // Simulate the move and check for a win.
        if (board[i][j] == 0) {
            board[i][j] = player;
            if (this.gb.WinCheck(board, player)) {
                board[i][j] = 0; // Undo the move after check.
                this.botPlay = i * 10 + j; // Record winning move.
                return true;
            }
            board[i][j] = 0; // Undo move if not winning.
        }
        return false;
    }

}
