var joined = false;

function askIfSetComplete(){

    let gameCode = document.getElementById("gameCodeH2").innerHTML;
    gameCode = gameCode.substr(10);

    fetch("/index/controller/menschVsMensch/checkIfGameComplete", {
        method: 'POST',
        body: JSON.stringify({
            "gameCode": gameCode
        }),
        headers: {
            "Content-type": "application/json"
        }
    })  .then(response => response.json())
        .then(data => {
            document.getElementById("ceckIfCompleteText").innerText = data.player2Name.concat(" ist beigetreten");
            console.log(data)
            document.getElementById("startGameButton").disabled = false;
            joined = true;
        })
        .catch(failure => document.getElementById("ceckIfCompleteText").innerText = "KEIN SPIELER WURDE GEFUNDEN")
}

function autoRefreshJoin(){
    setInterval(function () {
        if (!joined){
            askIfSetComplete();}}
            , 10000)
}


