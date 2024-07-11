package client.menuConrollers;

import controller.CardController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.role.Card;
import model.role.Faction;
import server.Account.User;

public class PreGameMenuController {
    public void setFaction(User loggedInUser, String name) {
        loggedInUser.setFaction(Faction.valueOf((name.toUpperCase())));
    }

    public static String removeFromDeck(VBox cardBox, String cardName, HBox currentHbox, ImageView imageView, Label label, Button button) throws Exception {
        currentHbox.getChildren().remove(label);
        currentHbox.getChildren().remove(imageView);
        currentHbox.getChildren().remove(button);
        currentHbox.getChildren().remove(cardBox);
        return "[SUCC]: " + cardName + "removed from deck";
    }

    public static String addToDeck(String cardName, User currentUser) {

        if(!currentUser.isVerified()) {
            return ("[ERR]: Please go to profile menu and verify your email first");
        }

        Card card = CardController.createCardWithName(cardName);
        currentUser.getDeck().add(card);
        if (currentUser.getSpecialCount() > 10) {
            currentUser.getDeck().remove(card);
            return ("[ERR]: you already have 10 special cards");
        }

        return "[SUCC]: " + cardName + " added to deck";
    }

}