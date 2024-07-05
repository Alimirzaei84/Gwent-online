package server.controller;

import client.In;
import server.Chatroom;
import server.game.Game;
import server.User;
import server.error.SimilarRequest;
import server.request.FriendRequest;
import server.request.Invitation;

import java.io.IOException;
import java.util.ArrayList;

public abstract class ServerController {

    public static ArrayList<Invitation> invitations = new ArrayList<>();
    public static ArrayList<FriendRequest> friendRequests = new ArrayList<>();
    public static ArrayList<Game> runningGames = new ArrayList<>();

    public static void passMessageToChatRoom(User user, String message) throws IOException {
        int gameId = user.getGameId();

        Game game = runningGames.get(gameId);
        if (game == null) {
            user.sendMessage("[ERROR] game id is wrong.");
            return;
        }

        game.getChatroom().handleCommand(user, message);
    }

    public static void passMessageToGameOfUser(User user, String message) throws IOException {
        int gameId = user.getGameId();

        Game game = getRunningGameById(gameId);
        if (game == null) {
            user.sendMessage("[ERROR] id is wrong");
            return;
        }

        game.handleCommand(message);
    }

    public static void attendNewViewerToRunningGame(User user, int gameId) throws IOException {
        Game game = getRunningGameById(gameId);

        if (game == null) {
            user.sendMessage("[ERROR] can't find the game with id " + gameId);
            return;
        }

        user.setViewing();
        user.setGameId(gameId);

        game.attendUserAsViewer(user);
    }

    public static void createNewFriendRequest(User requester, User recipient) throws IOException {
        FriendRequest friendRequest = new FriendRequest(requester, recipient);

        if (existSimilarFriendRequest(friendRequest)) {
            throw new SimilarRequest();
        }

        addFriendRequest(friendRequest);
        announceFriendRequest(friendRequest);
    }

    private static void announceFriendRequest(FriendRequest friendRequest) throws IOException {
        User recipient = friendRequest.getRecipient();
        recipient.announceFriendRequest(friendRequest.getRequester());
    }

    private static void addFriendRequest(FriendRequest friendRequest) {
        friendRequests.add(friendRequest);
    }

    private static boolean existSimilarFriendRequest(FriendRequest friendRequest) {
        for (FriendRequest f : friendRequests) {
            if (f.equals(friendRequest))
                return true;
        }
        return false;
    }

    public static void acceptFriendRequest(User recipient, User requester) throws IOException {
        FriendRequest request = getFriendRequestByMember(requester, recipient);

        if (request == null) {
            recipient.getHandler().sendMessage("[ERROR] you can't accept a friend request since you are not asked to be friend");
            return;
        }

        removeFriendRequest(request);

        recipient.sendMessage("[SUCCESS] you accepted a friend request");
        requester.sendMessage("[SUCCESS] your friend request has been accepted");

        requester.addFriend(recipient);
        recipient.addFriend(requester);
    }

    private static void removeFriendRequest(FriendRequest request) {
        friendRequests.remove(request);
    }

    public static void denyFriendRequest(User recipient, User requester) throws IOException {
        FriendRequest request = getFriendRequestByMember(requester, recipient);

        if (request == null) {
            recipient.sendMessage("[ERROR] you don't have an active friend request from " + requester.getUsername() + ".");
            return;
        }

        removeFriendRequest(request);
        recipient.sendMessage("[SUCC] the friend request from " + requester.getUsername() + " was denied.");
        requester.sendMessage("[INFO] " + recipient.getUsername() + " denied your friend request.");
        System.out.println("[INFO] " + recipient.getUsername() + "denied friend request from " + requester.getUsername() + ".");
    }

    public static void denyInvitation(User recipient, User inviter) throws IOException {
        Invitation invitation = getInvitationByMember(inviter, recipient);

        if (invitation == null) {
            recipient.sendMessage("[ERROR] you don't have an active invitation from " + inviter.getUsername());
            return;
        }

        removeInvitation(invitation);

        recipient.sendMessage("[SUCC] the invitation from " + inviter.getUsername() + " was denied.");
        inviter.sendMessage("[INFO] " + recipient.getUsername() + " denied your friend request.");
        System.out.println("[INFO] " + recipient.getUsername() + "denied friend request from " + inviter.getUsername() + ".");
    }

    public static void cancelFriendRequest(User requester, User recipient) throws IOException {
        FriendRequest request = getFriendRequestByMember(requester, recipient);

        if (request == null) {
            requester.sendMessage("[ERROR] you don't have an active friend request");
            return;
        }

        removeFriendRequest(request);

        System.out.println("[INFO] " + requester.getUsername() + " cancels his friend request");
        requester.sendMessage("[SUCC] your friend request has been cancelled");
        recipient.sendMessage("[INFO] " + requester.getUsername() + " has cancelled his friend request.");
    }

    private static FriendRequest getFriendRequestByMember(User requester, User recipient) {
        for (FriendRequest f : friendRequests) {
            if (f.getRequester().equals(requester) && f.getRecipient().equals(recipient))
                return f;
        }

        return null;
    }

    public static ArrayList<FriendRequest> getAUsersFriendRequests(User recipient) {
        ArrayList<FriendRequest> requests = new ArrayList<>();
        for (FriendRequest request : friendRequests) {
            if (request.getRecipient().equals(recipient))
                requests.add(request);
        }

        return requests;
    }

    public static void createNewInvitation(User inviter, User receiver) throws IOException {
        Invitation invitation = new Invitation(inviter, receiver);

        if (existSimilarInvitation(invitation)) {
            throw new SimilarRequest();
        }

        inviter.setInviting();

        addInvitation(invitation);
        announceInvitation(invitation);
    }

    private static void announceInvitation(Invitation invitation) throws IOException {
        User recipient = invitation.getRecipient();
        recipient.announceInvitation(invitation.getInviter());
    }

    private static void addInvitation(Invitation invitation) {
        invitations.add(invitation);
    }

    public static boolean existSimilarInvitation(Invitation invitation) {
        for (Invitation inv : invitations) {
            if (inv.equals(invitation))
                return true;
        }

        return false;
    }

    public static void acceptGame(User recipient, User inviter) throws IOException {
        Invitation invitation = getInvitationByMember(inviter, recipient);

        // there is no invitation from inviter
        if (invitation == null) {
            recipient.getHandler().sendMessage("[ERROR] you can't accept a game since you are not invited");
            return;
        }

        removeInvitation(invitation);

        // inviter is now play another game so his invitation now dismissed
        if (inviter.isPlaying()) {
            recipient.sendMessage("[ERROR] inviter has attended to another game");
            return;
        }

        inviter.sendMessage("[INFO] " + recipient.getUsername() + " accepted your invitation.");

        // start a new game
        startNewGame(inviter, recipient);
    }

    public static ArrayList<Invitation> getAUsersInvitations(User user) {
        ArrayList<Invitation> invites = new ArrayList<>();
        for (Invitation invite : invitations) {
            if (invite.getRecipient().getUsername().equals(user.getUsername()))
                invites.add(invite);
        }

        return invites;
    }

    private static Invitation getInvitationByMember(User inviter, User recipient) {
        for (Invitation i : invitations) {
            if (i.getInviter().equals(inviter) && i.getRecipient().equals(recipient))
                return i;
        }

        return null;
    }

    private static void removeInvitation(Invitation invitation) {
        invitations.remove(invitation);
    }

    private static void startNewGame(User user1, User user2) throws IOException {
        // TODO what are the restrictions for starting a game?

        user1.setPlaying();
        user2.setPlaying();

        user1.sendMessage("[INFO] starting a game with " + user2.getUsername() + ".");
        user2.sendMessage("[INFO] starting a game with " + user1.getUsername() + ".");

        // TODO for now every game is public
        Game game = new Game(user1, user2, Game.AccessType.PUBLIC);
        runningGames.add(game);

        user1.setGameId(game.getId());
        user2.setGameId(game.getId());

        Thread thread = new Thread(game);
        thread.start();
    }

    public static void cancelInvitation(User inviter) throws IOException {
        Invitation invitation = getInvitationByInviter(inviter);

        if (invitation == null) {
            System.out.println("[ERROR] " + inviter.getUsername() + " doesn't have any active invitation.");
            inviter.sendMessage("[ERROR] you don't have an active invitation.");
            return;
        }

        removeInvitation(invitation);

        System.out.println("[INFO] " + inviter.getUsername() + " cancels his invitation");
        inviter.sendMessage("[SUCC] your invitation has been cancelled");
        invitation.getRecipient().sendMessage("[INFO] " + inviter.getUsername() + " has cancelled his invitation.");
    }

    public static Invitation getInvitationByInviter(User inviter) {
        for (Invitation i : invitations) {
            if (i.getInviter().equals(inviter))
                return i;
        }

        return null;
    }

    public static Game getRunningGameById(int id) {
        for (Game game : runningGames) {
            if (game.getId() == id)
                return game;
        }

        return null;
    }
}
