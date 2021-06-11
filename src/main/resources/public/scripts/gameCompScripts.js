var gameComp
var currentIndexComp = 0;
var color1Comp;
var color2Comp;
var nextStepAuto


    function startAndStopGame(checked) {

        if (checked) {
            clearInterval(nextStepAuto)
            console.log("pause")
        } else {
            nextStepAuto = setInterval(nextStep, 1000)

        }
    }

    function nextStep(){
        if(gameComp.round < 18){
            putComp()
        }
        else {
            moveComp()
        }

    }

    function putComp(){
        console.log("put")
        fetch("/gameComp/controller/put", {
            method: 'POST',
            body: JSON.stringify({
                "gameCode": gameComp.gamecode,
                "uuid" : gameComp.getComputerPlayerByIndex(currentIndexComp).uuid
            }),
            headers: {
                "Content-type": "application/json"
            }
        })
            .then(resp => resp.json())
            .then(responseData => {
                console.log(responseData);
                let position = new Position(responseData.ring, responseData.field)
                gameComp.board.putStone(position, currentIndexComp)
                console.log(gameComp.board.toString())
                putStoneGraphicComp(responseData.ring, responseData.field, currentIndexComp)
                if (gameComp.board.checkMorris(position) && gameComp.board.isThereStoneToKill(1-currentIndexComp)){
                    killComp()
                }
                else {
                    currentIndexComp = 1 - currentIndexComp;
                }
                increaseRoundComp();
            })
    }

function moveComp(){
    console.log("move")
    fetch("/gameComp/controller/move", {
        method: 'POST',
        body: JSON.stringify({
            "gameCode": gameComp.gamecode,
            "uuid" : gameComp.getComputerPlayerByIndex(currentIndexComp).uuid
        }),
        headers: {
            "Content-type": "application/json"
        }
    })
        .then(resp => resp.json())
        .then(responseData => {
            console.log(responseData);
            let fromPosition = new Position(responseData.moveFromRing, responseData.moveFromField);
            let toPosistion = new Position(responseData.moveToRing, responseData.moveToField);
            let move = new Move(fromPosition, toPosistion);
            gameComp.board.move(move, currentIndexComp)
            console.log(gameComp.board.toString())
            moveStoneGraphicComp(move, currentIndexComp)
            if (gameComp.board.checkMorris(move.toPosition) && gameComp.board.isThereStoneToKill(1-currentIndexComp)){
                killComp()
            }
            else {
                currentIndexComp = 1 - currentIndexComp;
            }
            increaseRoundComp();
        })
}

    function killComp(){
        console.log("kill")
        fetch("/gameComp/controller/kill", {
            method: 'POST',
            body: JSON.stringify({
                "gameCode": gameComp.gamecode,
                "uuid" : gameComp.getComputerPlayerByIndex(currentIndexComp).uuid
            }),
            headers: {
                "Content-type": "application/json"
            }
        })
            .then(resp => resp.json())
            .then(responseData => {
                console.log(responseData);
                let position = new Position(responseData.ring, responseData.field)
                gameComp.board.clearStone(position)
                clearStoneGraphicComp(responseData.ring, responseData.field, true)
                console.log(gameComp.board.toString())
                currentIndexComp = 1 - currentIndexComp;
    })}

function putStoneGraphicComp(ring, field, index){

    if (index == 0){
        if (color1Comp == "BLACK"){
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneBlack.png" height="100%" width="100%"/>');}
        if (color1Comp == "WHITE"){
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneWhite.png" height="100%" width="100%"/>');}

        putStones++;
        $("#putLabel0").text("Steine gesetzt: " + putStones)
    }
    else {
        if (color2Comp == "WHITE"){
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneWhite.png" height="100%" width="100%"/>');}
        if (color2Comp == "BLACK"){
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneBlack.png" height="100%" width="100%"/>');}

        enemyPutStones++;
        $("#putLabel1").text("Steine gesetzt: " + enemyPutStones)

    }}

function clearStoneGraphicComp(ring, field, index){

    if (index == 0){
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

function moveStoneGraphicComp(move, index) {

    $('#'.concat("r").concat(move.fromPosition.getRing()).concat("f").concat(move.fromPosition.getField())).empty();


    let ring = move.toPosition.getRing();
    let field = move.toPosition.getField();

    if (index == 0) {
        if (color1Comp == "BLACK") {
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneBlack.png" height="100%" width="100%"/>');
        }
        if (color1Comp == "WHITE") {
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneWhite.png" height="100%" width="100%"/>');
        }

    } else {
        if (color2Comp == "WHITE") {
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneWhite.png" height="100%" width="100%"/>');
        }
        if (color2Comp == "BLACK") {
            $('#'.concat("r").concat(ring).concat("f").concat(field)).prepend('<img src="images/StoneBlack.png" height="100%" width="100%"/>');
        }
    }
}

    function increaseRoundComp(){
        gameComp.round++;
        $("#rundeText").text("Spielrunde: " + gameComp.round)

    }



