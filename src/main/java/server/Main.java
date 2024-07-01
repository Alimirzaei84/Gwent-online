package server;

public class Main {

    public static void main(String[] args) {
        Server.lunch();
        Thread server = new Thread(Server.getInstance());
        server.start();
    }

}
