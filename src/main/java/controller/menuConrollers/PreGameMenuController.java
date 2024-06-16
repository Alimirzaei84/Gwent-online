package controller.menuConrollers;

import controller.CardController;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.Account.User;
import model.role.Card;
import model.role.Faction;

public class PreGameMenuController {
    public void setFaction(User loggedInUser, String name) {
        System.out.println(name);
        loggedInUser.setFaction(Faction.valueOf((name.toUpperCase())));
    }

    public String removeFromDeck(String cardName, User currentUser, HBox currentHbox, ImageView imageView, Text text, Button button) throws Exception {
        for (Card card : currentUser.getDeck()) {
            if (card.getName().equals(cardName)) {
                currentUser.getDeck().remove(card);
                currentHbox.getChildren().remove(text);
                currentHbox.getChildren().remove(imageView);
                currentHbox.getChildren().remove(button);
                return "[SUCC]: " + cardName + "removed from deck";
            }
        }
        throw new Exception("[ERR]: you don't have this card in your deck");
    }

    public String addToDeck(String cardName, User currentUser) throws Exception {
        Card card = CardController.createCardWithName(cardName);
        currentUser.getDeck().add(card);
        if (currentUser.getSpecialCount() > 10) {
            currentUser.getDeck().remove(card);
            throw new Exception("[ERR]: you already have 10 special cards");
        }
        return "[SUCC]: " + cardName + "added to deck";
    }
}