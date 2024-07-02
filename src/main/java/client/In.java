package client;

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

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

    private void serverMessageHandler(String message) {
        // TODO
    }

    public DataInputStream getIn() {
        return in;
    }

    private boolean clientIsConnected() {
        return Main.isRunning();
    }
}
