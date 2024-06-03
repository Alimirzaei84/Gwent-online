package controller.menuConrollers;

import model.Account.User;
import model.role.Faction;

public class PreGameMenuController {
    public void setFaction(User loggedInUser, String name) {
        System.out.println(name);
        loggedInUser.setFaction(Faction.valueOf((name.toUpperCase())));
    }
}