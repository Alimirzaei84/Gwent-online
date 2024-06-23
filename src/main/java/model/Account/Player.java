package model.Account;

import controller.PlayerController;
import javafx.scene.control.IndexRange;
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

    public Player(User user) {
        this.user = user;
        this.leader = user.getLeader();
        running = true;
        rows = new Row[3];
        createRows();
        controller = new PlayerController(this);
    }

    @Override
    public void run() {

        try {
            socket = new Socket("127.0.0.1", 8080);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            InputHandler inHandler = new InputHandler();
            Thread thread = new Thread(inHandler);
            thread.start();

            String inMessage;
            while ((inMessage = in.readLine()) != null) {
                System.out.println(inMessage);
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
    * @info get input from terminal or view
    * */
    class InputHandler implements Runnable {

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);

            while (running) {
                String message = scanner.nextLine();

                // TODO handle inputs

                // for debug purpose:
                System.out.println("message: " + message);
            }
        }
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
