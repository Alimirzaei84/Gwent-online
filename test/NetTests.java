import model.Account.User;
import model.game.Game;
import org.junit.Test;

public class NetTests {

    @Test
    public void test() {
        User u1 = new User("a", "a", "a", "a");
        User u2 = new User("b", "b", "b", "b");
        Game game = new Game(u1, u2);

        game.run();
    }
}
