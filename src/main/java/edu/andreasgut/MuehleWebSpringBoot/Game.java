package edu.andreasgut.MuehleWebSpringBoot;

import java.time.LocalTime;

public class Game {

    private Player player1;
    private Player player2;
    private boolean gameComplete = false;
    private final Board board;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        gameComplete = true;
        this.board = new Board(this);
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Game mit 2 Spielern ("+ player1.getName() + "/" + player2.getName() + ") wurde erstellt");
    }

    public Game(Player player1) {
        this.player1 = player1;
        this.board = new Board(this);
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Game mit 1 Spieler ("+ player1.getName() +") wurde erstellt");
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
        gameComplete = true;
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": " + player1.getName() + " und "
                + player2.getName() + " bilden ein komplettes Game");
    }

    public Board getBoard() {
        return board;
    }

    public boolean isGameComplete() {
        return gameComplete;
    }
}
