package model.game;

import controller.CardController;
import model.Account.Player;
import model.Account.User;
import model.role.Card;
import model.role.Faction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Game {
    private final ArrayList<StateAfterADiamond> states;
    private final Player[] players;
    private short passedTurnCounter;
    private final ArrayList<Card> weathers;
    private int numTurn;
    private int indexCurPlayer;
    private Player winner;
    private static Game currentGame = null;

    public Game(User user1, User user2) {
        players = new Player[2];
        winner = null;
        if (!user1.getFaction().equals(Faction.SCOIA_TAEL) && user2.getFaction().equals(Faction.SCOIA_TAEL))
            createPlayers(user2, user1);
        else createPlayers(user1, user2);
        indexCurPlayer = 0;
        weathers = new ArrayList<>();
        numTurn = 0;
        passedTurnCounter = 0;
        states = new ArrayList<>();
    }

    public Player getWinner() {
        return winner;
    }

    public int getNumTurn() {
        return numTurn;
    }

    public ArrayList<StateAfterADiamond> getStates() {
        return states;
    }

    public void passRound() {
        if (++passedTurnCounter >= 2) {
            giveADiamondToWinner();
        }
        for (Card card : getCurrentPlayer().getInHand()) {
            card.setShouldBeChange();
        }
        indexCurPlayer = indexCurPlayer == 0 ? 1 : 0;
        if (getCurrentPlayer().equals(getPlayer1())) numTurn++;
        handleExtraTasks();
    }


    private void handleExtraTasks() {
        if (numTurn == 3) getCurrentPlayer().handleSkellige();
        getPlayer1().removeDeadCards();
        getPlayer2().removeDeadCards();
        getPlayer1().handleTransformers();
        getPlayer2().handleTransformers();
        getPlayer1().updatePointOfRows();
        getPlayer2().updatePointOfRows();
        getOtherPlayer().getCardInfo().clear();
    }

    private void giveADiamondToWinner() {
        passedTurnCounter = 0;
        if (getPlayer1().getTotalPoint() > getPlayer2().getTotalPoint()) {
            getPlayer1().addADiamond();
            addToStates(getPlayer1(), getPlayer2());
        } else if (getPlayer1().getTotalPoint() < getPlayer2().getTotalPoint()) {
            getPlayer2().addADiamond();
            addToStates(getPlayer2(), getPlayer1());
       } else {
            if (getPlayer1().getUser().getFaction().equals(Faction.NILFGAARDIAN_EMPIRE) && !getPlayer2().getUser().getFaction().equals(Faction.NILFGAARDIAN_EMPIRE)) {
                getPlayer1().addADiamond();
                addToStates(getPlayer1(), getPlayer2());
            } else if (!getPlayer1().getUser().getFaction().equals(Faction.NILFGAARDIAN_EMPIRE) && getPlayer2().getUser().getFaction().equals(Faction.NILFGAARDIAN_EMPIRE)) {
                getPlayer2().addADiamond();
                addToStates(getPlayer2(), getPlayer1());
            } else {
            }
        }
    }



    private void addToStates(Player winner, Player looser) {
        states.add(new StateAfterADiamond(winner, looser, winner.getTotalPoint(), looser.getTotalPoint(), numTurn, winner.getDiamond() + looser.getDiamond()));
    }


    public void changeTurn() {
        for (Card card : getCurrentPlayer().getInHand()) {
            card.setShouldBeChange();
        }
        indexCurPlayer = indexCurPlayer == 0 ? 1 : 0;
        passedTurnCounter = 0;
        if (getCurrentPlayer().equals(getPlayer1())) numTurn++;
        handleExtraTasks();
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
        new Game(u1, u2);
    }

    public void endOfTheGame(Player winner) {
        if (winner.getUser().getFaction().equals(Faction.MONSTERS))
            winner.getInHand().add(winner.getRandomCard(winner.getUser().getDeck()));
        this.winner = winner;
        updateUserHistory(getPlayer1());
        updateUserHistory(getPlayer2());
    }

    private void updateUserHistory(Player player) {
        User user = player.getUser();
        Player opponent = getPlayer1().equals(player) ? getPlayer2() : getPlayer1();

        if (user.equals(winner.getUser())) user.setWins(player.getUser().getWins() + 1);
        else user.setLosses((user.getLosses() + 1));

        user.setHighestScore(Math.max(user.getHighestScore(), player.getTotalPoint()));
        user.setGamesPlayed(user.getGamesPlayed() + 1);
        user.addToHistory(new GameHistory(opponent.getUser(), winner.getUser(), createFormattedDate(), states));
        player.getInHand().clear();
    }

    private String createFormattedDate() {
        LocalDateTime myDateObj = LocalDateTime.now();
        System.out.println("Date of the game before formatting: " + myDateObj);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }


    public ArrayList<Card> getWeathers() {
        return weathers;
    }
}