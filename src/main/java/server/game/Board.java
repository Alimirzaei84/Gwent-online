package server.game;

import model.role.Card;
import model.role.Faction;
import model.role.Leader;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private int numTurn;
    private boolean isMyTurn;
    private int myDiamondCount;
    private int opponentDiamondCount;
    private ArrayList<Card> myDiscardPile;
    private ArrayList<Card> opponentDiscardPile;
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


    public Row[] getMyRows() {
        return myRows;
    }

    public ArrayList<Card> getMyDiscardPile() {
        return myDiscardPile;
    }

    public void setMyDiscardPile(ArrayList<Card> myDiscardPile) {
        this.myDiscardPile = myDiscardPile;
    }

    public ArrayList<Card> getOpponentDiscardPile() {
        return opponentDiscardPile;
    }

    public void setOpponentDiscardPile(ArrayList<Card> opponentDiscardPile) {
        this.opponentDiscardPile = opponentDiscardPile;
    }

    public void setMyRows(Row[] myRows) {
        this.myRows = myRows;
    }

    public Row[] getOppRows() {
        return oppRows;
    }

    public int getMyDiamondCount() {
        return myDiamondCount;
    }

    public void setMyDiamondCount(int myDiamondCount) {
        this.myDiamondCount = myDiamondCount;
    }

    public int getOpponentDiamondCount() {
        return opponentDiamondCount;
    }

    public void setOpponentDiamondCount(int opponentDiamondCount) {
        this.opponentDiamondCount = opponentDiamondCount;
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

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }
}
