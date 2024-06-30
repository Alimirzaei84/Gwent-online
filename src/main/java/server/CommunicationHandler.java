package server;

import server.error.SimilarRequest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommunicationHandler implements Runnable {
    private final Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private User user;

    public CommunicationHandler(Socket socket) throws IOException {
        this.socket = socket;

        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        user = null;

    }

    @Override
    public void run() {

        try {
            String inMessage;
            while ((inMessage = in.readUTF()) != null) {
                // for debug purpose
                System.out.println("[" +getUsername()+ "] \"" + inMessage + "\"");
                handleCommand(inMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static final String invitationRequestRegex = "let's play ([\\S]+)",
            registerRegex = "^register ([\\S]+) ([\\S]+)$",
            loginRegex = "^login ([\\S]+) ([\\S]+)$",
            acceptGameRegex = "^accept game with ([\\S]+)$",
            cancelInvitationRegex = "^cancel invitation$",
            friendRequestRegex = "^let's be friend ([\\S]+)$",
            acceptFriendRequestRegex = "^accept friend request from ([\\S]+)$",
            showFriendsRegex = "^show friends$",
            cancelFriendRequestRegex = "^cancel friend request to ([\\S]+)$";


    private void handleCommand(String inMessage) throws IOException {

        if (user == null) {
            if (inMessage.matches(registerRegex)) {
                Matcher matcher = Pattern.compile(registerRegex).matcher(inMessage);
                matcher.find();

                User user = UserController.register(matcher);
                if (user == null) {
                    sendMessage("[ERROR] register failed");
                } else {
                    sendMessage("[SUCC] register successful");
                    user.getOnline(this);
                    setUser(user);
                }
            }
            else if (inMessage.matches(loginRegex)) {
                Matcher matcher = Pattern.compile(loginRegex).matcher(inMessage);
                matcher.find();

                User user = UserController.login(matcher);
                if (user == null) {
                    sendMessage("[ERROR] login failed");
                } else {
                    sendMessage("[SUCC] login successful");
                    user.getOnline(this);
                    setUser(user);
                }
            }
            else {
                sendMessage("[ERROR] unknown command");
            }

        }

        else if (inMessage.matches(invitationRequestRegex)) {
            Matcher matcher = getMatcher(invitationRequestRegex, inMessage);
            matcher.find();

            invitation(matcher);
        }

        else if (inMessage.matches(acceptGameRegex)) {
            Matcher matcher = getMatcher(acceptGameRegex, inMessage);
            matcher.find();

            acceptGame(matcher);
        }

        else if (inMessage.matches(cancelInvitationRegex)) {
            cancelInvitation();
        }

        else if (inMessage.matches(showFriendsRegex)) {
            showFriends();
        }

        else if (inMessage.matches(cancelFriendRequestRegex)) {
            Matcher matcher = getMatcher(cancelFriendRequestRegex, inMessage);
            matcher.find();

            cancelFriendRequest(matcher);
        }

        else if (inMessage.matches(friendRequestRegex)) {
            Matcher matcher = getMatcher(friendRequestRegex, inMessage);
            matcher.find();

            friendRequest(matcher);
        }

        else if (inMessage.matches(acceptFriendRequestRegex)) {
            Matcher matcher = getMatcher(acceptFriendRequestRegex, inMessage);
            matcher.find();

            acceptFriendRequest(matcher);
        }

        // TODO cancel a friend request

        else {
            sendMessage("[ERROR] unknown command");
        }
    }

    private void cancelFriendRequest(Matcher matcher) throws IOException {
        String recipientName = matcher.group(1);

        User recipient = UserController.getUserByName(recipientName);
        if (recipient == null) {
            System.out.println("[ERROR] user \"" + recipientName + "\" not found");
            sendMessage("[ERROR] there is no user \"" + recipientName + "\"");
            return;
        }

        ServerController.cancelFriendRequest(this.getUser(), recipient);
    }

    private void showFriends() throws IOException {
        UserController.showFriends(this.getUser());
    }

    private void acceptFriendRequest(Matcher matcher) throws IOException {
        String requesterName = matcher.group(1);

        User requester = UserController.getUserByName(requesterName);
        if (requester == null) {
            System.out.println("[ERROR] user \"" + requesterName + "\" not found");
            sendMessage("[ERROR] there is no user \"" + requesterName + "\"");
            return;
        }

        ServerController.acceptFriendRequest(this.getUser(), requester);
    }

    private void friendRequest(Matcher matcher) throws IOException {
        String recipientName = matcher.group(1);

        User recipient = UserController.getUserByName(recipientName);
        if (recipient == null) {
            System.out.println("[ERROR] user \"" + recipientName + "\" not found");
            sendMessage("[ERROR] there is no user \"" + recipientName + "\"");
            return;
        }

        if (this.getUser().equals(recipient)) {
            sendMessage("[ERROR] you can not send friend request to yourself.");
            return;
        }

        if (this.getUser().friendsWith(recipient)) {
            sendMessage("[ERROR] you are already " + recipientName + "'s friend");
            return;
        }

        try {
            ServerController.createNewFriendRequest(this.getUser(), recipient);
        } catch (SimilarRequest e) {
            System.out.println("[ERROR] similar friend request");
            sendMessage("[ERROR] similar friend request");
        }
    }

    private void cancelInvitation() throws IOException {
        ServerController.cancelInvitation(this.getUser());
    }

    private void acceptGame(Matcher matcher) throws IOException {
        String oppName = matcher.group(1);

        User inviter = UserController.getUserByName(oppName);
        if (user == null) {
            System.out.println("[ERROR] user \"" + oppName + "\" not found");
            sendMessage("[ERROR] there is no user \"" + oppName + "\"");
            return;
        }

        if (user.isOffline()) {
            System.out.println("[ERROR] user \"" + oppName + "\" is offline");
            sendMessage("[ERROR] user \"" + oppName + "\" is offline");
            return;
        }

        ServerController.acceptGame(this.getUser(), inviter);
    }

    private void invitation(Matcher matcher) throws IOException {
        String receiverName = matcher.group(1);

        User user = UserController.getUserByName(receiverName);
        if (user == null) {
            System.out.println("[ERROR] user \"" + receiverName + "\" not found");
            sendMessage("[ERROR] there is no user \"" + receiverName + "\"");
            return;
        }

        if (user.isOffline()) {
            System.out.println("[ERROR] user \"" + receiverName + "\" is offline");
            sendMessage("[ERROR] user \"" + receiverName + "\" is offline");
            return;
        }

        if (user.equals(this.getUser())) {
            sendMessage("[ERROR] you can not invite yourself.");
            return;
        }

        try {
            ServerController.createNewInvitation(this.getUser(), user);
        } catch (SimilarRequest e) {
            System.out.println("[ERROR] similar invitation");
            sendMessage("[ERROR] similar invitation");
        }
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }

    public void shutdown() {

        try {
            if (user != null) {
                user.getOffline();
            }

            in.close();
            out.close();

            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            // TODO handle exception (perhaps ignore)
        }
    }

    public DataOutputStream getOut() {
        return out;
    }

    public DataInputStream getIn() {
        return in;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUsername() {
        if (user == null) {
            return "Unknown";
        }

        return user.getUsername();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public static Matcher getMatcher(String regex, String command) {
        return Pattern.compile(regex).matcher(command);
    }
}
