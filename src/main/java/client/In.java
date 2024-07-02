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
                        serverMessageHandler(finalServerMessage);
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void serverMessageHandler(String message) {
        // TODO
        User user = User.getInsetance();
        AppMenu appMenu = user.getAppMenu();
        if (appMenu instanceof RegisterMenu){
            ((RegisterMenu) appMenu).handleCommand(message);
        } else if (appMenu instanceof LoginMenu){

        }
    }

    public DataInputStream getIn() {
        return in;
    }

    private boolean clientIsConnected() {
        return Main.isRunning();
    }
}
