var clickedPosition;





function setCursor(cursorURL){
    document.getElementById("boardImage").style.cursor = cursorURL;
}


function clickOnField(ring, field){
    console.log("Feld angeklickt");
    clickedPosition = new Position(ring, field);




}

