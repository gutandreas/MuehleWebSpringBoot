var modus = 1;
var gamecodemodus = 1;

function sendData() {

    switch (modus){
        case 1: {
            if (gamecodemodus == 1){
                sendGetRequestWaitingRoomHTML();
                sendDataMenschVsMenschStart();
            }
            if (gamecodemodus == 2) {
                sendDataMenschVsMenschJoin();
            }

            break;
        }
        case 2: {
            sendGetRequestGameHTML();
            sendDataMenschVsComputer();
            break;
        }
        case 3: {
            sendGetRequestGameHTML();
            sendDataComputerVsComputer();
            break;
        }
    }


}



function sendGetRequestGameHTML(){
    fetch('/index/controller/gameHTML', {method: 'GET'})
        .then((response) => {
            return response.text();
        })
        .then((html) => {
            //document.head.innerHTML = html
            document.body.innerHTML = html
        });
}

function sendGetRequestWaitingRoomHTML(){
    fetch('/index/controller/waitingRoomHTML', {method: 'GET'})
        .then((response) => {
            return response.text();
        })
        .then((html) => {
            //document.head.innerHTML = html
            document.body.innerHTML = html
        });
}


function sendDataMenschVsMenschStart(){


        let player1Name = document.getElementById("player1Textfield").value;
        let gameCodeStart = document.getElementById("gamecodeStart").value;
        let player1Color;
        if (document.getElementById("colorPlayer1").checked){
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
                console.log(responseData);
                document.getElementById('gameCodeH2').innerText += (" " + responseData.gameCode);
                document.getElementById('player1NameH2').innerText += (" " + responseData.player1Name);
                document.getElementById('player1UuidH2').innerText += (" " + responseData.player1Uuid);
                if (responseData.player1Color == "BLACK"){
                    document.getElementById('player1ColorH2').innerText += (" " + "Schwarz");}
                else {
                    document.getElementById('player1ColorH2').innerText += (" " + "Weiss");}

            })
            .catch(function(error) {
                console.log(error);
            });

        /*fetch("/index/controller/waitingRoomHTML/" + gameCodeStart, {
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
    })*/


    }

function sendDataMenschVsMenschJoin(){

        let player2Name = document.getElementById("player2Textfield").value;
        let gameCodeJoin = document.getElementById("gamecodeJoin").value;

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
            .then(data => console.log(data))
            .catch(error => console.log(error));
}

function sendDataMenschVsComputer(){

    let player1Name = document.getElementById("player1Textfield").value;
    let player1Color;
    if (document.getElementById("colorPlayer1").checked){
        player1Color = "WHITE";}
    else {player1Color = "BLACK";}

    let dropdown = document.getElementById("player2Dropdown");
    let computerName = dropdown.options[dropdown.selectedIndex].text;
    let computerCode = dropdown.options[dropdown.selectedIndex].value;


    fetch("/index/controller/menschVsComputer", {
        method: 'POST',
        body: JSON.stringify({
                "modus": 'Mensch vs. Computer',
                "player1Name" : player1Name,
                "player1Color" : player1Color,
                "computerName" : computerName,
                "computerCode" : computerCode
        }),
        headers: {
            "Content-type": "application/json"
        }
    })
        .then(resp => resp.json())
        .then(responseData => {
            console.log(responseData);
            //document.getElementById('player1NameGameText').innerText = ("Player 1: " + player1Name);
            $('#player1NameGameText').text("Player 1: " + player1Name)
            //document.getElementById('player2NameGameText').innerText = ("Player 2; " + computerName);
            $('#player2NameGameText').text("Player 2: " + computerName)
            //document.getElementById('modusH1').innerText = ("Mühle online – Spielmodus: Mensch vs. Computer");
            $('#modusH1').text("Mühle online – Spielmodus: Mensch vs. Computer")
            //document.getElementById('gameCodeH2').innerText = responseData.gameCode;
            $('#gameCodeH2').text(responseData.gameCode)
            //document.getElementById('playerUuidH2').innerText = responseData.player1Uuid;
            $('#player1UuidH2').text(responseData.player1Uuid)
            //document.getElementById('playerIndexH2').innerText = responseData.playerIndex;
            $('#playerIndexH2').text(responseData.playerIndex)

            $('#spielverlaufLabel').text(player1Name + " ist an der Reihe")

            if (player1Color == "BLACK"){
                //document.getElementById('player1StoneImage').src = "images/StoneBlack.png"
                $('#player1StoneImage').attr('src', 'images/StoneBlack.png')
                //document.getElementById('player2StoneImage').src = "images/StoneWhite.png";
                $('#player2StoneImage').attr('src', 'images/StoneWhite.png')
                window.color = "BLACK";
            }
            else {
                //document.getElementById('player1StoneImage').src = "images/StoneWhite.png";
                $('#player1StoneImage').attr('src', 'images/StoneWhite.png')
                //document.getElementById('player2StoneImage').src = "images/StoneBlack.png";
                $('#player2StoneImage').attr('src', 'images/StoneBlack.png')
                window.color = "WHITE";

            }

            window.game = new Game(new Player(player1Name, responseData.player1Uuid, responseData.playerIndex), responseData.gameCode, true);
            window.uuid = responseData.player1Uuid;
            window.playerIndex = responseData.playerIndex;
            window.name = player1Name;
            window.enemyName = computerName;


        })
        .catch(function(error) {
            console.log(error);
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
                "player1Color" : player1Color,
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
            document.getElementById('player1NameGameText').innerText += (" " + responseData.computerName1);
            document.getElementById('player2NameGameText').innerText += (" " + responseData.computerName2);
            document.getElementById('gameCodeGameH1').innerText = ("Mühle online – Spielmodus: " + responseData.modus + " / Gamecode: " + responseData.gameCode.substring(13));
            if (responseData.player1Color == "BLACK"){
                document.getElementById('player1StoneImage').src = "images/StoneBlack.png"
                document.getElementById('player2StoneImage').src = "images/StoneWhite.png";}
            else {
                document.getElementById('player1StoneImage').src = "images/StoneWhite.png";
                document.getElementById('player2StoneImage').src = "images/StoneBlack.png";}
        })
        .catch(function(error) {
            console.log(error);
        });
}




function checkChangeInDropdownMenu(element){

    if (element.id=="player1Dropdown"){
        if(element.value == 0){
            showComputerCodeTextArea(1, "display:block");
        }
        else {
            showComputerCodeTextArea(1, "display:none");
        }}

    if (element.id=="player2Dropdown"){
        if(element.value == 0){
            showComputerCodeTextArea(2, "display:block");
        }
        else {
            showComputerCodeTextArea(2, "display:none");
        }}
}

function showComputerCodeTextArea(player, display){
    switch (player){
        case 1:{
            document.getElementById("computerCode1").style=display;
            break;
        }
        case 2:{
            document.getElementById("computerCode2").style=display;
            break;
        }
    }
}

function showGamecodeTextfield(number){
    if (number == 1){
        document.getElementById("gamecodeStart").style="display:inline-block";
        document.getElementById("gamecodeJoin").style="display:none";
    }
    if (number == 2){
        document.getElementById("gamecodeStart").style="display:none";
        document.getElementById("gamecodeJoin").style="display:inline-block";
    }
}

function showPlayer1Setting(show){
    if (show){
        document.getElementById("player1Settings").style="display:show";
    }
    else {
        document.getElementById("player1Settings").style="display:none";
    }
}

function showPlayer2Setting(show){
    if (show){
        document.getElementById("player2Settings").style="display:show";
    }
    else {
        document.getElementById("player2Settings").style="display:none";
    }
}

function setVisibilityofPlayer2ColorSwitch(visible){
    if (visible){
        document.getElementById("labelSchwarzPlayer2").style="visible:true";
        document.getElementById("labelWeissPlayer2").style="visible:true";
        document.getElementById("colorPlayer2Switch").style="visible:true";
    }
    else {
        document.getElementById("labelSchwarzPlayer2").style="display:none";
        document.getElementById("labelWeissPlayer2").style="display:none";
        document.getElementById("colorPlayer2Switch").style="display:none";}
}

function showGamecode(show){
    if (show){
        document.getElementById("gamecode").style="display:show"
    }
    else {
        document.getElementById("gamecode").style="display:none"
    }
}

function saveComputer(player){
    switch (player){
        case 1:{
            if(document.getElementById("computerCodeName1").value != ""
            && document.getElementById("computerCode1TextArea").value != ""){
                document.getElementById("computerCode1").value;
                var addedOption = document.createElement("option");
                addedOption.text = document.getElementById("computerCodeName1").value;
                addedOption.value = document.getElementById("computerCode1TextArea").value;
                var dropdown = document.getElementById("player1Dropdown");
                var index = document.getElementById("player1Dropdown").options.length-1;
                dropdown.add(addedOption, dropdown[index]);
                document.getElementById("computerCode1").style="display: none";
                document.getElementById("player1Dropdown").selectedIndex = index;
                document.getElementById("computerCodeName1").value = "";
                document.getElementById("computerCode1TextArea").value = "";}
            else {window.alert("Es müssen ein Computername und ein Computercode vorhanden sein, um den Computer hinzuzufügen.");}
            break;
        }
        case 2:{
            if(document.getElementById("computerCodeName2").value != ""
                && document.getElementById("computerCode2TextArea").value != ""){
                document.getElementById("computerCode2").value;
                var addedOption = document.createElement("option");
                addedOption.text = document.getElementById("computerCodeName2").value;
                addedOption.value = document.getElementById("computerCode2TextArea").value;
                var dropdown = document.getElementById("player2Dropdown");
                var index = document.getElementById("player2Dropdown").options.length-1;
                dropdown.add(addedOption, dropdown[index]);
                document.getElementById("computerCode2").style="display: none";
                document.getElementById("player2Dropdown").selectedIndex = index;
                document.getElementById("computerCodeName2").value = "";
                document.getElementById("computerCode2TextArea").value = "";}
            else {window.alert("Es müssen ein Computername und ein Computercode vorhanden sein, um den Computer hinzuzufügen.");}
            break;
        }
    }
}

function setDropdownIndex(player, index){
    var dropdownString = "player" + player + "Dropdown";
    document.getElementById(dropdownString).selectedIndex = index;
}

function changeSwitchToOpposite(oppositeElement){

    if ($('#'.concat(oppositeElement)).prop('checked')){
        $('#'.concat(oppositeElement)).prop('checked', false)}
    else {
        $('#'.concat(oppositeElement)).prop('checked', true)
    }

    //document.getElementById(element.toString()).click();
}

function setModus(modus){
    window.modus = modus;
}

function setGamecodeModus(gamecodeModus){
    window.gamecodemodus = gamecodeModus;
}

function showComputerMenus(modus){
    switch (modus){

        case 1: {
            document.getElementById("player1Dropdown").style="display:none";
            document.getElementById("player1Textfield").style="display:inline-block";
            document.getElementById("player2Dropdown").style="display:none";
            document.getElementById("player2Textfield").style="display:inline-block";
            resetDropdownAndHideTextArea();
            showGamecode(true);
            showPlayer1Setting(true);
            showPlayer2Setting(false);
            setVisibilityofPlayer2ColorSwitch(true);
            document.getElementById("startGameRadio").click();
            break;}
        case 2: {
            document.getElementById("player1Dropdown").style="display:none";
            document.getElementById("player1Textfield").style="display:inline-block";
            document.getElementById("player2Dropdown").style="display:inline-block";
            document.getElementById("player2Textfield").style="display:none";
            resetDropdownAndHideTextArea();
            showGamecode(false);
            showPlayer1Setting(true);
            showPlayer2Setting(true);
            setVisibilityofPlayer2ColorSwitch(true);
            break;}
        case 3: {
            document.getElementById("player1Dropdown").style="display:inline-block";
            document.getElementById("player1Textfield").style="display:none";
            document.getElementById("player2Dropdown").style="display:inline-block";
            document.getElementById("player2Textfield").style="display:none";
            resetDropdownAndHideTextArea();
            showGamecode(false);
            showPlayer1Setting(true);
            showPlayer2Setting(true);
            setVisibilityofPlayer2ColorSwitch(true);
            break;}
}}

function resetDropdownAndHideTextArea(){
    showComputerCodeTextArea(1, "display:none")
    showComputerCodeTextArea(2, "display:none")
    setDropdownIndex(1, 0);
    setDropdownIndex(2, 0)
}