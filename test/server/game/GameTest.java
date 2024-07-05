package server.game;

import org.junit.Before;
import server.Main;

public class GameTest {

    @Before
    public void setUp() throws Exception {
        Main.main(null);
    }

//    @Test
//    public void sendObjectTest() throws IOException {
//        Socket socket = new Socket("127.0.0.1", 8080);
//
//        CommunicationHandler handler = new CommunicationHandler(socket);
//
//        User user = new User("a", "a", "a", "a");
//        user.getOnline(handler);
//        handler.setUser(user);
//
//        Board board = new Board();
//        user.sendMessage();
//    }
}