package client;

import client.view.GameLauncher;
import javafx.application.Platform;
import client.view.AppMenu;
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
        String serverMessage;

        while (clientIsConnected()) {
            try {

//                if (User.getInstance().isPlaying()) {

                    Object object = objectIn.readObject();
                    Platform.runLater(() -> {
                        try {
                            serverMessageHandler(object);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

//                    return;
//                }

//                serverMessage = in.readUTF();
//                if (!serverMessage.isEmpty()) {
//
//                    String finalServerMessage = serverMessage;
//                    Platform.runLater(() -> {
//                        System.out.println(finalServerMessage);
//                        try {
//                            serverMessageHandler(finalServerMessage);
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                    });
//                }

            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void serverMessageHandler(Object object) throws Exception {

        if (object instanceof String) {
            User user = User.getInstance();
            AppMenu appMenu = user.getAppMenu();
            appMenu.handleCommand((String) object);
        }

        else if (object instanceof Board board) {
            User user = User.getInstance();
            AppMenu appMenu = user.getAppMenu();
            ((GameLauncher)appMenu).getBoard((Board) object);
            System.out.println(board);
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
