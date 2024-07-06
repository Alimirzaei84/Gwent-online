package server;

import model.Message;
import server.Account.User;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chatroom implements Serializable {

    private static int incrementer = 0;

    private final ArrayList<User> attendees;
    private final ArrayList<Message> messages;
    private final int id;

    public Chatroom() {
        attendees = new ArrayList<>();
        messages = new ArrayList<>();
        id = incrementer++;
    }

    public synchronized void broadcast(String message) throws IOException {
        for (User user : attendees) {
            user.sendMessage(message);
        }
    }

    public synchronized void broadcast(Message message) throws IOException {
        for (User user : attendees) {
            user.sendMessage(message);
        }
    }

    public void handleCommand(User user, String command) throws IOException {

        if (!attendees.contains(user)) {
            user.sendMessage("you are not attendee of this chatroom.");
            System.out.println("user " + user.getUsername() + " is not attendee of chatroom.");
            return;
        }

        if (command.equals("\\quit")) {
            removeAttendee(user);
            return;
        }

        else if (command.equals("\\all")) {
            shareAllNames(user);
            return;
        }

        Message message = new Message(user.getUsername(), command, this.getId());
        broadcast(message);
    }

    public synchronized void removeAttendee(User attendee) throws IOException {
        attendees.remove(attendee);
        attendee.setStatus(User.Status.ONLINE);
        attendee.setGameId(-1); // home id

        attendee.sendMessage("[SUCC] you now left the chatroom");

        broadcast("[chatroom] "+ attendee.getUsername() + " left the chatroom");
    }

    private void shareAllNames(User attendee) throws IOException {
        attendee.sendMessage("all users: " + attendees.size());
        for (User user : attendees) {
            attendee.sendMessage("\t" + user.getUsername());
        }
    }

    public synchronized void addAttendee(User attendee) throws IOException {
        attendee.setViewing();

        attendees.add(attendee);
    }

    public int getId() {
        return id;
    }

    public ArrayList<User> getAttendees() {
        return attendees;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }
}
