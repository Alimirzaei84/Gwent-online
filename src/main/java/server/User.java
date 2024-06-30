package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class User {


    private CommunicationHandler handler;
    private final String username;
    private final String password;
    private ArrayList<User> friends;

    private boolean isPlaying;

    public User(String name, String password) throws IOException {
        this.username = name;
        this.password = password;
        isPlaying = false;
        friends = new ArrayList<>();
    }

    public CommunicationHandler getHandler() {
        return handler;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String username, String password) {
        return this.getUsername().equals(username) && this.password.equals(password);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void getOnline(CommunicationHandler handler) {
        setHandler(handler);
    }

    public void getOffline() {
        this.handler = null;
    }

    public boolean isOffline() {
        return !this.isOnline();
    }

    public boolean isOnline() {
        return handler != null;
    }

    public void announceInvitation(User inviter) throws IOException {
        handler.sendMessage("you have been invited to a match by " + inviter.getUsername());
    }

    public void announceFriendRequest(User requester) throws IOException {
        handler.sendMessage("you have been given friend request by " + requester.getUsername());
    }

    public void setHandler(CommunicationHandler handler) {
        this.handler = handler;
    }

    public void sendMessage(String message) throws IOException {
        getHandler().sendMessage(message);
    }

    public String getPassword() {
        return password;
    }

    public void addFriend(User friend) {
        friends.add(friend);
    }

    public boolean friendsWith(User user) {
        return friends.contains(user);
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return getUsername().equals(user.getUsername());
    }

}
