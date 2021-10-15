function giveUp(){

    var r = confirm("Wollen Sie wirklich aufgeben? Mit dem Aufgeben verlieren Sie das Spiel.");
    if (r == true) {
        sendMessage(websocket, JSON.stringify({
            "gameCode": game.gamecode,
            "playerUuid": game.player.getUuid(),
            "command" : "giveup",
            "name" : name}))
        getIndexPage();
    }

    else {
        alert("Schön, dass Sie dranbleiben, weiterhin viel Glück!")
    }

}

function sendChatMessage(){

    let name = game.player.name;
    let message = document.getElementById("messageLine").value


    $('#messageBox').append(name + ": " + message + "\n");
    document.getElementById('messageLine').value = "";
    $('#messageBox').scrollTop = $('#messageBox')[0].scrollHeight; // sollte nach unten scrollen, tut es aber noch nicht


    sendMessage(websocket, JSON.stringify({
        "gameCode": game.gamecode,
        "playerUuid": game.player.getUuid(),
        "command" : "chat",
        "name" : name,
        "message" : message}));

}




