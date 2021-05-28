class Game {

    constructor(player, gameCode, start) {

        this.player = player;
        this.board = new Board(this);
        this.myTurn = start;
        this.action = "put";
        this.gamecode = gameCode;
        this.round = 1;

        document.getElementById("boardImage").disabled = true;
    }

    getMyTurn(){
        return this.myTurn;
    }

    getPlayer(){
        return this.player;
    }




}