function doConnect(){
    websocket = new WebSocket("ws://localhost:443/board");
    //websocket = new WebSocket("ws://217.160.10.113:443/board");
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
}

function onOpen(evt){
    //alert("Websocket open")
}

function onClose(evt){
    alert("Die Verbindung zum Server ist abgebrochen. Sie werden auf die Startseite zurückgeleitet.")
    getIndexPage();
}

function onMessage(evt){

    var incommingMessage = JSON.parse(evt.data);

    if (incommingMessage.command == "start"){
        console.log("Spiel " + incommingMessage.gameCode + " eröffnet")
        $("#messageLine").prop('disabled', true);
        $("#messageButton").prop('disabled', true);
        $("#complimentEnemyButton").prop('disabled', true);
        $("#offendEnemyButton").prop('disabled', true);
    }

    if (incommingMessage.command == "join" && incommingMessage.playerUuid != uuid){
        window.enemyName = incommingMessage.player2Name
        window.enemyLoggedIn = true;
        $('#player2NameGameText').text("Player 2: " + enemyName)
        console.log(incommingMessage.player2Name + " ist dem Spiel beigetreten")
        $("#messageLine").prop('disabled', false);
        $("#messageButton").prop('disabled', false);
        $("#complimentEnemyButton").prop('disabled', false);
        $("#offendEnemyButton").prop('disabled', false);
        $("#putPhaseLabel").addClass("putPhaseLabel");
    }

    if (incommingMessage.command == "chat" && incommingMessage.playerUuid != uuid){
        console.log(incommingMessage);
        putMessageToMessageBox(incommingMessage.name, incommingMessage.message);
    }

    if (incommingMessage.command == "giveup" && incommingMessage.playerUuid != uuid){
        console.log(incommingMessage);
        alert(incommingMessage.name + " hat aufgeben und verliert das Spiel. Sie werden zur Startseite zurückgeleitet...");
        getIndexPage();
    }

    if (incommingMessage.command == "timeout"){
        console.log(incommingMessage);
        alert("Die Zeit maximale Zeit für dieses Spiel ist abgelaufen. Sie werden zur Startseite zurückgeleitet...");
        getIndexPage();
    }

    if (incommingMessage.command == "roboterConnected"){
        console.log(incommingMessage);
        game.setRoboterConnected(true);
        $("#roboterConnectedLabel").addClass("roboterConnectedLabel");
    }

    if (incommingMessage.command == "gameOver"){
        console.log(incommingMessage);

        $("#putPhaseLabel").removeClass("putPhaseLabel");
        $("#movePhaseLabel").removeClass("movePhaseLabel");
        $("#killPhaseLabel").removeClass("killPhaseLabel");
        $("#giveUpButton").prop("value", "ZUM MENÜ");
        $("#giveUpButton").css("background","#33cc66");
        $("#giveUpButton").css("-webkit-text-fill-color", "BLACK");

        gameOver = true;

        if (incommingMessage.playerIndex == playerIndex){
            alert(enemyName + " hat das Spiel gewonnen!")
            $('#spielverlaufLabel').text(enemyName + " hat das Spiel gewonnen!")


        }
        else {
            alert(name + " hat das Spiel gewonnen!")
            $('#spielverlaufLabel').text(name + " hat das Spiel gewonnen!")
        }
    }


    if (incommingMessage.command == "update" && incommingMessage.playerUuid != uuid){

        console.log(evt.data)

        if (incommingMessage.action == "put"){

            let position = new Position(incommingMessage.ring, incommingMessage.field);
            game.board.putStone(position, incommingMessage.playerIndex);
            putStoneGraphic(incommingMessage.ring, incommingMessage.field, incommingMessage.playerIndex);
            increaseRound();
            if (game.board.checkMorris(position) && game.board.isThereStoneToKill(playerIndex)){
                editMyTurn(false, true)
            }
            else {
                editMyTurn(true, false)
            }
        }

        if (incommingMessage.action == "move"){
            let from = new Position(incommingMessage.moveFromRing, incommingMessage.moveFromField);
            let to = new Position(incommingMessage.moveToRing, incommingMessage.moveToField);
            let move = new Move(from, to)
            let playerIndex = incommingMessage.playerIndex;
            console.log(move)
            game.board.move(move, playerIndex);
            moveStoneGraphic(move, playerIndex);
            increaseRound();
            if (game.board.checkMorris(to) && game.board.isThereStoneToKill(playerIndex)){
                editMyTurn(false, true)
            }
            else {
                editMyTurn(true, false)
            }
        }

        if (incommingMessage.action == "kill"){
            let position = new Position(incommingMessage.ring, incommingMessage.field);
            let index = 1-game.board.getFieldContent(position);
            game.board.clearStone(position);

            clearStoneGraphic(incommingMessage.ring, incommingMessage.field, index);

            editMyTurn(true, false);

        }

        if (game.isRoboterConnected()){

            setRoboterWaiting(true)
            console.log("Auf Roboter warten")

            setTimeout(function() {
                setRoboterWaiting(false);
                console.log("Fertig gewartet")
            }, 15000)
        }
    }

    if (incommingMessage.command == "exception"){
        console.log(incommingMessage.details);
    }




}

function onError(evt){
    alert("Websocket Error")
}

// https://dev.to/ndrbrt/wait-for-the-websocket-connection-to-be-open-before-sending-a-message-1h12
const waitForOpenConnection = (socket) => {
    return new Promise((resolve, reject) => {
        const maxNumberOfAttempts = 100
        const intervalTime = 200 //ms

        let currentAttempt = 0
        const interval = setInterval(() => {
            if (currentAttempt > maxNumberOfAttempts - 1) {
                clearInterval(interval)
                reject(new Error('Maximum number of attempts exceeded'))
            } else if (socket.readyState === socket.OPEN) {
                clearInterval(interval)
                resolve()
            }
            currentAttempt++
        }, intervalTime)
    })
}

// https://dev.to/ndrbrt/wait-for-the-websocket-connection-to-be-open-before-sending-a-message-1h12
const sendMessage = async (socket, msg) => {
    if (socket.readyState !== socket.OPEN) {
        try {
            await waitForOpenConnection(socket)
            socket.send(msg)
        } catch (err) { console.error(err) }
    } else {
        socket.send(msg)
    }
}