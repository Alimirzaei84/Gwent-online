package model.role;


import java.io.Serializable;

public class Leader extends Card implements Serializable {
    public Leader(String name, Type type, Faction faction, String description) {
        super(name, faction, description, type);
    }
}