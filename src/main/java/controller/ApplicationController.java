package controller;

import javafx.stage.Stage;

import java.util.Random;

public class ApplicationController {
    private static Stage stage;
    private static Random random = new Random();

    public static Stage getStage() {
        return stage;
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
