package server;

public class Game implements Runnable {

    public enum AccessType {
        PRIVATE(0),
        PUBLIC(1);

        private final int index;
        AccessType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }


    private final User[] users;
    private final AccessType accessType;

    public Game(User user1, User user2, AccessType accessType) {
        users = new User[]{user1, user2};
        this.accessType = accessType;
    }

    @Override
    public void run() {

    }

    public User[] getUsers() {
        return users;
    }

    public User getUser1() {
        return users[0];
    }

    public User getUser2() {
        return users[1];
    }

    public AccessType getAccessType() {
        return accessType;
    }
}
