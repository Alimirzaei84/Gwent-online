package model.role;

import java.util.ArrayList;

public class Leader extends Card {
    public Leader(String name, Type type, int power, Faction faction, ArrayList<String> abilities) {
        super(name, type, power, faction, abilities);
    }
}
