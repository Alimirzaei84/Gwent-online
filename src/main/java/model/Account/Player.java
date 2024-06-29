package model.Account;

import controller.ApplicationController;
import controller.CardController;
import controller.PlayerController;
import javafx.stage.Stage;
import model.game.Game;
import model.game.Row;
import model.role.*;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player {
    private short diamond;
    private int totalPoint;
    private boolean actionLeaderDone;
    private User user;
    private short vetoCounter;
    private final Row[] rows;
    private final ArrayList<Card> inHand;
    private final ArrayList<Card> discardCards;
    private final Leader leader;
    private final PlayerController controller;

    private final ArrayList<Card> weathers;

    public Player(User user) {
        actionLeaderDone = false;
        totalPoint = 0;
        diamond = 0;
        vetoCounter = 0;
        this.user = user;
        this.leader = user.getLeader();
        rows = new Row[3];
        weathers = new ArrayList<>();
        inHand = new ArrayList<>();
        discardCards = new ArrayList<>();
        createRows();
        controller = new PlayerController(this);

    }


    // TODO: 1
    private Player getOpponent() {
        Player player1 = Game.getCurrentGame().getPlayer1();
        Player player2 = Game.getCurrentGame().getPlayer2();
        if (player1.equals(this)) return player2;
        else return player1;
    }


    // TODO: 2
    private void playLeader() {
        if (!actionLeaderDone) {
            actionLeaderForMe(leader.getName());
            actionLeaderDone = true;
        }
    }


    // TODO: 3
    private void veto(Card card) {
        inHand.remove(card);
        inHand.add(getRandomCard(user.getDeck()));
        vetoCounter++;
    }


    // TODO: 4
    public void makeHandReady() {
        int counter = user.getLeader().getName().equals("Daisy of the Valley") ? 11 : 10;
        for (int c = 0; c < counter; c++) {
            Card card = getRandomCard(user.getDeck());
            user.getDeck().remove(card);
            inHand.add(card);
        }
    }


    // TODO: 5
    private void changeTurn() {
        handleTransformers();
        updatePointOfRows();
        getOpponent().updatePointOfRows();
        Game.getCurrentGame().changeTurn();
    }


    // TODO: 6
    private void passRound() {
        handleTransformers();
        updatePointOfRows();
        getOpponent().updatePointOfRows();
        Game.getCurrentGame().passRound();
    }


    // TODO: 7
    private void putCard(Card card) {
        int rowNumber = CardController.getRowNumber(card.getName());
        if (card.getType().equals(Type.WEATHER)) {
            inHand.remove(card);
            weathers.add(card);
            String weatherType = card.getName().split(" ")[1];
            int whichRow = 0;
            switch (weatherType) {
                case "rain" -> whichRow = 2;
                case "fog" -> whichRow = 1;
            }
            freeze(rows[whichRow]);
            changeTurn();
            return;
        }
        else if(card.getName().equals("")){

        }
        else
        putCardForMe(card, rowNumber);
    }


    public void removeDeadCards() {
        ArrayList<Card> toRemove = new ArrayList<>();

        for (Row row : rows) {
            toRemove.clear();
            for (Card card : row.getCards()) {
                if (card.getPower() <= 0) {
                    toRemove.add(card);
                }
            }
            row.getCards().removeAll(toRemove);
            discardCards.addAll(toRemove);
        }
    }


    private void increasePowerOfSpies() {
        for (Row row : rows) {
            for (Card card : row.getCards()) {
                if (card.getAbility().equals("Spy")) card.setPower(card.getPower() * 2);
            }
        }
    }

    private void actionLeaderForMe(String leaderName) {
        switch (leaderName) {
            case "madman lugos" -> {
                //TODO: we don't have this leader
            }
            case "The Siegemaster" -> {
                actionAImpenetrableFog();
            }
            case "The Steel-Forged" -> {
                avoidFreezingAspect();
            }
            case "King of Temeria" -> {
                increaseThePowerOfSiegeForMe();
            }
            case "Lord Commander of the North" -> {
                if (getOpponent().rows[2].getPoint() > 10) {
                    killTheMostPowerFul(getOpponent(), 2);
                }
            }
            case "Son of Medell" -> {
                if (getOpponent().rows[1].getPoint() > 10) killTheMostPowerFul(getOpponent(), 1);
//                    destroyOpponentMostPowerFullRanged();
            }
            case "The White Flame" -> {
                doRandomWeatherCard("torrential rain");
            }
            case "His Imperial Majesty" -> {
                show(getRandomCard(getOpponent().inHand), getRandomCard(getOpponent().inHand), getRandomCard(getOpponent().inHand));
            }
            case "Emperor of Nilfgaard" -> {
                getOpponent().actionLeaderDone = true;
            }
            case "The Relentless" -> {
                recoverFromOppDiscardPile();
            }
            case "Invader of the North" -> {
                recoverARandomCard();
                getOpponent().recoverARandomCard();
            }
            case "Bringer of Death" -> {
                increaseThePowerOfARowForMe(0);
            }
            case "King of the wild Hunt" -> {
                recoverARandomCard();
            }
            case "Destroyer of Worlds" -> {
                changeWithDistraction();
            }
            case "Commander of the Red Riders" -> {
                doRandomWeatherCard();
            }
            case "The Treacherous" -> {
                increasePowerOfSpies();
            }
            case "Queen of Dol Blathanna" -> {
                if (getOpponent().rows[0].getPoint() > 10) killTheMostPowerFul(getOpponent(), 1);
            }
            case "The Beautiful" -> {
                increaseThePowerOfARowForMe(1);
            }
            case "Daisy of the Valley" -> {
                // We handled it in choosing random cards
            }
            case "Pureblood Elf" -> {
                putAFrost();
            }
            case "Hope of the Aen Seidhe" -> {
                //TODO: I don't understand this
            }
            case "Crach an Craite" -> {
                recoverDiscardPiles();
                getOpponent().recoverDiscardPiles();
            }
            case "King Bran" -> {
                decreaseFreezingAspect();
            }
        }

        changeTurn();
        updatePointOfRows();
        getOpponent().updatePointOfRows();

    }

    private void show(Card... cards) {
        // TODO:  Show these cards to me
    }

    public void killTheMostPowerFul(Player player, int index) {
        Card card = getTheMostPowerFullCard(player.rows[index].getCards());
        player.rows[index].getCards().remove(card);
        player.discardCards.add(card);
    }

    private void recoverFromOppDiscardPile() {
        Card card = getRandomCard(getOpponent().discardCards);
        if (card instanceof Hero) {
            recoverFromOppDiscardPile();
            return;
        }
        getOpponent().inHand.remove(card);
        inHand.add(card);
    }


    // method Over Loading...
    private void doRandomWeatherCard() {
        ArrayList<Card> weathersOfMyhand = new ArrayList<>();
        for (Card card : inHand) {
            if (card.getType().equals(Type.WEATHER)) weathersOfMyhand.add(card);
        }
        Card card = getRandomCard(weathersOfMyhand);
        if (card == null) return;
        inHand.remove(card);
        weathers.add(card);
        String weatherType = card.getName().split(" ")[1];
        int whichRow = 0;
        switch (weatherType) {
            case "rain" -> whichRow = 2;
            case "fog" -> whichRow = 1;
        }
        freeze(rows[whichRow]);
    }


    // method Over Loading...
    private void doRandomWeatherCard(String cardName) {
        ArrayList<Card> weathersOfMyhand = new ArrayList<>();
        for (Card card : inHand) {
            if (card.getName().equals(cardName)) weathersOfMyhand.add(card);
        }
        Card card = getRandomCard(weathersOfMyhand);
        inHand.remove(card);
        weathers.add(card);
        String weatherType = cardName.split(" ")[1];
        int whichRow = 0;
        switch (weatherType) {
            case "rain" -> whichRow = 2;
            case "fog" -> whichRow = 1;
        }
        freeze(rows[whichRow]);
    }

    private void avoidFreezingAspect() {
        // TODO: Ignore Weather cards and remove graphical effects
    }

    private void actionAImpenetrableFog() {
        if (!isExistInMyHand("impenetrable fog")) return;
        putCardForMe(CardController.createCardWithName("impenetrable fog"), CardController.getRowNumber("impenetrable fog"));
    }

    private boolean isExistInMyHand(String cardName) {
        for (Card card : inHand) {
            if (card.getName().equals(cardName)) return true;
        }
        return false;
    }


    // This method prevents duplicate in code
    private void increaseThePowerOfARowForMe(int rowNum) {
        if (rows[rowNum].getSpecial().getName().equals("Commander’s horn")) return;
        for (Card card : rows[rowNum].getCards()) {
            card.setPower(card.getPower() * 2);
        }
    }

    private void increaseThePowerOfSiegeForMe() {
        increaseThePowerOfARowForMe(2);
    }

    private void changeWithDistraction() {
        if (inHand.size() < 2) return;

        // handle saving properly
        Card randomCardTrue = getRandomCard(user.getDeck());

        Card randomCard1 = getRandomCard(inHand);
        inHand.remove(randomCard1);
        discardCards.add(randomCard1);
        Card randomCard2 = getRandomCard(inHand);
        inHand.remove(randomCard2);
        discardCards.add(randomCard2);


        // handle saving properly
        inHand.add(randomCardTrue);

    }


    private void recoverARandomCard() {

        if (!discardCards.isEmpty()) {
            Card card = getRandomCard(discardCards);
            discardCards.remove(card);
            inHand.add(card);
        }
    }


    private void putAFrost() {
        for (Card card : inHand) {
            if (card.getName().contains("frost")) {
                putCardForMe(card, CardController.getRowNumber(card.getName()));
                break;
            }
        }
    }

    private void recoverDiscardPiles() {
        if (discardCards.isEmpty()) return;
        Card card = getRandomCard(discardCards);
        discardCards.remove(card);
        inHand.add(card);
        recoverDiscardPiles();
    }

    private void decreaseFreezingAspect() {
        // TODO: units only loose half of their powers
    }


    public void handleTransformers() {
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

        updatePointOfRows();
    }
    private void putCardForMe(Card card, int rowNumber) {

        if (card.getAbility().equals("Spy")) {
            getOpponent().rows[rowNumber].addCard(card);
        }
        else if(card.getName().equals("Commander’s horn") || card.getName().equals("mardoeme")){
            for (Row row : rows) {
                if(row.getSpecial() != null) continue;
                row.setSpecial((Special) card);
                break;
            }
        }
        else {
            rows[rowNumber].addCard(card);
            inHand.remove(card);
        }


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
                handleScorch(card.getName());
                break;
            case "Moral Boost":
                for (Card card1 : rows[rowNumber].getCards()) {
                    card1.setPower(card1.getPower() - 1);
                }
                break;
            case "Commander’s horn":
                for (Card card1 : rows[rowNumber].getCards()) {
                    card1.setPower(card1.getPower() * 2);
                }
                break;
            case "Medic":
                if (!discardCards.isEmpty()) {
                    Card card1 = discardCards.get(ApplicationController.getRandom().nextInt(0, discardCards.size() - 1));
                    discardCards.remove(card1);
                    rows[rowNumber].addCard(card1);
                }
                break;
            case "Spy":
                addTwoRandomCardToHand();
                break;

            case "Tight Bond":
             tightBoundAbility(card,rowNumber);
                break;
            // TODO: Why we have this ???
            case "NORTHERN_REALMS":
                break;

            // TODO: And this ???
            case "NILFGAARDIAN_EMPIRE":
                break;

        }


        changeTurn();
    }

    private void tightBoundAbility(Card card,int rowNumber) {
        int count = 0;
        for (Card card10 : rows[rowNumber].getCards()) {
            if (card10.getAbility().equals("Tight Bond")) count++;
        }
        for (Card card10 : rows[rowNumber].getCards()) {
            card10.setPower(card.getPower() * count);
        }
    }

    private void addTwoRandomCardToHand() {
        ArrayList<Card> deck = user.getDeck();
        Card card1 = deck.get(ApplicationController.getRandom().nextInt(0, deck.size() - 1));
        deck.remove(card1);
        inHand.add(card1);
        Card card2 = deck.get(ApplicationController.getRandom().nextInt(0, deck.size() - 1));
        deck.remove(card2);
        inHand.add(card2);
    }

    private void handleScorch(String name) {
        switch (name) {
            case "clan dimun pirate" -> {
                Card maxMe = getTheMostPowerFullCard(rows);
                Card maxOpp = getTheMostPowerFullCard(getOpponent().rows);
                if (maxMe.getPower() > maxOpp.getPower()) {
                    removeMyCard(maxMe);
                } else if (maxOpp.getPower() > maxMe.getPower()) {
                    getOpponent().removeMyCard(maxOpp);
                }
            }
            case "villentretenmerth", "schirru" -> {
                for (int i = 0; i < 3; i++) {
                    if (getOpponent().rows[i].getPoint() > 10) killTheMostPowerFul(getOpponent(), i);
                }
            }
        }
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

    //
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


    public void updatePointOfRows() {
        for (Row row : rows) {
            int point = 0;
            for (Card card : row.getCards()) {
                point += card.getPower();
            }
            row.setPoint(point);
            totalPoint += point;
        }

    }


    private void createRows() {
        rows[0] = new Row(Row.RowName.FIRST);
        rows[1] = new Row(Row.RowName.SEC);
        rows[2] = new Row(Row.RowName.THIRD);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return Objects.equals(getUser(), player.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser());
    }

    public Row[] getRows() {
        return rows;
    }

    public int getTotalPoint() {
        return totalPoint;
    }


    public void updateTotalPoint() {
        totalPoint = 0;
        for (Row row : rows) {
            totalPoint += row.getPoint();
        }
    }

    public void addADiamond() {
        if(++diamond >= 2){
            Game.getCurrentGame().endOfTheGame(this);
        }
    }
}