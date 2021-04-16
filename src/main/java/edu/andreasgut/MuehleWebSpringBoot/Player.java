package edu.andreasgut.MuehleWebSpringBoot;


public abstract class Player {

    private final String name;

    public Player( String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /*
    abstract Position[] move(Board board, int playerIndex, boolean allowedToJump);
    abstract Position put(Board board, int playerIndex);
    abstract Position kill(Board board, int otherPlayerIndex);

     */
}
