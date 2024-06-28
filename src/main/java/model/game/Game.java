package model.game;

import controller.CardController;
import controller.PlayerController;
import javafx.stage.Stage;
import model.Account.Player;
import model.Account.User;
import model.Enum.GameRegexes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

    private final Player[] players;
    private short passedTurnCounter;

    private int numTurn = 0;

    private static Game currentGame = null;

    public Game(User user1, User user2){
        players = new Player[2];
        createPlayers(user1,user2);
    }

    class CommunicationHandler {

        public void startTurn() throws IOException {
            //TODO : CHOOSE CARD
            //TODO : CHOOSE CARD
        }
    }



    public void createPlayers(User user1, User user2) {
        players[0] = new Player(user1);
        players[1] = new Player(user2);
    }

    public Player getPlayer1() {
        return players[0];
    }

    public Player getPlayer2() {
        return players[1];
    }


    public static Game getCurrentGame() {
        return currentGame;
    }

    public static void setCurrentGame(Game game) {
        currentGame = game;
    }

    private static Matcher getMatcher(String regex, String command) {
        return Pattern.compile(regex).matcher(command);
    }

    public static void main(String[] args) {
        try {
            CardController.load_data();
        } catch (Exception ignored) {
        }
        User u1 = new User("ali", "a", "a", "a");
        User u2 = new User("erfan", "b", "b", "b");
        u1.setDeck(u1.getRandomDeck());
        u2.setDeck(u1.getRandomDeck());
        Game game = new Game(u1, u2);
    }
}