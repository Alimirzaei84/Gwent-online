package controller.menuConrollers;


import server.User;

public class LoginMenuController {
    public static String login(String username, String password) {
        User user = User.getUserByUsername(username);
        if (user == null) {
            return ("[ERR]: Username not found!");
        }
        if (!user.getPassword().equals(password)) {
            return ("[ERR]: Password is incorrect");
        }

        return "[INFO]: user << " + username + " >> logged in successfully";
    }
}