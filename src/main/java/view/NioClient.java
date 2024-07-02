
//Client code

package view;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    private SocketChannel client;
    private ByteBuffer buffer;

    public void startConnection(String ip, int port) throws IOException {
        client = SocketChannel.open(new InetSocketAddress(ip, port));
        buffer = ByteBuffer.allocate(1024);
    }

    public void sendMessage(String message) throws IOException {
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        client.write(buffer);
    }

    public String receiveMessage() throws IOException {
        buffer.clear();
        int bytesRead = client.read(buffer);
        if (bytesRead == -1) {
            throw new IOException("Connection closed by server");
        }
        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        return new String(data);
    }

    public void stopConnection() throws IOException {
        client.close();
    }

    public static void main(String[] args) {
        NioClient client = new NioClient();
        try {
            client.startConnection("127.0.0.1", 6666);
            for (int i = 0; i < 5; i++) {
                Thread.sleep(2000);
                client.sendMessage("Message " + i);
                System.out.println("Received: " + client.receiveMessage());
            }
            client.stopConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}