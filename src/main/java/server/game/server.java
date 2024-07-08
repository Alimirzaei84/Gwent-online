package server.game;

import controller.CardController;
import model.role.Card;

import java.net.*;
import java.io.*;

public class server {

    public static void main(String[] args) throws IOException {
        CardController.load_data();

        //The client is used to handle connections with a client once a connection is
        //established.
        Socket client = null;

        //The following two objects handles our Serialization operations, ObjectOutputStream
        //writes an object to the stream. ObjectInputStream reads an object from the stream.
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            ServerSocket server = new ServerSocket(8888);
            client = server.accept();
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

//            Row row = (Row) in.readObject();
            Game game = (Game) in.readObject();
//            for (Card card : row.getCards()) {
//                System.out.println(card.getName());
//            }
//            System.out.println("special: " + row.getSpecial().getName());
//            System.out.println("point: " + row.getPoint());
            // close resources

            System.out.println(game.getUser1().getName() + game.getUser2().getPassword());
            out.close();
            in.close();
            client.close();
            server.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}