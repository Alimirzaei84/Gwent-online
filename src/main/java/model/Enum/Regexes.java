package model.Enum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Regexes {
    VALID_USERNAME("[a-zA-Z\\d\\-]+"),
    VALID_EMAIL("^[a-zA-Z\\d]+@[a-zA-Z]+\\.com$"),
    VALID_PASSWORD("^[a-zA-Z\\d!@#$%^&*]+$"),
    CONTAINS_LOWERCASE("^.*[a-z].*$"),
    CONTAINS_UPPERCASE("^.*[A-Z].*$"),
    CONTAINS_NUMBERS("^.*\\d.*$"),
    MENU_NAME("^show\\s+current\\s+menu$"),
    EXIT("^exit$"),
    ;


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

    public String getGroup(String input, String group) {
        return getMatcher(input).group(group);
    }
}