package model.role;

import java.util.ArrayList;

public abstract class Card {
    private final String name;
    private String description;
    private final Type type;
    private final int power;
    private final int maxNum;
    private final Faction faction;
    private final String ability;


    public Card(String name, Type type, int power, int maxNum, Faction faction, String ability) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.maxNum = maxNum;
        this.faction = faction;
        this.ability = ability;
    }

    public Card(String name, Faction faction, int maxNum, Type type, String description) {
        this.name = name;
        this.faction = faction;
        this.maxNum = maxNum;
        this.type = type;
        this.description = description;
        this.power = -1;
        this.ability = null;
    }

    public Card(String name, Faction faction, String description, Type type) {
        this.name = name;
        this.faction = faction;
        this.maxNum = 1;
        this.description = description;
        this.power = -1;
        this.ability = null;
        this.type = type;
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

    @Override
    public String toString() {
        return STR."Card [name=\{name}, type=\{type}, power=\{power}, maxNum=\{maxNum}, faction=\{faction}, ability=\{ability}]";
    }
}