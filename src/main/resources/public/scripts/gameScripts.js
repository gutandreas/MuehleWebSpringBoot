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

                    game.round++;
                    console.log("Spielrunde: " + game.round);

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
}}, 1000)}


function clickOnField(ring, field){

    console.log("Feld " + ring + "/" + field + " angeklickt");

    if (myTurn){

        if (kill){
            killStone(ring, field);
        }

        if (game.round < 18 && !kill){
            putStone(ring, field);
            }

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
           console.log("Spielrunde: " + game.round);
           game.round++;

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
                   console.log(game.board.toString());
               }
           )}
       else {
           alert("Dieses Feld ist nicht frei");
       }
    }

    function killStone(ring, field){
        if (game.board.checkKill(new Position(ring, field)), 1-playerIndex){
            console.log("Kill an Server senden");
            game.board.clearStone(new Position(ring, field));

            fetch("/game/controller/kill", {
                method: 'POST',
                body: JSON.stringify({
                    "gameCode": game.gamecode,
                    "playerUuid": game.player.getUuid(),
                    "killRing": ring,
                    "killField": field
                }),
                headers: {
                    "Content-type": "application/json"
                }
            })
                .then(resp => resp.json())
                .then(responseData => {
                        console.log(responseData);
                        console.log(game.board.toString());
                        myTurn = false;
                        kill = false;
                    }
                )}

        else {
            alert("Auf diesem Feld kann kein Stein entfernt werden")}
    }

