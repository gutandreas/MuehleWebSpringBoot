class Game {

    constructor(player, gameCode, start) {

        this.player = player;
        this.board = new Board(this);
        this.myTurn = start;
        this.gamecode = gameCode;
        this.round = 0;

        $('#boardImage').attr('disabled', true)
    }

    getMyTurn(){
        return this.myTurn;
    }

    getPlayer(){
        return this.player;
    }




}