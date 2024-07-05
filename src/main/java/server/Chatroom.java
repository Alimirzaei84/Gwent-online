package server;

import server.Account.User;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Chatroom implements Serializable {

    private final ArrayList<User> attendees;

    public Chatroom() {
        attendees = new ArrayList<>();
    }

    public synchronized void broadcast(String message) throws IOException {
        for (User user : attendees) {
            user.sendMessage(message);
        }
    }

    public void addAttendee(User attendee) throws IOException {
        attendee.setViewing();

        attendees.add(attendee);
    }

    public ArrayList<User> getAttendees() {
        return attendees;
    }

    public void handleCommand(User user, String command) throws IOException {

        if (command.equals("\\quit")) {
            removeAttendee(user);
            return;
        }

        broadcast("[chatroom]["+ user.getUsername() + "] " + command);
    }

    public void removeAttendee(User attendee) throws IOException {
        attendees.remove(attendee);
        attendee.setStatus(User.Status.ONLINE);
        attendee.setGameId(-1); // home id

        attendee.sendMessage("[SUCC] you now left the chatroom");

        broadcast("[chatroom] "+ attendee.getUsername() + " left the chatroom");
    }
}
