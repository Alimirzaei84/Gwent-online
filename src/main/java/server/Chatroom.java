package server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chatroom {

    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
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

        broadcast("[chatroom] " + attendee.getUsername() + " joined the chatroom.");
    }

    public ArrayList<User> getAttendees() {
        return attendees;
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


        broadcast("[chatroom]["+ user.getUsername() + "]["+ timeFormatter.format(new Date()) + "]" + command);
    }

    private void shareAllNames(User attendee) throws IOException {
        attendee.sendMessage("all users: " + attendees.size());
        for (User user : attendees) {
            attendee.sendMessage("\t" + user.getUsername());
        }
    }

    public void removeAttendee(User attendee) throws IOException {
        attendees.remove(attendee);
        attendee.setStatus(User.Status.ONLINE);
        attendee.setGameId(-1); // home id

        attendee.sendMessage("[SUCC] you now left the chatroom");

        broadcast("[chatroom] "+ attendee.getUsername() + " left the chatroom");
    }
}
