package server;

import controller.CardController;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CardController.load_data();
        Server.lunch();
        Thread server = new Thread(Server.getInstance());
        server.start();
    }

}