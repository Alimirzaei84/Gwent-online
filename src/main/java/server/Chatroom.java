package server;

import model.Message;
import server.Account.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chatroom {

    private static int incrementer = 0;

    private final ArrayList<User> attendees;
    private final ArrayList<Message> messages;
    private final int id;

    public Chatroom() {
        attendees = new ArrayList<>();
        messages = new ArrayList<>();
        id = incrementer++;
    }

    public synchronized void broadcast(Object message) throws IOException {
        for (User user : attendees) {
            user.sendMessage(message);
        }
    }

    public synchronized void broadcast(Message message) throws IOException {
        for (User user : attendees) {
            user.sendMessage(message);
        }

    }

    private static final String replyRegex = "^reply to:([\\d]+) message:(.+)$",
        reactRegex = "^react to :([\\d]+) ([\\S]+)$";

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

        else if (command.matches(replyRegex)) {
            Matcher matcher = Pattern.compile(replyRegex).matcher(command);
            matcher.find();

            Message message = replyMessage(user, matcher);
            broadcast(message);
            return;
        }

        else if (command.matches(reactRegex)) {
            Matcher matcher = Pattern.compile(reactRegex).matcher(command);
            matcher.find();
        }

        Message message = new Message(user.getUsername(), command, this.getId());
        broadcast(message);
    }

    public synchronized void react(User user, Matcher matcher) throws IOException {
        int reactedMessageId = Integer.parseInt(matcher.group(1));
        String reactStr = matcher.group(2);

        Message reactedMessage = Message.getMessageById(reactedMessageId);
        if (reactedMessage == null) {
            throw new IOException("Message with id " + reactedMessageId + " not found.");
        }

        Message.React react = Message.React.valueOf(reactStr.toUpperCase());
        reactedMessage.setReact(react);
    }

    public Message replyMessage(User user, Matcher matcher) throws IOException {
        int messageId = Integer.parseInt(matcher.group(1));
        String message = matcher.group(2);

        Message replyTo = Message.getMessageById(messageId);
        if (replyTo == null) {
            throw new IOException("Message with id " + messageId + " not found.");
        }

        return new Message(user.getUsername(), message, this.getId(), replyTo);
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
