package model.game;

import controller.CardController;
import model.Account.Player;
import model.Account.User;
import model.role.Card;

import java.io.IOException;
import java.util.ArrayList;

public class Game {

    private final Player[] players;
    private short passedTurnCounter;
    private ArrayList<Card> weathers;
    private int numTurn;
    private int indexCurPlayer; //TODO : CHANGE AFTER EACH TURN
    private static Game currentGame = null;

    public Game(User user1, User user2) {
        players = new Player[2];
        createPlayers(user1, user2);
        indexCurPlayer = 0;
        weathers = new ArrayList<>();
        numTurn = 0;
        passedTurnCounter = 0;
    }

    public void passRound() {
        if (++passedTurnCounter >= 2) {
            giveADiamondToWinner();
        }
        indexCurPlayer = indexCurPlayer == 0 ? 1 : 0;
        if (getCurrentPlayer().equals(getPlayer1())) numTurn++;
        handleExtraTasks();
    }

    private void handleExtraTasks() {
        getPlayer1().removeDeadCards();
        getPlayer2().removeDeadCards();
        getPlayer1().handleTransformers();
        getPlayer2().handleTransformers();
        getPlayer1().updatePointOfRows();
        getPlayer2().updatePointOfRows();
    }

    private void giveADiamondToWinner() {
        // TODO
    }

    public void changeTurn() {
        indexCurPlayer = indexCurPlayer == 0 ? 1 : 0;
        passedTurnCounter = 0;
        if (getCurrentPlayer().equals(getPlayer1())) numTurn++;
        handleExtraTasks();
    }

        public void startTurn() throws IOException {
            //TODO : CHOOSE CARD
            //TODO : CHOOSE CARD
        }

    public Player getCurrentPlayer() {
        return players[indexCurPlayer];
    }

    public Player getOtherPlayer() {
        if (indexCurPlayer == 0) return players[1];
        return players[0];
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