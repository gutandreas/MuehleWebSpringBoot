var myTurn = true;
var kill = false;
var game;
var uuid;
var playerIndex;
var color;
var name;
var enemyName;
var gameOver = false;
var watingForRoboter = false;
var enemyLoggedIn = true;

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

    if (myTurn && !gameOver && enemyLoggedIn && !watingForRoboter){

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
            return;
        }
        if (!myTurn){
            alert("Du bist nicht an der Reihe. Warte auf den Zug des Gegenspielers");
            return;
        }
        if (!enemyLoggedIn){
            alert("Bitte warten Sie, bis Ihr Gegenspieler eingeloggt ist")
            return;
        }
        if (watingForRoboter){
            alert("Bitte warten Sie, der Roboter den Zug ausgeführt hat")
            return;
        }
    }
    }

function editMyTurn(isMyTurn, isKill){

    myTurn = isMyTurn;
    kill = isKill;

    if (isMyTurn){

        if (game.isRoboterWatching() || (game.isRoboterPlaying() && isKill)){
            waitForRoboter();
        }


        if (isKill){
            $('#spielverlaufLabel').text(name + " darf einen Stein entfernen");
            removeAllPhaseLabelClasses();
            $("#killPhaseLabel").addClass("killPhaseLabel");

        }
        else {
            if (game.round < 18){
                $('#spielverlaufLabel').text(name + " darf einen Stein setzen");
                removeAllPhaseLabelClasses();
                $("#putPhaseLabel").addClass("putPhaseLabel");
            }
            else {
                $('#spielverlaufLabel').text(name + " darf einen Stein bewegen");
                removeAllPhaseLabelClasses();
                $("#movePhaseLabel").addClass("movePhaseLabel");
            }
        }
    }
    else {
        if (isKill){
            $('#spielverlaufLabel').text(enemyName + " darf einen Stein entfernen");
            removeAllPhaseLabelClasses();

        }
        else {
            if (game.round < 18){
                $('#spielverlaufLabel').text(enemyName + " darf einen Stein setzen")
                removeAllPhaseLabelClasses()
            }
            else {
                $('#spielverlaufLabel').text(enemyName + " darf einen Stein bewegen")
                removeAllPhaseLabelClasses()
            }
        }
    }
}

function removeAllPhaseLabelClasses(){
    $("#putPhaseLabel").removeClass("putPhaseLabel");
    $("#movePhaseLabel").removeClass("movePhaseLabel");
    $("#killPhaseLabel").removeClass("killPhaseLabel");
}

function waitForRoboter(){
    setRoboterWaiting(true)
    console.log("Auf Roboter warten")
    $("#progressbar").addClass("progress");

    setTimeout(function() {
        setRoboterWaiting(false);
        console.log("Fertig gewartet")
        $("#progressbar").removeClass("progress");
    }, 10000)
}






function putStone(ring, field){

   if (game.board.isPositionFree(new Position(ring, field))){
       console.log("Put an Server senden");
       game.board.putStone(new Position(ring, field), playerIndex)
       console.log("Spielrunde: " + game.round);
       increaseRound();

       putStoneGraphic(ring, field, playerIndex);

           if (game.board.isInMorris(new Position(ring, field))
               && (game.board.canPlayerKill(playerIndex)
                   || (game.board.numberOfStonesOf(1-playerIndex) == 3 && game.round > 18))){

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
                   "callComputer": !myTurn,
                   "triggerAxidraw": true}))

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
    if (game.board.isPositionFree(position) || (moveTakePosition.ring == ring && moveTakePosition.field == field)){
        return position;}
    else {
        alert("Dieses Feld ist nicht frei")}
}


function moveStone(move){

    if (game.board.isMovePossibleAt(move, game.board.numberOfStonesOf(playerIndex) == 3)){
        console.log("Move an Server senden");
        game.board.moveStone(move, playerIndex);
        console.log("Spielrunde: " + game.round);
        increaseRound();

        moveStoneGraphic(move, playerIndex);


        if (game.board.isInMorris(move.toPosition)
            && (game.board.canPlayerKill(playerIndex)
                || (game.board.numberOfStonesOf(1-playerIndex) == 3 /*&& game.round > 18*/))){

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
                    "callComputer": !myTurn,
                    "triggerAxidraw": true}))
            }
            , delay)}
    else {
        alert("Das ist kein gültiger Zug")
        fadeStone(move.fromPosition.getRing(), move.fromPosition.getField(), 500, true)}
}

function killStone(ring, field){
    if (game.board.isKillPossibleAt(new Position(ring, field), 1-playerIndex)){
        console.log("Kill an Server senden");
        game.board.removeStone(new Position(ring, field));

        clearStoneGraphic(ring, field, playerIndex);

        editMyTurn(false, false);

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
                    "callComputer": true,
                    "triggerAxidraw": true
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

function setRoboterWaiting(waiting){
    watingForRoboter = waiting;
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

