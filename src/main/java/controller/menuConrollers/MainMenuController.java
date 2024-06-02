package controller.menuConrollers;

import model.Account.User;
import model.Enum.Regexes;
import model.game.Game;

public class MainMenuController {

    public static String opponentValidation(String opponentUsername) throws Exception {

        if (opponentUsername == null || opponentUsername.equals("")) {
            throw new Exception("Username is empty!");
        }

        if (!Regexes.VALID_USERNAME.matches(opponentUsername)) {
            throw new Exception("Entered username is invalid!");
        }

        if (opponentUsername.equals(User.getLoggedInUser().getName())) {
            throw new Exception("You can't play with yourself!");
        }

        User opponent = User.getUserByUsername(opponentUsername);

        if (opponent == null) {
            throw new Exception("Such user doesn't exist!");
        }

        Game game = new Game(User.getLoggedInUser(), opponent);
        Game.setCurrentGame(game);
        return "Entered Username is valid!";
    }

}
