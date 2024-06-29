package model.role;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Card extends TypeAdapter<Card> {
    private String name;
    private String description;
    private final Type type;
    private int power;
    private final int maxNum;
    private final Faction faction;
    private final String ability;


    @Override
    public void write(JsonWriter out, Card card) throws IOException {
        out.beginObject();
        out.name("name").value(card.getName());
        // Other fields (excluding 'name')...
        out.endObject();
    }

    public void setPower(int power){
        this.power = power;
    }


    @Override
    public Card read(JsonReader in) throws IOException {
//        Card card = new Card(); // Create a new Card object
//
//        in.beginObject();
//        while (in.hasNext()) {
//            String fieldName = in.nextName();
//            switch (fieldName) {
//                case "name":
//                    card.setName(in.nextString());
//                    break;
//                // Handle other fields here (if needed)
//                default:
//                    in.skipValue(); // Skip unknown fields
//                    break;
//            }
//        }
//        in.endObject();
//
//        return card;
        return null;
    }


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

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxNum() {
        return maxNum;
    }


    public String getAbility() {

        return ability;
    }

    @Override
    public String toString() {
        return "Card [name=" + name + ", type=" + type + ", power=" + power + ", maxNum=" + maxNum + ", faction=" + faction + ", ability=" + ability + "]";
    }
}