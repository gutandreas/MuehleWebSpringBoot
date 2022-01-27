package edu.andreasgut.MuehleWebSpringBoot;

public class Board {

    final private int[][] array;
    private final Game game;

    public Board(Game game) {
        array = new int[3][8];
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 8; j++){
                array[i][j] = 9;}
        }
        this.game = game;
    }

    protected Board(Board board){
        int[][] tempArray = new int[3][8];

        for (int i = 0; i < board.array.length; i++) {
            System.arraycopy(board.array[i], 0, tempArray[i], 0, board.array[0].length);
        }

        array = tempArray;
        this.game = board.game;
    }

    public int[][] getArray() {
        return array.clone();
    }

    public boolean isFieldFree(Position position) {
        return array[position.getRing()][position.getField()] == 9;
    }

    public boolean isFieldOccupied(Position position){
        return !isFieldFree(position);
    }

    public boolean isThisMyStone(Position position, int ownPlayerIndex){
        return array[position.getRing()][position.getField()] == ownPlayerIndex;
    }

    public boolean isThisMyEnemysStone(Position position, int ownPlayerIndex){
        return isFieldOccupied(position) && !isThisMyStone(position, ownPlayerIndex);
    }

    public boolean isPutPossibleAt(Position position){
        return isFieldFree(position);
    }

    public boolean isMovePossibleAt(Move move, boolean allowedToJump){

        boolean destinationFree = isFieldFree(move.getTo());

        boolean destinationInRing = (move.getFrom().getRing()==move.getTo().getRing() && Math.abs(move.getFrom().getField()-move.getTo().getField())==1)
                || (move.getFrom().getRing()==move.getTo().getRing() && Math.abs(move.getFrom().getField()-move.getTo().getField())==7);

        boolean destinationBetweenRings = move.getFrom().getField()%2==1 && move.getFrom().getField()==move.getTo().getField()
                && Math.abs(move.getFrom().getRing()-move.getTo().getRing())==1;

        return destinationFree && (destinationInRing || destinationBetweenRings || allowedToJump);
    }

    public boolean isKillPossibleAt(Position position, int enemysPlayerIndex){
        return array[position.getRing()][position.getField()] == enemysPlayerIndex &&
                (!isPositionPartOfMorris(position) || numberOfStonesOf(enemysPlayerIndex)==3);
    }

    public void putStone(Position position, int playerIndex) {
        array[position.getRing()][position.getField()] =  playerIndex;
    }

    public void moveStone(Move move, int playerIndex) {
        putStone(move.getTo(), playerIndex);
        removeStone(move.getFrom());
    }

    public void removeStone(Position position) {
        array[position.getRing()][position.getField()] = 9;
    }

    public boolean isPositionPartOfMorris(Position position){
        boolean cornerField = position.getField()%2==0;
        boolean morris;
        int stone = array[position.getRing()][position.getField()];

        if(cornerField){
            morris = isPositionPartOfMorrisInRingFromCornerField(position, stone);
        }
        else {
            morris = isPositionPartOfMorrisInRingFromCenterField(position, stone) || isPositionPartOfMorrisBetweenRings(position, stone);
        }

        return morris;
    }


    private boolean isPositionPartOfMorrisInRingFromCornerField(Position position, int playerIndex){

        boolean morrisUpwards = playerIndex == array[position.getRing()][(position.getField()+1)%8]
                && playerIndex == array[position.getRing()][(position.getField()+2)%8];
        boolean morrisDownwards = playerIndex == array[position.getRing()][(position.getField()+6)%8]
                && playerIndex == array[position.getRing()][(position.getField()+7)%8];

        return morrisUpwards || morrisDownwards;
    }


    private boolean isPositionPartOfMorrisInRingFromCenterField(Position position, int playerIndex){
        return playerIndex == array[position.getRing()][(position.getField()+1)%8]
                && playerIndex == array[position.getRing()][(position.getField()+7)%8];
    }


    private boolean isPositionPartOfMorrisBetweenRings(Position position, int playerIndex){
        return playerIndex == array[(position.getRing()+1)%3][position.getField()]
                && playerIndex == array[(position.getRing()+2)%3][position.getField()];
    }


    public boolean canPlayerMove(int playerIndex){
        return canPlayerMoveInRing(playerIndex) || canPlayerMoveBetweenRings(playerIndex)
                || numberOfStonesOf(playerIndex) == 3;
    }

    private boolean canPlayerMoveInRing(int playerIndex){
        for (int ring = 0; ring < 3; ring++){
            for (int field = 0; field < 8; field++){
                if (array[ring][field] == playerIndex
                        && (array[ring][(field+1)%8] == 9 || array[ring][(field+7)%8] == 9)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canPlayerMoveBetweenRings(int playerIndex){
        for (int ring = 0; ring < 3; ring++){
            for (int field = 1; field < 8; field+=2){
                if (array[ring][field] == playerIndex){
                    switch (ring){
                        case 0:
                            if (array[ring+1][field] == 9) return true;
                            break;
                        case 1:
                            if (array[ring-1][field] == 9 || array[ring+1][field] == 9) return true;
                            break;
                        case 2:
                            if (array[ring-1][field] == 9) return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canPlayerKill(int playerIndex){
        int otherPlayerIndex = 1-playerIndex;

        if (numberOfStonesOf(otherPlayerIndex) == 3 && game.getRound() > 18){
            return true;
        }

        for (int ring = 0; ring < 3; ring++){
            for (int field = 0; field < 8; field++){
                if (array[ring][field] == otherPlayerIndex && !isPositionPartOfMorris(new Position(ring,field))){
                    return true;
                }
            }
        }
        return false;
    }

    public int numberOfStonesOf(int playerIndex){
        int counter = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 8; j++){
                if (playerIndex == array[i][j]){
                    counter++;
                }
            }
        }
        return counter;
    }

    @Override
    public String toString(){
        String board = "";
        for (int i = 0; i <= 6; i++){
            board += printRow(i);}
        return board;
    }

    private String printRow(int row){
        String rowString ="";
        String space;
        switch (row){
            case 0:
                space = "    ";
                for (int i = 0; i < 3; i++){
                    rowString += array[row][i] + space;
                }
                rowString += "\n";
                break;
            case 1:
                space = "   ";
                rowString += " ";
                for (int i = 0; i < 3; i++){
                    rowString += array[row][i] + space;
                }
                rowString += "\n";
                break;
            case 2:
                space = "  ";
                rowString += "  ";
                for (int i = 0; i < 3; i++){
                    rowString += array[row][i] + space;
                }
                rowString += "\n";
                break;
            case 3:
                for (int i = 0; i < 3; i++){
                    rowString += array[i][7];
                }
                rowString += "     ";
                for (int i = 2; i >= 0; i--){
                    rowString += array[i][3];
                }
                rowString += "\n";
                break;
            case 4:
                space = "  ";
                rowString += "  ";
                for (int i = 6; i > 3; i--){
                    rowString += array[2][i] + space;
                }
                rowString += "\n";
                break;
            case 5:
                space = "   ";
                rowString += " ";
                for (int i = 6; i > 3; i--){
                    rowString += array[1][i] + space;
                }
                rowString += "\n";
                break;
            case 6:
                space = "    ";
                for (int i = 6; i > 3; i--){
                    rowString += array[0][i] + space;
                }
                rowString += "\n";
                break;
        }

        return rowString;}


    public String getBoardAsString(){
        String boardAsString = "";

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 8; j++){
                boardAsString += getArray()[i][j];
            }
        }

        return boardAsString;
    }


    public Object clone(){
        return new Board(this);
    }}
