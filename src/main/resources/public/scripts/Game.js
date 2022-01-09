class Game {


    //Konstruktor für Spieler
    constructor(player, gameCode, start) {

        this.player = player;
        this.board = new Board(this);
        this.myTurn = start;
        this.gamecode = gameCode;
        this.round = 0;
        this.roboterConnected = false;
        this.roboterWatching = false;
        this.roboterPlaying = false;

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

    setRoboterWatching(watching){
        this.roboterWatching = watching
    }

    doesRoboterWatch(){
        return this.roboterWatching
    }

    setRoboterPlaying(playing){
        this.roboterPlaying
    }

    isRoboterPlaying(){
        return this.roboterPlaying
    }




}