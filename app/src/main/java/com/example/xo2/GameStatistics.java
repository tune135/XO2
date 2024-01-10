package com.example.xo2;

public class GameStatistics {
    private int wins;
    private int losses;
    private int draws;

    public GameStatistics() {
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getWins() {
        return wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getLosses() {
        return losses;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getDraws() {
        return draws;
    }
}
