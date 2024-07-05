package model.role;


import java.io.Serializable;

public class Unit extends Card implements Serializable {

    public Unit(String name, Type type, int power, int maxNum, Faction faction, String ability) {
        super(name, type, power, maxNum, faction, ability);
    }
}