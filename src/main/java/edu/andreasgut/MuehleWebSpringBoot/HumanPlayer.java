package edu.andreasgut.MuehleWebSpringBoot;



public class HumanPlayer extends edu.andreasgut.MuehleWebSpringBoot.Player {

    public HumanPlayer(String name, String uuid, STONECOLOR stonecolor) {
        super(name, uuid, stonecolor);
    }


    @Override
    Position put(Board board, int playerIndex) {


        return null;
    }

    @Override
    Position[] move(Board board, int playerIndex, boolean allowedToJump) {

        return null;
    }

    @Override
    Position kill(Board board, int otherPlayerIndex) {

        return null;
    }


}
