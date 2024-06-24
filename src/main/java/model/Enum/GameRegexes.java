package model.Enum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameRegexes {
    VALID_USERNAME("[a-zA-Z\\d\\-]+"),
    CHOOSE_CARD("choose card"),
    START_TURN("start turn"),
    PUT_CARD("put card (?<cardName>.+)"),
    PASS_ROUND("passed"),
    A_USER_PUT_CARD("(?<username>.+) put card (?<cardName>.+)"),
    EXIT("^exit$"),
    ;


    final String regex;

    GameRegexes(String regex) {
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

    public String getGroup(String input, String group) {
        return getMatcher(input).group(group);
    }
}
