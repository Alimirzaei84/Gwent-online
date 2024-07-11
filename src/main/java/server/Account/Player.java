package server.Account;

import controller.ApplicationController;
import controller.CardController;
import controller.PlayerController;
import server.game.Row;
import model.role.*;
import server.game.Game;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private short diamond;
    private ArrayList<Card> cardInfo;
    private int totalPoint;
    private boolean actionLeaderDone;
    private User user;
    private boolean makeHandReadyCalled;
    private short vetoCounter;
    private final Row[] rows;
    private final ArrayList<Card> inHand;
    private final ArrayList<Card> discardCards;
    private final PlayerController controller;
    private Game game;

    public Player(User user, Game game) {
        makeHandReadyCalled = false;
        actionLeaderDone = false;
        cardInfo = new ArrayList<>();
        totalPoint = 0;
        diamond = 0;
        vetoCounter = 0;
        this.user = user;
        rows = new Row[3];
        inHand = new ArrayList<>();
        discardCards = new ArrayList<>();
        createRows();
        controller = new PlayerController(this);
        this.game = game;
    }


    public boolean isActionLeaderDone() {
        return actionLeaderDone;
    }

    public short getDiamond() {
        return diamond;
    }

    public void setDiamond(short diamond) {
        this.diamond = diamond;
    }

    private Player getOpponent() {
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();
        if (player1.equals(this)) return player2;
        else return player1;
    }


    public void playLeader() {
        if (!actionLeaderDone) {
            actionLeaderForMe(user.getLeader().getName());
            actionLeaderDone = true;
        }
    }


    public void veto(Card card) throws Exception {

        if (++vetoCounter > 2) {
            throw new Exception("is already done");
        }
        inHand.remove(card);
        inHand.add(getRandomCard(user.getDeck()));
    }


    public void makeHandReady() {
        if (!inHand.isEmpty() || makeHandReadyCalled) return;
        int counter = user.getLeader().getName().equals("Daisy of the Valley") ? 11 : 10;
        for (int c = 0; c < counter; c++) {
            Card card = getRandomCard(getUser().getDeck());
            user.getDeck().remove(card);
            inHand.add(card);
        }
        makeHandReadyCalled = true;
    }


    private void changeTurn() {
        handleTransformers();
        updatePointOfRows();
        getOpponent().updatePointOfRows();
        game.changeTurn();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void passRound() throws IOException {
        handleTransformers();
        updatePointOfRows();
        getOpponent().updatePointOfRows();
        game.passRound();
    }


    public void putCard(Card card) {
        System.out.println("put card method in player class " + user.getName() + " put card " + card.getName());
        int rowNumber = CardController.getRowNumber(card.getName());
        if (card.getType().equals(Type.WEATHER)) {
            if (game.getWeathers().size() < 3) {
                inHand.remove(card);
                game.getWeathers().add(card);
                String weatherType = card.getName().split(" ")[1];
                int whichRow = 0;
                switch (weatherType) {
                    case "rain" -> whichRow = 2;
                    case "fog" -> whichRow = 1;
                }
                freeze(rows[whichRow]);
                freeze(getOpponent().rows[whichRow]);
            }
            changeTurn();
        } else if (card.getName().equals("")) {

        } else putCardForMe(card, rowNumber);
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
            case "the siegemaster" -> {
                actionAImpenetrableFog();
            }
            case "the steel-forged" -> {
                avoidFreezingAspect();
            }
            case "king of temeria" -> {
                increaseThePowerOfSiegeForMe();
            }
            case "lord commander of the north" -> {
                if (getOpponent().rows[2].getPoint() > 10) {
                    killTheMostPowerFul(getOpponent(), 2);
                }
            }
            case "Son of Medell" -> {
                if (getOpponent().rows[1].getPoint() > 10) killTheMostPowerFul(getOpponent(), 1);
            }
            case "the white flame" -> {
                doRandomWeatherCard("torrential rain");
            }
            case "his imperial majesty" -> {
                show(getRandomCard(getOpponent().getInHand()), getRandomCard(getOpponent().getInHand()), getRandomCard(getOpponent().getInHand()));
            }
            case "emperor of nilfgaard" -> getOpponent().actionLeaderDone = true;
            case "The Relentless" -> {
                recoverFromOppDiscardPile();
            }
            case "invader of the north" -> {
                recoverARandomCard();
                getOpponent().recoverARandomCard();
            }
            case "bringer of death" -> {
                increaseThePowerOfARowForMe(0);
            }
            case "king of the wild hunt" -> {
                recoverARandomCard();
            }
            case "destroyer of worlds" -> {
                changeWithDistraction();
            }
            case "commander of the red riders" -> {
                doRandomWeatherCard();
            }
            case "the treacherous" -> {
                increasePowerOfSpies();
            }
            case "queen of Dol blathanna" -> {
                if (getOpponent().rows[0].getPoint() > 10) killTheMostPowerFul(getOpponent(), 1);
            }
            case "the beautiful" -> {
                increaseThePowerOfARowForMe(1);
            }
            case "daisy of the valley" -> {
                // We handled it in choosing random cards
            }
            case "pureblood elf" -> {
                putAFrost();
            }
            case "hope of the aen seidhe" -> {
                //TODO: I don't understand this
            }
            case "crach an craite" -> {
                recoverDiscardPiles();
                getOpponent().recoverDiscardPiles();
            }
            case "kling bran" -> {
                decreaseFreezingAspect();
            }
        }

        changeTurn();
        updatePointOfRows();
        getOpponent().updatePointOfRows();

    }

    private void show(Card... cards) {
        getOpponent().cardInfo.addAll(List.of(cards));
        System.out.println("size of this is :" + cardInfo.size());
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
        game.getWeathers().add(card);
        String weatherType = card.getName().split(" ")[1];
        int whichRow = 0;
        switch (weatherType) {
            case "rain" -> whichRow = 2;
            case "fog" -> whichRow = 1;
        }
        freeze(rows[whichRow]);
        freeze(getOpponent().rows[whichRow]);
    }


    // method Over Loading...
    private void doRandomWeatherCard(String cardName) {
        ArrayList<Card> weathersOfMyhand = new ArrayList<>();
        for (Card card : inHand) {
            if (card.getName().equals(cardName)) weathersOfMyhand.add(card);
        }
        Card card = getRandomCard(weathersOfMyhand);
        inHand.remove(card);
        game.getWeathers().add(card);
        String weatherType = cardName.split(" ")[1];
        int whichRow = 0;
        switch (weatherType) {
            case "rain" -> whichRow = 2;
            case "fog" -> whichRow = 1;
        }
        freeze(rows[whichRow]);
        freeze(getOpponent().rows[whichRow]);
    }

    private void avoidFreezingAspect() {
        game.getWeathers().clear();

        for (Row row : rows) {
            row.setOnFrost(false);
            for (Card card : row.getCards()) {
                card.setPower(card.getPower() + 3);
            }
        }

        for (Row row : getOpponent().rows) {
            row.setOnFrost(false);
            for (Card card : row.getCards()) {
                card.setPower(card.getPower() + 3);
            }
        }

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
        Card randomCardTrue = getRandomCard(discardCards);

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

        for (Row row : rows) {
            for (Card card : row.getCards()) {
                card.setPower(card.getPower() + 1);
            }
        }

        for (Row row : getOpponent().rows) {
            for (Card card : row.getCards()) {
                card.setPower(card.getPower() + 1);
            }
        }

    }


    public void handleTransformers() {
        String cardName;

        for (Row row : rows) {
            for (Card rowCard : row.getCards()) {
                if (!rowCard.shouldBeChange()) continue;
                if (rowCard.getAbility().equals("Transformer") || rowCard.getAbility().equals("Berserker")) {
                    cardName = switch (rowCard.getName()) {
                        case "young berserker" -> "young vidkaarl";
                        case "berserker" -> "vidkaarl";
                        case "cow" -> "triss merigold";
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
        System.out.println("put crud for me in player class ");
        System.out.println("Hand size is " + inHand);
        if (card.getAbility().equals("Spy")) {
            getOpponent().rows[rowNumber].addCard(card);
            inHand.remove(card);
        } else if (card.getName().equals("commander’s horn") || card.getName().equals("mardoeme")) {
            for (Row row : rows) {
                if (row.getSpecial() != null) continue;
                row.setSpecial((Special) card);
                break;
            }
        } else {
            System.out.println("we are in else and the Hand size is " + inHand.size());
            rows[rowNumber].addCard(card);
            inHand.remove(card);
            System.out.println("remove from inHand and now the size of hand is " + inHand.size());
        }

        System.out.println("Hand size is " + inHand.size());


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
                tightBoundAbility(card, rowNumber);
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

    private void tightBoundAbility(Card card, int rowNumber) {
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


    private void freeze(Row row) {
        row.setOnFrost(true);
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
        totalPoint = 0;
        for (Row row : rows) {
            int point = 0;
            if (row.isOnFrost()) {
                for (Card card : row.getCards()) {
                    card.setPower(Math.max(card.getPower() / 2, card.getPower() - 4));
                }
            }
            for (Card card : row.getCards()) {
                point += card.getPower();
            }
            row.setPoint(point);
            totalPoint += point;
        }
    }


    private void createRows() {
        rows[0] = new Row();
        rows[1] = new Row();
        rows[2] = new Row();
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


    public PlayerController getController() {
        return controller;
    }

    public Card getRandomCard(ArrayList<Card> arrayList) {
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

    public void addADiamond() throws IOException {
        if (++diamond >= 2) {
            game.endOfTheGame(this);
        }
    }

    public ArrayList<Card> getDiscardCards() {
        return discardCards;
    }

    public void handleSkellige() {
        if (user.getFaction().equals(Faction.SKELLIGE)) {
            for (int i = 0; i < 2; i++) {
                if (discardCards.isEmpty()) continue;
                Card card = getRandomCard(discardCards);
                discardCards.remove(card);
                inHand.add(card);
            }
        }

        updatePointOfRows();
    }

    public ArrayList<Card> getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(ArrayList<Card> cardInfo) {
        this.cardInfo = cardInfo;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public void setActionLeaderDone(boolean actionLeaderDone) {
        this.actionLeaderDone = actionLeaderDone;
    }

    public short getVetoCounter() {
        return vetoCounter;
    }

    public void setVetoCounter(short vetoCounter) {
        this.vetoCounter = vetoCounter;
    }

    public Card getCardFromHandByName(String carName) {
        for (Card card : inHand) {
            if (card.getName().equals(carName)) return card;
        }
        return null;
    }

}
