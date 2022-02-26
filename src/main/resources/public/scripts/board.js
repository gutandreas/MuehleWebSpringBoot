class Board {

    constructor(game) {
        this.game = game;
        this.array = new Array(3);

        for (let i = 0; i < 3; i++){
            this.array[i] = new Array(8)
        }

        for (let i = 0; i < 3; i++) {
            for (let j = 0; j < 8; j++) {
                this.array[i][j] = 9;
            }
        }
    }

    putStone(position, playerIndex){
        this.array[position.getRing()][position.getField()] = playerIndex;
    }

    moveStone(move, playerIndex) {
        this.putStone(move.toPosition, playerIndex);
        this.removeStone(move.fromPosition);
    }

    removeStone(position) {
        this.array[position.getRing()][position.getField()] = 9;
    }

    isMovePossibleAt(move, allowedToJump){

        let destinationFree = this.isPositionFree(move.toPosition);

        let destinationInRing = (move.fromPosition.getRing()==move.toPosition.getRing() && Math.abs(move.fromPosition.getField()-move.toPosition.getField())==1)
            || (move.fromPosition.getRing()==move.toPosition.getRing() && Math.abs(move.fromPosition.getField()-move.toPosition.getField())==7);

        let destinationBetweenRings = move.fromPosition.getField()%2==1 && move.fromPosition.getField()==move.toPosition.getField()
            && Math.abs(move.fromPosition.getRing()-move.toPosition.getRing())==1;

        return destinationFree && (destinationInRing || destinationBetweenRings || allowedToJump);
    }

    isKillPossibleAt(position, otherPlayerIndex){
        return this.array[position.getRing()][position.getField()] == otherPlayerIndex &&
        (!this.isInMorris(position) || this.numberOfStonesOf(otherPlayerIndex)==3);
    }

    canPlayerKill(playerIndex){

        let otherPlayerIndex = 1-playerIndex

        for (let ring = 0; ring < 3; ring++){
            for (let field = 0; field < 8; field++){
                if (this.array[ring][field] == otherPlayerIndex && !this.isInMorris(new Position(ring,field))){
                    return true;}
            }
        }
        return false;
    }

    numberOfStonesOf(playerIndex){
        let counter = 0;
        for (let i = 0; i < 3; i++){
            for (let j = 0; j < 8; j++){
                if (playerIndex == this.array[i][j]){
                counter++;
                }
            }
        }
        return counter;
    }

    isInMorris(position){
        let cornerField = position.getField()%2==0;
        let morris;
        let stone = this.array[position.getRing()][position.getField()];

        if(cornerField){
            morris = this.isInMorrisInRingFromCorner(position, stone);
        }
        else {
        morris = this.isInMorrisInRingFromCenter(position, stone) || this.isInMorrisBetweenRings(position, stone);
        }

        return morris;
    }

    isInMorrisInRingFromCorner(position, playerIndex){

        let morrisUpwards = playerIndex == this.array[position.getRing()][(position.getField()+1)%8]
            && playerIndex == this.array[position.getRing()][(position.getField()+2)%8];
        let morrisDownwards = playerIndex == this.array[position.getRing()][(position.getField()+6)%8]
            && playerIndex == this.array[position.getRing()][(position.getField()+7)%8];

        return morrisUpwards || morrisDownwards;
    }

    isInMorrisInRingFromCenter(position, playerIndex){
        return playerIndex == this.array[position.getRing()][(position.getField()+1)%8]
            && playerIndex == this.array[position.getRing()][(position.getField()+7)%8];
    }

    isInMorrisBetweenRings(position, playerIndex){
        return playerIndex == this.array[(position.getRing()+1)%3][position.getField()]
            && playerIndex == this.array[(position.getRing()+2)%3][position.getField()];
    }

    isPositionFree(position) {
        return this.array[position.getRing()][position.getField()] == 9;
    }

    isThisMyStone(position, ownPlayerIndex){
        return this.array[position.getRing()][position.getField()] == ownPlayerIndex;
    }

    getNumberAt(position){
        return this.array[position.getRing()][position.getField()];
    }


    toString(){
        let board = "";
        for (let i = 0; i <= 6; i++){
            board += this.printRow(i);}
        return board;
    }

    printRow(row){
        let rowString ="";
        let space;
        switch (row){
        case 0:
            space = "      ";
            for (let i = 0; i < 3; i++){
                rowString += this.array[row][i] + space;
            }
            rowString += "\n";
            break;
        case 1:
            space = "    ";
            rowString += "  ";
            for (let i = 0; i < 3; i++){
                rowString += this.array[row][i] + space;
            }
            rowString += "\n";
            break;
        case 2:
            space = " ";
            rowString += "     ";
            for (let i = 0; i < 3; i++){
                rowString += this.array[row][i] + space;
            }
            rowString += "\n";
            break;
        case 3:
            for (let i = 0; i < 3; i++){
                rowString += this.array[i][7];
            }
            rowString += "     ";
            for (let i = 2; i >= 0; i--){
                rowString += this.array[i][3];
            }
            rowString += "\n";
            break;
        case 4:
            space = " ";
            rowString += "     ";
            for (let i = 6; i > 3; i--){
                rowString += this.array[2][i] + space;
            }
            rowString += "\n";
            break;
        case 5:
            space = "    ";
            rowString += "  ";
            for (let i = 6; i > 3; i--){
                rowString += this.array[1][i] + space;
            }
            rowString += "\n";
            break;
        case 6:
            space = "      ";
            for (let i = 6; i > 3; i--){
                rowString += this.array[0][i] + space;
            }
            rowString += "\n";
            break;
        }

        return rowString;}
}