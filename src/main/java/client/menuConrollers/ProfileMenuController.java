package client.menuConrollers;

import model.Enum.Regexes;
import server.Account.User;

public class ProfileMenuController {

    public static String changeNickname(String nickname, User user) {
        if (nickname.isEmpty()) {
            return "[ERR]: Nickname field is empty!";
        }

        if (nickname.equals(user.getNickname())) {
            return "[ERR]: Enter a new nickname!";
        }

        return "[SUCC]: Nickname changed successfully";
    }

    public static String changeUsername(String username, User user) {
        if (username == null || username.equals("")) {
            return ("[ERR]:Username field is empty!");
        }

        if (!Regexes.VALID_USERNAME.matches(username)) {
            return ("[ERR]:Username format is not valid!");
        }

        if (user.getName().equals(username)) {
            return ("[ERR]:Enter a new username!");
        }

        return "[SUCC]:Username changed successfully";
    }

    public static String changeEmail(String email, User user) {

        if (email == null || email.equals("")) {
            return ("[ERR]:Email field is empty!");
        }

        if (!Regexes.VALID_EMAIL.matches(email)) {
            return ("[ERR]:Email format is invalid!");
        }

        if (email.equals(user.getEmail())) {
            return ("[ERR]:Enter a new email!");
        }

        return "[SUCC]:Email changed successfully";
    }

    public static String changePassword(String newPassword, String oldPassword , User user) {

        if (newPassword == null || newPassword.equals("")) {
            return ("[ERR]:New password filed is empty!");
        }

        if (oldPassword == null || oldPassword.equals("")) {
            return ("[ERR]:Old password field is empty!");
        }

        if (oldPassword.equals(newPassword)) {
            return ("[ERR]:The new password is identical to the old password!");
        }

        if (!oldPassword.equals(user.getPassword())) {
            return ("[ERR]:The old password is incorrect!");
        }

        if (Regexes.VALID_PASSWORD.matches(newPassword)) {
            return ("[ERR]:Invalid password format!");
        }

        if (Regexes.CONTAINS_LOWERCASE.matches(newPassword)) {
            return ("[ERR]:Password should contain lowercase letter!");
        }

        if (Regexes.CONTAINS_UPPERCASE.matches(newPassword)) {
            return ("[ERR]:Password should contain uppercase letter!");
        }

        if (Regexes.CONTAINS_NUMBERS.matches(newPassword)) {
            return ("[ERR]:Password must contains numbers");
        }

        if (newPassword.length() < 8) {
            return ("[ERR]:The password should be at least 8 characters!");
        }

        return "[SUCC]:Password changed successfully!";
    }

}


