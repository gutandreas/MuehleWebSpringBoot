var modus = 1;
var gamecodemodus = 1;

function sendData() {

    switch (modus){
        case 1: {
            if (gamecodemodus == 1){
                sendDataMenschVsMenschStart();
                sendGetRequestWaitingRoomHTML();

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
    fetch('/game/controller/gameHTML', {method: 'GET'})
        .then((response) => {
            return response.text();
        })
        .then((html) => {
            document.head.innerHTML = html
            document.body.innerHTML = html
        });
}

function sendGetRequestWaitingRoomHTML(){
    fetch('/game/controller/waitingRoomHTML', {method: 'GET'})
        .then((response) => {
            return response.text();
        })
        .then((html) => {
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

        fetch("/game/controller/menschVsMensch/start", {
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
                let gameCode = responseData.gameCode;
                let gamecodeField = document.getElementById('gameCode');
                gamecodeField.innerText += (" " + gameCode);
            })
            .catch(function(error) {
                console.log(error);
            });

        fetch("/game/controller/waitingRoomHTML/" + gameCodeStart, {
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


    }

function sendDataMenschVsMenschJoin(){

        let player2Name = document.getElementById("player2Textfield").value;
        let gameCodeJoin = document.getElementById("gamecodeJoin").value;

        fetch("/game/controller/menschVsMensch/join", {
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


    fetch("/game/controller/menschVsComputer", {
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
        .then(resp => resp.text())
        .then(data => {
            console.log(data)})}


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

    fetch("/game/controller", {
        method: 'POST',
        body: JSON.stringify({
                "title": 'Modus Computer vs. Computer',
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
        .then(res => res.json())
        .then(data => console.log(data))
        .catch(error => console.log(error));}




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
        document.getElementById("colorPlayer2").style="visible:true";
    }
    else {
        document.getElementById("labelSchwarzPlayer2").style="display:none";
        document.getElementById("labelWeissPlayer2").style="display:none";
        document.getElementById("colorPlayer2").style="display:none";}
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

function clickOnElement(element){
    document.getElementById(element).click();
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
}

function resetDropdownAndHideTextArea(){
    showComputerCodeTextArea(1, "display:none")
    showComputerCodeTextArea(2, "display:none")
    setDropdownIndex(1, 0);
    setDropdownIndex(2, 0)
}}