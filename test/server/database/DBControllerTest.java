package server.database;

import controller.CardController;
import model.role.Card;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DBControllerTest {

    @Test
    public void registerTest() throws SQLException {
        DBController.registerUser("parsa", "1357", "@gmail.com", "jimi");
        DBController.showUsersTable();
    }

    @Test
    public void truncateTest() throws SQLException {
        DBController.truncateUsersTable();
        DBController.showUsersTable();
    }

    @Test
    public void loginTest() throws SQLException {
        boolean flag = DBController.loginUser("ali", "1357");
        assertTrue(flag);
    }

    @Test
    public void deleteTest() throws SQLException {
        DBController.deleteUser("ali");
        DBController.showUsersTable();
    }

    @Test
    public void nullDeleteTest() throws SQLException {
        DBController.deleteUser("ali");
        DBController.showUsersTable();
    }

    @Test
    public void findTest() throws SQLException {
        DBController.deleteUser("ali");
        DBController.registerUser("ali", "1357", "@gmail.com", "baba");

        assertEquals(DBController.getEmail("ali"), "@gmail.com");
        assertEquals(DBController.getNickname("ali"), "baba");
        assertEquals(DBController.getPassword("ali"), "1357");
    }

    @Test
    public void saveTest() throws SQLException, IOException {
        DBController.deleteUser("ali");
        DBController.registerUser("ali", "1357", "@gmail.com", "baba");

        DBController.saveCard("ali", "cow");
    }

    @Test
    public void friendshipTest() throws SQLException {
        DBController.defineFriendship("parsa", "ali");
    }

    @Test
    public void friendshipExistTest() throws SQLException {
        assertTrue(DBController.friendshipExists("ali", "parsa"));
    }
}