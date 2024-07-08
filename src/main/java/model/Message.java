package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Message implements Serializable {

    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    private static int incrementer = 0;
    private static final ArrayList<Message> messages = new ArrayList<>();

    private final String senderName;
    private final String time;
    private final String message;
    private final int id;
    private final int chatroomId;

    private Message reply;
    private React react = null;

    public enum React implements Serializable{
        LAUGH(0),
        ANGRY(1),
        CRY(2);

        private final int value;
        React(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Message(String senderName, String message, int chatroomId, Message reply) {
        this(senderName, new Date(), message, chatroomId);
        this.reply = reply;
    }

    public Message(String senderName, String message, int chatroomId) {
        this(senderName, new Date(), message, chatroomId);
        this.reply = null;
    }

    private Message(String senderName, Date date, String message, int chatroomId) {
        this.senderName = senderName;
        this.time = timeFormatter.format(date);
        this.message = message;
        this.id = incrementer++;
        this.chatroomId = chatroomId;

        messages.add(this);
    }

    public String getTime() {
        return time;
    }

    public String getSenderName() {
        return senderName;
    }

    @Override
    public String toString() {
        return "sender: " + getSenderName() + ", time: " + getTime() + ", message: " + getMessage();
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public int getChatroomId() {
        return chatroomId;
    }

    public static Message getMessageById(int id) {
        for (Message m : messages) {
            if (m.getId() == id) {
                return m;
            }
        }

        return null;
    }

    public React getReact() {
        return react;
    }

    public void setReact(React react) {
        this.react = react;
    }
}