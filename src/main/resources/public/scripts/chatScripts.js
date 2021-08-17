function sendChatMessage(){

    let name = game.player.name;
    let message = document.getElementById('messageLine').value

    $('#messageBox').append(name + ": " + message + "\n");
    document.getElementById('messageLine').value = "";
    $('#messageBox').scrollTop = $('#messageBox')[0].scrollHeight;


    sendMessage(websocket, JSON.stringify({
        "gameCode": game.gamecode,
        "playerUuid": game.player.getUuid(),
        "command" : "chat",
        "name" : name,
        "message" : message}));


}