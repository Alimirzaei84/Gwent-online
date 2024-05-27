package model.role;

import java.util.ArrayList;

public class Unit extends Card {

    public Unit(String name, Type type, int power, Faction faction, ArrayList<String> abilities) {
        super(name, type, power, faction, abilities);
    }
}
