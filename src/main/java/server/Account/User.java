package server.Account;

import com.fasterxml.jackson.annotation.JsonProperty;
import controller.ApplicationController;
import controller.CardController;
import model.role.*;
import server.CommunicationHandler;
import server.controller.EmailController;
import server.game.GameHistory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class User implements Serializable {

    public enum Status {
        PLAYING, OFFLINE, INVITING, VIEWING, ONLINE
    }

    private boolean isVerified;
    private final ArrayList<GameHistory> gameHistories;
    private final ArrayList<User> friends;

    private String email;
    private String nickname;
    private int rank;
    private int highestScore;
    private int ties;
    private int wins;
    private int losses;
    private int gamesPlayed;
    private Faction faction;
    private ArrayList<Card> deck;
    private Leader leader;
    private static final ArrayList<User> allUsers = new ArrayList<>();
    private static User loggedInUser;
    private transient CommunicationHandler handler;
    private String username;
    private String password;

    private Status status;

    private int gameId = -1; // -1 means home

    private HashMap<String, String> answers;

    public User(@JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("email") String email,
                @JsonProperty("nickname") String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        faction = generateRandomFaction();
        highestScore = 0;
        gameHistories = new ArrayList<>();
        ties = 0;
        wins = 0;
        losses = 0;
        gamesPlayed = 0;
        answers = new HashMap<>();
        allUsers.add(this);
        deck = new ArrayList<>();
        leader = getRandomLeader();
        status = Status.OFFLINE;
        friends = new ArrayList<>();
        isVerified = false;
        EmailController.sendVerificationEmail(email);
    }

    public void addToHistory(GameHistory gameHistory) {
        gameHistories.add(gameHistory);
    }

    private Faction generateRandomFaction() {
        int x = ApplicationController.getRandom().nextInt(0, 4);
        switch (x) {
            case 0 -> {
                return Faction.NORTHERN_REALMS;
            }
            case 1 -> {
                return Faction.NILFGAARDIAN_EMPIRE;
            }
            case 2 -> {
                return Faction.MONSTERS;
            }
            case 3 -> {
                return Faction.SCOIA_TAEL;
            }
            default -> {
                return Faction.SKELLIGE;
            }
        }
    }


    private Leader getRandomLeader() {
        String leaderName = CardController.leaders.get(ApplicationController.getRandom().nextInt(0, CardController.leaders.size()));
        return (Leader) CardController.createLeaderCard(leaderName);
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified() {
        isVerified = true;
    }

    public Leader getLeader() {
        return leader;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
        System.out.println(leader.getName());
    }

    public void addQuestionAnswer(String question, String answer) throws Exception {
        if (question == null || answer == null) {
            throw new Exception("Question or answer is null");
        }

        answers.put(question, answer);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }


    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getTies() {
        return ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public String getEmail() {
        return email;
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public HashMap<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<String, String> answers) {
        this.answers = answers;
    }

    public static User getUserByUsername(String username) {

        for (User user : allUsers)
            if (user.getName().equals(username)) return user;

        return null;
    }

    public ArrayList<GameHistory> getGameHistories() {
        return gameHistories;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }

    public int getUnitCount() {
        int out = 0;
        for (Card card : deck) {
            if (card instanceof Unit) out++;
        }
        return out;
    }

    public int getSpecialCount() {
        int out = 0;
        for (Card card : deck) {
            if (card instanceof Special) out++;
        }
        return out;
    }

    public int getCardCount(String cardName) {
        int out = 0;
        for (Card card : deck) {
            if (card.getName().equals(cardName)) out++;
        }
        return out;
    }

    public int getHeroCount() {
        int out = 0;
        for (Card card : deck) {
            if (card instanceof Hero) out++;
        }
        return out;
    }

    public long getSumOfPower() {
        long result = 0;
        for (Card card : deck) {
            result += card.getPower();
        }
        return result;
    }

    public ArrayList<Card> getRandomDeck() {
        ArrayList<Card> deck = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < 22; i++) {
            String cardName = CardController.units.get(random.nextInt(CardController.units.size()));
            deck.add(CardController.createUnitCard(cardName));
        }

        return deck;
    }


    public Card getCardFromDeckByName(String cardName) {
        for (Card card : deck) {
            if (card.getName().equals(cardName)) return card;
        }

        return null;
    }

    public CommunicationHandler getHandler() {
        return handler;
    }


    public boolean authenticate(String username, String password) {
        return this.getUsername().equals(username) && this.password.equals(password);
    }

    public boolean isPlaying() {
        return status.equals(Status.PLAYING);
    }

    public boolean isViewing() {
        return status.equals(Status.VIEWING);
    }

    public boolean isInviting() {
        return status.equals(Status.INVITING);
    }

    public boolean isJustOnline() {
        return status.equals(Status.ONLINE);
    }

    public void setPlaying() {
        status = Status.PLAYING;
    }

    public void getOnline(CommunicationHandler handler) {
        status = Status.ONLINE;
        setHandler(handler);
    }

    public void getOffline() {
        status = Status.OFFLINE;
        this.handler = null;
    }

    public boolean isOffline() {
        return status.equals(Status.OFFLINE);
    }

    public boolean isOnline() {
        return !isOffline();
    }

    public void announceInvitation(User inviter) throws IOException {
        handler.sendMessage("you have been invited to a match by " + inviter.getUsername());
    }

    public void announceFriendRequest(User requester) throws IOException {
        handler.sendMessage("you have been given friend request by " + requester.getUsername());
    }

    public void setHandler(CommunicationHandler handler) {
        this.handler = handler;
    }

    public void sendMessage(Object message) throws IOException {
        getHandler().sendMessage(message);
    }


    public void addFriend(User friend) {
        friends.add(friend);
    }

    public boolean friendsWith(User user) {
        return friends.contains(user);
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setInviting() {
        status = Status.INVITING;
    }

    public void setViewing() {
        status = Status.VIEWING;
    }

    public Status getStatus() {
        return status;
    }

    public int getGameId() {
        return gameId;
    }


    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword());
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        User.loggedInUser = loggedInUser;
    }

    public int getRank() {
        allUsers.sort(Comparator.comparingInt(User::getWins).thenComparingInt(User::getHighestScore).thenComparingInt(User::getGamesPlayed).reversed());
        return allUsers.indexOf(this) + 1;
    }

    public void addToDeck(Card card) {
        deck.add(card);
    }

    public String getFriendsUsernames() {
        StringBuilder builder = new StringBuilder();
        builder.append("[FRIENDS]:");
        for (User user : friends) {
            builder.append(user.getUsername()).append("|");
        }

        return builder.toString();
    }

}
