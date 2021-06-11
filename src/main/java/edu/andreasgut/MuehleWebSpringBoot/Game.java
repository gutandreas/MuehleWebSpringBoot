package edu.andreasgut.MuehleWebSpringBoot;

import java.time.LocalTime;
import java.util.ArrayList;

public class Game {

    private Player player0;
    private Player player1;
    private boolean gameComplete;
    private Player winner;
    private int round;
    private final int NUMBEROFSTONES = 9;
    private final Board board;
    private boolean putPhase = true;
    private boolean movePhase = false;
    private int currentPlayerIndex;

    private ArrayList<Player> playerArrayList = new ArrayList<>();

    public Game(Player player0, Player player1, int startPlayerIndex) {
        this.player0 = player0;
        this.player1 = player1;
        playerArrayList.add(0, player0);
        playerArrayList.add(1, player1);
        round = 0;
        currentPlayerIndex = 0;
        startPlayerIndex = startPlayerIndex;
        gameComplete = true;
        this.board = new Board(this);
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Game mit 2 Spielern ("+ player0.getName() + "/" + player1.getName() + ") wurde erstellt");
    }

    public Game(Player player0) {
        this.player0 = player0;
        gameComplete = false;
        playerArrayList.add(0, player0);
        this.board = new Board(this);
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Game mit 1 Spieler ("+ player0.getName() +") wurde erstellt");
    }

    public Player getPlayer0() {
        return player0;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
        playerArrayList.add(1, player1);
        gameComplete = true;
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": " + player0.getName() + " und "
                + player1.getName() + " bilden ein komplettes Game");
    }

    public void increaseRound(){
        round++;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayerArrayList() {
        return playerArrayList;
    }

    public int getNUMBEROFSTONES() {
        return NUMBEROFSTONES;
    }

    public boolean isGameComplete() {
        return gameComplete;
    }



    public Player getPlayerByIndex(int playerIndex){
        return getPlayerArrayList().get(playerIndex);
    }

    public int getRound() {
        return round;
    }

    public int getPlayerIndexByUuid(String uuid){

        if (getPlayerArrayList().get(0).getUuid().equals(uuid)){
            return 0;
        }
        else {
            return 1;
        }
    }

    public Player getPlayerByUuid(String ownUuid){
        if (getPlayerArrayList().get(0).getUuid().equals(ownUuid)){
            return getPlayerArrayList().get(0);
        }
        else {
            return getPlayerArrayList().get(1);
        }
    }

    public Player getOtherPlayerByOwnUuid(String ownUuid){
        if (getPlayerArrayList().get(0).getUuid().equals(ownUuid)){
            return getPlayerArrayList().get(1);
        }
        else {
            return getPlayerArrayList().get(0);
        }
    }


}
