package client;

import java.io.*;
import java.net.Socket;

public class Main {

    private static Socket socket;
    private static Thread accessThread;
    private static boolean running;


    public static boolean connectSocket() {
        System.out.println("connecting...");

        try {
            socket = new Socket("127.0.0.1", 8080);
            running = true;

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Out.setOut(out);

            In inHandler = new In(in);
            accessThread = new Thread(inHandler);
            accessThread.start();

            Thread.sleep(1000);
            System.out.println("connected!");
            System.out.println("socket:" + socket.getLocalSocketAddress());

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public static boolean isRunning() {
        return running;
    }
}