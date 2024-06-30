package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


/*
* @Info this is a Test class and will be TODO deleted at the end of the project
* */
public class Client {

    private Socket socket;
    private final String username;
    private final String password;
    private Thread accessThread;
    private boolean running;

    private DataInputStream in;
    private DataOutputStream out;

    public Client(String username, String password) {
        this.username = username;
        this.password = password;
        running = false;
    }

    public boolean connectSocket() {
        try {
            System.out.println("connecting...");
            socket = new Socket("127.0.0.1", 8080);
            Thread.sleep(1000);
            System.out.println("connected!");

            running = true;

            InputHandler handler = new InputHandler();
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            accessThread = new Thread(handler);
            accessThread.start();

            String serverMessage;
            while ((serverMessage = in.readUTF()) != null) {
                System.out.println("[SERVER] " + serverMessage);
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
    /*
     * @Info get input from terminal or view
     * */
    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(System.in);
                while (running) {

                    String message = scanner.nextLine();
                    System.out.println("[YOU] " + message);
                    userCommandHandler(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            try {
                out.writeUTF(message);
            } catch (IOException e) {
                // TODO handle
                e.printStackTrace();
            }
        }

        private void userCommandHandler(String message) {
            sendMessage(message);
        }

        private void sendAuthentication() {
            sendMessage("authentication " + username + " " + password);
        }
    }



    public static void main(String[] args) {
        Client client = new Client("admin", "password");
        boolean res = client.connectSocket();
    }
}
