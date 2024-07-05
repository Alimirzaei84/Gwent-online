package server.game;

import server.Account.Player;

public record StateAfterADiamond(Player winner, Player looser, int winnerScore, int looserScore, int round,
                                 int diamondCount) {
    public Player getLooser() {
        return looser;
    }

    public int getLooserScore() {
        return looserScore;
    }

    @Override
    public Player winner() {
        return winner;
    }

    @Override
    public int winnerScore() {
        return winnerScore;
    }

    @Override
    public int looserScore() {
        return looserScore;
    }
}