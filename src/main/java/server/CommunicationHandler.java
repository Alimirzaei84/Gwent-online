package server;

import client.Out;
import controller.menuConrollers.LoginMenuController;
import controller.menuConrollers.RegisterMenuController;

import javafx.scene.Scene;
import server.Enum.Regexes;
import server.controller.ServerController;
import server.controller.UserController;
import server.error.SimilarRequest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommunicationHandler implements Runnable {
    private final Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private User user;
    private User tempUser;

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
                System.out.println("[" + getUsername() + "] \"" + inMessage + "\"");
                handleCommand(inMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static final String invitationRequestRegex = "let's play ([\\S]+)", registerRegex = "^register ([\\S]+) ([\\S]+)$", loginRegex = "^login ([\\S]+) ([\\S]+)$", acceptGameRegex = "^accept game with ([\\S]+)$", cancelInvitationRegex = "^cancel invitation$", denyInvitationRegex = "^deny invitation from ([\\S]+)$", friendRequestRegex = "^let's be friend ([\\S]+)$", acceptFriendRequestRegex = "^accept friend request from ([\\S]+)$", showFriendsRegex = "^show friends$", cancelFriendRequestRegex = "^cancel friend request to ([\\S]+)$", denyFriendRequestRegex = "^deny friend request from ([\\S]+)$", watchOnlineGameRegex = "^watch online game:([\\d]+)";


    private void handleCommand(String inMessage) throws Exception {

        if (Regexes.FAVORITE_COLOR.matches(inMessage)) {
            tempUser.addQuestionAnswer("your favorite color?", Regexes.FAVORITE_COLOR.getGroup(inMessage, "color"));
            System.out.println("the user favorite color set");
        } else if (Regexes.FAVORITE_MONTH.matches(inMessage)) {
            tempUser.addQuestionAnswer("your favorite month?", Regexes.FAVORITE_MONTH.getGroup(inMessage, "month"));
            System.out.println("the user favorite month set");
        } else if (Regexes.FAVORITE_FOOD.matches(inMessage)) {
            tempUser.addQuestionAnswer("your favorite food?", Regexes.FAVORITE_FOOD.getGroup(inMessage, "food"));
            System.out.println("the user favorite food set");
        } else if (inMessage.equals("back")) {
            user = null;
            tempUser = null;
        } else if (Regexes.LOGIN.matches(inMessage)) {
            String result = LoginMenuController.login(Regexes.LOGIN.getGroup(inMessage, "username"), Regexes.LOGIN.getGroup(inMessage, "password"));
            if (result.startsWith("[INFO]")) {
                user = tempUser;
                tempUser = null;
                user.getOnline(this);
            }
            Out.sendMessage("login " + result);
        } else if (Regexes.FORGET_PASSWORD.matches(inMessage)) {
            System.out.println("I am here");
            handleForgetPasswordRequest(inMessage);
        }else if (Regexes.CHANGE_PASSWORD.matches(inMessage)){
            handleChangePasswordRequest(inMessage);
        }


        if (user == null) {

            if (Regexes.REGISTER.matches(inMessage)) {
                String username = Regexes.REGISTER.getGroup(inMessage, "username"), password = Regexes.REGISTER.getGroup(inMessage, "password"), passwordAgain = Regexes.REGISTER.getGroup(inMessage, "passwordAgain"), nickname = Regexes.REGISTER.getGroup(inMessage, "nickname"), email = Regexes.REGISTER.getGroup(inMessage, "email");
                String message = RegisterMenuController.register(username, password, passwordAgain, nickname, email);
                if (message.startsWith("[SUCC]")) {
                    tempUser = new User(username, password, nickname, email);
                    System.out.println("user with username: " + username + " created");
                }
                sendMessage(message);
            }

        } else if (user.isOffline()) {

            if (Regexes.FAVORITE_COLOR.matches(inMessage)) {
                tempUser.addQuestionAnswer("your favorite color?", Regexes.FAVORITE_COLOR.getGroup(inMessage, "color"));
                System.out.println("the user favorite color set");
            } else if (Regexes.FAVORITE_MONTH.matches(inMessage)) {
                tempUser.addQuestionAnswer("your favorite month?", Regexes.FAVORITE_MONTH.getGroup(inMessage, "month"));
                System.out.println("the user favorite month set");
            } else if (Regexes.FAVORITE_FOOD.matches(inMessage)) {
                tempUser.addQuestionAnswer("your favorite food?", Regexes.FAVORITE_FOOD.getGroup(inMessage, "food"));
                System.out.println("the user favorite food set");
            } else if (inMessage.equals("back")) {
                user = null;
                tempUser = null;
            } else if (Regexes.LOGIN.matches(inMessage)) {
                String result = LoginMenuController.login(Regexes.LOGIN.getGroup(inMessage, "username"), Regexes.LOGIN.getGroup(inMessage, "password"));
                if (result.startsWith("[INFO]")) {
                    user = tempUser;
                    tempUser = null;
                    user.getOnline(this);
                }
                Out.sendMessage("login " + result);
            } else if (Regexes.FORGET_PASSWORD.matches(inMessage)) {
                System.out.println("I am here");
                handleForgetPasswordRequest(inMessage);
            }else if (Regexes.CHANGE_PASSWORD.matches(inMessage)){
                handleChangePasswordRequest(inMessage);
            }

        } else if (user.isJustOnline()) {

            if (inMessage.matches(invitationRequestRegex)) {
                Matcher matcher = getMatcher(invitationRequestRegex, inMessage);
                matcher.find();

                invitation(matcher);
            } else if (inMessage.matches(acceptGameRegex)) {
                Matcher matcher = getMatcher(acceptGameRegex, inMessage);
                matcher.find();

                acceptGame(matcher);
            } else if (inMessage.matches(showFriendsRegex)) {
                showFriends();
            } else if (inMessage.matches(cancelFriendRequestRegex)) {
                Matcher matcher = getMatcher(cancelFriendRequestRegex, inMessage);
                matcher.find();

                cancelFriendRequest(matcher);
            } else if (inMessage.matches(friendRequestRegex)) {
                Matcher matcher = getMatcher(friendRequestRegex, inMessage);
                matcher.find();

                friendRequest(matcher);
            } else if (inMessage.matches(acceptFriendRequestRegex)) {
                Matcher matcher = getMatcher(acceptFriendRequestRegex, inMessage);
                matcher.find();

                acceptFriendRequest(matcher);
            } else if (inMessage.matches(denyInvitationRegex)) {
                Matcher matcher = getMatcher(denyInvitationRegex, inMessage);
                matcher.find();

                denyInvitation(matcher);
            } else if (inMessage.matches(denyFriendRequestRegex)) {
                Matcher matcher = getMatcher(denyFriendRequestRegex, inMessage);
                matcher.find();

                denyFriendRequest(matcher);
            } else if (inMessage.matches(watchOnlineGameRegex)) {
                Matcher matcher = getMatcher(watchOnlineGameRegex, inMessage);
                matcher.find();

                watchOnlineGame(matcher);
            } else {
                sendMessage("[ERROR] unknown command");
            }
        } else if (user.isInviting()) {
            if (inMessage.matches(cancelInvitationRegex)) {
                cancelInvitation();
            } else {
                sendMessage("[ERROR] unknown command");
            }
        } else if (user.isPlaying()) {
            ServerController.passMessageToGameOfUser(this.getUser(), inMessage);
        } else if (user.isViewing()) {
            System.out.println("im still viewing you bitch.");
            ServerController.passMessageToChatRoom(this.getUser(), inMessage);
        } else {
            sendMessage("[ERROR] unknown command");
        }
    }

    private void handleChangePasswordRequest(String request) throws IOException {
        String color = Regexes.CHANGE_PASSWORD.getGroup(request, "color");
        String food = Regexes.CHANGE_PASSWORD.getGroup(request, "food");
        String month = Regexes.CHANGE_PASSWORD.getGroup(request, "month");
        String newPassword = Regexes.CHANGE_PASSWORD.getGroup(request, "newPassword");
        HashMap<String, String> answers = tempUser.getAnswers();

        if (!answers.getOrDefault("your favorite color?", "DASH!").equals(color)
                && !answers.getOrDefault("your favorite food?", "^DASH^").equals(food)
                && !answers.getOrDefault("your favorite month?", "DASH").equals(month)
        ) {
            sendMessage("[ERR]: your answers does not correct!");
            return;
        }


        tempUser.setPassword(newPassword);
        sendMessage("[SUCC]: password changed successfully");
    }

    private void handleForgetPasswordRequest(String message) throws IOException {
        User temp = User.getUserByUsername(Regexes.FORGET_PASSWORD.getGroup(message, "username"));
        if (temp == null) sendMessage("forgetPassword [ERR]");
        else {
            tempUser = temp;
            user = null;
            sendMessage("forgetPassword [SUCC]");
        }
    }

    private void register(String inMessage) {
        user = new User(Regexes.REGISTER.getGroup(inMessage, "username"), Regexes.REGISTER.getGroup(inMessage, "password"), Regexes.REGISTER.getGroup(inMessage, "email"), Regexes.REGISTER.getGroup(inMessage, "nickname"));

        System.out.println("HERE-->>");
        for (User allUser : User.getAllUsers()) {
            System.out.println(allUser.getUsername());
        }
    }


    private void watchOnlineGame(Matcher matcher) throws IOException {
        String idStr = matcher.group(1);

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            sendMessage("[ERROR] id format must be integer.");
            return;
        }

        ServerController.attendNewViewerToRunningGame(this.getUser(), id);
    }

    private void denyInvitation(Matcher matcher) throws IOException {
        String inviterName = matcher.group(1);

        User inviter = UserController.getUserByName(inviterName);

        if (inviter == null) {
            System.out.println("[ERROR] user \"" + inviterName + "\" not found");
            sendMessage("[ERROR] there is no user \"" + inviterName + "\"");
            return;
        }

        ServerController.denyInvitation(this.getUser(), inviter);
    }

    private void denyFriendRequest(Matcher matcher) throws IOException {
        String requesterName = matcher.group(1);

        User requester = UserController.getUserByName(requesterName);
        if (requester == null) {
            System.out.println("[ERROR] user \"" + requesterName + "\" not found");
            sendMessage("[ERROR] there is no user \"" + requesterName + "\"");
            return;
        }

        ServerController.denyFriendRequest(this.getUser(), requester);
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

//        User user = UserController.getUserByName(receiverName);
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
