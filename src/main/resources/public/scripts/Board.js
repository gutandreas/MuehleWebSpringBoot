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

    }}

    putStone(position, playerIndex){
        this.array[position.getRing()][position.getField()] = playerIndex;
        document.write(array.toString());
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

    updateBoardAndGetChanges(boardAsString){

        let tempBoardAsString = boardAsString;
        let changedPositions = new Array(2);
        let enemysIndex = 1-this.game.getPlayer().getIndex();


        for (let i = 0; i < 3; i++) {
            for (let j = 0; j < 8; j++) {
                //veränderter gegnerischer Stein
                if (this.array[i][j] == 9
                    && enemysIndex == boardAsString.charAt(0)){
                    changedPositions[0] = new Position(i,j);
                }

                //geschlagener eigener Stein
                if (this.array[i][j] == this.game.getPlayer().getIndex()
                    && boardAsString.charAt(0) == 9){
                    changedPositions[1] = new Position(i,j);
                }


                this.array[i][j] = boardAsString.charAt(0);
                tempBoardAsString = tempBoardAsString.substr(1);

            }
        }

        console.log(this.toString());
        return changedPositions;
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
        space = "    ";
        for (let i = 0; i < 3; i++){
            rowString += this.array[row][i] + space;
        }
        rowString += "\n";
        break;
    case 1:
        space = "   ";
        rowString += " ";
        for (let i = 0; i < 3; i++){
            rowString += this.array[row][i] + space;
        }
        rowString += "\n";
        break;
    case 2:
        space = "  ";
        rowString += "  ";
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
        space = "  ";
        rowString += "  ";
        for (let i = 6; i > 3; i--){
            rowString += this.array[2][i] + space;
        }
        rowString += "\n";
        break;
    case 5:
        space = "   ";
        rowString += " ";
        for (let i = 6; i > 3; i--){
            rowString += this.array[1][i] + space;
        }
        rowString += "\n";
        break;
    case 6:
        space = "    ";
        for (let i = 6; i > 3; i--){
            rowString += this.array[0][i] + space;
        }
        rowString += "\n";
        break;
    }

    return rowString;}

}