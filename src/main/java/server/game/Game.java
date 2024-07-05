package server.game;

import server.Account.Player;
import model.role.Card;
import model.role.Faction;
import server.Chatroom;
import server.Account.User;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Game implements Runnable, Serializable {

    private static int gameCounter = 0;

    public enum AccessType {
        PRIVATE(0), PUBLIC(1);

        private final int index;

        AccessType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public Board getCurrentPlayerBoard() {
        return generateBoard(getCurrentPlayer(), getOtherPlayer());
    }

    public Board getOtherPlayerBoard() {
        return generateBoard(getOtherPlayer(), getCurrentPlayer());
    }

    private Board generateBoard(Player curr, Player other) {

        Board board = new Board(this);
        board.setWeatherArrayList(this.getWeathers());

        board.setMyDeck(curr.getUser().getDeck());
        board.setMyHand(curr.getInHand());
        board.setMyRows(curr.getRows());
        board.setMyPoint(curr.getTotalPoint());

        board.setOppDeck(other.getUser().getDeck());
        board.setOppHand(other.getInHand());
        board.setOppRows(other.getRows());
        board.setOppPoint(other.getTotalPoint());

        return board;
    }


    private final User[] users;
    private final AccessType accessType;
    private final Chatroom chatroom;
    private final int id;

    private boolean running;
    private boolean isPlayerListening;

    private GameCommunicationHandler handler;


    private final ArrayList<StateAfterADiamond> states;
    private final Player[] players;
    private short passedTurnCounter;
    private final ArrayList<Card> weathers;
    private int numTurn;
    private int indexCurPlayer;
    private Player winner;
//    private static model.game.Game currentGame = null;


    public Game(User user1, User user2, AccessType accessType) {
        users = new User[]{user1, user2};
        this.accessType = accessType;
        chatroom = new Chatroom();
        id = gameCounter++;

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


    public void createPlayers(User user1, User user2) {
        players[0] = new Player(user1, this);
        players[1] = new Player(user2, this);
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


    public Player getPlayer1() {
        return players[0];
    }

    public Player getPlayer2() {
        return players[1];
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


    @Override
    public void run() {

        // start communication handler
        handler = new GameCommunicationHandler(this);
        Thread handlerThread = new Thread(handler);
        handlerThread.start();


        running = true;
        isPlayerListening = true;

        try {
            broadcast("game id:" + this.getId());

            while (running) {
                waitUntilPlayersAreListening();
                getUser1().sendMessage("start turn");
                isPlayerListening = false;

                waitUntilPlayersAreListening();
                getUser2().sendMessage("start turn");
                isPlayerListening = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public String handleCommand(String command) throws IOException {
        return handler.handleCommand(command);
    }


    private void waitUntilPlayersAreListening() {

        while (!isPlayerListening) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }

    }

    protected boolean isGameListening() {
        return !isPlayerListening;
    }

    public User[] getUsers() {
        return users;
    }

    public User getUser1() {
        return users[0];
    }

    public User getUser2() {
        return users[1];
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public int getId() {
        return id;
    }

    public void attendUserAsViewer(User user) throws IOException {
        // TODO maybe you should not attend user as viewer when the game is not running
        user.sendMessage("you will watch game id:" + getId() + " online.");
        chatroom.addAttendee(user);
    }

    protected void setPlayerListening() {
        isPlayerListening = true;
    }

    public void broadcast(String message) throws IOException {
        getUser1().sendMessage(message);
        getUser2().sendMessage(message);
        getChatroom().broadcast(message);
    }

}
