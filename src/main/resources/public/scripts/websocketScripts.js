function doConnect(){
    websocket = new WebSocket("ws://localhost:8080/board");
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
}

function onOpen(evt){
    //alert("Websocket open")
}

function onClose(evt){
    alert("Websocket closed")
}

function onMessage(evt){

    var message = JSON.parse(evt.data);

    if (message.command == "start"){
        console.log("Spiel " + message.gameCode + " eröffnet")
    }

    if (message.command == "join" && message.playerUuid != uuid){
        window.enemyName = message.player2Name
        window.enemyLoggedIn = true;
        $('#player2NameGameText').text("Player 2: " + enemyName)
        console.log(message.player2Name + " ist dem Spiel beigetreten")
    }

    if (message.command == "chat" && message.playerUuid != uuid){
        console.log(message);
        $('#messageBox').append(message.name + ": " + message.message + "\n");
    }


    if (message.command == "update" && message.playerUuid != uuid){

        console.log(evt.data)

        if (message.action == "put"){
            let position = new Position(message.ring, message.field);
            game.board.putStone(position, message.playerIndex);
            putStoneGraphic(message.ring, message.field, message.playerIndex);
            increaseRound();
            if (game.board.checkMorris(position) && game.board.isThereStoneToKill(playerIndex)){
                editMyTurn(false, true)
            }
            else {
                editMyTurn(true, false)
            }
        }

        if (message.action == "move"){
            let from = new Position(message.moveFromRing, message.moveFromField);
            let to = new Position(message.moveToRing, message.moveToField);
            let move = new Move(from, to)
            console.log(move)
            game.board.move(move, message.playerIndex);
            moveStoneGraphic(move, message.playerIndex);
            increaseRound();
            if (game.board.checkMorris(to) && game.board.isThereStoneToKill(playerIndex)){
                editMyTurn(false, true)
            }
            else {
                editMyTurn(true, false)
            }
        }

        if (message.action == "kill"){
            let position = new Position(message.ring, message.field);
            game.board.clearStone(position);
            clearStoneGraphic(message.ring, message.field, false);
            if (game.board.countPlayersStones(playerIndex) < 3 && game.round > 18){
                gameOver = true;
                alert("Sie haben das Spiel verloren")
            }
            else {
                editMyTurn(true, false);}

        }
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