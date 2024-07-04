package server.database;

import org.junit.Test;

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
        DBController.truncateTable();
        DBController.showUsersTable();
    }

    @Test
    public void loginTest() throws SQLException {
        boolean flag = DBController.loginUser("ali", "1357");
        assertTrue(flag);
    }
}