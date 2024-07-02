package controller.menuConrollers;

import model.Enum.Regexes;
import model.game.Game;
import server.User;

public class MainMenuController {

    public static String opponentValidation(String opponentUsername) throws Exception {

        if (opponentUsername == null || opponentUsername.isEmpty()) {
            throw new Exception("Username is empty!");
        }

        if (!Regexes.VALID_USERNAME.matches(opponentUsername)) {
            throw new Exception("Entered username is invalid!");
        }

        if (opponentUsername.equals(User.getLoggedInUser().getName())) {
            throw new Exception("You can't play with yourself!");
        }

//        server.User opponent = server.User.getUserByUsername(opponentUsername);
        server.User opponent = server.User.getUserByUsername(opponentUsername);

        if (opponent == null) {
            throw new Exception("Such user doesn't exist!");
        }

        Game game = new Game(User.getLoggedInUser(),opponent);
        Game.setCurrentGame(game);
        return "Entered Username is valid!";
    }

}
