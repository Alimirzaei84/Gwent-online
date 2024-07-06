package server.game;

import model.role.Card;
import model.role.Faction;
import model.role.Leader;
import server.Account.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private int numTurn;
    private Leader myLeader;
    private Leader opponentLeader;
    private String myUsername;
    private String opponentUsername;
    private Faction myFaction;
    private Faction opponentFaction;
    private Row[] myRows;
    private Row[] oppRows;
    private int myPoint;
    private int oppPoint;
    private ArrayList<Card> weatherArrayList;
    private ArrayList<Card> myHand;
    private ArrayList<Card> oppHand;
    private ArrayList<Card> myDeck;
    private ArrayList<Card> oppDeck;

    public Board() {

    }

    public Row[] getMyRows() {
        return myRows;
    }

    public void setMyRows(Row[] myRows) {
        this.myRows = myRows;
    }

    public Row[] getOppRows() {
        return oppRows;
    }

    public void setOppRows(Row[] oppRows) {
        this.oppRows = oppRows;
    }

    public int getMyPoint() {
        return myPoint;
    }


    public void setMyPoint(int myPoint) {
        this.myPoint = myPoint;
    }

    public int getOppPoint() {
        return oppPoint;
    }

    public void setOppPoint(int oppPoint) {
        this.oppPoint = oppPoint;
    }

    public ArrayList<Card> getWeatherArrayList() {
        return weatherArrayList;
    }

    public void setWeatherArrayList(ArrayList<Card> weatherArrayList) {
        this.weatherArrayList = weatherArrayList;
    }

    public ArrayList<Card> getMyHand() {
        return myHand;
    }

    public void setMyHand(ArrayList<Card> myHand) {
        this.myHand = myHand;
    }

    public ArrayList<Card> getOppHand() {
        return oppHand;
    }

    public void setOppHand(ArrayList<Card> oppHand) {
        this.oppHand = oppHand;
    }

    public ArrayList<Card> getMyDeck() {
        return myDeck;
    }

    public void setMyDeck(ArrayList<Card> myDeck) {
        this.myDeck = myDeck;
    }

    public ArrayList<Card> getOppDeck() {
        return oppDeck;
    }

    public void setOppDeck(ArrayList<Card> oppDeck) {
        this.oppDeck = oppDeck;
    }

    public Leader getMyLeader() {
        return myLeader;
    }

    public void setMyLeader(Leader myLeader) {
        this.myLeader = myLeader;
    }

    public Leader getOpponentLeader() {
        return opponentLeader;
    }

    public void setOpponentLeader(Leader opponentLeader) {
        this.opponentLeader = opponentLeader;
    }

    public String getMyUsername() {
        return myUsername;
    }

    public void setMyUsername(String myUsername) {
        this.myUsername = myUsername;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }

    public Faction getMyFaction() {
        return myFaction;
    }

    public void setMyFaction(Faction myFaction) {
        this.myFaction = myFaction;
    }

    public Faction getOpponentFaction() {
        return opponentFaction;
    }

    public void setOpponentFaction(Faction opponentFaction) {
        this.opponentFaction = opponentFaction;
    }

    public int getNumTurn() {
        return numTurn;
    }

    public void setNumTurn(int numTurn) {
        this.numTurn = numTurn;
    }
}
