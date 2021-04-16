function askIfSetComplete(){

    let gameCode = document.getElementById("gameCode").innerHTML;
    gameCode = gameCode.substr(10);

    fetch("/game/controller/menschVsMensch/checkPlayerSetComplete", {
        method: 'POST',
        body: JSON.stringify({
            "gameCode": gameCode
        }),
        headers: {
            "Content-type": "application/json"
        }
    })
        /*.then(resp => resp.json())
        .then(data => console.log(data))*/
}