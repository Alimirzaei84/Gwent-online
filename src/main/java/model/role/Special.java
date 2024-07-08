package model.role;

import java.io.Serializable;

public class Special extends Card implements Serializable {
    public Special(String name, Faction faction, int maxNum, Type type, String description) {
        super(name, faction, maxNum, type, description);
    }
}