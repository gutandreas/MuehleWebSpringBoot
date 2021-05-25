var myTurn = false;
var game;
var uuid;
var playerIndex;



function setCursor(cursorURL){
    document.getElementById("boardImage").style.cursor = cursorURL;
}

function autoRefreshMyTurn(){
    setInterval(function () {
        if (!myTurn){
        console.log("Frage an, ob ich an der Reihe bin...");
        fetch("/game/controller/myTurn", {
            method: 'POST',
            body: JSON.stringify({
                "gameCode": game.gamecode,
                "playerUuid": game.player.getUuid()
            }),
            headers: {
                "Content-type": "application/json"
            }
        })
            .then(resp => resp.json())
            .then(responseData => {
                console.log(responseData);
                if (responseData.myTurn) {
                    myTurn = true;
                    refreshField();
                }
            })
            .catch(error => console.log("MyTurn konnte nicht abgefragt werrden"))}


    }, 3000)
}

function refreshField(){
        console.log("Board bei Server abfragen");
        fetch("/game/controller/getBoard", {
            method: 'POST',
            body: JSON.stringify({
                "gameCode": game.gamecode,
            }),
            headers: {
                "Content-type": "application/json"
            }
        })
            .then(resp => resp.json())
            .then(responseData => {
                    console.log(responseData);
                    let changedPositions = game.board.updateBoardAndGetChanges(responseData.board);

                    if (changedPositions[0] != null) {
                        // Gegnerischer Zug führt nicht zu einer Mühle
                        if (!game.board.checkMorris(changedPositions[0])) {
                            game.myTurn = true;
                        }

                        // Gegnerischer Zug führt zu Mühle und es wurde bereits Stein entfernt
                        if (game.board.checkMorris(changedPositions[0])
                            && changedPositions[1] != null) {
                            game.myTurn = true;
                        }
                    }
                }
            )
}


function clickOnField(ring, field){

    if (myTurn){
        putStone(ring, field);



    }
    else {
        alert("Du bist nicht an der Reihe. Warte auf den Zug des Gegenspielers");
    }
    console.log("Feld " + ring + "/" + field + " angeklickt");

    setCursor("url('images/StoneBlackCursor.png'), auto");

    }

   function putStone(ring, field){
       console.log("Put an Server senden");
       fetch("/game/controller/put", {
           method: 'POST',
           body: JSON.stringify({
               "gameCode": game.gamecode,
               "playerUuid": game.player.getUuid(),
               "putRing": ring,
               "putField": field
           }),
           headers: {
               "Content-type": "application/json"
           }
       })
           .then(resp => resp.json())
           .then(responseData => {
                   console.log(responseData);
                   game.board.putStone(new Position(ring, field), game.player.playerIn)

                   if (changedPositions[0] != null) {
                       // Gegnerischer Zug führt nicht zu einer Mühle
                       if (!game.board.checkMorris(changedPositions[0])) {
                           game.myTurn = true;
                       }

                       // Gegnerischer Zug führt zu Mühle und es wurde bereits Stein entfernt
                       if (game.board.checkMorris(changedPositions[0])
                           && changedPositions[1] != null) {
                           game.myTurn = true;
                       }
                   }
               }
           )
    }

