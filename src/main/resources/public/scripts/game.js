class Game {

    constructor(player, gameCode) {

        this.player = player;
        this.board = new Board(this);
        this.gamecode = gameCode;
        this.round = 0;
        this.roboterConnected = false;
        this.roboterWatching = false;
        this.roboterPlaying = false;

        $('#boardImage').attr('disabled', true)
    }

    setRoboterConnected(connected){
        this.roboterConnected = connected
    }

    setRoboterWatching(watching){
        this.roboterWatching = watching
    }

    isRoboterWatching(){
        return this.roboterWatching
    }

    setRoboterPlaying(playing){
        this.roboterPlaying = playing;
    }

    isRoboterPlaying(){
        return this.roboterPlaying
    }
}