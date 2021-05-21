array = Array(3).fill(9).map(x => Array(8).fill(9));
var phase = 1;
var kill = false;
var playerId = 0;
var gameId;


function clickOnField(row, field){
    console.log("Angeklicktes Feld: Reihe ".concat(row).concat(" , Feld: ").concat(field));

    switch (phase) {

        case 1: {

            if (isFieldFree(row, field)) {
                array[row][field] = playerId;
                fetch("/game/controller/put", {
                    method: 'POST',
                    body: JSON.stringify({

                        "gameCode" : document.getElementById("gameCodeH2").innerText,
                        "playerUuid" : document.getElementById("playerUuidH2").innerText,
                        "putRing" : row,
                        "putField" : field
                    }),
                    headers: {
                        "Content-type": "application/json"
                    }
                })

            } else {
                alert("Das Feld ist nicht frei.")
            }
            break;}
        case 2: {

        }
    }


}

function isFieldFree(row, field){
    if (array[row][field] == 9){
        return true;}
}