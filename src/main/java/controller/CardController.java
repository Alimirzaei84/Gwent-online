package controller;

import model.role.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
* @Info i change the num of card Vreemde.
* */

public class CardController {

    public static HashMap<String, Type> type = new HashMap<>();
    public static HashMap<String, Faction> faction = new HashMap<>();
    public static HashMap<String, Integer> num = new HashMap<>();
    public static HashMap<String, Integer> power = new HashMap<>();
    public static HashMap<String, String> ability = new HashMap<>();
    public static HashMap<String, String> description = new HashMap<>();

    public static ArrayList<String> units = new ArrayList<>();
    public static ArrayList<String> specials = new ArrayList<>();
    public static ArrayList<String> leaders = new ArrayList<>();
    public static ArrayList<String> heroes = new ArrayList<>();

    public static void main(String[] args) {
        try {
            load_data();
            Card card = createCardWithName("cow");
            System.out.println(card);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load_data() throws FileNotFoundException, IOException {
        String unitPath = "./src/main/resources/data/unit cards",
                leaderPath = "./src/main/resources/data/leader cards",
                specialPath = "./src/main/resources/data/special cards";
        BufferedReader unitReader = new BufferedReader(new FileReader(unitPath)),
                specialReader = new BufferedReader(new FileReader(specialPath)),
                leaderReader = new BufferedReader(new FileReader(leaderPath));

        String line;

        // skip the first line
        line = unitReader.readLine();
        while ((line = unitReader.readLine()) != null) {
            String[] data = line.split(",");
            addUnitToRecord(data);
        }
        unitReader.close();


        // skip the first line
        line = specialReader.readLine();
        while ((line = specialReader.readLine()) != null) {
            String[] data = line.split(",");
            addSpecialToRecord(data);
        }
        specialReader.close();


        // skip the first line
        line = leaderReader.readLine();
        while ((line = leaderReader.readLine()) != null) {
            String[] data = line.split(",");
            addLeaderToRecord(data);
        }
        leaderReader.close();

    }

    private static void addLeaderToRecord(String[] data) {
        String index = data[0];
        String factionName = data[1];
        String attribute = data[2];
        String leaderName = data[3];
        String descriptionStr = data[4];

        leaders.add(leaderName);
        faction.put(leaderName, toFaction(factionName));
        description.put(leaderName, descriptionStr);
    }

    private static void addSpecialToRecord(String[] data) {
        String index = data[0];
        String name = data[1];
        String factionStr = data[2];
        String numStr = data[3];
        String typeStr = data[4].toUpperCase();
        String descriptionStr = data[5];

        specials.add(name);
        faction.put(name, toFaction(factionStr));
        num.put(name, Integer.parseInt(numStr));
        type.put(name, Type.valueOf(typeStr));
        description.put(name, descriptionStr);

        // default
        power.put(name, 0);

    }

    private static void addUnitToRecord(String[] line) {
        String index = line[0];
        String cardName = line[1].toLowerCase();
        String factionName = line[2].toUpperCase();
        String powerStr = line[3];
        String numStr = line[4];
        String typeStr = line[5].toUpperCase();
        String abilityStr = line[6];
        String isHeroStr = line[7];


        String descriptionStr = null;
        // if contains the description part
        if (line.length >= 9) {
            descriptionStr = line[8];
        }

        faction.put(cardName, toFaction(factionName));
        power.put(cardName, Integer.parseInt(powerStr));
        num.put(cardName, Integer.parseInt(numStr));
        type.put(cardName, Type.valueOf(typeStr));
        ability.put(cardName, abilityStr);
        description.put(cardName, descriptionStr);


        boolean isHero = Boolean.parseBoolean(isHeroStr);
        if (isHero)
            heroes.add(cardName);
        else
            units.add(cardName);
    }

    public static Card createCardWithName(String name) {
        name = name.toLowerCase();

        if (units.contains(name)) {
            return createUnitCard(name);
        }

        if (heroes.contains(name)) {
            return createHeroCard(name);
        }

        if (specials.contains(name)) {
            return createSpecialCard(name);
        }

        if (leaders.contains(name)) {
            return createLeaderCard(name);
        }


        throw new RuntimeException("Invalid card name");
    }

    public static Card createUnitCard(String name) {
        name = name.toLowerCase();

        Type t = type.get(name);
        Faction f = faction.get(name);
        int pow = power.get(name);
        int maxNum = num.get(name);
        String a = ability.get(name);


        return new Unit(name, t, pow, maxNum, f, a);
    }

    public static Card createSpecialCard(String name) {
        name = name.toLowerCase();
        Type t = type.get(name);
        Faction f = faction.get(name);
        int maxNum = num.get(name);
        String d = description.get(name);

        return new Special(name, f, maxNum, t, d);
    }

    public static Card createLeaderCard(String name) {
        name = name.toLowerCase();
        Type t = type.get(name);
        Faction f = faction.get(name);
        String d = description.get(name);

        return new Leader(name, t, f, d);
    }

    public static Card createHeroCard(String name) {
        name = name.toLowerCase();
        Type t = type.get(name);
        int pow = power.get(name);
        Faction f = faction.get(name);
        String a = ability.get(name);

        return new Hero(name, t, pow, f, a);
    }

    private static Faction toFaction(String s) {
        if (s.contains(" ")) {
            s = s.replace(" ", "_");
        }

        if (s.contains("'")) {
            s = s.replace("'", "_");
        }

        return Faction.valueOf(s.toUpperCase());
    }
}
