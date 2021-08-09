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
    console.log(evt.data)

    var message = JSON.parse(evt.data);

    if (message.command == "join" && message.playerUuid != uuid){
        window.enemyName = message.player2Name;
        window.enemyLoggedIn = true;
        $('#player2NameGameText').text("Player 2: " + message.player2Name)
    }

    if (message.command == "update" && message.playerUuid != uuid){

        let changedPositions = game.board.updateBoardAndGetChanges(message.boardAsString);
        if (message.action == "put"){
            updateBoardAfterEnemysPut(changedPositions)}
        if (message.action == "move"){
            updateBoardAfterEnemysMove(changedPositions)}
        if (message.action == "kill"){
            updateBoardAfterEnemysKill(changedPositions)}
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