function askForJoinedPlayer(){

    setTimeout(function() {
        console.log('hello');
        let i = 0;
        i++;
        if (i < 10) {
            askForJoinedPlayer();
        }
    }, 20000)
}