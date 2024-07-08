package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import controller.CardController;
import controller.menuConrollers.*;

import model.role.Card;
import model.role.Faction;
import model.role.Leader;
import server.Account.User;
import server.Enum.Regexes;
import server.controller.ServerController;
import server.controller.UserController;
import server.error.SimilarRequest;
import server.request.FriendRequest;
import server.request.Invitation;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommunicationHandler implements Runnable {
    private final Socket socket;
    private final ObjectInputStream in;
    private ObjectOutputStream out;
    private User user;
    private User tempUser;

    public CommunicationHandler(Socket socket) throws IOException {
        this.socket = socket;

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        user = null;
    }

    @Override
    public void run() {

        try {
            String inMessage;
            while ((inMessage = (String) in.readObject()) != null) {
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

    private static final String invitationRequestRegex = "let's play (.+)", registerRegex = "^register ([\\S]+) ([\\S]+)$", loginRegex = "^login ([\\S]+) ([\\S]+)$", acceptGameRegex = "^accept game with (.+)$", cancelInvitationRegex = "^cancel invitation$", denyInvitationRegex = "^deny invitation from (.+)$", friendRequestRegex = "^let's be friend (.+)$", acceptFriendRequestRegex = "^accept friend request from ([\\S]+)$", showFriendsRegex = "^show friends$", cancelFriendRequestRegex = "^cancel friend request to ([\\S]+)$", denyFriendRequestRegex = "^deny friend request from ([\\S]+)$", watchOnlineGameRegex = "^watch online game:([\\d]+)";

    private void handleCommand(String inMessage) throws Exception {
        if (user == null) {

            if (Regexes.REGISTER.matches(inMessage)) {
                String username = Regexes.REGISTER.getGroup(inMessage, "username"), password = Regexes.REGISTER.getGroup(inMessage, "password"), passwordAgain = Regexes.REGISTER.getGroup(inMessage, "passwordAgain"), nickname = Regexes.REGISTER.getGroup(inMessage, "nickname"), email = Regexes.REGISTER.getGroup(inMessage, "email");
                String message = RegisterMenuController.register(username, password, passwordAgain, nickname, email);
                if (message.startsWith("[SUCC]")) {
                    tempUser = new User(username, password, nickname, email);
                    System.out.println("user with username: " + username + " created");
                }
                sendMessage(message);
              
            } else if (Regexes.FAVORITE_COLOR.matches(inMessage)) {
                tempUser.addQuestionAnswer("your favorite color?", Regexes.FAVORITE_COLOR.getGroup(inMessage, "color"));
                System.out.println("the user favorite color set");
            } else if (Regexes.FAVORITE_MONTH.matches(inMessage)) {
                tempUser.addQuestionAnswer("your favorite month?", Regexes.FAVORITE_MONTH.getGroup(inMessage, "month"));
                System.out.println("the user favorite month set");
            } else if (Regexes.FAVORITE_FOOD.matches(inMessage)) {
                tempUser.addQuestionAnswer("your favorite food?", Regexes.FAVORITE_FOOD.getGroup(inMessage, "food"));
                System.out.println("the user favorite food set");
            } else if (inMessage.equals("back")) {
                tempUser = null;
            } else if (Regexes.LOGIN.matches(inMessage)) {
                String result = LoginMenuController.login(Regexes.LOGIN.getGroup(inMessage, "username"), Regexes.LOGIN.getGroup(inMessage, "password"));
                if (result.startsWith("[INFO]")) {
                    user = tempUser;
                    user.getOnline(this);
                    tempUser = null;
                }
                sendMessage("login " + result);
            } else if (Regexes.FORGET_PASSWORD.matches(inMessage)) {
                System.out.println("I am here");
                handleForgetPasswordRequest(inMessage);
            } else if (Regexes.CHANGE_PASSWORD.matches(inMessage)) {
                handleChangePasswordRequest(inMessage);
            }

            else {
                sendMessage("invalid command");
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
                    user.getOnline(this);
                    tempUser = null;
                }
                sendMessage("login " + result);
            } else if (Regexes.FORGET_PASSWORD.matches(inMessage)) {
                System.out.println("I am here");
                handleForgetPasswordRequest(inMessage);
            } else if (Regexes.CHANGE_PASSWORD.matches(inMessage)) {
                handleChangePasswordRequest(inMessage);
            }

        } else if (user.isJustOnline()) {

            if (Regexes.GET_USERNAME.matches(inMessage)) {
                sendMessage("[USERNAME]:" + user.getUsername());
            } else if (Regexes.GET_EMAIL.matches(inMessage)) {
                sendMessage("[EMAIL]:" + user.getEmail());
            } else if (Regexes.GET_NICKNAME.matches(inMessage)) {
                sendMessage("[NICKNAME]:" + user.getNickname());
            } else if (Regexes.GET_GAMES_PLAYED.matches(inMessage)) {
                sendMessage("[GAMESPLAYED]:" + user.getGamesPlayed());
            } else if (Regexes.GET_LOSSES.matches(inMessage)) {
                sendMessage("[LOSSES]:" + user.getLosses());
            } else if (Regexes.GET_WINS.matches(inMessage)) {
                sendMessage("[WINS]:" + user.getWins());
            } else if (Regexes.GET_TIE.matches(inMessage)) {
                sendMessage("[TIE]:" + user.getTies());
            } else if (Regexes.GET_RANK.matches(inMessage)) {
                sendMessage("[RANK]:" + user.getRank());
            } else if (Regexes.GET_MAX_SCORE.matches(inMessage)) {
                sendMessage("[MAXSCORE]:" + user.getHighestScore());
            } else if (Regexes.CHANGE_PASSWORD_PROFILEMENU.matches(inMessage)) {
                String newPassword = Regexes.CHANGE_PASSWORD_PROFILEMENU.getGroup(inMessage, "password");
                String oldPassword = Regexes.CHANGE_PASSWORD_PROFILEMENU.getGroup(inMessage, "oldPassword");
                String res = ProfileMenuController.changePassword(newPassword, oldPassword, user);
                if (res.startsWith("[SUCC]")) {
                    user.setPassword(newPassword);
                }

                sendMessage(res);
            } else if (Regexes.LOGOUT.matches(inMessage)) {
                user.getOffline();
//                this.setUser(null);
            } else if (Regexes.CHANGE_NICKNAME.matches(inMessage)) {
                String res = ProfileMenuController.changeNickname(Regexes.CHANGE_NICKNAME.getGroup(inMessage, "newNickname"), user);
                if (res.startsWith("[SUCC]")) {
                    user.setNickname(Regexes.CHANGE_NICKNAME.getGroup(inMessage, "newNickname"));
                }

                sendMessage(res);
            } else if (Regexes.CHANGE_USERNAME.matches(inMessage)) {
                String res = ProfileMenuController.changeUsername(Regexes.CHANGE_USERNAME.getGroup(inMessage, "newUsername"), user);
                if (res.startsWith("[SUCC]")) {
                    user.setUsername(Regexes.CHANGE_USERNAME.getGroup(inMessage, "newUsername"));
                }

                sendMessage(res);
            } else if (Regexes.CHANGE_EMAIL.matches(inMessage)) {
                String newEmail = Regexes.CHANGE_EMAIL.getGroup(inMessage, "newEmail");
                String res = ProfileMenuController.changeEmail(newEmail, user);
                if (res.startsWith("[SUCC]")) {
                    user.setEmail(newEmail);
                }

                sendMessage(res);
            } else if (Regexes.GET_GAME_HISTORIES.matches(inMessage)) {
                String res = GameHistoryController.convertToJson(user.getGameHistories());
                if (res == null) {
                    sendMessage("");
                } else {
                    sendMessage(res);
                }
            } else if (Regexes.GET_FRIENDS.matches(inMessage)) {
                String res = user.getFriendsUsernames();
                sendMessage(res);
            } else if (Regexes.GET_REQUESTS.matches(inMessage)) {
                ArrayList<FriendRequest> requests = ServerController.getAUsersFriendRequests(user);
                String jsonString = toJsonStringRequestsArrayList(requests);
                StringBuilder builder = new StringBuilder();
                builder.append("[REQUESTS]:");

                for (FriendRequest request : requests) {
                    builder.append(request.getRequester().getUsername()).append("|");
                }

                sendMessage(builder.toString());
            } else if (inMessage.matches(invitationRequestRegex)) {
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
            } else if (inMessage.equals("user please")) {
                sendMessage("user: " + new ObjectMapper().writeValueAsString(user));
            } else if (inMessage.equals("show cards")) {
                sendShowCardCommand();
            } else if (inMessage.equals("show deck")) {
                sendShowDeckCommand();
            } else if (inMessage.equals("showCurrentUserInfo")) {
                sendShowCurrentUserInfo();
            } else if (Regexes.ADD_TO_DECK.matches(inMessage)) {
                handleAddToDeckRequest(Regexes.ADD_TO_DECK.getGroup(inMessage, "cardName"));
            } else if (Regexes.REMOVE_FROM_DECK_REQUEST.matches(inMessage)) {
                getUser().getDeck().remove(getUser().getCardFromDeckByName(Regexes.REMOVE_FROM_DECK_REQUEST.getGroup(inMessage, "cardName")));
            } else if (inMessage.startsWith("show leader")) {
                sendFactionForShowLeaderRequest();
            } else if (Regexes.CHANGE_LEADER_REQUEST.matches(inMessage)) {
                handleChangeLeaderRequest(inMessage);
            } else if (inMessage.equals("download deck")) {
                sendMessage("download deck " + new ObjectMapper().writeValueAsString(convertToTheirName(user.getDeck())));
            } else if (Regexes.SET_DECK.matches(inMessage)) {
                handleSetDeckRequest(inMessage);
            } else if (inMessage.equals("show factions")) {
                sendMessage("show factions " + new ObjectMapper().writeValueAsString(user.getFaction()));
            } else if (Regexes.SET_FACTION.matches(inMessage)) {
                setFaction(inMessage);
            } else if (Regexes.GET_INVITES.matches(inMessage)) {
                StringBuilder builder = new StringBuilder();
                builder.append("[INVITES]:");
                ArrayList<Invitation> invites = ServerController.getAUsersInvitations(user);
                for (Invitation invite : invites){
                    builder.append(invite.getInviter().getUsername()).append("|");
                }

                sendMessage(builder.toString());
            } else if (inMessage.equals("get end of game data")) {
                sendMessage(user.getGameHistories().getLast().toString());
            } else{
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

    private void setFaction(String message) {
        String factionName = Regexes.SET_FACTION.getGroup(message, "factionName");
        this.getUser().setFaction(Faction.valueOf(factionName.toUpperCase()));
        this.getUser().getDeck().clear();
    }

    private void handleSetDeckRequest(String message) throws JsonProcessingException {
        System.out.println("we are trying to update");
        ArrayList<String> arrayList = new ObjectMapper().readValue(Regexes.SET_DECK.getGroup(message, "deckJson"), ArrayList.class);
        getUser().getDeck().clear();
        for (String cardName : arrayList) {
            getUser().addToDeck(CardController.createCardWithName(cardName));
        }
    }

    private void handleChangeLeaderRequest(String message) {
        String leaderName = Regexes.CHANGE_LEADER_REQUEST.getGroup(message, "leaderName");
        Card card = CardController.createCardWithName(leaderName);
        getUser().setLeader((Leader) card);
        System.out.println("[INFO]: " + user.getName() + " changed its leader to " + user.getLeader().getName());
    }

    private String toJsonStringRequestsArrayList(List<FriendRequest> friendRequests) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            return mapper.writeValueAsString(friendRequests);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendFactionForShowLeaderRequest() throws IOException {
        String factionJson = (new ObjectMapper()).writeValueAsString(user.getFaction());
        sendMessage("showLeader " + factionJson);
    }

    private void handleAddToDeckRequest(String cardName) throws Exception {
        String result = PreGameMenuController.addToDeck(cardName, user);
//        if (result.startsWith("[SUCC]")) user.addToDeck(CardController.createCardWithName(cardName));
        sendMessage("addToDeckResult " + result);
    }

    private void sendShowCurrentUserInfo() throws IOException {
        sendMessage("showCurrentUserInfo " + user.getName() + " " + user.getFaction().name() + " " + user.getDeck().size() + " " + user.getUnitCount() + " " + user.getSpecialCount()
                + " " + user.getHeroCount() + " " + user.getSumOfPower());
    }

    private void sendShowDeckCommand() throws IOException {
        ArrayList<String> cardNames = new ArrayList<>();
        for (Card card : user.getDeck()) {
            cardNames.add(card.getName());
        }

        Gson gson = new Gson();
        String json = gson.toJson(cardNames);

        sendMessage("showManyCardsInScrollBar " + json + " true " + gson.toJson(convertToTheirName(user.getDeck())));
    }

    private ArrayList<String> convertToTheirName(ArrayList<Card> arrayList) {
        ArrayList<String> result = new ArrayList<>();
        for (Card card : arrayList) {
            result.add(card.getName());
        }
        return result;
    }

    private void sendShowCardCommand() throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        result.addAll(CardController.units);
        result.addAll(CardController.specials);
        result.addAll(CardController.heroes);
        for (String cardName : result) {
            Faction faction = CardController.faction.getOrDefault(cardName, Faction.ALL);
            if (faction.equals(user.getFaction())
                    || faction.equals(Faction.ALL)) {
                arrayList.add(cardName);
            }


        }


        Gson gson = new Gson();
        String json = gson.toJson(arrayList);

        sendMessage("showManyCardsInScrollBar " + json + " false " + gson.toJson(convertToTheirName(user.getDeck())));
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

        User inviter = User.getUserByUsername(inviterName);

        if (inviter == null) {
            System.out.println("[ERR]: user \"" + inviterName + "\" not found");
            sendMessage("[ERR]: there is no user \"" + inviterName + "\"");
            return;
        }

        ServerController.denyInvitation(this.getUser(), inviter);
    }

    private void denyFriendRequest(Matcher matcher) throws IOException {
        String requesterName = matcher.group(1);

        User requester = User.getUserByUsername(requesterName);
        if (requester == null) {
            System.out.println("[ERR] user \"" + requesterName + "\" not found");
            sendMessage("[ERR] there is no user \"" + requesterName + "\"");
            return;
        }

        ServerController.denyFriendRequest(this.getUser(), requester);
    }

    private void cancelFriendRequest(Matcher matcher) throws IOException {
        String recipientName = matcher.group(1);

        User recipient = UserController.getUserByName(recipientName);
        if (recipient == null) {
            System.out.println("[ERR] user \"" + recipientName + "\" not found");
            sendMessage("[ERR] there is no user \"" + recipientName + "\"");
            return;
        }

        ServerController.cancelFriendRequest(this.getUser(), recipient);
    }

    private void showFriends() throws IOException {
        UserController.showFriends(this.getUser());
    }

    private void acceptFriendRequest(Matcher matcher) throws IOException {
        String requesterName = matcher.group(1);

        User requester = User.getUserByUsername(requesterName);
        if (requester == null) {
            System.out.println("[ERR] user \"" + requesterName + "\" not found");
            sendMessage("[ERR] there is no user \"" + requesterName + "\"");
            return;
        }

        ServerController.acceptFriendRequest(this.getUser(), requester);
    }

    private void friendRequest(Matcher matcher) throws IOException {
        String recipientName = matcher.group(1);

        User recipient = User.getUserByUsername(recipientName);
        if (recipient == null) {
            System.out.println("[ERR] user \"" + recipientName + "\" not found");
            sendMessage("[ERR] there is no user \"" + recipientName + "\"");
            return;
        }

        if (this.getUser().equals(recipient)) {
            sendMessage("[ERR] you can not send friend request to yourself.");
            return;
        }

        if (this.getUser().friendsWith(recipient)) {
            sendMessage("[ERR] you are already " + recipientName + "'s friend");
            return;
        }

        try {
            ServerController.createNewFriendRequest(this.getUser(), recipient);
        } catch (SimilarRequest e) {
            System.out.println("[ERR] similar friend request");
            sendMessage("[ERR] similar friend request");
        }
    }

    private void cancelInvitation() throws IOException {
        ServerController.cancelInvitation(this.getUser());
    }

    private void acceptGame(Matcher matcher) throws IOException {
        String oppName = matcher.group(1);

        User inviter = User.getUserByUsername(oppName);
        if (inviter == null) {
            System.out.println("[ERR] user \"" + oppName + "\" not found");
            sendMessage("[ERR] there is no user \"" + oppName + "\"");
            return;
        }

        if (inviter.isOffline()) {
            System.out.println("[ERR] user \"" + oppName + "\" is offline");
            sendMessage("[ERR] user \"" + oppName + "\" is offline");
            return;
        }

        ServerController.acceptGame(this.getUser(), inviter);
    }

    private void invitation(Matcher matcher) throws IOException {
        String receiverName = matcher.group(1);

        User user = User.getUserByUsername(receiverName);
        if (user == null) {
            System.out.println("[ERR] user \"" + receiverName + "\" not found");
            sendMessage("[ERR] there is no user \"" + receiverName + "\"");
            return;
        }

        if (user.isOffline()) {
            System.out.println("[ERR] user \"" + receiverName + "\" is offline");
            sendMessage("[ERR] user \"" + receiverName + "\" is offline");
            return;
        }

        if (user.equals(this.getUser())) {
            sendMessage("[ERR] you can not invite yourself.");
            return;
        }

        try {
            ServerController.createNewInvitation(this.getUser(), user);
        } catch (SimilarRequest e) {
            System.out.println("[ERR] similar invitation");
            sendMessage("[ERR] similar invitation");
        }
    }

    public void sendMessage(Object message) throws IOException {
        out.writeObject(message);
        out.flush();
        out.reset();
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

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
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
