package controller;

import javafx.stage.Stage;
import server.User;

import java.util.Random;

public class ApplicationController {
    private static Stage stage;
    private static Stage stage1;
    private static Stage stage2;

    public static Stage getStage1() {
        return stage1;
    }

    public static void setStage1(Stage stage1) {
        ApplicationController.stage1 = stage1;
    }

    public Stage getStage2() {
        return stage2;
    }

    public static void setStage2(Stage stage2) {
        ApplicationController.stage2 = stage2;
    }

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
        ApplicationController.setStage2(new Stage());
        ApplicationController.setStage1(new Stage());
    }

    public static void closeStage(){
        if (stage.isShowing()) stage.close();
    }
}
