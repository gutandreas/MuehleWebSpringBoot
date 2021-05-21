array = Array(3).fill(9).map(x => Array(8).fill(9));
var phase = 1;
var kill = false;
var myTurn = true;
var playerId = 0;
var gameId;

function setCursor(cursorURL){
    document.getElementById("boardImage").style.cursor = "url('images/StoneBlackCursor.png'), auto";
}


function setMyTurn(myTurn){
    window.myTurn = myTurn;
}

function clickOnField(ring, field){
    console.log("Angeklicktes Feld: Reihe ".concat(ring).concat(" , Feld: ").concat(field));

    switch (phase) {

        case 1: {

            if (myTurn && isFieldFree(ring, field)) {
                myTurn = false;
                array[ring][field] = playerId;
                fetch("/game/controller/put", {
                    method: 'POST',
                    body: JSON.stringify({
                        "gameCode" : document.getElementById("gameCodeH2").innerText,
                        "playerUuid" : document.getElementById("playerUuidH2").innerText,
                        "putRing" : ring,
                        "putField" : field
                    }),
                    headers: {
                        "Content-type": "application/json"
                    }
                })

            } else {
                alert("Du bist nicht an der Reihe oder das Feld ist nicht frei.")
            }
            break;}
        case 2: {


        }
    }


}

function isFieldFree(ring, field){
    return array[ring][field] == 9;
}

function isThisMyStone(ring, field){
    return array[ring][field] == playerId;
}