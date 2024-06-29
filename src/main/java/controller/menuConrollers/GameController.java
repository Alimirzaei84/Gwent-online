package controller.menuConrollers;

import javafx.scene.image.ImageView;
import model.Account.Player;
import model.game.Game;
import model.role.Card;

public class GameController {

    private Game game;
    private Card selectedCard;
    private ImageView selectedImageView;

    public GameController(Game game){
        this.game = game;
    }

    public void initializeGame(){
        game.getPlayer1().makeHandReady();
        game.getPlayer2().makeHandReady();

    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public ImageView getSelectedImageView() {
        return selectedImageView;
    }

    public void setSelectedImageView(ImageView selectedImageView) {
        this.selectedImageView = selectedImageView;
    }
}
