var animationspeed = 150;

function organizeMenu(number){

    setModus(number);

    switch (number){
        case 1:
            $('#gamecode').show(animationspeed)
            $('#gamecodeWatch').hide(animationspeed)
            $('#player1Settings').show(animationspeed)
            $('#player2Settings').hide(animationspeed)
            $('#startGameRadio').trigger('click');
            break

        case 2:
            $('#gamecode').hide(animationspeed)
            $('#gamecodeWatch').hide(animationspeed)
            $('#player1Settings').show(animationspeed)
            $('#player2Settings').show(animationspeed)
            $('#player2Dropdown').show(animationspeed)
            $('#player2Textfield').hide(animationspeed)
            $('#colorPlayer2Switch').show()
            $('#labelSchwarzPlayer2').show()
            $('#labelWeissPlayer2').show()
            break

        case 3:
            $('#gamecode').hide(animationspeed)
            $('#gamecodeWatch').show(animationspeed)
            $('#SpielerangabenH2').hide(animationspeed)
            $('#player1Settings').hide(animationspeed)
            $('#player2Settings').hide(animationspeed)
            break
    }
}


function organizeGameCodeSettings(number) {
    switch (number) {
        case 1:
            $('#gamecodeStart').show(animationspeed)
            $('#gamecodeJoin').hide(animationspeed)
            $('#player1Settings').show(animationspeed)
            $('#player2Settings').hide(animationspeed)
            setGamecodeModus(1)
            break
        case 2:
            $('#gamecodeStart').hide(animationspeed)
            $('#gamecodeJoin').show(animationspeed)
            $('#player1Settings').hide(animationspeed)
            $('#player2Settings').show(animationspeed)
            $('#player2Dropdown').hide(animationspeed)
            $('#player2Textfield').show(animationspeed)
            $('#colorPlayer2Switch').hide()
            $('#labelSchwarzPlayer2').hide()
            $('#labelWeissPlayer2').hide()
            setGamecodeModus(2)
            break
    }
}


function changeSwitchToOpposite(oppositeElement){

    if ($('#'.concat(oppositeElement)).prop('checked')){
        $('#'.concat(oppositeElement)).prop('checked', false)}
    else {
        $('#'.concat(oppositeElement)).prop('checked', true)
    }
}

function setModus(modus){
    window.modus = modus;
}

function setGamecodeModus(gamecodeModus){
    window.gamecodemodus = gamecodeModus;
}