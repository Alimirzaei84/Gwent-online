package client.menuConrollers;

import model.Enum.Regexes;
import server.Account.User;

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

        User opponent = User.getUserByUsername(opponentUsername);

        if (opponent == null) {
            throw new Exception("Such user doesn't exist!");
        }

        return "Entered Username is valid!";
    }

}
