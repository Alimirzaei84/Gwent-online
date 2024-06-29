package controller.menuConrollers;

import model.Account.Player;
import model.game.Game;

public class GameController {

    private Game game;


    public GameController(Game game){
        this.game = game;
    }

    public void initializeGame(){
        game.getPlayer1().makeHandReady();
        game.getPlayer2().makeHandReady();

    }



}
