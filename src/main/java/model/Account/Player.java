package model.Account;

import controller.ApplicationController;
import controller.CardController;
import controller.PlayerController;
import model.Enum.GameRegexes;
import model.game.Row;
import model.role.Card;
import model.role.Leader;
import model.role.Type;
import model.role.Weather;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player implements Runnable {
    private User user;
    private final Row[] rows;
    private final Row[] opponentRows;
    private ArrayList<Card> inHand;
    private ArrayList<Card> discardCards;
    private final Leader leader;
    private final PlayerController controller;
    private ArrayList<Card> transformers;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean running;

    private InputHandler inHandler;
    private final ArrayList<Weather> weathers;

    private boolean isServerListening;

    public Player(User user) {
        this.user = user;
        this.leader = user.getLeader();
        running = true;
        rows = new Row[3];
        opponentRows = new Row[3];
        weathers = new ArrayList<>();
        inHand = new ArrayList<>();
        discardCards = new ArrayList<>();
        transformers = new ArrayList<>();
        createRows();
        controller = new PlayerController(this);
        isServerListening = false;

        try {
            socket = new Socket("127.0.0.1", 8080);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Erfan handle
        }

    }

    @Override
    public void run() {

        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            inHandler = new InputHandler();
            Thread thread = new Thread(inHandler);
            thread.start();

            String inMessage;
            while ((inMessage = in.readUTF()) != null) {
                System.out.println("[PLAYER " + getUser().getName() + "] server saying: " + inMessage);
                serverCommandHandler(inMessage);
            }

        } catch (UnknownHostException e) {
            // TODO handle
            e.printStackTrace();
        } catch (IOException e) {
            // TODO handle
            e.printStackTrace();
        }

    }

    /*
     * @Info get input from terminal or view
     * */
    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                while (running) {
                    Scanner scanner = new Scanner(System.in);

                    if (isServerListening) {
                        String message = scanner.nextLine();
                        System.out.println("[PLAYER " + getUser().getName() + "] recieve form user: " + message);
                        userCommandHandler(message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            try {
                out.writeUTF(message);
            } catch (IOException e) {
                // TODO handle
                e.printStackTrace();
            }
        }
    }

    /*
     * @Info this function process the message from user
     * */

    public void putACard(String cardName) {
        inHandler.sendMessage(user.getName() + " put card " + cardName);
        endTurn();
    }

    private static final String putCardRegex= "^put card (\\S+) (\\S+)$";

    private void userCommandHandler(String inMessage) {
//        else if (GameRegexes.PUT_CARD.matches(inMessage)) {
////            putCard(inMessage);
//        }
        if (GameRegexes.PASS_ROUND.matches(inMessage)) {
            passRound(inMessage);
        }

        else if (inMessage.matches(putCardRegex)) {
            inHandler.sendMessage(inMessage);
        }

        else if (inMessage.equals("end turn")) {
            isServerListening = false;
            System.out.println("[PLAYER] this turn has ended");
            inHandler.sendMessage("end turn");
        }

        else {
            //TODO: Handle Alert for invalid action
        }
        // TODO
    }

    private void passRound(String string) {
        inHandler.sendMessage(user.getName() + "|" + string);
        endTurn();
    }

    /*
     * @Info this function process the message from server
     * */
    private void serverCommandHandler(String message) throws IOException {
        if (message.equals("start communication")) {
            inHandler.sendMessage("communication accepted");
        }
//        else if (GameRegexes.A_USER_PUT_CARD.matches(message)) {
//            handlePuttingACard(GameRegexes.A_USER_PUT_CARD.getGroup(message, "username"), GameRegexes.A_USER_PUT_CARD.getGroup(message, "cardName"));
//        }
        else if (message.equals(GameRegexes.START_TURN.toString())) {
            startTurn();
        }
        else if (message.equals("ok")) {
        }
    }

    public void handlePuttingACard(String username, String cardName) {

        // handle adding the card to the appropriate list
        addCardToBoard(username, cardName);

        // handle powers of cards if needed
        updatePointOfARow();
    }

    void addCardToBoard(String username, String cardName) {
        boolean isMe = user.getUsername().equals(username);
        int rowNum = CardController.getRowNumber(cardName);
        Card card = CardController.createCardWithName(cardName);
        if (card.getAbility().equals("Spy")) {
            if (isMe) {
                opponentRows[rowNum].addCard(card);
            } else {
                rows[rowNum].addCard(card);
            }
        } else if (rowNum != -1) {
            if (isMe) {
                rows[rowNum].addCard(card);
            } else {
                opponentRows[rowNum].addCard(card);
            }
        }

        // TODO: difference between SPELL and WEATHER ????
        if (card.getType().equals(Type.WEATHER) || card.getType().equals(Type.SPELL) || card instanceof Weather) {
            weathers.add((Weather) card);
            doWeatherAbility(card);
        }

        // handle abilities
        actionTheAbility(card, username, cardName, card.getAbility());


    }

    private void freeze(Row... rows) {
        // TODO: effects and others
    }

    private void doWeatherAbility(Card card) {
        switch (card.getName()) {
            case "fog":
                freeze(rows[1], opponentRows[1]);
                break;
            case "rain":
                freeze(rows[2], opponentRows[2]);
                break;
            case "biting frost":
                freeze(rows[0], opponentRows[0]);
                break;
        }
    }

    void actionTheAbility(Card newCard, String username, String cardName, String ability) {
        boolean isMe = user.getUsername().equals(username);
        int rowNum = CardController.getRowNumber(cardName);

        switch (ability) {
            case "Muster":


                break;
            case "Transformer":
                transformers.add(newCard);
                break;

            case "Scorch":


                break;

            case "Moral Boost":
                Row[] array;
                if (isMe) {
                    array = rows;
                } else {
                    array = opponentRows;
                }
                for (Card card : array[rowNum].getCards()) {
                    card.setPower(card.getPower() - 1);
                }
                updatePointOfARow();
                // TODO: update the score icons on cards...


                break;
            case "Commander’s horn":
                Row[] array1;
                if (isMe) {
                    array1 = rows;
                } else {
                    array1 = opponentRows;
                }
                for (Card card : array1[rowNum].getCards()) {
                    card.setPower(card.getPower() * 2);
                }
                updatePointOfARow();
                // TODO: update the score icon on each card...
                break;


            case "Medic":
                if (isMe && discardCards.size() > 1) {
                    Card card = discardCards.get(ApplicationController.getRandom().nextInt(0, discardCards.size() - 1));
                    discardCards.remove(card);
                    rows[CardController.getRowNumber(card.getName())].addCard(card);
                }
                break;
            case "Spy":
                if (isMe) {
                    ArrayList<Card> deck = user.getDeck();
                    ApplicationController.getRandom().nextInt(0, deck.size() - 1);
                    Card card1 = deck.get(ApplicationController.getRandom().nextInt(0, deck.size() - 1));
                    deck.remove(card1);
                    inHand.add(card1);
                    Card card2 = deck.get(ApplicationController.getRandom().nextInt(0, deck.size() - 1));
                    deck.remove(card2);
                    inHand.add(card2);
                }
                break;

            case "Tight Bond":
                Row[] array2;
                if (isMe) {
                    array2 = rows;
                } else {
                    array2 = opponentRows;
                }
                int count = 0;
                for (Card card : array2[rowNum].getCards()) {
                    if (card.getAbility().equals("Tight Bond")) count++;
                }
                for (Card card : array2[rowNum].getCards()) {
                    card.setPower(card.getPower() * count);
                }
                updatePointOfARow();
                // TODO: update the score icon on each card...
                break;


            // TODO: Why we have this ???
            case "NORTHERN_REALMS":
                break;

            // TODO: And this ???
            case "NILFGAARDIAN_EMPIRE":
                break;

        }
        updatePointOfARow();
    }


    void updatePointOfARow() {
        for (Row row : rows) {
            int point = 0;
            for (Card card : row.getCards()) {
                point += card.getPower();
            }
            row.setPoint(point);
        }

        for (Row row : opponentRows) {
            int point = 0;
            for (Card card : row.getCards()) {
                point += card.getPower();
            }
            row.setPoint(point);
        }
    }

    private void startTurn() {
        isServerListening = true;
        // TODO
    }

    private void endTurn() {
        isServerListening = false;
        // TODO
    }

    public void shutdown() {
        running = false;
        try {
            in.close();
            out.close();

            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

            // TODO handle
        }
    }

    private void createRows() {
        rows[0] = new Row(Row.RowName.FIRST);
        rows[1] = new Row(Row.RowName.SEC);
        rows[2] = new Row(Row.RowName.THIRD);
        opponentRows[0] = new Row(Row.RowName.FIRST);
        opponentRows[1] = new Row(Row.RowName.SEC);
        opponentRows[2] = new Row(Row.RowName.THIRD);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Player)) {
            return false;
        }

        return this.user.equals(((Player) obj).user);
    }

    public ArrayList<Card> getInHand() {
        return inHand;
    }

    public Leader getLeader() {
        return leader;
    }

    public PlayerController getController() {
        return controller;
    }


    private static Matcher getMatcher(String regex, String command) {
        return Pattern.compile(regex).matcher(command);
    }
}
