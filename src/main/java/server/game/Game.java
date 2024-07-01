package server.game;

import server.Chatroom;
import server.Enum.GameRegexes;
import server.User;

import java.io.IOException;

public class Game implements Runnable {

    private static int gameCounter = 0;

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
    private final Chatroom chatroom;
    private final int id;

    private boolean running;
    private boolean isPlayerListening;

    private GameCommunicationHandler handler;

    public Game(User user1, User user2, AccessType accessType) {
        users = new User[]{user1, user2};
        this.accessType = accessType;
        chatroom = new Chatroom();
        id = gameCounter++;
    }

    @Override
    public void run() {

        // start communication handler
        handler = new GameCommunicationHandler(this);
        Thread handlerThread = new Thread(handler);
        handlerThread.start();


        running = true;
        isPlayerListening = true;

        try {
            broadcast("game id:" + this.getId());

            while (running) {
                waitUntilPlayersAreListening();
                getUser1().sendMessage("start turn");
                isPlayerListening = false;

                waitUntilPlayersAreListening();
                getUser2().sendMessage("start turn");
                isPlayerListening = false;
            }

        } catch(IOException e){
            e.printStackTrace();
            return;
        }
    }

    public String handleCommand(String command) throws IOException {
        return handler.handleCommand(command);
    }


    private void waitUntilPlayersAreListening() {

        while (!isPlayerListening) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }

    }

    protected boolean isGameListening() {
        return !isPlayerListening;
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

    public Chatroom getChatroom() {
        return chatroom;
    }

    public int getId() {
        return id;
    }

    public void attendUserAsViewer(User user) throws IOException {
        // TODO maybe you should not attend user as viewer when the game is not running
        user.sendMessage("you will watch game id:" + getId() + " online.");
        chatroom.addAttendee(user);
    }

    protected void setPlayerListening() {
        isPlayerListening = true;
    }

    public void broadcast(String message) throws IOException {
        getUser1().sendMessage(message);
        getUser2().sendMessage(message);
        getChatroom().broadcast(message);
    }
}
