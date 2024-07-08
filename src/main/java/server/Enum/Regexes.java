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
    GET_USER("user: (?<user>.+)"),
    SHOW_MANY_CARDS("showManyCardsInScrollBar (?<list>.+) (?<boolean>true|false) (?<deck>.+)"),
    ADD_TO_DECK("addToDeck (?<cardName>.+)"),
    REMOVE_FROM_DECK_REQUEST("removeFromDeck (?<cardName>.+)"),
    SHOW_LEADER("showLeader (?<factionJson>.+)"),
    CHANGE_LEADER_REQUEST("change leader (?<leaderName>.+)"),
    DOWNLOAD_DECK("download deck (?<deckJson>.+)"),
    SET_DECK("set deck (?<deckJson>.+)"),
    SHOW_FACTION_RESULT("show factions (?<factionJson>.+)"),
    SET_FACTION("set faction (?<factionName>.+)"),
    SEND_DATA("winner(?<winnerUsername>.+)winnerScores(?<winnerScores>.+)looser(?<loserUsername>.+)looserScores(?<looserScores>.+)total(?<winnerTotal>.+)total(?<looserTotal>.+)"),
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

    ECHO("^echo (.+)$"),

    GET_USERNAME("^give username$"),

    GET_EMAIL("^give email$"),

    GET_NICKNAME("^give nickname$"),

    GET_GAMES_PLAYED("^give gamesplayed$"),

    GET_LOSSES("^give losses$"),

    GET_WINS("^give wins$"),

    GET_TIE("^give tie$"),

    GET_RANK("^give rank$"),

    GET_MAX_SCORE("^give maxscore$"),

    CHANGE_PASSWORD_PROFILEMENU("^change password (?<password>.+) (?<oldPassword>.+)$"),

    CHANGE_NICKNAME("^change nickname (?<newNickname>.*)$"),

    CHANGE_USERNAME("^change username (?<newUsername>.*)$"),

    CHANGE_EMAIL("^change email (?<newEmail>.*)$"),

    LOGOUT("^logout$"),

    GET_GAME_HISTORIES("^get game histories$"),

    GET_FRIENDS("^get friends$"),

    GET_REQUESTS("^get requests$"),

    SEND_FRIEND_REQUEST("^send friend request -u (?<username>.*)$"),

    GET_INVITES("^get invites$")
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

    public String getGroup(String input, int group) {
        return getMatcher(input).group(group);
    }

    public String getGroup(String input, String group) {
        return getMatcher(input).group(group);
    }
}