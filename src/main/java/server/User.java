package server;

import java.io.IOException;
import java.util.Objects;

public class User {


    private CommunicationHandler handler;
    private final String name;
    private final String password;


    public User(String name, String password) throws IOException {
        this.name = name;
        this.password = password;
    }

    public CommunicationHandler getHandler() {
        return handler;
    }

    public String getUsername() {
        return name;
    }

    public boolean authenticate(String username, String password) {
        return this.name.equals(username) && this.password.equals(password);
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

    public void setHandler(CommunicationHandler handler) {
        this.handler = handler;
    }

    public void sendMessage(String message) throws IOException {
        getHandler().sendMessage(message);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return getUsername().equals(user.getUsername());
    }

}
