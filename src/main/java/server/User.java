package server;

import java.io.IOException;
import java.util.ArrayList;

public class User {

    public enum Status {
        PLAYING,
        OFFLINE,
        INVITING,
        VIEWING,
        ONLINE
    }


    private CommunicationHandler handler;
    private final String username;
    private final String password;
    private final ArrayList<User> friends;

    private Status status;

    private int gameId = -1; // -1 means home
//    private int roomId = -1; // -1 means home

    public User(String name, String password) throws IOException {
        this.username = name;
        this.password = password;
        status = Status.OFFLINE;
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
        return status.equals(Status.PLAYING);
    }

    public boolean isViewing() {
        return status.equals(Status.VIEWING);
    }

    public boolean isInviting() {
        return status.equals(Status.INVITING);
    }

    public boolean isJustOnline() {
        return status.equals(Status.ONLINE);
    }

    public void setPlaying() {
        status = Status.PLAYING;
    }

    public void getOnline(CommunicationHandler handler) {
        status = Status.ONLINE;
        setHandler(handler);
    }

    public void getOffline() {
        status = Status.OFFLINE;
        this.handler = null;
    }

    public boolean isOffline() {
        return status.equals(Status.OFFLINE);
    }

    public boolean isOnline() {
        return !isOffline();
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

    public void setInviting() {
        status = Status.INVITING;
    }

    public void setViewing() {
        status = Status.VIEWING;
    }

    public Status getStatus() {
        return status;
    }

    public int getGameId() {
        return gameId;
    }

//    public int getRoomId() {
//        return roomId;
//    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

//    public void setRoomId(int roomId) {
//        this.roomId = roomId;
//    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return getUsername().equals(user.getUsername());
    }

}
