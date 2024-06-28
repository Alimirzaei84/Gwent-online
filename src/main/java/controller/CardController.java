package controller;

import model.role.*;

import java.io.*;
import java.util.*;

// @Info I change the num of card Redeem.


/*
 *   TODO missing assets
 *   monsters_crone_brewess
 *   monsters_crone_whispess
 *   nilfgaard_nausicaa_cavalry_rider
 *   nilfgaard_morvran_voorhis
 *   nilfgaard_heavy_zerrikanian_fire_scorpion
 *   nilfgaard_morvran_voorhis
 *   scoiatael_seasenthessis
 *   skellige_vidkaarl
 *   skellige_young_vidkaarl
 * */

public abstract class CardController {

    public static HashMap<String, Type> type = new HashMap<>();
    public static HashMap<String, Faction> faction = new HashMap<>();
    public static HashMap<String, Integer> num = new HashMap<>();
    public static HashMap<String, Integer> power = new HashMap<>();
    public static HashMap<String, String> ability = new HashMap<>();
    public static HashMap<String, String> description = new HashMap<>();
    public static HashMap<String, String> imagePath = new HashMap<>();
    public static int getRowNumber(String cardName){
        switch (CardController.type.get(cardName)) {
            case SIEGE -> {
                return 2;
            }
            case CLOSE -> {
                return  0;
            }
            case RANGED -> {
                return  1;
            }
        }
        return -1;
    }

    public static ArrayList<String> units = new ArrayList<>();
    public static ArrayList<String> specials = new ArrayList<>();
    public static ArrayList<String> leaders = new ArrayList<>();
    public static ArrayList<String> heroes = new ArrayList<>();

    static int numNotPassed = 0;

    public static ArrayList<String> removeDuplicates(List<String> list) {
        Set<String> set = new HashSet<>(list);
        return new ArrayList<>(set);
    }

    public static void load_data() throws IOException {
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
//        System.out.println(Arrays.toString(leaders.toArray()));
//        for (String leader : leaders) {
//            imagePath.put(leader,"src/main/resources/assets/lg/skellige_king_bran.jpg");
//        }
    }

    private static void addLeaderToRecord(String[] data) {
        String index = data[0];
        String factionName = data[1];
        String attribute = data[2];
        String leaderName = data[3].toLowerCase();
        String descriptionStr = data[4];

        leaders.add(leaderName);
        faction.put(leaderName, toFaction(factionName));
        description.put(leaderName, descriptionStr);

        type.put(leaderName, Type.LEADER);

        String tmp1 = factionName.toLowerCase();
        if (tmp1.equals("all")) {
            tmp1 = "neutral";
        }

        else if (tmp1.equals("scoia'tael")) {
            tmp1 = "scoiatael";
        }

        else if (tmp1.startsWith("northern")) {
            tmp1 = tmp1.split(" ")[1];
        }

        else if (tmp1.startsWith("nilfgaardian")) {
            tmp1 = "nilfgaard";
        }

        String middle = tmp1 + "_" + leaderName.toLowerCase().replaceAll("[\\s+]", "_");
        middle = middle.replace("’", "");
        middle = middle.replace(":", "");
        String img_path = "./src/main/resources/assets/sm/" + middle + ".jpg";
        imagePath.put(leaderName, img_path);


        File file = new File(img_path);
        if (!file.exists()) {
            numNotPassed++;
            imagePath.put(leaderName, null);
            System.out.println(img_path);
            System.out.println(middle);
        }
    }

    private static void addSpecialToRecord(String[] data) {
        String index = data[0];
        String name = data[1].toLowerCase();
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

        String middle = "special_" + name.toLowerCase().replace(" ", "_");
        middle = middle.replace("’", "");
        String img_path = "./src/main/resources/assets/sm/" + middle + ".jpg";
        imagePath.put(name, img_path);


        File file = new File(img_path);
        if (!file.exists()) {
            numNotPassed++;
            imagePath.put(name, null);
            System.out.println(img_path);
            System.out.println(middle);
        }

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

        String tmp1 = factionName.toLowerCase();
        if (tmp1.equals("all")) {
            tmp1 = "neutral";
        }

        else if (tmp1.equals("scoia'tael")) {
            tmp1 = "scoiatael";
        }

        else if (tmp1.startsWith("northern")) {
            tmp1 = tmp1.split(" ")[1];
        }

        else if (tmp1.startsWith("nilfgaardian")) {
            tmp1 = "nilfgaard";
        }

        String middle = tmp1 + "_" + cardName.toLowerCase().replaceAll("[\\s+]", "_");
        middle = middle.replace("’", "");
        middle = middle.replace(":", "");
        String img_path = "./src/main/resources/assets/sm/" + middle + ".jpg";
        imagePath.put(cardName, img_path);


        File file = new File(img_path);
        if (!file.exists()) {
            numNotPassed++;
            imagePath.put(cardName, null);
//            System.out.println(img_path);
//            System.out.println(middle);
        }

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