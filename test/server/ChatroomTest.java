package server;

import controller.CardController;
import org.junit.Before;
import org.junit.Test;
import server.controller.ServerController;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class ChatroomTest {

    @Before
    public void setUp() throws Exception {
        Main.main(new String[]{});
        CardController.load_data();
    }

    @Test
    public void test1() throws IOException, InterruptedException {
        Chatroom chatroom = new Chatroom();
        assertNotNull(chatroom);

        User u1 = new User("ali", "1234", "e", "e");
        User u2 = new User("erfan", "1234", "e", "e");

        CommunicationHandler handler1 = new CommunicationHandler(new Socket("127.0.0.1", 8080)),
                handler2 = new CommunicationHandler(new Socket("127.0.0.1", 8080));

        assertNotNull(handler1);
        assertNotNull(handler2);
        assertNotNull(u1);
        assertNotNull(u2);

        u1.setHandler(handler1);
        u2.setHandler(handler2);

        chatroom.addAttendee(u1);
        chatroom.addAttendee(u2);

        chatroom.handleCommand(u1, "salam");
        chatroom.handleCommand(u2, "salam");

        Thread.sleep(60 * 1000);
        chatroom.handleCommand(u1, "chetori?");
    }

}