package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* @Info Server class is singleton
* */

public class Server implements Runnable {

    private static Server instance = null;


    private int port;

    private ServerSocket serverSocket;
    private ExecutorService pool;

    private Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(this.port);
            pool = Executors.newCachedThreadPool();

            acceptUsers();
        } catch (IOException e) {
            System.err.println("Could not connect to port: " + this.port);
            System.exit(1);
        }
    }

    private void acceptUsers() {
        System.out.println("Server port: " + serverSocket.getLocalSocketAddress());

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                CommunicationHandler handler = new CommunicationHandler(socket);

                pool.execute(handler);

            } catch (IOException e) {
                System.out.println("Failed accepting new user on port: " + this.port);
            }
        }
    }

    public static Server getInstance() {
        return instance;
    }

    public static void lunch(int port) {
        instance = new Server(port);
    }

    public static void lunch() {
        lunch(8080);
    }


}
