function askIfSetComplete(){

    let gameCode = document.getElementById("gameCode").innerHTML;
    gameCode = gameCode.substr(10);

    fetch("/game/controller/menschVsMensch/checkIfSetComplete", {
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
        })
        .catch(failure => document.getElementById("ceckIfCompleteText").innerText = "KEIN SPIELER WURDE GEFUNDEN")

    function sendGetRequestGameHTML(){
        fetch('/game/controller/gameHTML', {method: 'GET'})
            .then((response) => {
                return response.json();
            })
            .then((html) => {
                document.head.innerHTML = html
                document.body.innerHTML = html
            });
    }
}