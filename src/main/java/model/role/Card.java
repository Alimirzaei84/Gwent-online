package model.role;

import java.util.ArrayList;

public abstract class Card {
    private final String name;
    private String description;
    private final Type type;
    private final int power;
    private final Faction faction;
    private final ArrayList<String> abilities;


    public Card(String name, Type type, int power, Faction faction, ArrayList<String> abilities) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.faction = faction;
        this.abilities = abilities;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public int getPower() {
        return power;
    }

    public Faction getFaction() {
        return faction;
    }

    public ArrayList<String> getAbilities() {
        return abilities;
    }
}


