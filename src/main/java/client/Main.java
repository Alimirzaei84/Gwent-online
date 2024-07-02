package client;

import javafx.application.Platform;
import client.view.RegisterMenu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {

    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Thread accessThread;
    private static boolean running;




    public static boolean connectSocket() {
        System.out.println("connecting...");

        try {
            socket = new Socket("127.0.0.1", 8080);
            running = true;

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            Out.setOut(out);

            In inHandler = new In(in);
            accessThread = new Thread(inHandler);
            accessThread.start();

//            Platform.runLater(inHandler);

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