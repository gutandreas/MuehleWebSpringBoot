class Game {


    constructor(player, gameCode) {

        this.player = player;
        this.board = new Board();
        this.myTurn = false;
        this.action = "put";
        this.gamecode = gameCode;
        this.play();
        document.getElementById("boardImage").disabled = true;
    }

    getMyTurn(){
        return this.myTurn;
    }

    play() {

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