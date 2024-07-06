package server.game;

import model.role.Card;
import server.Account.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private int numTurn;
    private Player currPlayer;
    private Player oppPlayer;
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

    public int getNumTurn() {
        return numTurn;
    }

    public void setNumTurn(int numTurn) {
        this.numTurn = numTurn;
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

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }

    public Player getOppPlayer() {
        return oppPlayer;
    }

    public void setOppPlayer(Player oppPlayer) {
        this.oppPlayer = oppPlayer;
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
}
