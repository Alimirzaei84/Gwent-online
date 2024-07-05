package server.game;

import model.role.Card;
import model.role.Special;

import java.io.Serializable;
import java.util.ArrayList;

public class Row implements Serializable {
    private boolean isOnFrost;

    public enum RowName {
        FIRST(0),
        SEC(1),
        THIRD(2);
        private final int value;

        RowName(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private Special special;
    private ArrayList<Card> cards;
    private int point;

    public Row() {
        cards = new ArrayList<>();
        special = null;
        point = 0;
        isOnFrost = false;
    }

    public boolean isOnFrost() {
        return isOnFrost;
    }

    public void setOnFrost(boolean onFrost) {
        isOnFrost = onFrost;
    }

    public void addCard(Card card) {
        cards.add(card);
    }


    public void increaseScore(int score) {
        point += score;
    }

    public Special getSpecial() {
        return special;
    }

    public void setSpecial(Special special) {
        this.special = special;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
