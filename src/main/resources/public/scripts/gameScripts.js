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

var player1PutStones = 0;
var player1KilledStones = 0;
var player1LostStones = 0;

var player2PutStones = 0;
var player2KilledStones = 0;
var player2LostStones = 0;





function clickOnField(ring, field){

    console.log("Feld " + ring + "/" + field + " angeklickt");

    if (myTurn && !gameOver && enemyLoggedIn){

        if (kill){
            killStone(ring, field);
        }
        else {  // put-Phase
                if (game.round < 18 && !kill){
                    putStone(ring, field);
                    return;
                    }

                // move-Phase
                if (game.round >= 18 && !kill){
                    if (moveTakePosition == null){
                        moveTakePosition = moveStoneTakeStep(ring,field);

                    }
                    else {
                        moveReleasePosition = moveStoneReleaseStep(ring, field);
                        console.log(moveReleasePosition)
                        //Stein an gleiche Position zurücklegen
                        if (moveTakePosition.ring === moveReleasePosition.ring && moveTakePosition.field === moveReleasePosition.field){
                            moveTakePosition = null;
                            fadeStone(ring, field, 500, true)}
                        //Stein an neue Position setzen
                        else {
                            moveStone(new Move(moveTakePosition, moveReleasePosition))
                            moveTakePosition = null;
                            }

                        }
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

    if (isMyTurn){
        if (isKill){
            $('#spielverlaufLabel').text(name + " darf einen Stein entfernen")
            $("#putPhaseLabel").removeClass("putPhaseLabel");
            $("#movePhaseLabel").removeClass("movePhaseLabel");
            $("#killPhaseLabel").removeClass("killPhaseLabel");
            $("#killPhaseLabel").addClass("killPhaseLabel");

        }
        else {
            if (game.round <18){
                $('#spielverlaufLabel').text(name + " darf einen Stein setzen")
                $("#putPhaseLabel").removeClass("putPhaseLabel");
                $("#putPhaseLabel").removeClass("movePhaseLabel");
                $("#putPhaseLabel").removeClass("killPhaseLabel");
                $("#putPhaseLabel").addClass("putPhaseLabel");
            }
            else {
                $('#spielverlaufLabel').text(name + " darf einen Stein bewegen")
                $("#putPhaseLabel").removeClass("putPhaseLabel");
                $("#movePhaseLabel").removeClass("movePhaseLabel");
                $("#putPhaseLabel").removeClass("killPhaseLabel");
                $("#movePhaseLabel").addClass("movePhaseLabel");
            }
        }
    }
    else {
        if (isKill){
            $('#spielverlaufLabel').text(enemyName + " darf einen Stein entfernen")
            $("#putPhaseLabel").removeClass("putPhaseLabel");
            $("#movePhaseLabel").removeClass("movePhaseLabel");
            $("#killPhaseLabel").removeClass("killPhaseLabel");

        }
        else {
            if (game.round <18){
                $('#spielverlaufLabel').text(enemyName + " darf einen Stein setzen")
                $("#putPhaseLabel").removeClass("putPhaseLabel");
                $("#movePhaseLabel").removeClass("movePhaseLabel");
                $("#killPhaseLabel").removeClass("killPhaseLabel");
            }
            else {
                $('#spielverlaufLabel').text(enemyName + " darf einen Stein bewegen")
                $("#putPhaseLabel").removeClass("putPhaseLabel");
                $("#movePhaseLabel").removeClass("movePhaseLabel");
                $("#killPhaseLabel").removeClass("killPhaseLabel");
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

               }
               , delay)}
       else {
           alert("Dieses Feld ist nicht frei");
       }
    }



function moveStoneTakeStep(ring, field){
    if (game.board.isThisMyStone(new Position(ring, field), playerIndex)){
        fadeStone(ring, field, 500, false);
        return new Position(ring, field);}
    else {
        alert("Auf diesem Feld befindet sich keiner deiner Steine")}
}


function moveStoneReleaseStep(ring, field){
    var position = new Position(ring, field);
    if (game.board.isFieldFree(position) || (moveTakePosition.ring == ring && moveTakePosition.field == field)){
        return position;}
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
            }
            , delay)}
    else {
        alert("Das ist kein gültiger Zug")
        fadeStone(move.fromPosition.getRing(), move.fromPosition.getField(), 500, true)}
}

    function killStone(ring, field){
        if (game.board.checkKill(new Position(ring, field), 1-playerIndex)){
            console.log("Kill an Server senden");
            game.board.clearStone(new Position(ring, field));

            clearStoneGraphic(ring, field, playerIndex);

            if (game.board.countPlayersStones(1-playerIndex) < 3 && game.round > 18){
                gameOver = true;
                alert(name + " hat das Spiel gewonnen!")
                $('#spielverlaufLabel').text(name + " hat das Spiel gewonnen")
                $("#putPhaseLabel").removeClass("putPhaseLabel");
                $("#movePhaseLabel").removeClass("movePhaseLabel");
                $("#killPhaseLabel").removeClass("killPhaseLabel");

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
                }
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


    }
    else {
        if (color == "WHITE"){
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneBlack.png" height="100%" width="100%"/>');}
        if (color == "BLACK"){
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneWhite.png" height="100%" width="100%"/>');}



    }

    if (index == 0){
        player1PutStones++;
        $("#putLabel0").text("Steine gesetzt: " + player1PutStones)
    }
    else {
        player2PutStones++;
        $("#putLabel1").text("Steine gesetzt: " + player2PutStones)
    }


}



    function fadeStone(ring, field, speed, fadeIn) {
        if (fadeIn){
            $('#'.concat("r").concat(ring).concat("f").concat(field)).find("img").fadeTo(speed, 1);
        }
        else {
        $('#'.concat("r").concat(ring).concat("f").concat(field)).find("img").fadeTo(speed, 0.5);
        }

    }



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

    function clearStoneGraphic(ring, field, killingPlayerIndex){

        if (killingPlayerIndex == 0){
            player1KilledStones++;
            player2LostStones++;
            $("#killedLabel0").text("Steine gewonnen: " + player1KilledStones)
            $("#lostLabel1").text("Steine verloren: " + player2LostStones)
        }
        else {
            player2KilledStones++;
            player1LostStones++;
            $("#killedLabel1").text("Steine gewonnen: " + player2KilledStones)
            $("#lostLabel0").text("Steine verloren: " + player1LostStones)

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

