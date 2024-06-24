package controller.menuConrollers;

import model.Account.User;

public class LoginMenuController {
    public String login(String username, String password) throws Exception {
        User user = User.getUserByUsername(username);
        if (user == null) {
            throw new Exception("[ERR]: Username not found!");
        }
        if (!user.getPassword().equals(password)) {
            throw new Exception("[ERR]: Password is incorrect");
        }

        User.setLoggedInUser(user);
        return "[INFO]: user << " + username + " >> logged in successfully";
    }
}

