package model.game;

import model.Account.Player;

<<<<<<< HEAD:src/main/java/model/game/StateAfterADiamond.java
class StateAfterADiamond {
=======
public class stateAfterADiamond {
>>>>>>> 0bfe1a7666ea14b035b64e7f7e1b9e36eea70fc2:src/main/java/model/game/stateAfterADiamond.java
   private final Player winner;
   private final Player looser;
   private final int winnerScore;
   private final int looserScore;
   private final int round;
   private final int  diamondCount;

    public StateAfterADiamond(Player winner, Player looser, int winnerScore, int looserScore, int round, int diamondCount) {
        this.winner = winner;
        this.looser = looser;
        this.winnerScore = winnerScore;
        this.looserScore = looserScore;
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

    public int getLooserScore() {
        return looserScore;
    }

    public int getRound() {
        return round;
    }

    public int getDiamondCount() {
        return diamondCount;
    }
}
