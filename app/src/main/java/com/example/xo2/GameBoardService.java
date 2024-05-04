package com.example.xo2;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GameBoardService extends Service {
    private final IBinder binder = new GameBoardBinder();
    private GameBoard gameBoard;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the game board when the service is created
        gameBoard = new GameBoard();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // Binder class to provide binding capability with activities
    public class GameBoardBinder extends Binder {
        GameBoardService getService() {
            // Return this instance of GameBoardService so clients can call public methods
            return GameBoardService.this;
        }
    }

    // Public methods that can be called from the activity
    public String getCurrentTurn() {
        return gameBoard.getTurn();
    }

    public void setPlayerTurn(String turn) {
        gameBoard.setTurn(turn);
    }

    public int[][] getBigBoard() {
        return gameBoard.getBigBoard();
    }

    public int[][][][] getSmallBoard() {
        return gameBoard.getSmallBoard();
    }

    public String makePlay(int playOnS, int playOnB) {
        return gameBoard.gamePlay(playOnS, playOnB);
    }

    public boolean whoWon(int[][] board, int player){
        return gameBoard.WinCheck(board, player);
    }
}