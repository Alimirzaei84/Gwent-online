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

    public static Map<String, String> getUserHistory(String name) throws SQLException {
        int user_id = getId(name);

        Map<String, String> userHistory = new HashMap<>();
        userHistory.put("id", "");
        userHistory.put("user_id", Integer.toString(user_id));
        userHistory.put("ties", "");
        userHistory.put("wins", "");
        userHistory.put("looses", "");
        userHistory.put("gamePlayed", "");
        userHistory.put("faction", "");
        userHistory.put("leader", "");

        String query = "SELECT * FROM user_history WHERE user_id = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userHistory.put("id", resultSet.getString("id"));
                userHistory.put("user_id", Integer.toString(user_id));
                userHistory.put("ties", resultSet.getString("ties"));
                userHistory.put("wins", resultSet.getString("wins"));
                userHistory.put("looses", resultSet.getString("looses"));
                userHistory.put("gamePlayed", resultSet.getString("gamePlayed"));
                userHistory.put("faction", resultSet.getString("faction"));
                userHistory.put("leader", resultSet.getString("leader"));
            }
        }

        return userHistory;
    }

    public static String getLeader(String name) throws SQLException {
        return getUserHistory(name).get("leader");
    }

    public static String getFaction(String name) throws SQLException {
        return getUserHistory(name).get("faction");
    }

    public static void setFaction(String name, String faction) throws SQLException {
        int user_id = getId(name);
        String query = "UPDATE user_history SET faction = ? WHERE user_id = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, user_id);
            preparedStatement.setString(2, faction);

            preparedStatement.executeQuery();
        }
    }

    public static void setLeader(String name, String leader) throws SQLException {
        int user_id = getId(name);
        String query = "UPDATE user_history SET leader = ? WHERE user_id = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(2, leader);
            preparedStatement.setInt(1, user_id);
            preparedStatement.executeQuery();
        }
    }

    public static int getGamePlayed(String name) throws SQLException {
        return Integer.parseInt(getUserHistory(name).get("gamePlayed"));
    }

    public static int getWins(String name) throws SQLException {
        return Integer.parseInt(getUserHistory(name).get("wins"));
    }

    public static int getLoses(String name) throws SQLException {
        return Integer.parseInt(getUserHistory(name).get("looses"));
    }

    public static int getTies(String name) throws SQLException {
        return Integer.parseInt(getUserHistory(name).get("ties"));
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
            if (rowsAffected <= 0) {
                throw new SQLException();
            }

            query = "SELECT * FROM users WHERE name = '" + name + "'";
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
}
