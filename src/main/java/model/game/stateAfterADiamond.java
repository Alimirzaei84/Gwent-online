package model.game;

import model.Account.Player;

class stateAfterADiamond {
   private final Player winner;
   private final Player looser;
   private final int winnerScore;
   private final int looserScoer;
   private final int round;
   private final int  diamondCount;

    public stateAfterADiamond(Player winner, Player looser, int winnerScore, int looserScoer, int round, int diamondCount) {
        this.winner = winner;
        this.looser = looser;
        this.winnerScore = winnerScore;
        this.looserScoer = looserScoer;
        this.round = round;
        this.diamondCount = diamondCount;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getLooser() {
        return looser;
    }

    public int getWinnerScore() {
        return winnerScore;
    }

    public int getLooserScoer() {
        return looserScoer;
    }

    public int getRound() {
        return round;
    }

    public int getDiamondCount() {
        return diamondCount;
    }
}
