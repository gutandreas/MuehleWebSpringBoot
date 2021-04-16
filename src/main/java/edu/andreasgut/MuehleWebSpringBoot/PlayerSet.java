package edu.andreasgut.MuehleWebSpringBoot;

public class PlayerSet {

private Player player1;
private Player player2;
private String gameCode;

    public PlayerSet(Player player1, String gameCode) {
        this.player1 = player1;
        this.gameCode = gameCode;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String getGameCode() {
        return gameCode;
    }
}
