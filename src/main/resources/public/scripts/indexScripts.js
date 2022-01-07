var modus = 1;
var gamecodemodus = 1;
var websocket;


function sendData() {

    switch (modus){
        case 1: {

            if (gamecodemodus == 1) {
                checkAndSendDataMenschVsMenschStart();
            }

            if (gamecodemodus == 2) {
                checkAndSendDataMenschVsMenschJoin();
            }

            break;
        }
        case 2: {
            checkAndSendDataMenschVsComputer()
            break;
        }
        case 3: {
            checkAndSendDataGameWatch()
            break;
        }
    }
}

function checkAndSendDataMenschVsMenschStart() {

            let gameCodeStart = $("#gamecodeStart").val();
            let player1Name = $("#player1Textfield").val();

            if (gameCodeStart.length == 0){
                alert("Bitte geben Sie einen Gamecode ein")
                return
            }
            if (player1Name.length == 0){
                alert("Bitte geben Sie einen Namen ein")
                return
            }

            sendDataMenschVsMenschStart();


            /*fetch("/index/controller/ableToStart", {
                method: 'POST',
                body: JSON.stringify({
                    "gamecode": gameCodeStart
                })})
                .then(resp => resp.json())
                .then(responseData => {
                    console.log(responseData)
                    if (responseData.gamecodeOk){
                        sendDataMenschVsMenschStart();

                    }
                    else {
                        alert("Der Gamecode wird bereits verwendet. Bitte wählen Sie einen anderen Gamecode");
                    }
            })*/




}

function checkAndSendDataMenschVsMenschJoin() {

    let gameCodeJoin = $("#gamecodeJoin").val();
    let player2Name = $("#player2Textfield").val();

    if (gameCodeJoin.length == 0){
        alert("Bitte geben Sie einen Gamecode ein")
        return
    }
    if (player2Name.length == 0){
        alert("Bitte geben Sie einen Namen ein")
        return
    }

    sendDataMenschVsMenschJoin();

    /*fetch("/index/controller/ableToJoin", {
        method: 'POST',
        body: JSON.stringify({
            "gamecode": gameCodeJoin
        })})
        .then(resp => resp.json())
        .then(responseData => {
            console.log(responseData)
            if (responseData.gamecodeOk){
                sendDataMenschVsMenschJoin();
            }
            else {
                alert("Der Gamecode wird noch nicht verwendet oder das Spiel ist bereits komplett. Bitte kontrollieren Sie die Eingabe oder starten Sie ein neues Spiel.");
            }
        })*/
}

function checkAndSendDataMenschVsComputer() {

    let player1Name = $("#player1Textfield").val();

    if (player1Name.length == 0) {
        alert("Bitte geben Sie einen Namen ein")
        return}

    sendDataMenschVsComputer();

}


async function checkAndSendDataGameWatch(){
    let gameCodeJoin = $("#gamecodeWatch").val();

    if (gameCodeJoin.length == 0){
        alert("Bitte geben Sie einen Gamecode ein")
        return
    }

    sendDataGameWatch();

    /*fetch("/index/controller/ableToWatch", {
        method: 'POST',
        body: JSON.stringify({
            "gamecode": gameCodeJoin
        })})
        .then(resp =>
            {if (resp.ok){
                resp.json()}
            else {
                throw new Error("Der Gamecode wird noch nicht verwendet oder das Spiel wurde bereits begonnen. Bitte kontrollieren Sie die Eingabe.")
            }})
        .then(responseData => {
            console.log(responseData)
                })
        .then(() => sendDataGameWatch())
        .catch(e => alert(e.message))*/
}



function sendDataMenschVsMenschStart(){


        let player1Name = $("#player1Textfield").val();
        let gameCodeStart = $("#gamecodeStart").val();
        let player1Color;
        if ($("#colorPlayer1").prop('checked')){
        player1Color = "WHITE";}
        else {player1Color = "BLACK";}

        fetch("/index/controller/menschVsMensch/start", {
            method: 'POST',
            body: JSON.stringify({
                    "modus": 'Mensch vs. Mensch',
                    "startGame" : true,
                    "player1Name" : player1Name,
                    "gameCode" : gameCodeStart,
                    "player1Color" : player1Color
            }),
            headers: {
                "Content-type": "application/json"
            }
        })
            .then(resp => resp.json())
            .then(responseData => {
                console.log("MENSCHVSMENSCH")

                let head = responseData.html.match(/<head[^>]*>[\s\S]*<\/head>/gi);
                let body = responseData.html.match(/<body[^>]*>[\s\S]*<\/body>/gi);

                $("head").html(head);
                $("body").html(body);

                //document.body.innerHTML = responseData.html;
                console.log(responseData);
                $('#player1NameGameText').text("Player 1: " + player1Name)
                $('#modusH1').text("Mühle online – Spielmodus: Mensch vs. Mensch")
                $('#gameCodeH2').text("Gamecode: " + gameCodeStart)
                $('#playerUuidH2').text("UUID: " + responseData.player1Uuid)
                $('#playerIndexH2').text(responseData.playerIndex)


                if (player1Color == "BLACK"){
                    $('#player1StoneImage').attr('src', 'images/StoneBlack.png')
                    $('#player2StoneImage').attr('src', 'images/StoneWhite.png')
                    window.color = "BLACK";
                }
                else {
                    $('#player1StoneImage').attr('src', 'images/StoneWhite.png')
                    $('#player2StoneImage').attr('src', 'images/StoneBlack.png')
                    window.color = "WHITE";
                }

                window.game = new Game(new Player(player1Name, responseData.player1Uuid, responseData.player1Index), gameCodeStart, true);
                window.uuid = responseData.player1Uuid;
                window.playerIndex = responseData.player1Index;
                window.name = player1Name;
                window.myTurn = true;
                window.enemyLoggedIn = false;
                $("#spielverlaufLabel").text(player1Name + " startet das Spiel.")

                doConnect();
                sendMessage(websocket, JSON.stringify({
                    "gameCode" : gameCodeStart,
                    "command" : "start"
                }))



            })
            .catch(function(error) {
                console.log(error)
                alert("Dieser Gamecode wird bereits verwendet.")
            });
    }


function sendDataMenschVsMenschJoin(){

        let player2Name = $("#player2Textfield").val();
        let gameCodeJoin = $("#gamecodeJoin").val();

        fetch("/index/controller/menschVsMensch/join", {
            method: 'POST',
            body: JSON.stringify({
                    "modus": 'Mensch vs. Mensch',
                    "startGame" : false,
                    "player2Name" : player2Name,
                    "gameCode" : gameCodeJoin
            }),
            headers: {
                "Content-type": "application/json"
            }
        })
            .then(res => res.json())
            .then(responseData => {

                let head = responseData.html.match(/<head[^>]*>[\s\S]*<\/head>/gi);
                let body = responseData.html.match(/<body[^>]*>[\s\S]*<\/body>/gi);

                $("head").html(head);
                $("body").html(body);

                //document.body.innerHTML = responseData.html;
                console.log(responseData)
                $('#modusH1').text("Mühle online – Spielmodus: Mensch vs. Mensch")
                $('#gameCodeH2').text("Gamecode: " + gameCodeJoin)
                $('#playerUuidH2').text("UUID: " + responseData.player2Uuid)
                $("#player1NameGameText").text("Player 1: " + responseData.player1Name);
                $('#player2NameGameText').text("Player 2: " + player2Name);

                let player2Color = responseData.player2Color;

                if (player2Color == "BLACK"){
                    $('#player2StoneImage').attr('src', 'images/StoneBlack.png')
                    $('#player1StoneImage').attr('src', 'images/StoneWhite.png')
                    window.color = "BLACK";
                }
                else {
                    $('#player2StoneImage').attr('src', 'images/StoneWhite.png')
                    $('#player1StoneImage').attr('src', 'images/StoneBlack.png')
                    window.color = "WHITE";
                }


                window.game = new Game(new Player(player2Name, responseData.player2Uuid, responseData.player2Index), gameCodeJoin , false);

                if (responseData.roboterConnected){
                    game.setRoboterConnected(true)
                    $("#roboterConnectedLabel").addClass("roboterConnectedLabel");
                    if (responseData.roboterWaitingTime){
                        game.setRoboterNeedsWaitingTime(true);
                        console.log("Wartezeit in Game gesetzt")
                    }
                }

                window.uuid = responseData.player2Uuid;
                window.playerIndex = responseData.player2Index;
                window.name = player2Name;
                window.enemyName = responseData.player1Name;
                window.myTurn = false;
                $("#spielverlaufLabel").text(enemyName + " darf einen Stein setzen.")

                doConnect();
                sendMessage(websocket, JSON.stringify({
                    "gameCode" : gameCodeJoin,
                    "command" : "join",
                    "player2Name" : player2Name,
                    "playerUuid" : uuid
                }))

            })
            .catch(
                error => {
                    console.log(error)
                    alert("Der Gamecode wird noch nicht verwendet oder das Spiel ist bereits komplett. Bitte kontrollieren Sie die Eingabe oder starten Sie ein neues Spiel.")}
            );
}

function sendDataMenschVsComputer(){

    let player1Name = $('#player1Textfield').val();
    let player1Color;
    if ($("#colorPlayer1").prop('checked')){
        player1Color = "WHITE";}
    else {player1Color = "BLACK";}

    let computerName = $("#player2Dropdown option:selected").text();
    let computerLevel = $("#player2Dropdown option:selected").val();


    fetch("/index/controller/menschVsComputer", {
        method: 'POST',
        body: JSON.stringify({
                "modus": 'Mensch vs. Computer',
                "player1Name" : player1Name,
                "player1Color" : player1Color,
                "computerName" : computerName,
                "computerLevel" : computerLevel
        }),
        headers: {
            "Content-type": "application/json"
        }
    })
        .then(resp => resp.json())
        .then(responseData => {

            let head = responseData.html.match(/<head[^>]*>[\s\S]*<\/head>/gi);
            let body = responseData.html.match(/<body[^>]*>[\s\S]*<\/body>/gi);

            $("head").html(head);
            $("body").html(body);

            //document.body.innerHTML = responseData.html;
            console.log(responseData);
            $('#player1NameGameText').text("Player 1: " + player1Name)
            $('#player2NameGameText').text("Player 2: " + computerName)
            $('#modusH1').text("Mühle online – Spielmodus: Mensch vs. Computer")
            $('#gameCodeH2').text("Gamecode: " + responseData.gameCode)
            $('#player1UuidH2').text(responseData.player1Uuid)
            $('#playerIndexH2').text(responseData.playerIndex)

            $('#spielverlaufLabel').text(player1Name + " darf einen Stein setzen")
            $("#putPhaseLabel").addClass("putPhaseLabel");

            if (player1Color == "BLACK"){
                $('#player1StoneImage').attr('src', 'images/StoneBlack.png')
                $('#player2StoneImage').attr('src', 'images/StoneWhite.png')
                window.color = "BLACK";
            }
            else {
                $('#player1StoneImage').attr('src', 'images/StoneWhite.png')
                $('#player2StoneImage').attr('src', 'images/StoneBlack.png')
                window.color = "WHITE";
            }

            window.game = new Game(new Player(player1Name, responseData.player1Uuid, responseData.playerIndex), responseData.gameCode, true);
            window.uuid = responseData.player1Uuid;
            window.playerIndex = responseData.playerIndex;
            window.name = player1Name;
            window.enemyName = computerName;
            window.myTurn = true;

            doConnect();
            sendMessage(websocket, JSON.stringify({
                "gameCode" : responseData.gameCode,
                "command" : "start"
            }))



        })
        .catch(function(error) {
            console.log(error);
        });
}

function sendDataGameWatch(){

    let gameCode = $('#gamecodeWatch').val();


    fetch("/index/controller/gameWatch", {
        method: 'POST',
        body: JSON.stringify({
            "modus": 'Game Watch',
            "gameCode" : gameCode
        }),
        headers: {
            "Content-type": "application/json"
        }
    })
        .then(resp => resp.json())
        .then(responseData => {

            let head = responseData.html.match(/<head[^>]*>[\s\S]*<\/head>/gi);
            let body = responseData.html.match(/<body[^>]*>[\s\S]*<\/body>/gi);

            $("head").html(head);
            $("body").html(body);

            //document.body.innerHTML = responseData.html;
            console.log(responseData);
            $('#modusH1').text("Mühle online – Spielmodus: Spiel beobachten")
            $('#player1NameGameText').text("Player 1: " + responseData.player1Name)
            $('#player2NameGameText').text("Player 2: " + responseData.player2Name)
            $('#gameCodeH2').text("Gamecode: " + responseData.gameCodeWatch)

            $('#spielverlaufLabel').text(responseData.player1Name + " darf einen Stein setzen")

            if (responseData.player1Color == "BLACK"){
                $('#player1StoneImage').attr('src', 'images/StoneBlack.png')
                $('#player2StoneImage').attr('src', 'images/StoneWhite.png')
                window.color = "BLACK";
            }
            else {
                $('#player1StoneImage').attr('src', 'images/StoneWhite.png')
                $('#player2StoneImage').attr('src', 'images/StoneBlack.png')
                window.color = "WHITE";
            }

            window.game = new Game(new Player(responseData.player1Name, 0, 0), responseData.gameCode, true);
            window.playerIndex = responseData.player1Index;

            doConnect();
            sendMessage(websocket, JSON.stringify({
                "gameCode" : gameCode,
                "command" : "watch",
            }))



        })
        .catch(function(error) {
            console.log(error);
            alert("Der Gamecode existiert nicht oder das Game wurde bereits gestartet und kann deshalb nicht mehr beobachtet werden.")
        });
}



function sendDataComputerVsComputer(){

    let dropdown1 = document.getElementById("player1Dropdown");
    let computerName1 = dropdown1.options[dropdown1.selectedIndex].text;
    let computerCode1 = dropdown1.options[dropdown1.selectedIndex].value;

    let player1Color;
    if (document.getElementById("colorPlayer1").checked){
        player1Color = "WHITE";}
    else {player1Color = "BLACK";}

    let dropdown2 = document.getElementById("player2Dropdown");
    let computerName2 = dropdown2.options[dropdown2.selectedIndex].text;
    let computerCode2 = dropdown2.options[dropdown2.selectedIndex].value;

    fetch("/index/controller/computerVsComputer", {
        method: 'POST',
        body: JSON.stringify({
                "modus": 'Modus Computer vs. Computer',
                "computerName1" : computerName1,
                "computerCode1" : computerCode1,
                "computerName2" : computerName2,
                "computerCode2" : computerCode2
        }),
        headers: {
            "Content-type": "application/json"
        }
    })
        .then(resp => resp.json())
        .then(responseData => {
            console.log(responseData);
            $('#player1NameGameText').text("Player 1: " + computerName1);
            $('#player2NameGameText').text("Player2: " + computerName2);
            $('#modusH1').text("Mühle online – Spielmodus: Computer vs. Computer")
            if (player1Color == "BLACK"){
                $('#player1StoneImage').attr('src', 'images/StoneBlack.png')
                $('#player2StoneImage').attr('src', 'images/StoneWhite.png')
                color1Comp = "BLACK";
                color2Comp = "WHITE";
            }
            else {
                $('#player1StoneImage').attr('src', 'images/StoneWhite.png')
                $('#player2StoneImage').attr('src', 'images/StoneBlack.png')
                color1Comp = "WHITE";
                color2Comp = "BLACK";
            }
            window.gameComp = new GameComp(new Player(computerName1, responseData.uuid1, 0),
                new Player(computerName2, responseData.uuid2, 1), responseData.gameCode);
        })
        .catch(function(error) {
            console.log(error);
        });
}




