package model.game;

import controller.PlayerController;
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

public class Game implements Runnable {

    private final Player[] players;
    private PlayerController[] playerControllers;
    private CommunicationHandler[] communicationHandlers;
    private short passedTurnCounter;

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
        log = new StringBuilder();
        createPlayers(user1, user2);
        passedTurnCounter = 0;
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
            CommunicationHandler tunnel1 = new CommunicationHandler(p1, getPlayer1());
            communicationHandlers[0] = tunnel1;
            pool.execute(tunnel1);

            // player 2
            Socket p2 = server.accept();
            Thread t2 = new Thread(getPlayer2());
            t2.start();
            CommunicationHandler tunnel2 = new CommunicationHandler(p2, getPlayer2());
            communicationHandlers[1] = tunnel2;
            pool.execute(tunnel2);

            isPlayerListening = true;

            // choose card
//            chooseCard();

            while (gameStillOn()) {
                getReadyToCommuincateWithPlayer();
                tunnel1.startCommunication();

                getReadyToCommuincateWithPlayer();
                tunnel1.startTurn();

                getReadyToCommuincateWithPlayer();
                tunnel2.startCommunication();

                getReadyToCommuincateWithPlayer();
                tunnel2.startTurn();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        private Player player;

        public CommunicationHandler(Socket socket, Player player) {
            this.socket = socket;
            this.player = player;
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
                    System.out.println("[SERVER] message from player " + player.getUser().getName() + " :  \"" + inMessage + "\"");
                    handleCommand(inMessage, getPlayer(), getOpp().getPlayer());
//
//                    if (response != null) {
//                        System.out.println("[SERVER] response to player: \"" + response + "\"");
//                        sendMessage(response);
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public Player getPlayer() {
            return player;
        }

        private CommunicationHandler getOpp() {
            for (CommunicationHandler c : communicationHandlers) {
                if (c.equals(this)) continue;
                else return c;
            }

            throw new IllegalStateException();
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

        public void startTurn() throws IOException {
            sendMessage(GameRegexes.START_TURN.toString());
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

    private void getReadyToCommuincateWithPlayer() {
        waitUntilPlayerIsAvailable();
        isPlayerListening = false;
    }

    // commands
//    private final static String openCommunicationCommand = "open communication",
//        startTurnCommand = "start turn ", // num turn will place at the end of this command
//        chooseCardCommand = "choose card";

    // regex
    private final static String playerCommunicationAcceptedRegex = "^communication accepted$",
            endTurnRegex = "^end turn$";


    private static final String putCardRegex = "^put card (\\S+) (\\S+)$";


    private void handleCommand(String command, Player caller, Player opp) throws IOException {

//        if(GameRegexes.A_USER_PUT_CARD.matches(command)){
//            getTunel1().sendMessage(command);
//            getTunel2().sendMessage(command);
//        }

//        else
        if (command.matches(putCardRegex)) {
            Matcher matcher = getMatcher(putCardRegex, command);
            matcher.find();
            putCard(matcher, caller, opp);
        } else if (command.matches(playerCommunicationAcceptedRegex)) {
            System.out.println("[SUCC] communication has established");
            isPlayerListening = true;
        } else if (GameRegexes.PLAY_LEADER.matches(command)) {
            broadcastLeader(command);
        } else if (command.matches(endTurnRegex)) {
            System.out.println("[SERVER] the turn has ended");
            isPlayerListening = true;
            passedTurnCounter = 0;
        } else if (command.matches(".+ passed")) {
            System.out.println("[SERVER] the turn has passed");
            if (++passedTurnCounter >= 2) {
                checkForWinnerOfADiamond();
            }
            isPlayerListening = true;
        } else if (GameRegexes.A_USER_PUT_CARD.matches(command)) {
            puttingCardBroadCast(command);

        } else if (command.matches("\\d")) {
            testMethod(command, caller, opp);
        } else if (command.matches(".+ has ready hand")) {
            getTunel1().sendMessage(command);
            getTunel2().sendMessage(command);
        } else if (GameRegexes.VETO_COMPLETED.matches(command)) {
            getTunel1().sendMessage(command);
            getTunel2().sendMessage(command);
        }
        else if(GameRegexes.JSON_OF_ROWS.matches(command)){
            getTunel1().sendMessage(command);
            getTunel2().sendMessage(command);
        }

    }

    private void broadcastLeader(String command) throws IOException {
        getTunel1().sendMessage(command);
        getTunel2().sendMessage(command);
    }

    private void puttingCardBroadCast(String command) throws IOException {
        getTunel1().sendMessage(command);
        getTunel2().sendMessage(command);
    }

    private void checkForWinnerOfADiamond() {
        // TODO: compare players points
    }

    private void testMethod(String command, Player caller, Player opp) throws IOException {
        getTunel1().sendMessage(command);
        getTunel2().sendMessage(command);
    }

    private boolean putCard(Matcher matcher, Player caller, Player opp) {
        String cardName = matcher.group(1);
        String pos = matcher.group(2);

//        Card card;
//        try {
//            card = CardController.createCardWithName(cardName);
//        } catch (RuntimeException e) {
//            return false;
//        }


//        System.out.println(card);


        // accepted
//        String s = affectOnOpponent();

        LOG("[" + caller.getUser().getName() + "]: put card " + cardName + " " + pos);
//        LOG(s);

        broadcastLog();
        cleanLog();

        return true;
    }

    private void LOG(CharSequence sequence) {
        log.append(sequence).append('\n');
    }

    private void cleanLog() {
        log = new StringBuilder();
    }

    private void broadcastLog() {
        CommunicationHandler t1 = getTunel1(),
                t2 = getTunel2();

        String[] arr = log.toString().split("\n");

        try {
            for (String l : arr) {
                t1.sendMessage(l);
                t2.sendMessage(l);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private void chooseCard() {
        CommunicationHandler t1 = getTunel1(),
                t2 = getTunel2();

        try {
            getReadyToCommuincateWithPlayer();
            t1.startCommunication();

            getReadyToCommuincateWithPlayer();
            t1.sendMessage(GameRegexes.CHOOSE_CARD.toString());

            getReadyToCommuincateWithPlayer();
            t2.startCommunication();

            getReadyToCommuincateWithPlayer();
            t2.sendMessage(GameRegexes.CHOOSE_CARD.toString());


        } catch (Exception e) {

        }

    }

    private boolean gameStillOn() throws Exception {
        // TODO define the rule for finishing the game
        return true;
    }

    private void goNextTurn() throws Exception {
        numTurn++;

//        addToLog("start turn " + numTurn);

        // TODO
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

    public PlayerController getController1() {
        return playerControllers[0];
    }

    public PlayerController getController2() {
        return playerControllers[1];
    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static void setCurrentGame(Game game) {
        currentGame = game;
    }

    private CommunicationHandler getTunel1() {
        return communicationHandlers[0];
    }


    private CommunicationHandler getTunel2() {
        return communicationHandlers[1];
    }

    private static Matcher getMatcher(String regex, String command) {
        return Pattern.compile(regex).matcher(command);
    }

    public static void main(String[] args) {
        User u1 = new User("a", "a", "a", "a");
        User u2 = new User("b", "b", "b", "b");
        Game game = new Game(u1, u2);

        game.run();
    }
}
