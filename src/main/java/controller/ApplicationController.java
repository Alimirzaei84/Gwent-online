package controller;

import javafx.stage.Stage;
import server.User;
import view.RegisterMenu;

import java.util.*;

public class ApplicationController {
//    private static final ArrayList<Stage> stageArrayList = new ArrayList<>();
//    private static final HashMap<Integer, Stage> stageHashMap = new HashMap<>();

//    public static void addStage(int id, Stage stage) {
//        stageArrayList.add(stage);
//        stageHashMap.put(id, stage);
//    }

//    public static Stage closeStage(int id) {
//        for (Map.Entry<Integer, Stage> entry : stageHashMap.entrySet()) {
//            if (entry.getKey().equals(id)) {
//                //TODO: why it can't find the correct stage ???
//                System.out.println(entry.getValue());
//                return entry.getValue();
//            }
//        }
//        throw new RuntimeException("Can not find id: " + id);
//    }
//
//    public static Stage returnAStage() {
//        for (Stage stage3 : stageArrayList) {
//            if (!stage3.isShowing()) return stage3;
//        }
//        return null;
//    }

    private static Stage stage;
    private static Stage stage1;
    private static Stage stage2;

    public static Stage getStage1() {
        return stage1;
    }

    public static void setStage1(Stage stage1) {
        ApplicationController.stage1 = stage1;
    }

    public static Stage getStage2() {
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

    public synchronized static Random getRandom() {
        return random;
    }

    public static void setRandom(Random random) {
        ApplicationController.random = random;
    }

    public static void setStage(Stage stage) {
        ApplicationController.stage = stage;
    }

    public static void closeStage() {
        if (stage.isShowing()) stage.close();
    }
}