var myTurn = true;
var kill = false;
var game;
var uuid;
var playerIndex;



function setCursor(cursorURL){
    document.getElementById("boardImage").style.cursor = cursorURL;
}


function autoRefreshField(){
    setInterval(function () {
        if (!myTurn){
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
                            myTurn = true;
                        }

                        // Gegnerischer Zug führt zu Mühle und es wurde bereits Stein entfernt
                        if (game.board.checkMorris(changedPositions[0])
                            && changedPositions[1] != null) {
                            myTurn = true;
                        }
                    }
                }
            )
}}, 3000)}


function clickOnField(ring, field){

    console.log("Feld " + ring + "/" + field + " angeklickt");
    if (myTurn){

        putStone(ring, field);

    }
    else {
        alert("Du bist nicht an der Reihe. Warte auf den Zug des Gegenspielers");
    }


    //setCursor("url('images/StoneBlackCursor.png'), auto");

    }

   function putStone(ring, field){

       if (game.board.isFieldFree(new Position(ring, field))){
       console.log("Put an Server senden");
       game.board.putStone(new Position(ring, field), playerIndex)

           if (game.board.checkMorris(new Position(ring, field))){
               console.log("Mühle gebildet, Stein darf gekillt werden...")
               myTurn = true;
               kill = true;
           }
           else {myTurn = false;}

       fetch("/game/controller/put", {
           method: 'POST',
           body: JSON.stringify({
               "gameCode": game.gamecode,
               "playerUuid": game.player.getUuid(),
               "putRing": ring,
               "putField": field,
               "callComputer": !myTurn
           }),
           headers: {
               "Content-type": "application/json"
           }
       })
           .then(resp => resp.json())
           .then(responseData => {
                   console.log(responseData);
                   game.board.putStone(new Position(ring, field), playerIndex);
                   myTurn = false;
                   console.log(game.board.toString());
               }
           )}
       else {
           alert("Dieses Feld ist nicht frei");
       }
    }

