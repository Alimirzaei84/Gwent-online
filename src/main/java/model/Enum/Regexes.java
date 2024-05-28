package model.Enum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Regexes
{
    VALID_USERNAME("[a-zA-Z\\d\\-]"),
    VALID_EMAIL("^[a-zA-Z\\d]+@[a-zA-Z]+\\.com$"),
    VALID_PASSWORD("^[a-zA-Z\\d!@#$%^&*]+$"),
    CONTAINS_LOWERCASE("^.*[a-z].*$"),
    CONTAINS_UPPERCASE("^.*[A-Z].*$"),
    CONTAINS_NUMBERS("^.*\\d.*$"),
    MENU_NAME("^show\\s+current\\s+menu$"),
    EXIT("^exit$"),
    REGISTER("^\\s*register\\s+username\\s+(?<username>\\S+)\\s+password\\s+(?<password>\\S+)\\s+email\\s+(?<email>\\S+)\\s*$"),
    //Check for register
    CONTAINS_SPECIAL_CHARACTERS("(?=.*[@#$^&!]).*"),
    START_WITH_LETTER("^[A-Za-z]+.*"),
    EMAIL_END_CHECKER("^\\S+@[a-z]+\\.com$"),
    EMAIL_ADDRESS_CHECKER("^[a-zA-Z\\d\\.]+@[a-z]+\\.com$"),
    LOGIN("^\\s*login\\s+username\\s+(?<username>\\S+)\\s+password\\s+(?<password>\\S+)\\s*$"),
    /*----Main Menu----*/
    NEW_GAME("^start new game with (?<username>\\S+)$"),
    /*----Shop Menu----*/
    BUY_CARD("^buy card (?<cardName>\\S+)$"),
    SELL_CARD("^sell card (?<cardName>\\S+)$"),
    /*----Profile menu-----*/
    ADD_TO_DECK("^equip card (?<cardName>\\S+) to my deck$"),
    REMOVE_FROM_DECK("^unequip card (?<cardName>\\S+) from my deck$"),
    /*-----Game menu------*/
    SHOW_MY_INFO("^\\s*show\\s+my\\s+info\\s+(?<placeNumber>-?\\d+)\\s*$"),
    SHOW_ENEMY_INFO("^\\s*show\\s+enemy\\s+info\\s+(?<placeNumber>-?\\d+)\\s*$"),
    PUT_CARD("^\\s*put\\s+card\\s+(?<cardName>\\S+)\\s+to\\s+(?<placeNumber>-?\\d+)\\s*$"),
    SWAP_WITH_ACTIVE("^\\s*substitute\\s+active\\s+card\\s+with\\s+bench\\s+(?<benchNumber>-?\\d+)\\s*$"),
    EXECUTE_ACTION("^\\s*execute\\s+action(\\s+-t\\s+(?<target>-?\\d+))?\\s*$"),
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
