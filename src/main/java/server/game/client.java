package server.game;

import controller.CardController;
import model.role.Special;
import server.Account.User;

import java.net.*;
import java.io.*;

public class client {

    public static void main(String[] args) throws IOException {
        CardController.load_data();
        Socket client = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {

            client = new Socket("127.0.0.1", 8888);
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

//            Row row = new Row();
//            row.setPoint(100);
//            row.setSpecial((Special) CardController.createCardWithName("Commander’s horn"));
//            row.getCards().add(CardController.createCardWithName("cow"));
//            row.getCards().add(CardController.createCardWithName("cow"));

            Game game = new Game(new User("ali", "aaa", " ", "s"), new User("mammad", "aaa", " ", "s"), Game.AccessType.PUBLIC);

            out.writeObject(game);
            out.flush();

            out.close();
            in.close();
            client.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}