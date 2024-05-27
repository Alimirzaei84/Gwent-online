package model.game;

import model.Account.Player;
import model.Account.User;

public class Game {

    private Player[] players;
    private int turn;


    public Game(User user1, User user2) {
        players = new Player[2];
    }

    public Game(Player player1, Player player2) {
        players = new Player[]{player1, player2};
    }

    private void createPlayers(User user1, User user2) {
        players[0] = new Player(user1);
        players[1] = new Player(user2);
    }
}
