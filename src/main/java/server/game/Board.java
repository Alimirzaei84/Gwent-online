package server.game;

import model.role.Card;
import model.role.Weather;

import java.util.ArrayList;

public class Board {
    private Game game;
    private Row[] myRows;
    private Row[] oppRows;
    private int myPoint;
    private int oppPoint;
    private ArrayList<Card> weatherArrayList;
    private ArrayList<Card> myHand;
    private ArrayList<Card> oppHand;
    private ArrayList<Card> myDeck;
    private ArrayList<Card> oppDeck;

    public Board(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
}
