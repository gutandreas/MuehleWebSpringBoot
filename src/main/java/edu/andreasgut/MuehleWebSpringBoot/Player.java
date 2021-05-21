package edu.andreasgut.MuehleWebSpringBoot;


public abstract class Player {

    private final String name;
    private final String uuid;
    private final STONECOLOR stonecolor;

    public Player(String name, String uuid, STONECOLOR stonecolor) {
        this.name = name;
        this.uuid = uuid;
        this.stonecolor = stonecolor;
    }

    public String getName() {
        return name;
    }

    public STONECOLOR getStonecolor() {
        return stonecolor;
    }

    public String getUuid() {
        return uuid;
    }

    /*
    abstract Position[] move(Board board, int playerIndex, boolean allowedToJump);
    abstract Position put(Board board, int playerIndex);
    abstract Position kill(Board board, int otherPlayerIndex);

     */
}
