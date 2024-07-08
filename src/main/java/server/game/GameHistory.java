package server.game;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import server.Account.User;

import java.util.ArrayList;

public class GameHistory {

    private final User opponent;
    private final User winner;
    private final String dateFormattedString;
    private final ArrayList<StateAfterADiamond> roundsInformation;

    @JsonCreator
    public GameHistory(
            @JsonProperty("opponent") User opponent,
            @JsonProperty("winner") User winner,
            @JsonProperty("dateFormattedString") String date,
            @JsonProperty("roundsInformations") ArrayList<StateAfterADiamond> roundsInformation) {
        this.winner = winner;
        this.opponent = opponent;
        this.dateFormattedString = date;
        this.roundsInformation = roundsInformation;
    }

    public User getOpponent() {
        return opponent;
    }

    public User getWinner() {
        return winner;
    }

    public String getDateFormattedString() {
        return dateFormattedString;
    }

    public ArrayList<StateAfterADiamond> getRoundsInformation() {
        return roundsInformation;
    }

    public int getWinnerTotalScore() {
        int point = 0;
        for (StateAfterADiamond stateAfterADiamond : roundsInformation) {
            point += stateAfterADiamond.winnerScore();
        }

        return point;
    }

    public int getLoserTotalScore() {
        int point = 0;
        for (StateAfterADiamond stateAfterADiamond : roundsInformation) {
            point += stateAfterADiamond.looserScore();
        }
        return point;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("winner").append(winner.getUsername()).append("winnerScores");
        int winnerScore = 0;
        for (StateAfterADiamond state : roundsInformation) {
            builder.append(state.winnerScore()).append(" ");
            winnerScore += state.winnerScore();
        }

        int looserScore = 0;
        builder.append("looser").append(opponent.getUsername()).append("looserScores");
        for (StateAfterADiamond state : roundsInformation) {
            builder.append(state.looserScore()).append(" ");
            looserScore += state.looserScore();
        }

        builder.append("total").append(winnerScore);
        builder.append("total").append(looserScore);



        return builder.toString();
    }
}
