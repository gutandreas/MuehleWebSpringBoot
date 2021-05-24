package edu.andreasgut.MuehleWebSpringBoot;

import java.time.LocalTime;
import java.util.ArrayList;

public class Game {

    private Player player0;
    private Player player1;
    private boolean gameComplete = false;
    private Player winner;
    private int round;
    private final int NUMBEROFSTONES = 9;
    private Player currentPlayer;
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
        currentPlayer=playerArrayList.get(0);
        currentPlayerIndex = 0;
        startPlayerIndex = startPlayerIndex;
        gameComplete = true;
        this.board = new Board(this);
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Game mit 2 Spielern ("+ player0.getName() + "/" + player1.getName() + ") wurde erstellt");
        play();
    }

    public Game(Player player0) {
        this.player0 = player0;
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
        gameComplete = true;
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": " + player0.getName() + " und "
                + player1.getName() + " bilden ein komplettes Game");
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayerArrayList() {
        return playerArrayList;
    }

    public boolean isGameComplete() {
        return gameComplete;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void play(){

        if (round>18){
            putPhase = false;
            movePhase = true;
        }


        if (putPhase){
            if (currentPlayer instanceof ComputerPlayer){
                board.putStone(currentPlayer.put(board, getCurrentPlayerIndex()),currentPlayerIndex);
            }

            if (currentPlayer instanceof HumanPlayer){

            }
        }

        round++;


    }
}
