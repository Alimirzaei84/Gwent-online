package server.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.User;
import java.util.Objects;

public class FriendRequest {

    private User requester;
    private User recipient;

//    public FriendRequest() {
//        // Default constructor needed by Jackson
//    }

    public FriendRequest(@JsonProperty User requester,
                         @JsonProperty User recipient) {
        this.requester = requester;
        this.recipient = recipient;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
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
