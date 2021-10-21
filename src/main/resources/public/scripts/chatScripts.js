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

function complimentEnemy(){

    let text = ["Cleverer Zug!", "Du spielst beeindruckend!", "Gute Strategie!", "Du bist ein wirklich harter Gegner!", "Wow, der war gut!"];

    let message = text[randomNumber(0, text.length-1)]

    sendMessage(websocket, JSON.stringify({
        "gameCode": game.gamecode,
        "playerUuid": game.player.getUuid(),
        "command" : "chat",
        "name" : name,
        "message" : message}))

    $('#messageBox').append(name + ": " + message + "\n");
}

function offendEnemy(){

    let text = ["Uuuuuh, das war blöd...", "Mein Cousin spielt besser. ...und der ist 3.", "Das war ja gar nix...", "Und so willst du gewinnen?", "Deine Strategie ist.... ...speziell."];

    let message = text[randomNumber(0, text.length-1)]

    sendMessage(websocket, JSON.stringify({
        "gameCode": game.gamecode,
        "playerUuid": game.player.getUuid(),
        "command" : "chat",
        "name" : name,
        "message" : message}))

    $('#messageBox').append(name + ": " + message + "\n");
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

function randomNumber(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}





