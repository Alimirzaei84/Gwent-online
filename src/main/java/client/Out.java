package client;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Out {

    private static ObjectOutputStream out;

    public static void setOut(ObjectOutputStream out) {
        Out.out = out;
    }

    public static void sendMessage(String message) throws IOException {
        out.writeObject(message);
        out.flush();
        out.reset();
    }
}
