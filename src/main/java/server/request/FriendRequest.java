package server.request;

import server.User;

import java.util.Objects;

public class FriendRequest {

    private final User requester;
    private final User recipient;

    public FriendRequest(User requester, User recipient) {
        this.requester = requester;
        this.recipient = recipient;
    }

    public User getRecipient() {
        return recipient;
    }

    public User getRequester() {
        return requester;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        FriendRequest that = (FriendRequest) object;
        return Objects.equals(requester, that.requester) && Objects.equals(recipient, that.recipient) ||
                Objects.equals(requester, that.recipient) && Objects.equals(recipient, that.requester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requester, recipient);
    }
}
