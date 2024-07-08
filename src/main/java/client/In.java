package client;

import client.view.GameLauncher;
import javafx.application.Platform;
import client.view.AppMenu;
import model.Message;
import server.game.Board;

import java.io.IOException;
import java.io.ObjectInputStream;

public class In implements Runnable {

    private final ObjectInputStream objectIn;

    public In(ObjectInputStream objectIn) {
        this.objectIn = objectIn;
    }

    @Override
    public void run() {
        while (clientIsConnected()) {
            try {

                    Object object = objectIn.readObject();
                    Platform.runLater(() -> {
                        try {
                            serverMessageHandler(object);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void serverMessageHandler(Object object) throws Exception {
        if (object instanceof String) {
            User user = User.getInstance();
            AppMenu appMenu = user.getAppMenu();
            appMenu.handleCommand((String) object);
        }

        else if (object instanceof Board board) {
            System.out.println(board);
            System.out.println("in In class line 73 + " + board.getMyHand().size() + " " + board.getOppHand().size());
            User user = User.getInstance();
            AppMenu appMenu = user.getAppMenu();
            System.out.println("size of hand in 75 of In class " + ((Board) object).getMyHand().size() + " " + ((Board) object).getOppHand().size());
            ((GameLauncher)appMenu).getBoard(board);
            System.out.println(board);
        }

        else if (object instanceof Message message) {
            User user = User.getInstance();
            System.out.println(message.getMessage());
        }

        else {
            throw new ClassCastException();
        }
    }

    public ObjectInputStream getIn() {
        return objectIn;
    }

    private boolean clientIsConnected() {
        return Main.isRunning();
    }
}
