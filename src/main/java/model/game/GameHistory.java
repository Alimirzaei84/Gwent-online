package model.game;


import server.User;

import java.util.ArrayList;

public class GameHistory {

    private final User opponent;
    private final User winner;
    private final String dateFormattedString;
    private final ArrayList<StateAfterADiamond> roundsInformations;

    public GameHistory(User opponent, User winner, String date, ArrayList<StateAfterADiamond> roundsInformation) {
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
