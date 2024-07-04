package client.view;

import controller.ApplicationController;

import java.io.IOException;

public class Main extends Thread {
    public static void main(String[] args) throws IOException, InterruptedException {
        new RegisterMenu().start(ApplicationController.getStage1());
        new RegisterMenu().start(ApplicationController.getStage2());
    }
}