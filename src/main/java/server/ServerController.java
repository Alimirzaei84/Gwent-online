package server;

import server.error.SimilarInvitation;

import java.io.IOException;
import java.util.ArrayList;

public abstract class ServerController {

    public static ArrayList<Invitation> invitations = new ArrayList<>();

    public static void createNewInvitation(User inviter, User receiver) throws IOException {
        Invitation invitation = new Invitation(inviter, receiver);

        if (existSimilarInvitation(invitation)) {
            throw new SimilarInvitation();
        }

        addInvitation(invitation);
        processInvitation(invitation);
    }

    private static void processInvitation(Invitation invitation) throws IOException {
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

        // start a new game
        inviter.sendMessage("[INFO] starting a game with " + recipient.getUsername() + ".");
        recipient.sendMessage("[INFO] starting a game with " + inviter.getUsername() + ".");


    }

    private static Invitation getInvitationByMember(User inviter, User recipient) {
        for (Invitation i : invitations) {
            if (i.getInviter().equals(inviter) && i.getRecipient().equals(recipient))
                return i;
        }

        return null;
    }
}
