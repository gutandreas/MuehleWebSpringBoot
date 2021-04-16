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
    })
        .then(resp => resp.text())
        .then(data => {
            console.log(data)
            document.getElementById("notCompleteText").innerText = data;
        })
}