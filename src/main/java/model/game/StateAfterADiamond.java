package model.game;

import model.Account.Player;

public record StateAfterADiamond(Player winner, Player looser, int winnerScore, int looserScore, int round,
                                 int diamondCount) {
}