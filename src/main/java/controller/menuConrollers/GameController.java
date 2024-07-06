package controller.menuConrollers;

import javafx.scene.image.ImageView;
import model.role.*;

public class GameController {

    private Card selectedCard;
    private ImageView selectedImageView;

    public GameController( ){
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