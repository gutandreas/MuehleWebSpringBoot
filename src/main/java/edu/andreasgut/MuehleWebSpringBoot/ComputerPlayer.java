package edu.andreasgut.MuehleWebSpringBoot;



public class ComputerPlayer extends Player {


    public ComputerPlayer(String name, String uuid,  STONECOLOR stonecolor) {
        super(name, uuid, stonecolor);
    }


    @Override
    Position put(Board board, int playerIndex) {

        Position position = new Position();
        int i;
        int j;

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 8; j++) {
                if (board.getArray()[i][j] == 9) {
                    position.setRing(i);
                    position.setField(j);

                    return position;
                }
            }
        }

        return position;
    }


    @Override
    Move move(Board board, int playerIndex, boolean allowedToJump) {
        Move move = new Move();

        loop:{
        for (int i = 0; i < 3; i++){
            for(int j = 0; j < 7; j++){
                if (board.isThisMyStone(new Position(i,j),playerIndex)){
                    if(board.isFieldFree(new Position(i,(j+1)%8))){
                        move.setFrom(new Position(i, j));
                        move.setTo(new Position(i, (j+1)%8));
                        break loop;
                    }
                    if(board.isFieldFree(new Position(i,(j+7)%8))){
                        move.setFrom(new Position(i, j));
                        move.setTo(new Position(i, (j+7)%8));
                        break loop;
                    }
                }
        }}}
        return move;
    }


    @Override
    Position kill(Board board, int otherPlayerIndex) {
        Position position = new Position();
        int i;
        int j;

        loop:{
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 8; j++) {
                if (board.checkKill(new Position(i,j), otherPlayerIndex)) {
                    position.setRing(i);
                    position.setField(j);

                    break loop;
                }
            }
        }}
        return position;
    }



}
