package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Out {

    private static DataOutputStream out;

    public static void setOut(DataOutputStream out) {
        Out.out = out;
    }

    public static void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }
}
