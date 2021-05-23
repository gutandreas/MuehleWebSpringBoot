class Board {

    constructor() {
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

}