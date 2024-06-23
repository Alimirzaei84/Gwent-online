package model.game;

import controller.PlayerController;
import model.Account.Player;
import model.Account.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game implements Runnable {

    private final Player[] players;
    private PlayerController[] playerControllers;
    private int turn;
    private boolean hasFinished = false;
    private StringBuilder log;
    private int numTurn = 0;

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

    public Player getPlayer1(){
        return players[0];
    }

    public Player getPlayer2(){
        return players[1];
    }

    public PlayerController getController1(){
        return playerControllers[0];
    }

    public PlayerController getController2(){
        return playerControllers[1];
    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static void setCurrentGame(Game game) {
        currentGame = game;
    }

    @Override
    public void run() {
        // creating controllers
        createControllers();
        // players choose their cards
        try {
            playersChooseCard();

            goNextTurn();
            // while the game is still on
            // start the turn
            // ask the first player for action
            // get response from player
            // ask the second player for action
            // get response from sec player
            // store the data

            while (gameStillOn()) {

                // start the turn from first player
                openCommunication(getController1());
                startTurn(getController1());
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createControllers() {
        playerControllers = new PlayerController[players.length];
        for (int i = 0; i < players.length; i++) {
            playerControllers[i] = new PlayerController(this, players[i]);
        }
    }


    // commands
    private final static String openCommunicationCommand = "open communication",
        startTurnCommand = "start turn "; // num turn will place at the end of this command

    // regex
    private final static String playerCommunicationAcceptedRegex = "^communication accepted$",
        endTurnRegex = "^end turn$";


    // communicator functions
    private void playersChooseCard() throws Exception {
        String command = "choose card";

        // TODO log

        // first player:
        try {
            sendCommand(command, getController1());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Player secondPlayer = getPlayer2();
    }

    private boolean gameStillOn() throws Exception {
        // TODO define the rule for finishing the game
        return true;
    }

    private void openCommunication(PlayerController controller) throws Exception {
        sendCommand(openCommunicationCommand, controller);
        String response = getResponse(controller);

        if (!getMatcher(playerCommunicationAcceptedRegex, response).find()) {
            throw new Exception("can not establish communication");
        }

        System.out.println("start communication with player");
    }

    private void startTurn(PlayerController controller) throws Exception {
        sendCommand(startTurnCommand + numTurn, controller);

        String playerCommand;
        do {
            playerCommand = getResponse(controller);

            // TODO handle player demands
        } while (!playerCommand.matches(endTurnRegex));
    }

    public String getResponse(PlayerController controller) throws Exception {
        return controller.getResponseToGame();
    }

    private void sendCommand(String command, PlayerController controller) throws Exception {
        controller.getCommand(command);
    }





    private void addToLog(CharSequence sequence) {
        log.append(sequence).append("\n");
    }

    private void goNextTurn() throws Exception {
        numTurn++;

        addToLog("start turn " + numTurn);

        // TODO
    }

    private static Matcher getMatcher(String regex, String command) {
        return Pattern.compile(regex).matcher(command);
    }
}
