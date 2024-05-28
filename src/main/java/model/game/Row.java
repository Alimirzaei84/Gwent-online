package model.game;

import model.role.Card;
import model.role.Special;

import java.util.ArrayList;

public class Row {

    public enum RowName {
        FIRST (0),
        SEC (1),
        THIRD (2);
        private final int value;

        RowName(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private final RowName name;
    private Special special;
    private ArrayList<Card> cards;
    private int point;

    public Row(RowName name) {
        this.name = name;
        cards = new ArrayList<>();
        special = null;
        point = 0;
    }
}
