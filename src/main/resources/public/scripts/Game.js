class Game {


    //Konstruktor für Spieler
    constructor(player, gameCode, start) {

        this.player = player;
        this.board = new Board(this);
        this.myTurn = start;
        this.gamecode = gameCode;
        this.round = 0;
        this.roboterConnected = false;

        $('#boardImage').attr('disabled', true)
    }




    getMyTurn(){
        return this.myTurn;
    }


    getPlayer(){
        return this.player;
    }

    setRoboterConnected(connected){
        this.roboterConnected = connected

    }

    isRoboterConnected(){
        return this.roboterConnected;
    }




}