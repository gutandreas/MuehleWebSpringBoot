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