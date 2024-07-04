package server.database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public abstract class DBController {

    private static Connection connection;

    public static Map<String, String> getUserData(String name) throws SQLException {
        Map<String, String> userData = new HashMap<>();
        userData.put("id", "");
        userData.put("name", name);
        userData.put("password", "");
        userData.put("email", "");
        userData.put("nickname", "");

        String query = "SELECT * FROM users WHERE name = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {

            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userData.put("id", resultSet.getString("id"));
                userData.put("password", resultSet.getString("password"));
                userData.put("email", resultSet.getString("email"));
                userData.put("nickname", resultSet.getString("nickname"));
            }
        }

        return userData;
    }

    public static int getId(String name) throws SQLException {
        return Integer.parseInt(getUserData(name).get("id"));
    }

    public static String getEmail(String name) throws SQLException {
        return getUserData(name).get("email");
    }

    public static String getNickname(String name) throws SQLException {
        return getUserData(name).get("nickname");
    }

    public static String getPassword(String name) throws SQLException {
        return getUserData(name).get("password");
    }

    public static void deleteUser(String name) throws SQLException {
        String query = "DELETE FROM users WHERE name = ?";
        PreparedStatement preparedStatement = getConnection().prepareStatement(query);

        preparedStatement.setString(1, name);
        int rowsAffected = preparedStatement.executeUpdate();
        System.out.println("[INFO] Deleted " + rowsAffected + " row(s) with name " + name);
    }

    public static boolean loginUser(String username, String password) throws SQLException {
        try (Statement statement = getConnection().createStatement()) {
            String query = "SELECT * FROM users WHERE name = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                String pass = resultSet.getString("password");

                if (!pass.equals(password)) {
                    throw new SQLException("Wrong password");
                }
            }

            else {
                throw new SQLException("Invalid username");
            }
        }

        return true;
    }

    /*
    * throws SQLIntegrityConstraintViolationException when the name is not unique
    * */
    public static void registerUser(String name, String password, String email, String nickname) throws SQLException {

        try (Statement statement = getConnection().createStatement()) {
            String query = "INSERT INTO users (name, password, email, nickname) " +
                    "VALUES ('" + name + "', '" + password + "', '" + email + "', '" + nickname + "')";

            int rowsAffected = statement.executeUpdate(query);
            System.out.println("[INFO] Registered " + rowsAffected + " user to users table.");
        }
    }

    public static void truncateTable() throws SQLException {
        try (Statement statement = getConnection().createStatement()) {
            String query = "TRUNCATE TABLE users";
            int rowsAffected = statement.executeUpdate(query);
            System.out.println("[INFO] Truncated users table.");
        }
    }

    public static void showUsersTable() throws SQLException {
        try (Statement statement = getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String nickname = resultSet.getString("nickname");

                System.out.println(id + " " + name + " " + email + " " + nickname);
            }
        }
    }

    public static void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/gwent";
            String user = "root";
            String password = "";

            connection = DriverManager.getConnection(url, user, password);

            System.out.println("SQL connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            createConnection();
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        getConnection().close();
    }

    public static void main(String[] args) throws SQLException {
        showUsersTable();
    }
}
