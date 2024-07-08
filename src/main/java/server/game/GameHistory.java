package server.game;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import server.Account.User;

import java.util.ArrayList;

public class GameHistory {

    private final User opponent;
    private final User winner;
    private final String dateFormattedString;
    private final ArrayList<StateAfterADiamond> roundsInformations;

    @JsonCreator
    public GameHistory(
            @JsonProperty("opponent") User opponent,
            @JsonProperty("winner") User winner,
            @JsonProperty("dateFormattedString") String date,
            @JsonProperty("roundsInformations") ArrayList<StateAfterADiamond> roundsInformation) {
        this.winner = winner;
        this.opponent = opponent;
        this.dateFormattedString = date;
        this.roundsInformations = roundsInformation;
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

    public ArrayList<StateAfterADiamond> getRoundsInformations() {
        return roundsInformations;
    }

    public int getWinnerTotalScore(){
        int point = 0;
        for (StateAfterADiamond stateAfterADiamond : roundsInformations){
            point += stateAfterADiamond.winnerScore();
        }

        return point;
    }

    public int getLoserTotalScore(){
        int point = 0;
        for (StateAfterADiamond stateAfterADiamond : roundsInformations){
            point += stateAfterADiamond.looserScore();
        }
        return point;
    }
}
