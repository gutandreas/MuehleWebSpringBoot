function giveUp(){

    if (!gameOver) {
        var r = confirm("Wollen Sie wirklich aufgeben? Mit dem Aufgeben verlieren Sie das Spiel.");
        if (r == true) {
            sendMessage(websocket, JSON.stringify({
                "gameCode": game.gamecode,
                "playerUuid": game.player.getUuid(),
                "command": "giveup",
                "name": name
            }))
            getIndexPage();
        } else {
            alert("Schön, dass Sie dranbleiben, weiterhin viel Glück!")
        }
    }
    else {
        getIndexPage();
    }

}

function complimentEnemy(){

    let text = ["Cleverer Zug!",
            "Du spielst beeindruckend!",
            "Gute Strategie!",
            "Du bist ein wirklich harter Gegner!",
            "Wow, der war gut!",
            "Echt stark gespielt!",
            "Saubere Leistung!",
            "Du spielst gut!",
            "Du machst mir das Leben schwer!",
            "Gut gespielt!"];

    let message = text[getRandomNumber(0, text.length-1)]

    sendMessage(websocket, JSON.stringify({
        "gameCode": game.gamecode,
        "playerUuid": game.player.getUuid(),
        "command" : "chat",
        "name" : name,
        "message" : message}))

    putMessageToMessageBox(name, message)
}

function offendEnemy(){

    let text = ["Uuuuuh, das war blöd...",
        "Mein Cousin spielt besser. ...und der ist 3.",
        "Das war ja gar nix...",
        "Und so willst du gewinnen?",
        "Deine Strategie ist... ...speziell.",
        "Effiziente Strategie, um zu verlieren.",
        "Also so gewinnst du garantiert nicht!",
        "Meine Grossmutter gewinnt gegen dich im Schlaf!",
        "Meinst du diesen Zug wirklich ernst?",
        "Hoffentlich gibt's Spiele, die du besser spielst!",
        "Hoffentlich hast du ein anderes Talent!",
        "Ist das wirklich alles, was du kannst?",
        "Ist dein Gehirn schon an?",
        "Weisst du wirklich, was das Ziel des Spiels ist?",
        "Fährst du nebenbei noch Auto?",
        "Du bist ein guter Egobooster für mich!",
        "Soll ich dir das Ziel des Spiels nochmals erklären?"];

    let message = text[getRandomNumber(0, text.length-1)]

    sendMessage(websocket, JSON.stringify({
        "gameCode": game.gamecode,
        "playerUuid": game.player.getUuid(),
        "command" : "chat",
        "name" : name,
        "message" : message}))

    putMessageToMessageBox(name, message)

}

function getRandomNumber(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function sendChatMessage(){

    if (document.getElementById("messageLine").value != ""){
        let name = game.player.name;
        let message = document.getElementById("messageLine").value


        sendMessage(websocket, JSON.stringify({
            "gameCode": game.gamecode,
            "playerUuid": game.player.getUuid(),
            "command" : "chat",
            "name" : name,
            "message" : message}));

        document.getElementById('messageLine').value = "";
        putMessageToMessageBox(name, message)
    }
    else {
        alert("Sie können keine leeren Nachrichten senden!")
    }

}


function putMessageToMessageBox(name, message){
    if (document.getElementById("messageBox").value.length != 0){
        $('#messageBox').append("\n");
    }
    $('#messageBox').append(name + ": " + message);
    $('#messageBox').scrollTop($('#messageBox')[0].scrollHeight);

}





