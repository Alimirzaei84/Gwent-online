package server.controller;

import server.Account.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;

public abstract class UserController {

    public static ArrayList<User> users = new ArrayList<>();

    // TODO throw exception instead of null
    public static User login(String username, String password) {
        User user = getUserByName(username);
        if (user == null || user.isOnline()) {
            return null;
        }

        if (user.getPassword().equals(password)) {
            return null;
        }

        return null;
    }

    public static User register(String username, String password) throws IOException {
        User user = login(username, password);
        if (user != null) {
            return null;
        }

        user = new User(username, password, "", "");
        users.add(user);
        return user;
    }

    public static User register(Matcher matcher) throws IOException {
        String username = matcher.group(1);
        String password = matcher.group(2);

        return register(username, password);
    }

    public static User login(Matcher matcher) {
        String username = matcher.group(1);
        String password = matcher.group(2);

        return login(username, password);
    }

    public static User getUserByName(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
                return user;
        }

        return null;
    }

    public static void showFriends(User user) throws IOException {
        ArrayList<User> friends = user.getFriends();

        if (friends.isEmpty()) {
            user.sendMessage("you are lonely for now.");
        }

        for (int i = 0; i < friends.size(); i++) {
            user.sendMessage(i + ", " + friends.get(i).getUsername());
        }
    }
}
