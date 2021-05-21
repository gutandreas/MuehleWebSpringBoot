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
    boolean putPhase = true;
    boolean movePhase = false;

    ArrayList<Player> playerArrayList = new ArrayList<>();

    public Game(Player player0, Player player1) {
        this.player0 = player0;
        this.player1 = player1;
        playerArrayList.add(0, player0);
        playerArrayList.add(1, player1);
        round = 0;
        currentPlayer=playerArrayList.get(0);
        gameComplete = true;
        this.board = new Board(this);
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Game mit 2 Spielern ("+ player0.getName() + "/" + player1.getName() + ") wurde erstellt");
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

    public boolean isGameComplete() {
        return gameComplete;
    }
}
