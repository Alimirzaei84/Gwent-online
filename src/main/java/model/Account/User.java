package model.Account;

import controller.ApplicationController;
import controller.CardController;
import model.game.Game;
import model.game.GameHistory;
import model.role.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class User implements Comparable<User> {

    private ArrayList<GameHistory> gameHistories;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private int rank; //TODO : UPDATE AFTER EACH GAME
    private int highestScore;
    private int ties;
    private int wins;
    private int losses;
    private int gamesPlayed;
    private Faction faction;
    private ArrayList<Card> deck;
    private Leader leader;
    private static final ArrayList<User> allUsers = new ArrayList<>();
    private static User loggedInUser = null;


    // mapping questions to answers
    private HashMap<String, String> answers;

    public User(String username, String password, String email, String nickname) {
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
    }

    public void addToHistory(GameHistory gameHistory){
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
        Random rand = new Random();
        String leaderName = CardController.leaders.get(rand.nextInt(CardController.leaders.size()));
        return (Leader) CardController.createLeaderCard(leaderName);
    }

    public Leader getLeader() {
        return leader;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
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

    public int getRank() {
        return rank;
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

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static User getUserByUsername(String username) {
        for (User user : allUsers) {
            if (user.getName().equals(username)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }

    @Override
    public int compareTo(User o) {
        return 0;
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
            if (card.getName().equals(cardName))
                return card;
        }

        return null;
    }
}