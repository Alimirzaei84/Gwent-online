package model.role;

import java.util.ArrayList;

public class Leader extends Card {
    public Leader(String name, Type type, Faction faction, String description) {
        super(name, faction, description, type);
    }
}
