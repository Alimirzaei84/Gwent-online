package model.game;

import model.Account.Player;

public record StateAfterADiamond(Player winner, Player looser, int winnerScore, int looserScore, int round,
                                 int diamondCount) {
    public Player getLooser() {
        return looser;
    }

    public int getLooserScore() {
        return looserScore;
    }
}