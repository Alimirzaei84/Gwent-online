package server;

public class Invitation {

    private final User inviter;
    private final User recipient;

    Invitation(User inviter, User recipient) {
        this.inviter = inviter;
        this.recipient = recipient;
    }

    public User getInviter() {
        return inviter;
    }

    public User getRecipient() {
        return recipient;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        Invitation other = (Invitation) obj;

        return (inviter.equals(other.inviter) && recipient.equals(other.recipient)) ||
                (inviter.equals(other.recipient) && recipient.equals(other.inviter));
    }
}
