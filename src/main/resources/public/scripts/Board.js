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
        this.changedPositions = new Array(3);
    }

    putStone(position, playerIndex){
        this.array[position.getRing()][position.getField()] = playerIndex;
    }

    move(move, playerIndex) {
        this.putStone(move.toPosition, playerIndex);
        this.clearStone(move.fromPosition);
    }

    checkMove(move, allowedToJump){

    let destinationFree = this.isFieldFree(move.toPosition);

    let destinationInRing = (move.fromPosition.getRing()==move.toPosition.getRing() && Math.abs(move.fromPosition.getField()-move.toPosition.getField())==1)
        || (move.fromPosition.getRing()==move.toPosition.getRing() && Math.abs(move.fromPosition.getField()-move.toPosition.getField())==7);

    let destinationBetweenRings = move.fromPosition.getField()%2==1 && move.fromPosition.getField()==move.toPosition.getField()
        && Math.abs(move.fromPosition.getRing()-move.toPosition.getRing())==1;

    return destinationFree && (destinationInRing || destinationBetweenRings || allowedToJump);
    }

    checkKill(position,  otherPlayerIndex){
    return this.array[position.getRing()][position.getField()] == otherPlayerIndex &&
    (!this.checkMorris(position) || this.countPlayersStones(otherPlayerIndex)==3);
    }

    isThereStoneToKill(otherPlayerIndex){
        for (let ring = 0; ring < 3; ring++){
            for (let field = 0; field < 8; field++){
                if (this.array[ring][field] == otherPlayerIndex && !this.checkMorris(new Position(ring,field))){
                    console.log("Stone to Kill okay.")
                    return true;}
            }
        }
        return false;
    }

    clearStone(position) {
    this.array[position.getRing()][position.getField()] = 9;
    }

    countPlayersStones(playerIndex){
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

    checkMorris(position){
        let cornerField = position.getField()%2==0;
        let morris;
        let stone = this.array[position.getRing()][position.getField()];

        if(cornerField){
            morris = this.checkMorrisInRingFromCorner(position, stone);
        }
        else {
        morris = this.checkMorrisInRingFromCenter(position, stone) || this.checkMorrisBetweenRings(position, stone);
        }

        return morris;
    }


    checkMorrisInRingFromCorner(position, playerIndex){

        let morrisUpwards = playerIndex == this.array[position.getRing()][(position.getField()+1)%8]
            && playerIndex == this.array[position.getRing()][(position.getField()+2)%8];
        let morrisDownwards = playerIndex == this.array[position.getRing()][(position.getField()+6)%8]
            && playerIndex == this.array[position.getRing()][(position.getField()+7)%8];

        return morrisUpwards || morrisDownwards;
    }


    checkMorrisInRingFromCenter( position, playerIndex){
        return playerIndex == this.array[position.getRing()][(position.getField()+1)%8]
            && playerIndex == this.array[position.getRing()][(position.getField()+7)%8];
    }


    checkMorrisBetweenRings(position, playerIndex){
        return playerIndex == this.array[(position.getRing()+1)%3][position.getField()]
            && playerIndex == this.array[(position.getRing()+2)%3][position.getField()];
    }

    isFieldFree(position) {
    return this.array[position.getRing()][position.getField()] == 9;
    }

    isThisMyStone(position, ownPlayerIndex){
    return this.array[position.getRing()][position.getField()] == ownPlayerIndex;
    }

    getFieldContent(position){
        return this.array[position.getRing()][position.getField()];
    }


    isThisMyEnemysStone( position,  ownPlayerIndex){
    return this.isFieldOccupied(position) && !this.isThisMyStone(position, ownPlayerIndex);
    }

    isFieldOccupied( position){
    return !this.isFieldFree(position);
    }

    updateBoardAndGetChanges(boardAsString){

        let tempBoardAsString = boardAsString;
        let enemysIndex = 1-this.game.getPlayer().getIndex();

        this.changedPositions[0] = null;
        this.changedPositions[1] = null;
        this.changedPositions[2] = null;


        for (let i = 0; i < 3; i++) {
            for (let j = 0; j < 8; j++) {

                //gegnerischer Stein auf vorher freiem Feld
                if (this.array[i][j] == 9
                    && enemysIndex == tempBoardAsString.charAt(0)){
                    this.changedPositions[0] = new Position(i,j);
                }

                //freies Feld, auf dem vorher gegnerischer Stein war
                if (this.array[i][j] == enemysIndex
                    && tempBoardAsString.charAt(0) == 9){
                    this.changedPositions[1] = new Position(i,j);
                }

                //geschlagener eigener Stein
                if (this.array[i][j] == this.game.getPlayer().getIndex()
                    && tempBoardAsString.charAt(0) == 9){
                    this.changedPositions[2] = new Position(i,j);
                }

                this.array[i][j] = tempBoardAsString.charAt(0);
                tempBoardAsString = tempBoardAsString.substr(1);
            }
        }

        console.log(this.toString());
        return this.changedPositions;
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