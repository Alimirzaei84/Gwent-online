package model.Account;

import java.util.ArrayList;
import java.util.HashMap;

public class User implements Comparable<User> {

    private String username;
    private String password;
    private String email;
    private String nickname;
    private static final ArrayList<User> allUsers = new ArrayList<>();
    private static User loggedInUser = null;



    // mapping questions to answers
    private HashMap<String, String> answers;

    public User(String username, String password, String email, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        allUsers.add(this);
        answers = new HashMap<>();
    }

    public void addQuestionAnswer(String question, String answer) throws Exception {
        if (question == null || answer == null) {
            throw new Exception("Question or answer is null");
        }

        answers.put(question, answer);
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
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

    public static void setLoggedInUser(User user){
        loggedInUser = user;
    }

    public static User getLoggedInUser(){
        return loggedInUser;
    }

    public static User getUserByUsername(String username){
        for (User user : allUsers){
            if (user.getName().equals(username)){
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