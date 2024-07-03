package client;

import javafx.application.Platform;
import client.view.AppMenu;
import client.view.LoginMenu;
import client.view.RegisterMenu;

import java.io.DataInputStream;
import java.io.IOException;

public class In implements Runnable {

    private final DataInputStream in;

    public In(DataInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        String serverMessage;

        while (clientIsConnected()) {
            try {
                serverMessage = in.readUTF();
                if (!serverMessage.isEmpty()) {

                    String finalServerMessage = serverMessage;
                    Platform.runLater(() -> {
                        System.out.println(finalServerMessage);
                        try {
                            serverMessageHandler(finalServerMessage);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void serverMessageHandler(String message) throws Exception {
        User user = User.getInstance();
        AppMenu appMenu = user.getAppMenu();
        appMenu.handleCommand(message);
    }

    public DataInputStream getIn() {
        return in;
    }

    private boolean clientIsConnected() {
        return Main.isRunning();
    }
}
