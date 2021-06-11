class GameComp {

    constructor(computer1, computer2, gameCode) {

        this.computer1 = computer1;
        this.computer2 = computer2;
        this.board = new Board(this);
        this.gamecode = gameCode;
        this.round = 0;
    }


    getComputer1(){
        return this.computer1;
    }

    getComputer2(){
        return this.computer2;
    }

    getComputerPlayerByIndex(index){
        if (index==0){
            return this.computer1;
        }
        if (index==1){
            return this.computer2;
        }
    }

}