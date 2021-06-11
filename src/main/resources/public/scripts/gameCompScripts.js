var gameComp
var currentIndexComp = 0;


    function startAndStopGame(checked){
    if (checked){
        putComp()

    }

    else {
        console.log("pause")
    }

    }

    function putComp(){
        console.log("play")
        fetch("/gameComp/controller/put", {
            method: 'POST',
            body: JSON.stringify({
                "gameCode": gameComp.gamecode,
                "uuid" : gameComp.getComputerPlayerByIndex(currentIndexComp).uuid
            }),
            headers: {
                "Content-type": "application/json"
            }
        })
            .then(resp => resp.json())
            .then(responseData => {
                console.log(responseData);
                let position = new Position(responseData.ring, responseData.field)
                gameComp.board.putStone(position, currentIndexComp)
                console.log(gameComp.board.toString())
                if (gameComp.board.checkMorris(position) && gameComp.board.isThereStoneToKill(1-currentIndexComp)){
                    killComp()
                }
                else {
                    currentIndexComp = 1 - currentIndexComp;
                }
            })
    }

    function killComp(){
        fetch("/gameComp/controller/kill", {
            method: 'POST',
            body: JSON.stringify({
                "gameCode": gameComp.gamecode,
                "uuid" : gameComp.getComputerPlayerByIndex(currentIndexComp).uuid
            }),
            headers: {
                "Content-type": "application/json"
            }
        })
            .then(resp => resp.json())
            .then(responseData => {
                console.log(responseData);
                let position = new Position(responseData.ring, responseData.field)
                gameComp.board.clearStone(position)
                console.log(gameComp.board.toString())
                currentIndexComp = 1 - currentIndexComp;
    })}



