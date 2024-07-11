package client.menuConrollers;


import javafx.stage.Stage;
import server.Account.User;
import model.Enum.Regexes;

public class RegisterMenuController {
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static String register(String username, String password, String passwordAgain, String nickname, String email) {
        if (username == null || password == null || passwordAgain == null || nickname == null || email == null) {
            return ("[ERR]: please fill the fields");
        }
        if (User.getUserByUsername(username) != null) {
            return ("[ERR]: already exists");
        }
        if (!Regexes.VALID_USERNAME.matches(username)) {
            return ("[ERR]: Invalid username");
        }
        if (!Regexes.VALID_EMAIL.matches(email)) {
            return ("[ERR]: Invalid email format");
        }
        if (!Regexes.VALID_PASSWORD.matches(password)) {
            return ("[ERR]: Invalid password format");
        }
        if (!Regexes.CONTAINS_LOWERCASE.matches(password)) {
            return ("[ERR]: password must contains lowercase letters");
        }
        if (!Regexes.CONTAINS_UPPERCASE.matches(password)) {
            return ("[ERR]: password must contains uppercase letters");
        }
        if (!Regexes.CONTAINS_NUMBERS.matches(password)) {
            return ("[ERR]: password must contain numbers");
        }
        if (password.length() < 8) {
            return ("[ERR]: password should be at least 8 characters");
        }
        if (!password.equals(passwordAgain)) {
            return ("[ERR]: the passwords are not the same");
        }
        new User(username, password, email, nickname);
        return "[SUCC]: User registered successfully";
    }
}