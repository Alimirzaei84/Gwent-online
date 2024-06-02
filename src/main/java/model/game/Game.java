package model.game;

import model.Account.Player;
import model.Account.User;

public class Game {

    private final Player[] players;
    private int turn;
    private static Game currentGame = null;

    public Game(User user1, User user2) {
        players = new Player[2];
        createPlayers(user1, user2);
    }

    public Game(Player player1, Player player2) {
        players = new Player[]{player1, player2};
    }

    public void createPlayers(User user1, User user2) {
        players[0] = new Player(user1);
        players[1] = new Player(user2);
    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static void setCurrentGame(Game game) {
        currentGame = game;
    }
}
