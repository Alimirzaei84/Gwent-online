package controller.menuConrollers;


import server.User;

public class RegisterMenuController {
    public String register(String username, String password, String passwordAgain, String nickname, String email) throws Exception {
        if (username == null || password == null || passwordAgain == null || nickname == null || email == null) {
            throw new Exception("[ERR]: please fill the fields");
        }
        if (User.getUserByUsername(username) != null) {
            throw new Exception("[ERR]: already exists");
        }
//        if (!Regexes.VALID_USERNAME.matches(username)) {
//            throw new Exception("[ERR]: Invalid username");
//        }
//        if (!Regexes.VALID_EMAIL.matches(email)) {
//            throw new Exception("[ERR]: Invalid email format");
//        }
//        if (!Regexes.VALID_PASSWORD.matches(password)) {
//            throw new Exception("[ERR]: Invalid password format");
//        }
//        if (!Regexes.CONTAINS_LOWERCASE.matches(password)) {
//            throw new Exception("[ERR]: password must contains lowercase letters");
//        }
//        if (!Regexes.CONTAINS_UPPERCASE.matches(password)) {
//            throw new Exception("[ERR]: password must contains uppercase letters");
//        }
//        if (!Regexes.CONTAINS_NUMBERS.matches(password)) {
//            throw new Exception("[ERR]: password must contain numbers");
//        }
//        if (password.length() < 8) {
//            throw new Exception("[ERR]: password should be at least 8 characters");
//        }
//        if (!password.equals(passwordAgain)) {
//            throw new Exception("[ERR]: the passwords are not the same");
//        }
        return "[SUCC]: User registered successfully";
    }
}