package controller;

import server.Account.Player;
import model.role.Card;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerController implements Serializable {
    private final Player player;
    private String responseToGame;

    public PlayerController(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }


    public String getResponseToGame()  {
        return responseToGame;
    }

    public void setResponseToGame(String responseToGame) {
        this.responseToGame = responseToGame;
    }

    private void chooseCard() throws Exception {
        ArrayList<Card> inHand = player.getInHand(),
                deck = player.getUser().getDeck();

        if (!inHand.isEmpty()) {
            throw new Exception();
        }

        // choosing 10 cards randomly
        ArrayList<Integer> cardIndex = getTenRandomCardIndex();

        // presenting to player:
        System.out.println("Your cards are:");
        for (Integer index : cardIndex) {
            System.out.println(deck.get(index));
        }
        /*
         * TODO generate this functions in front controller
         *  setCards(ArrayList<Integer>)
         *  showCards()
         *  wantToVeto() -> boolean
         *  getVetoCard() -> integer
         * */
    }

    private final static String chooseCardRegex = "^choose card$",
                startCommunicationRegex = "^open communication$",
                startTurnRegex  = "^start turn (\\d+)$";

    public void getCommand(String command) throws Exception {

        if (command.matches(chooseCardRegex)) {
            chooseCard();
        }

        else if (command.matches(startCommunicationRegex)) {
            acceptCommunication();
        }

        else if (command.matches(startTurnRegex)) {
            startTurn();
        }

        else {
            throw new Exception("Invalid command");
        }

    }

    private void startTurn() throws Exception {
        // TODO
    }

    private void acceptCommunication() throws Exception {
        setResponseToGame("communication accepted");
    }

    private ArrayList<Integer> getTenRandomCardIndex() {
        HashSet<Integer> indexSet = new HashSet<>();
        Random random = new Random();
        int numCardsInDeck = player.getUser().getDeck().size();

        while (indexSet.size() < 10) {
            indexSet.add(random.nextInt(numCardsInDeck));
        }

        return new ArrayList<>(indexSet);
    }

    public static Matcher getCommandMatcher(String command, String regex) {
        return Pattern.compile(regex).matcher(command);
    }
}
