package model.role;


import java.util.ArrayList;

public class Hero extends Card {
    public Hero(String name, Type type, int power, Faction faction, String ability) {
        super(name, type, power, 1, faction, ability);
    }

}