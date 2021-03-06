package edu.andreasgut.MuehleWebSpringBoot;

import org.springframework.web.socket.WebSocketSession;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;

public class Game {

    private final Player player0;
    private Player player1;
    private boolean gameComplete;
    private boolean gameStarted = false;
    private String gameCode;
    private int round;
    private final Board board;
    private boolean roboterConnected;
    private boolean roboterWatching;
    private boolean roboterPlaying;
    private final LinkedList<WebSocketSession> sessionList = new LinkedList<>();

    private final ArrayList<Player> playerArrayList = new ArrayList<>();

    public Game(Player player0, Player player1) {
        this.player0 = player0;
        this.player1 = player1;
        playerArrayList.add(0, player0);
        playerArrayList.add(1, player1);
        round = 0;
        gameComplete = true;
        this.board = new Board(this);
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() +
                ": Game mit 2 Spielern (" + player0.getName() + "/" + player1.getName() + ") wurde erstellt");
    }

    public Game(Player player0) {
        this.player0 = player0;
        gameComplete = false;
        playerArrayList.add(0, player0);
        this.board = new Board(this);
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() +
                ": Game mit 1 Spieler (" + player0.getName() + ") wurde erstellt");
    }

    public void addToSessionList(WebSocketSession session) {
        sessionList.add(session);
        System.out.println(this.getClass().getSimpleName() +
                ": Session " + session + " wurde zur Sessionlist hinzugefügt");
    }

    public void removeFromSessionList(WebSocketSession session) {
        if (sessionList.contains(session)) {
            sessionList.remove(session);
            System.out.println(this.getClass().getSimpleName() +
                    ": Session " + session + " wurde aus der Sessionlist entfernt");
        }
    }

    public void increaseRound() {
        round++;
    }

    public LinkedList<WebSocketSession> getSessionList() {
        return sessionList;
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

    public Player getPlayerByIndex(int playerIndex) {
        return getPlayerArrayList().get(playerIndex);
    }

    public ArrayList<Player> getPlayerArrayList() {
        return playerArrayList;
    }

    public int getPlayerIndexByUuid(String uuid) {

        if (getPlayerArrayList().get(0).getUuid().equals(uuid)) {
            return 0;
        } else {
            return 1;
        }
    }

    public Board getBoard() {
        return board;
    }

    public int getRound() {
        return round;
    }

    public boolean isGameComplete() {
        return gameComplete;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public boolean isRoboterConnected() {
        return roboterConnected;
    }

    public void setRoboterConnected(boolean roboterConnected) {
        this.roboterConnected = roboterConnected;
    }

    public boolean isRoboterWatching() {
        return roboterWatching;
    }

    public void setRoboterWatching(boolean roboterWatching) {
        this.roboterWatching = roboterWatching;
    }

    public boolean isRoboterPlaying() {
        return roboterPlaying;
    }

    public void setRoboterPlaying(boolean roboterPlaying) {
        this.roboterPlaying = roboterPlaying;
    }


}
