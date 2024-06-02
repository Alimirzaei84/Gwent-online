package controller.menuConrollers;

import model.Account.User;
import model.Enum.Regexes;

public class ProfileMenuController {

    public static void changeNickname(String nickname) {
        User.getLoggedInUser().setNickname(nickname);
    }

    public static String changeUsername(String username) throws Exception {
        if (username == null || username.equals("")) {
            throw new Exception("Username field is empty!");
        }

        if (!Regexes.VALID_USERNAME.matches(username)) {
            throw new Exception("Username format is not valid!");
        }

        if (User.getLoggedInUser().getName().equals(username)) {
            throw new Exception("Enter a new username!");
        }

        User.getLoggedInUser().setName(username);
        return "Username changed successfully";
    }

    public static String changeEmail(String email) throws Exception {

        if (email == null || email.equals("")) {
            throw new Exception("Email field is empty!");
        }

        if (!Regexes.VALID_EMAIL.matches(email)) {
            throw new Exception("Email format is invalid!");
        }

        if (email.equals(User.getLoggedInUser().getEmail())) {
            throw new Exception("Enter a new email!");
        }

        User.getLoggedInUser().setEmail(email);
        return "Email changed successfully";
    }

    public static String changePassword(String newPassword, String oldPassword) throws Exception {

        if (newPassword == null || newPassword.equals("")) {
            throw new Exception("New password filed is empty!");
        }

        if (oldPassword == null || oldPassword.equals("")) {
            throw new Exception("Old password field is empty!");
        }

        if (oldPassword.equals(newPassword)) {
            throw new Exception("The new password is identical to the old password!");
        }

        if (!oldPassword.equals(User.getLoggedInUser().getPassword())) {
            throw new Exception("The old password is incorrect!");
        }

        if (Regexes.VALID_PASSWORD.matches(newPassword)) {
            throw new Exception("Invalid password format!");
        }

        if (Regexes.CONTAINS_LOWERCASE.matches(newPassword)) {
            throw new Exception("Password should contain lowercase letter!");
        }

        if (Regexes.CONTAINS_UPPERCASE.matches(newPassword)) {
            throw new Exception("Password should contain uppercase letter!");
        }

        if (Regexes.CONTAINS_NUMBERS.matches(newPassword)) {
            throw new Exception("Password must contains numbers");
        }

        if (newPassword.length() < 8) {
            throw new Exception("The password should be at least 8 characters!");
        }

        User.getLoggedInUser().setPassword(newPassword);
        return "Password changed successfully!";
    }

}


