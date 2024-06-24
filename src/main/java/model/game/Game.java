package model.game;

import controller.PlayerController;
import model.Account.Player;
import model.Account.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game implements Runnable {

    private final Player[] players;
    private PlayerController[] playerControllers;
    private CommunicationHandler[] communicationHandlers;

    private ServerSocket server;
    private ExecutorService pool;

    private int turn;
    private boolean running = false;
    private StringBuilder log;
    private int numTurn = 0;

    private boolean isPlayerListening;

    private static Game currentGame = null;

    public Game(User user1, User user2) {
        try {
            server = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO handle
        }
        communicationHandlers = new CommunicationHandler[2];
        players = new Player[2];
        createPlayers(user1, user2);
    }

    public Game(Player player1, Player player2) {
        communicationHandlers = new CommunicationHandler[2];
        players = new Player[]{player1, player2};
    }

    @Override
    public void run() {

        try {
            pool = Executors.newCachedThreadPool();

            // writing trash
            Socket p1 = server.accept();

            // player 1
            Thread t = new Thread(getPlayer1());
            t.start();
            CommunicationHandler handler1 = new CommunicationHandler(p1);
            communicationHandlers[0] = handler1;
            pool.execute(handler1);

            // player 2
            Socket p2 = server.accept();
            Thread t2 = new Thread(getPlayer2());
            t2.start();
            CommunicationHandler handler2 = new CommunicationHandler(p2);
            communicationHandlers[1] = handler2;
            pool.execute(handler2);


            isPlayerListening = false;
            handler1.startCommunication();

            waitUntilPlayerIsAvailable();
            isPlayerListening = false;
            handler1.sendMessage("start turn");

            waitUntilPlayerIsAvailable();
            isPlayerListening = false;
            handler2.startCommunication();

            waitUntilPlayerIsAvailable();
            isPlayerListening = false;
            handler2.sendMessage("start turn");



        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
        // creating controllers
//        createControllers();
        // players choose their cards
//        try {
//            playersChooseCard();

//            goNextTurn();
            // while the game is still on
            // start the turn
            // ask the first player for action
            // get response from player
            // ask the second player for action
            // get response from sec player
            // store the data

//            while (gameStillOn()) {
//
//                // start the turn from first player
//                openCommunication(getController1());
//                startTurn(getController1());
//                break;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private void waitUntilPlayerIsAvailable() {
        while (!isPlayerListening) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }


    class CommunicationHandler implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;

        public CommunicationHandler(Socket socket) {
            this.socket = socket;

            try {
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                // TODO handle
            }
        }

        @Override
        public void run() {

            try {
                String inMessage;
                while ((inMessage = in.readUTF()) != null) {
                    // for debug purpose
                    System.out.println("[SERVER] message from player: \"" + inMessage + "\"");
                    String response = handleCommand(inMessage);

                    if (response != null) {
                        System.out.println("[SERVER] response to player: \"" + response + "\"");
                        sendMessage(response);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void startCommunication() throws IOException {
            sendMessage("start communication");
        }

        public void sendMessage(String message) throws IOException {
            out.writeUTF(message);
        }

        public void shutdown() {

            try {
                in.close();
                out.close();
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                // TODO handle exception (perhaps ignore)
            }
        }
    }

    private void shutdown() {
        try {
            running = false;

            // TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // commands
    private final static String openCommunicationCommand = "open communication",
        startTurnCommand = "start turn"; // num turn will place at the end of this command

    // regex
    private final static String playerCommunicationAcceptedRegex = "^communication accepted$",
        endTurnRegex = "^end turn$";


    private String handleCommand(String command) {

        if (command.matches(playerCommunicationAcceptedRegex)) {
            System.out.println("[SUCC] communication has established");
            isPlayerListening = true;
            return null;
        }

        else if (command.matches(endTurnRegex)){
            System.out.println("[SERVER] the turn has ended");
            isPlayerListening = true;
            return null;
        }

        return "invalid command";
    }
    // communicator functions
//    private void playersChooseCard() throws Exception {
//        String command = "choose card";
//
//        // TODO log
//
//        // first player:
//        try {
//            sendCommand(command, getController1());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Player secondPlayer = getPlayer2();
//    }
//
//    private boolean gameStillOn() throws Exception {
//        // TODO define the rule for finishing the game
//        return true;
//    }
//
//    private void openCommunication(PlayerController controller) throws Exception {
//        sendCommand(openCommunicationCommand, controller);
//        String response = getResponse(controller);
//
//        if (!getMatcher(playerCommunicationAcceptedRegex, response).find()) {
//            throw new Exception("can not establish communication");
//        }
//
//        System.out.println("start communication with player");
//    }
//
//    private void startTurn(PlayerController controller) throws Exception {
//        sendCommand(startTurnCommand + numTurn, controller);
//
//        String playerCommand;
//        do {
//            playerCommand = getResponse(controller);
//
//            // TODO handle player demands
//        } while (!playerCommand.matches(endTurnRegex));
//    }
//
//    public String getResponse(PlayerController controller) throws Exception {
//        return controller.getResponseToGame();
//    }
//
//    private void sendCommand(String command, PlayerController controller) throws Exception {
//        controller.getCommand(command);
//    }
//
//    private void addToLog(CharSequence sequence) {
//        log.append(sequence).append("\n");
//    }
//
//    private void goNextTurn() throws Exception {
//        numTurn++;
//
//        addToLog("start turn " + numTurn);
//
//        // TODO
//    }
//
//    private static Matcher getMatcher(String regex, String command) {
//        return Pattern.compile(regex).matcher(command);
//    }


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


    public static void main(String[] args) {
        User u1 = new User("a", "a", "a", "a");
        User u2 = new User("b", "b", "b", "b");
        Game game = new Game(u1, u2);

        game.run();
    }
}
