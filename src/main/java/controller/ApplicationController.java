package controller;

import javafx.stage.Stage;
import model.Account.User;

import java.util.Random;

public class ApplicationController {
    private static Stage stage;
    private static User forgetPasswordUser;

    private static Random random = new Random();

    public static Stage getStage() {
        return stage;
    }

    public static User getForgetPasswordUser() {
        return forgetPasswordUser;
    }

    public static void setForgetPasswordUser(User forgetPasswordUser) {
        ApplicationController.forgetPasswordUser = forgetPasswordUser;
    }

    public static Random getRandom() {
        return random;
    }

    public static void setRandom(Random random) {
        ApplicationController.random = random;
    }

    public static void setStage(Stage stage) {
        ApplicationController.stage = stage;
    }
}
