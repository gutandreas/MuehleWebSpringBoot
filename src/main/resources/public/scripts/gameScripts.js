var myTurn = true;
var kill = false;
var game;
var uuid;
var playerIndex;
var color;
var name;
var enemyName;
var gameOver = false;
var enemyLoggedIn = true;
var pathWaitingCursor = "images/SandglassCursor.png"
var pathPutCursor = "images/PutCursor.png"


var moveTakePosition;
var moveReleasePosition;

var putStones = 0;
var killedStones = 0;
var lostStones = 0;

var enemyPutStones = 0;
var enemyKilledStones = 0;
var enemyLostStones = 0;


function setCursor(cursorURL){
    document.getElementById("boardImage").style.cursor = cursorURL;
}



function clickOnField(ring, field){

    console.log("Feld " + ring + "/" + field + " angeklickt");

    if (myTurn && !gameOver && enemyLoggedIn){

        if (kill){
            killStone(ring, field);
        }
        else {  // put-Phase
                if (game.round < 18 && !kill){
                    putStone(ring, field);}

                // move-Phase
                if (game.round >= 18 && !kill){
                    if (moveTakePosition == null){
                        moveTakePosition = moveStoneTakeStep(ring,field);}
                    else {
                        moveReleasePosition = moveStoneReleaseStep(ring, field);
                        moveStone(new Move(moveTakePosition, moveReleasePosition))
                        moveTakePosition = null;}
                }}

    }
    else {
        if (gameOver){
            alert("Das Spiel ist bereits zu Ende...");
        }
        if (!myTurn){
            alert("Du bist nicht an der Reihe. Warte auf den Zug des Gegenspielers");
        }
        if (!enemyLoggedIn){
            alert("Bitte warten Sie, bis Ihr Gegenspieler eingeloggt ist")
        }
    }
    }

    function editMyTurn(isMyTurn, isKill){

    myTurn = isMyTurn;
    kill = isKill;
        console.log(enemyName);

    if (isMyTurn){
        if (isKill){
            $('#spielverlaufLabel').text(name + " darf einen gegnerischen Stein entfernen")

        }
        else {
            if (game.round <18){
                $('#spielverlaufLabel').text(name + " darf einen Stein setzen")
            }
            else {
                $('#spielverlaufLabel').text(name + " darf einen Stein bewegen")
            }
        }
    }
    else {
        if (isKill){
            $('#spielverlaufLabel').text(enemyName + " darf einen gegnerischen Stein entfernen")
        }
        else {
            if (game.round <18){
                $('#spielverlaufLabel').text(enemyName + " darf einen Stein setzen")
            }
            else {
                $('#spielverlaufLabel').text(enemyName + " darf einen Stein bewegen")
            }
        }

    }
    }



    function putStone(ring, field){

       if (game.board.isFieldFree(new Position(ring, field))){
           console.log("Put an Server senden");
           game.board.putStone(new Position(ring, field), playerIndex)
           console.log("Spielrunde: " + game.round);
           increaseRound();

           putStoneGraphic(ring, field, playerIndex);

               if (game.board.checkMorris(new Position(ring, field))
                   && (game.board.isThereStoneToKill(1-playerIndex)
                       || (game.board.countPlayersStones(1-playerIndex) == 3 && game.round > 18))){

                   console.log("Mühle gebildet, Stein darf gekillt werden")
                   editMyTurn(true, true);

               }
               else {
                   editMyTurn(false, false)
                   //setBoardCursor(pathWaitingCursor);
                   }

           let delay;
           if (window.modus == 1){
               delay = 0;}
           if (window.modus == 2){
               delay = 300;}



           setTimeout(function(){

                   sendMessage(websocket, JSON.stringify({
                       "gameCode": game.gamecode,
                       "playerUuid": game.player.getUuid(),
                       "playerIndex": playerIndex,
                       "command" : "update",
                       "action": "put",
                       "ring": ring,
                       "field": field,
                       "callComputer": !myTurn}))



           /*fetch("/game/controller/put", {
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
               )
               .then( e => {
                   sendMessage(websocket, JSON.stringify({
                               "gameCode": game.gamecode,
                               "playerUuid": game.player.getUuid(),
                               "playerIndex": playerIndex,
                               "command" : "update",
                               "action": "put",
                               "ring": ring,
                               "field": field}))
               })*/}
               , delay)}
       else {
           alert("Dieses Feld ist nicht frei");
       }
    }



function moveStoneTakeStep(ring, field){
    if (game.board.isThisMyStone(new Position(ring, field), playerIndex)){
        return new Position(ring, field);}
    else {
        alert("Auf diesem Feld befindet sich keiner deiner Steine")}
}


function moveStoneReleaseStep(ring, field){
    if (game.board.isFieldFree(new Position(ring, field))){
        return new Position(ring, field);}
    else {
        alert("Dieses Feld ist nicht frei")}
}


function moveStone(move){

    if (game.board.checkMove(move, game.board.countPlayersStones(playerIndex) == 3)){
        console.log("Move an Server senden");
        game.board.move(move, playerIndex);
        console.log("Spielrunde: " + game.round);
        increaseRound();

        moveStoneGraphic(move, playerIndex);


        if (game.board.checkMorris(move.toPosition)
            && (game.board.isThereStoneToKill(1-playerIndex)
                || (game.board.countPlayersStones(1-playerIndex) == 3 /*&& game.round > 18*/))){

            console.log("Mühle gebildet, Stein darf gekillt werden")
            editMyTurn(true, true)
        }
        else {
            editMyTurn(false, false)}

        let delay;
        if (window.modus == 1){
            delay = 0;}
        if (window.modus == 2){
            delay = 300;}

        setTimeout(function(){

                sendMessage(websocket, JSON.stringify({
                    "gameCode": game.gamecode,
                    "playerUuid": game.player.getUuid(),
                    "command" : "update",
                    "action": "move",
                    "moveFromRing": move.fromPosition.getRing(),
                    "moveFromField": move.fromPosition.getField(),
                    "moveToRing": move.toPosition.getRing(),
                    "moveToField": move.toPosition.getField(),
                    "playerIndex": playerIndex,
                    "callComputer": !myTurn}))

                /*fetch("/game/controller/move", {
                    method: 'POST',
                    body: JSON.stringify({
                        "gameCode": game.gamecode,
                        "playerUuid": game.player.getUuid(),
                        "moveFromRing": move.fromPosition.getRing(),
                        "moveFromField": move.fromPosition.getField(),
                        "moveToRing": move.toPosition.getRing(),
                        "moveToField": move.toPosition.getField(),
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
                    )
                    .then( e => {
                        //Nachricht an Websocket
                        sendMessage(websocket, JSON.stringify({
                            "gameCode": game.gamecode,
                            "playerUuid": game.player.getUuid(),
                            "command" : "update",
                            "action": "move",
                            "moveFromRing": move.fromPosition.getRing(),
                            "moveFromField": move.fromPosition.getField(),
                            "moveToRing": move.toPosition.getRing(),
                            "moveToField": move.toPosition.getField(),
                            "playerIndex": playerIndex}))
                    })*/}
            , delay)}
    else {
        alert("Das ist kein gültiger Zug")}
}

    function killStone(ring, field){
        if (game.board.checkKill(new Position(ring, field), 1-playerIndex)){
            console.log("Kill an Server senden");
            game.board.clearStone(new Position(ring, field));

            clearStoneGraphic(ring, field, true);

            if (game.board.countPlayersStones(1-playerIndex) < 3 && game.round > 18){
                gameOver = true;
                alert(name + " hat das Spiel gewonnen!")
                $('#spielverlaufLabel').text(name + " hat das Spiel gewonnen")
            }

            if (!gameOver){
                editMyTurn(false, false);
            }

            let delay;
            if (window.modus == 1){
                delay = 0;}
            if (window.modus == 2){
                delay = 300;}

            setTimeout(function(){

                    sendMessage(websocket, JSON.stringify({
                        "gameCode": game.gamecode,
                        "playerUuid": game.player.getUuid(),
                        "command" : "update",
                        "action": "kill",
                        "ring": ring,
                        "field": field,
                        "callComputer": !gameOver
                    }))

            /*fetch("/game/controller/kill", {
                method: 'POST',
                body: JSON.stringify({
                    "gameCode": game.gamecode,
                    "playerUuid": game.player.getUuid(),
                    "killRing": ring,
                    "killField": field,
                    "callComputer": !gameOver
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
                )
                .then( e => {
                    //Nachricht an Websocket
                    sendMessage(websocket, JSON.stringify({
                        "gameCode": game.gamecode,
                        "playerUuid": game.player.getUuid(),
                        "command" : "update",
                        "action": "kill",
                        "ring": ring,
                        "field": field
                    }))
                })*/}
                , delay)}

        else {
            alert("Auf diesem Feld kann kein Stein entfernt werden")}
    }


    function increaseRound(){
    game.round++;
    $("#rundeText").text("Spielrunde: " + game.round)

    }

    function putStoneGraphic(ring, field, index){

    if (index == playerIndex){
        if (color == "BLACK"){
        $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneBlack.png" height="100%" width="100%"/>');}
        if (color == "WHITE"){
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneWhite.png" height="100%" width="100%"/>');}

        putStones++;
        $("#putLabel0").text("Steine gesetzt: " + putStones)
    }
    else {
        if (color == "WHITE"){
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneBlack.png" height="100%" width="100%"/>');}
        if (color == "BLACK"){
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneWhite.png" height="100%" width="100%"/>');}

        enemyPutStones++;
        $("#putLabel1").text("Steine gesetzt: " + enemyPutStones)

    }}

    function moveStoneGraphic(move, index) {

        $('#'.concat("r").concat(move.fromPosition.getRing()).concat("f").concat(move.fromPosition.getField())).empty();


        let ring = move.toPosition.getRing();
        let field = move.toPosition.getField();

        if (index == playerIndex) {
            if (color == "BLACK") {
                $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneBlack.png" height="100%" width="100%"/>');
            }
            if (color == "WHITE") {
                $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneWhite.png" height="100%" width="100%"/>');
            }

        } else {
            if (color == "WHITE") {
                $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneBlack.png" height="100%" width="100%"/>');
            }
            if (color == "BLACK") {
                $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneWhite.png" height="100%" width="100%"/>');
            }
        }
    }

    function clearStoneGraphic(ring, field, ownPlayer){

        if (ownPlayer){
            killedStones++;
            enemyLostStones++;
            $("#killedLabel0").text("Steine gewonnen: " + killedStones)
            $("#lostLabel1").text("Steine verloren: " + enemyLostStones)
        }
        else {
            enemyKilledStones++;
            lostStones++;
            $("#killedLabel1").text("Steine gewonnen: " + enemyKilledStones)
            $("#lostLabel0").text("Steine verloren: " + lostStones)

        }

        $('#'.concat("r").concat(ring).concat("f").concat(field)).empty();
    }

    function setBoardCursor(path){
    let cursorText = 'url(' + path +'), auto'
        //document.body.style.cursor = "none";
        $('body').css('cursor', 'none');
        $('#boardImage').mousemove(function() {
            $(this).css( 'cursor', cursorText );
        });
        $('#boardImage').mouseleave(function() {
            $(this).css( 'cursor', 'none, auto' );
        });
    }

    function getIndexPage(){
        fetch('/index/loadIndex', {method: 'GET'})
            .then((response) => {
                return response.text();
            })
            .then((html) => {
                document.body.innerHTML = html
            });
    }