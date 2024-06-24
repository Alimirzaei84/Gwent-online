package model.Account;

import controller.PlayerController;
import model.game.Row;
import model.role.Card;
import model.role.Leader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Player implements Runnable {
    private User user;
    private final Row[] rows;
    private ArrayList<Card> inHand;
    private final Leader leader;
    private final PlayerController controller;

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean running;

    private InputHandler inHandler;

    private boolean isServerListening;

    public Player(User user) {
        this.user = user;
        this.leader = user.getLeader();
        running = true;
        rows = new Row[3];
        createRows();
        controller = new PlayerController(this);
        isServerListening = false;

        try {
            socket = new Socket("127.0.0.1", 8080);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO handle
        }

    }

    @Override
    public void run() {

        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            inHandler = new InputHandler();
            Thread thread = new Thread(inHandler);
            thread.start();

            String inMessage;
            while ((inMessage = in.readUTF()) != null) {
                System.out.println("[PLAYER] server saying: " + inMessage);
                serverCommandHandler(inMessage);
            }

        } catch (UnknownHostException e) {
            // TODO handle
            e.printStackTrace();
        } catch (IOException e) {
            // TODO handle
            e.printStackTrace();
        }

    }

    /*
    * @Info get input from terminal or view
    * */
    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                while (running) {
                    Scanner scanner = new Scanner(System.in);

                    if (isServerListening) {
                        String message = scanner.nextLine();
                        System.out.println("[PLAYER] recieve form user: " + message);
                        userCommandHandler(message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Scanner scanner = new Scanner(System.in);
//
//            while (running) {
//                String message = scanner.nextLine();
//
//                // TODO handle inputs
//
//                // for debug purpose:
//                System.out.println("message: " + message);
//            }
        }

        public void sendMessage(String message) {
            try {
                out.writeUTF(message);
            } catch (IOException e) {
                // TODO handle
                e.printStackTrace();
            }
        }
    }

    /*
     * @Info this function process the message from user
     * */
    private void userCommandHandler(String inMessage) {
        // TODO replace this conditions with regex
        if (inMessage.equals("end turn")) {
            endTurn();
            inHandler.sendMessage("end turn");
        }

        else {
            System.out.println("invalid command");
        }
        // TODO
    }

    /*
    * @Info this function process the message from server
    * */
    private void serverCommandHandler(String message) throws IOException {
        if (message.equals("start communication")) {
            inHandler.sendMessage("communication accepted");
        }

        else if (message.equals("start turn")) {
            startTurn();
        }

        else if (message.equals("ok")) {

        }
    }

    private void startTurn() {
        isServerListening = true;
        // TODO
    }

    private void endTurn() {
        isServerListening = false;
        // TODO
    }

    public void shutdown() {
        running = false;
        try {
            in.close();
            out.close();

            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

            // TODO handle
        }
    }

    private void createRows() {
        rows[0] = new Row(Row.RowName.FIRST);
        rows[1] = new Row(Row.RowName.SEC);
        rows[2] = new Row(Row.RowName.THIRD);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Player)) {
                return false;
            }

            return this.user.equals(((Player) obj).user);
    }

    public ArrayList<Card> getInHand() {
        return inHand;
    }

    public Leader getLeader() {
        return leader;
    }

    public PlayerController getController() {
        return controller;
    }
}
