
// server code
package view;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer {
    private Selector selector;
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);

    public void start(int port) throws IOException {
        selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started on port: " + port);

        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                if (!key.isValid()) continue;

                if (key.isAcceptable()) accept(key);
                if (key.isReadable()) read(key);
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
        SocketChannel clientSocket = serverSocket.accept();
        clientSocket.configureBlocking(false);
        clientSocket.register(selector, SelectionKey.OP_READ);
        System.out.println("Accepted connection from " + clientSocket);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel clientSocket = (SocketChannel) key.channel();
        buffer.clear();
        int bytesRead = clientSocket.read(buffer);

        if (bytesRead == -1) {
            clientSocket.close();
            System.out.println("Closed connection from " + clientSocket);
            return;
        }

        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        System.out.println("Received: " + new String(data));

        buffer.clear();
        buffer.put(data);
        buffer.flip();
        clientSocket.write(buffer);
    }

    public static void main(String[] args) {
        try {
            new NioServer().start(6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}