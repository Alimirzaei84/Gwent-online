package server.Enum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Regexes {
    FAVORITE_COLOR("favorite color (?<color>.+)"),
    FAVORITE_MONTH("favorite month (?<month>.+)"),
    FAVORITE_FOOD("favorite food (?<food>.+)"),
    LOGIN("login (?<username>.+) (?<password>.+)"),
    FORGET_PASSWORD("forgetPassword (?<username>.+)"),
    CHANGE_PASSWORD("changePassword (?<food>.+) (?<color>.+) (?<month>.+) (?<newPassword>.+)"),

    REGISTER("register (?<username>.+) (?<password>.+) (?<passwordAgain>.+) (?<nickname>.+) (?<email>.+)"),

    VALID_USERNAME("[a-zA-Z\\d\\-]+"),

    CHOOSE_CARD("choose card"),

    START_TURN("start turn"),

    PUT_CARD("put card (?<cardName>.+)"),

    PASS_ROUND("passed"),

    A_USER_PUT_CARD("(?<username>.+) put card (?<cardName>.+)"),

    VETO_A_CARD("veto (?<cardIndex>\\d+)"),

    VETO_COMPLETED(".+'s has been completed"),

    PLACE_CARD("place card (?<cardName>.+) (?<rowNumber>\\d+)"),

    A_USER_PLACED_A_CARD("(?<username>.+) place card (?<cardName>.+) (?<rowNumber>\\d+)"),

    JSON_OF_ROWS("(?<username>.+)Row0(?<json0>.+)Row1(?<json1>.+)Row2(?<json2>.+)"),

    PLAY_LEADER("leader(?<username>.+\\|(?<leaderName>.+))"),

    EXIT("^exit$"),

    SHOW_HAND("^show hand$"),

    ECHO("^echo (.+)$");


    final String regex;

    Regexes(String regex) {
        this.regex = regex;
    }

    public Matcher getMatcher(String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        return matcher;
    }

    public boolean matches(String input) {
        return input.matches(regex);
    }

    public String getGroup(String input, int group) {
        return getMatcher(input).group(group);
    }

    public String getGroup(String input, String group) {
        return getMatcher(input).group(group);
    }
}