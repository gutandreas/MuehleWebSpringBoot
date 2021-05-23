class Game {

    player;

    constructor(player, gameCode, start) {

        this.player = player;
        this.board = new Board(this);
        this.myTurn = false;
        this.action = "put";
        this.gamecode = gameCode;
        this.play();
        document.getElementById("boardImage").disabled = true;
    }

    getMyTurn(){
        return this.myTurn;
    }

    getPlayer(){
        return this.player;
    }

    play(){
        if (!this.myTurn) {
            const task = async () => {

                while (!this.myTurn) {

                    await new Promise(r => setTimeout(r, 1000));

                    console.log("Board bei Server abfragen");
                    fetch("/game/controller/getBoard", {
                        method: 'POST',
                        body: JSON.stringify({
                            "gameCode": this.gamecode,
                        }),
                        headers: {
                            "Content-type": "application/json"
                        }
                    })
                        .then(resp => resp.json())
                        .then(responseData => {
                                console.log(responseData);
                                let changedPositions = this.board.updateBoardAndGetChanges(responseData.board);

                                if (changedPositions[0] != null){
                                    // Gegnerischer Zug führt nicht zu einer Mühle
                                    if (!this.board.checkMorris(changedPositions[0])){
                                        this.myTurn = true;
                                    }

                                    // Gegnerischer Zug führt zu Mühle und es wurde bereits Stein entfernt
                                    if (this.board.checkMorris(changedPositions[0])
                                    && changedPositions[1] != null){
                                        this.myTurn = true;
                                    }}
                            }
                        )

                }}
                task();

        }
    }


    // Als Archiv zum Rauskopieren. Wird nicht mehr gebraucht...
    oldplay() {

        if (!this.myTurn) {
            const task = async () => {

                while (!this.myTurn) {

                    await new Promise(r => setTimeout(r, 1000));

                    console.log("Frage an, ob ich an der Reihe bin...");
                    fetch("/game/controller/myTurn", {
                        method: 'POST',
                        body: JSON.stringify({
                            "gameCode": this.gamecode,
                            "playerUuid": this.player.getUuid()
                        }),
                        headers: {
                            "Content-type": "application/json"
                        }
                    })
                        .then(resp => resp.json())
                        .then(responseData => {
                                console.log(responseData);
                                if (responseData.myTurn) {
                                    this.myTurn = true;
                                }
                            }
                        )

                }}
                task().then( r => {
                    if (this.action == "put") {
                        console.log("Test 2...")
                        setCursor("url('images/StoneBlackCursor.png'), auto");

                        window.clickedPosition = null;

                        const task = async () => {
                            while (window.clickedPosition == null){
                                await new Promise(r => setTimeout(r, 1000));
                                console.log("Warte auf Klick...")

                            }
                            console.log("angeklickte Position: " + window.clickedPosition.toString());

                        }

                        task();
                }
        })

        }








}
}