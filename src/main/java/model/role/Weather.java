package model.role;


import java.io.Serializable;

public class Weather extends Card implements Serializable {
    public Weather(String name, Faction faction, int maxNum, Type type, String description) {
        super(name, faction, maxNum, type, description);
    }


}