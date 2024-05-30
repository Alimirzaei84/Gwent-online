package model.Account;

import java.util.HashMap;

public class User implements Comparable<User> {

    private String name;
    private String password;
    private String email;
    private String nickname;
    private int rank; //TODO : UPDATE AFTER EACH GAME
    private int highestScore;
    private int ties;
    private int wins;
    private int losses;
    private int gamesPlayed;
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static User loggedInUser = null;


    // mapping questions to answers
    private HashMap<String, String> answers;

    public User(String name, String password, String email, String nickname) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        highestScore = 0;
        ties = 0;
        wins = 0;
        losses = 0;
        gamesPlayed = 0;
        allUsers.add(this);
    }

    public void addQuestionAnswer(String question, String answer) throws Exception {
        if (question == null || answer == null) {
            throw new Exception("Question or answer is null");
        }

        if (answers.containsKey(question)) {
            throw new Exception("Question already answered");
        }

        answers.put(question, answer);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public int compareTo(User o) {
        return 0;
    }
}
