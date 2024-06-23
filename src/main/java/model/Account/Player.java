package model.Account;

import javafx.scene.control.IndexRange;
import model.game.Row;
import model.role.Card;
import model.role.Leader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player{
    private User user;
    private final Row[] rows;
    private ArrayList<Card> inHand;
    private final Leader leader;

    public Player(User user) {
        this.user = user;
        this.leader = user.getLeader();
        rows = new Row[3];
        createRows();
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
}
