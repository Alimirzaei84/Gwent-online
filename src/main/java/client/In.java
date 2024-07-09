package client;

import client.view.GameLauncher;
import client.view.GameView;
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
            String s = (String) object;
            User user = User.getInstance();
            AppMenu appMenu = user.getAppMenu();
            appMenu.handleCommand(s);
        }
        if (object instanceof Board) {
            Board board = (Board) object;
            User user = User.getInstance();
            AppMenu appMenu = user.getAppMenu();
            if (appMenu instanceof GameLauncher) {
                ((GameLauncher) appMenu).getBoard(board);
            } else if (appMenu instanceof GameView) {
                ((GameView) appMenu).getBoard(board);
            } else
                throw new RuntimeException(appMenu.getClass().toString().toUpperCase() + " is not a valid menu for get the board object");
        }
        if (object instanceof Message) {
            Message message = (Message) object;
            User user = User.getInstance();
            System.out.println(message.getMessage());
        } else if (object == null) throw new ClassCastException();
    }



    public ObjectInputStream getIn() {
        return objectIn;
    }

    private boolean clientIsConnected() {
        return Main.isRunning();
    }
}
