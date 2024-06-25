package model.Account;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player implements Runnable {
    private short diamond;
    private int totalPoint;
    private int round;
    private User user;
    private short vetoCounter;
    private int opponentTotalPoints;
    private int X;
    private final Row[] rows;
    private final Row[] opponentRows;
    private ArrayList<Card> inHand;
    private ArrayList<Card> discardCards;
    private final Leader leader;
    private final PlayerController controller;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean running;

    private InputHandler inHandler;
    private final ArrayList<Card> weathers;

    private boolean isServerListening;

    public Player(User user) {
        totalPoint = 0;
        opponentTotalPoints = 0;
        diamond = 0;
        vetoCounter = 0;
        round = 0;
        X = 0;
        this.user = user;
        this.leader = user.getLeader();
        running = true;
        rows = new Row[3];
        opponentRows = new Row[3];
        weathers = new ArrayList<>();
        inHand = new ArrayList<>();
        discardCards = new ArrayList<>();
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

    private static final String putCardRegex = "^put card (\\S+) (\\S+)$";

    private void userCommandHandler(String inMessage) {
//        else if (GameRegexes.PUT_CARD.matches(inMessage)) {
////            putCard(inMessage);
//        }
        if (inMessage.equals("pass round")) {
            passRound();
        } else if (inMessage.equals("choose 10 random cards") && round == 0 && inHand.isEmpty()) {
            makeHandReady();
        } else if (GameRegexes.PLACE_CARD.matches(inMessage)) {
            placeCardRequest(inMessage);
        } else if (inMessage.matches(putCardRegex)) {
            inHandler.sendMessage(inMessage);
        } else if (GameRegexes.VETO_A_CARD.matches(inMessage) && vetoCounter < 2 && round == 0) {
            veto(inHand.get(Integer.parseInt(GameRegexes.VETO_A_CARD.getGroup(inMessage, "cardIndex"))));
        } else if (inMessage.equals("end turn") && !(round == 0 && inHand.isEmpty())) {
            round++;
            isServerListening = false;
            System.out.println("[PLAYER] this turn has ended");
            inHandler.sendMessage("end turn");
        } else if (inMessage.equals("test")) {
            inHandler.sendMessage(Integer.toString(++X));
        } else {
            //TODO: Handle Alert for invalid action
        }
        // TODO
    }

    private void placeCardRequest(String message) {
        inHandler.sendMessage(user.getName() + " " + message);
    }

    private void veto(Card card) {
        inHand.remove(card);
        inHand.add(getRandomCard(user.getDeck()));
        inHandler.sendMessage(user.getName() + "'s has been completed");
    }

    private void makeHandReady() {
        for (int i = 0; i < 10; i++) {
            inHand.add(getRandomCard(user.getDeck()));
        }
        inHandler.sendMessage(user.getName() + " has ready hand");
    }

    private void passRound() {
        round++;
        isServerListening = false;
        System.out.println("[PLAYER] this turn has ended");
        inHandler.sendMessage(user.getName() + " passed");
    }

    /*
     * @Info this function process the message from server
     * */
    private void serverCommandHandler(String message) throws IOException {
        if (message.equals("start communication")) {
            inHandler.sendMessage("communication accepted");
        } else if (GameRegexes.A_USER_PUT_CARD.matches(message)) {
            String username = GameRegexes.A_USER_PUT_CARD.getGroup(message, "username");
            String cardName = GameRegexes.A_USER_PUT_CARD.getGroup(message, "cardName");
            String rowNumber = GameRegexes.A_USER_PUT_CARD.getGroup(message, "rowNumber");
            putCard(cardName, Integer.parseInt(rowNumber), username.equals(user.getName()));
            sendMyRowsToOpp();
        } else if (GameRegexes.JSON_OF_ROWS.matches(message)) {
            updateRows(message);
        }
//        else if (GameRegexes.A_USER_PUT_CARD.matches(message)) {
//            handlePuttingACard(GameRegexes.A_USER_PUT_CARD.getGroup(message, "username"), GameRegexes.A_USER_PUT_CARD.getGroup(message, "cardName"));
//        }
        else if (message.equals(GameRegexes.START_TURN.toString())) {
            startTurn();
            handleTransformers();
        } else if (message.equals("ok")) {
        }
    }

    private void updateRows(String message) {
        if (user.getName().equals(GameRegexes.JSON_OF_ROWS.getGroup(message, "username"))) return;
        Gson gson = new Gson();
        ArrayList<String> row0 = gson.fromJson(GameRegexes.JSON_OF_ROWS.getGroup(message, "json0"), new TypeToken<ArrayList<String>>() {
        }.getType());
        ArrayList<String> row1 = gson.fromJson(GameRegexes.JSON_OF_ROWS.getGroup(message, "json1"), new TypeToken<ArrayList<String>>() {
        }.getType());
        ArrayList<String> row2 = gson.fromJson(GameRegexes.JSON_OF_ROWS.getGroup(message, "json2"), new TypeToken<ArrayList<String>>() {
        }.getType());

        opponentRows[0].setCards(generateCardsOfTheirNames(row0));
        opponentRows[1].setCards(generateCardsOfTheirNames(row1));
        opponentRows[2].setCards(generateCardsOfTheirNames(row2));

        updatePointOfRows();
    }

    private ArrayList<Card> generateCardsOfTheirNames(ArrayList<String> arrayList) {
        ArrayList<Card> out = new ArrayList<>();
        for (String string : arrayList) {
            out.add(CardController.createCardWithName(string));
        }
        return out;
    }

    private void sendMyRowsToOpp() {
        Gson gson = new Gson();
        String json0 = gson.toJson(converCardsToTheirNames(rows[0].getCards()));
        String json1 = gson.toJson(converCardsToTheirNames(rows[1].getCards()));
        String json2 = gson.toJson(converCardsToTheirNames(rows[2].getCards()));
        inHandler.sendMessage(user.getName() + "Row0" + json0 + "Row1" + json1 + "Row2" + json2);
    }


    private ArrayList<String> converCardsToTheirNames(ArrayList<Card> cards) {
        ArrayList<String> result = new ArrayList<>();
        for (Card card : cards) {
            result.add(card.getName());
        }
        return result;
    }


    private void handleTransformers() {
        String cardName;

        for (Row row : rows) {
            for (Card rowCard : row.getCards()) {
                if (rowCard.getAbility().equals("Transformer") || rowCard.getAbility().equals("Berserker")) {
                    cardName = switch (rowCard.getName()) {
                        case "young berserker" -> "young vidkaarl";
                        case "berserker" -> "vidkaarl";
                        case "cow" -> "triss";
                        case "kambi" -> "ermion";
                        default -> "vesemir";
                    };
                    Card card = CardController.createCardWithName(cardName);
                    row.getCards().set(row.getCards().indexOf(rowCard), card);
                }
            }
        }


        for (Row row : opponentRows) {
            for (Card rowCard : row.getCards()) {
                if (rowCard.getAbility().equals("Transformer") || rowCard.getAbility().equals("Berserker")) {
                    cardName = switch (rowCard.getName()) {
                        case "young berserker" -> "young vidkaarl";
                        case "berserker" -> "vidkaarl";
                        case "cow" -> "triss";
                        case "kambi" -> "ermion";
                        default -> "vesemir";
                    };
                    Card card = CardController.createCardWithName(cardName);
                    row.getCards().set(row.getCards().indexOf(rowCard), card);
                }
            }
        }
        updatePointOfRows();
    }

    private void putCard(String cardName, int rowNumber, boolean isMe) {
        Card card = CardController.createCardWithName(cardName);
        if (card.getType().equals(Type.WEATHER)) {
            weathers.add(card);
            String weatherType = cardName.split(" ")[1];
            int whichRow = 0;
            switch (weatherType) {
                case "rain" -> whichRow = 2;
                case "fog" -> whichRow = 1;
            }
            freeze(rows[whichRow], opponentRows[whichRow]);
            return;
        }
        if (isMe) putCardForMe(card, rowNumber);
        else putCardForOpponent(card, rowNumber);
    }

    private void putCardForOpponent(Card card, int rowNumber) {
        opponentRows[rowNumber].addCard(card);


        switch (card.getAbility()) {
            case "Muster":

                break;
            case "Transformer", "Berserker":
                //It is ok...
                break;

            case "Scorch":
                switch (card.getName()) {
                    case "clan dimun pirate" -> {
                        Card maxMe = getTheMostPowerFullCard(rows);
                        Card maxOpp = getTheMostPowerFullCard(opponentRows);
                        if (maxMe.getPower() > maxOpp.getPower()) {
                            removeMyCard(maxMe);
                        } else if (maxOpp.getPower() > maxMe.getPower()) {
                            removeOppCard(maxOpp);
                        }
                    }
                    case "villentretenmerth" -> {
                        Card removableCard = getTheMostPowerFullCard(rows[0].getCards());
                        rows[0].getCards().remove(removableCard);
                        discardCards.add(removableCard);
                        updatePointOfRows();
                    }
                    case "schirru"->{
                        if(getSumPowerOfARow(rows[2]) >= 10){
                            Card removableCard = getTheMostPowerFullCard(rows[2].getCards());
                            rows[2].getCards().remove(removableCard);
                            discardCards.add(removableCard);
                            updatePointOfRows();
                        }
                    }
                    case "toad" ->{
                        if(getSumPowerOfARow(rows[1]) >= 10){
                            Card removableCard = getTheMostPowerFullCard(rows[1].getCards());
                            rows[1].getCards().remove(removableCard);
                            discardCards.add(removableCard);
                            updatePointOfRows();
                        }
                    }
                }

                break;

            case "Moral Boost":
                for (Card card1 : opponentRows[rowNumber].getCards()) {
                    card1.setPower(card1.getPower() - 1);
                }
                updatePointOfRows();
                // TODO: update the score icons on cards...


                break;
            case "Commander’s horn":

                for (Card card1 : opponentRows[rowNumber].getCards()) {
                    card1.setPower(card1.getPower() * 2);
                }
                updatePointOfRows();
                // TODO: update the score icon on each card...
                break;


            case "Medic":
//                if (isMe && discardCards.size() > 1) {
//                    Card card = discardCards.get(ApplicationController.getRandom().nextInt(0, discardCards.size() - 1));
//                    discardCards.remove(card);
//                    rows[CardController.getRowNumber(card.getName())].addCard(card);
//                }
                break;
            case "Spy":
                rows[rowNumber].addCard(card);
                break;

            case "Tight Bond":
                int count = 0;
                for (Card card1 : opponentRows[rowNumber].getCards()) {
                    if (card1.getAbility().equals("Tight Bond")) count++;
                }
                for (Card card1 : opponentRows[rowNumber].getCards()) {
                    card1.setPower(card.getPower() * count);
                }
                updatePointOfRows();
                // TODO: update the score icon on each card...
                break;


            // TODO: Why we have this ???
            case "NORTHERN_REALMS":
                break;

            // TODO: And this ???
            case "NILFGAARDIAN_EMPIRE":
                break;

        }
        updatePointOfRows();

    }

    private int getSumPowerOfARow(Row row) {
        int out = 0;
        for (Card card : row.getCards()) {
            out += card.getPower();
        }
    }


    private void putCardForMe(Card card, int rowNumber) {
        if (!card.getAbility().equals("Spy")) rows[rowNumber].addCard(card);

        switch (card.getAbility()) {
            case "Muster":
                ArrayList<Card> musters = new ArrayList<>();
                for (Card card1 : inHand) {
                    if (card1.getAbility().equals("Muster")) musters.add(card1);
                }
                inHand.removeAll(musters);
                for (Card muster : musters)
                    rows[rowNumber].addCard(muster);

                musters.clear();
                for (Card card1 : user.getDeck()) {
                    if (card1.getAbility().equals("Muster")) musters.add(card1);
                }
                user.getDeck().removeAll(musters);
                for (Card muster : musters)
                    rows[rowNumber].addCard(muster);

                break;
            case "Transformer", "Berserker":
                //It is ok...
                break;

            case "Scorch":
                switch (card.getName()) {
                    case "clan dimun pirate" -> {
                        Card maxMe = getTheMostPowerFullCard(rows);
                        Card maxOpp = getTheMostPowerFullCard(opponentRows);
                        if (maxMe.getPower() > maxOpp.getPower()) {
                            removeMyCard(maxMe);
                        } else if (maxOpp.getPower() > maxMe.getPower()) {
                            removeOppCard(maxOpp);
                        }
                    }
                    case "villentretenmerth" -> {
                        // It is ok
                    }
                    case "schirru"->{
                        // It is ok
                    }
                }
                break;
            case "Moral Boost":
                for (Card card1 : rows[rowNumber].getCards()) {
                    card1.setPower(card1.getPower() - 1);
                }
                updatePointOfRows();
                // TODO: update the score icons on cards...


                break;
            case "Commander’s horn":
                for (Card card1 : rows[rowNumber].getCards()) {
                    card1.setPower(card1.getPower() * 2);
                }
                updatePointOfRows();
                // TODO: update the score icon on each card...
                break;


            case "Medic":
                if (!discardCards.isEmpty()) {
                    Card card1 = discardCards.get(ApplicationController.getRandom().nextInt(0, discardCards.size() - 1));
                    discardCards.remove(card1);
                    rows[rowNumber].addCard(card1);
                }
                break;
            case "Spy":
                ArrayList<Card> deck = user.getDeck();
                Card card1 = deck.get(ApplicationController.getRandom().nextInt(0, deck.size() - 1));
                deck.remove(card1);
                inHand.add(card1);
                Card card2 = deck.get(ApplicationController.getRandom().nextInt(0, deck.size() - 1));
                deck.remove(card2);
                inHand.add(card2);
                updatePointOfRows();
                break;

            case "Tight Bond":
                int count = 0;
                for (Card card10 : rows[rowNumber].getCards()) {
                    if (card10.getAbility().equals("Tight Bond")) count++;
                }
                for (Card card10 : rows[rowNumber].getCards()) {
                    card10.setPower(card.getPower() * count);
                }
                updatePointOfRows();
                // TODO: update the score icon on each card...
                break;


            // TODO: Why we have this ???
            case "NORTHERN_REALMS":
                break;

            // TODO: And this ???
            case "NILFGAARDIAN_EMPIRE":
                break;

        }
    }

    private void removeOppCard(Card maxOpp) {
        for (Row row : opponentRows) {
            for (Card card : row.getCards()) {
                if (card.equals(maxOpp)) {
                    row.getCards().remove(card);
                }
            }
        }
        updatePointOfRows();
    }

    private void removeMyCard(Card maxMe) {
        for (Row row : rows) {
            for (Card card : row.getCards()) {
                if (card.equals(maxMe)) {
                    row.getCards().remove(card);
                    discardCards.add(card);
                }
            }
        }
        updatePointOfRows();
    }

    private void freeze(Row... rows) {
        // TODO: effects and others
    }

    private Card getTheMostPowerFullCard(Row[] array) {
        ArrayList<Card> result = new ArrayList<>();
        result.add(getTheMostPowerFullCard(array[0].getCards()));
        result.add(getTheMostPowerFullCard(array[1].getCards()));
        result.add(getTheMostPowerFullCard(array[2].getCards()));
        return getTheMostPowerFullCard(result);
    }

    private Card getTheMostPowerFullCard(ArrayList<Card> arrayList) {
        Card out = null;
        int count = 0;
        for (Card card : arrayList) {
            if (card.getPower() >= count) {
                out = card;
            }
        }
        return out;
    }

    private Card getTheMostPowerFullCard(Card[] arrayList) {
        Card out = null;
        int count = 0;
        for (Card card : arrayList) {
            if (card.getPower() >= count) {
                out = card;
            }
        }
        return out;
    }

    void updatePointOfRows() {
        for (Row row : rows) {
            int point = 0;
            for (Card card : row.getCards()) {
                point += card.getPower();
            }
            row.setPoint(point);
            totalPoint += point;
        }


        for (Row row : opponentRows) {
            int point = 0;
            for (Card card : row.getCards()) {
                point += card.getPower();
            }
            row.setPoint(point);
            opponentTotalPoints += point;
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


    private Card getRandomCard(ArrayList<Card> arrayList) {
        if (arrayList.isEmpty()) return null;
        return arrayList.get(ApplicationController.getRandom().nextInt(0, arrayList.size()));
    }
}