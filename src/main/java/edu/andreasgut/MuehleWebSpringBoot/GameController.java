package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
public class GameController {


    @PostMapping(
            path = "/game/controller/myTurn"
    )
    public ResponseEntity<String> getCurrentPlayer(@RequestBody String body){

        JSONObject jsonRequestObject = new JSONObject(body);

        Game game = GameManager.getGame(jsonRequestObject.getString("gameCode"));
        String playerUuid = jsonRequestObject.getString("playerUuid");

        boolean myTurn = playerUuid.equals(game.getCurrentPlayer().getUuid());

        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("myTurn", myTurn);

        System.out.println(LocalTime.now() + " – " + getClass().getSimpleName()
                + "Spieler mit UUID " + playerUuid + " fragt an, ob er an der Reihe ist. Antwort: " + myTurn);


        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
    }

    @PostMapping(
            path = "/game/controller/getBoard"
    )
    public ResponseEntity<String> getBoard(@RequestBody String body){
        JSONObject jsonRequestObject = new JSONObject(body);

        Game game = GameManager.getGame(jsonRequestObject.getString("gameCode"));
        String boardAsString = transformBoardToString(game.getBoard());

        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("board", boardAsString);

        System.out.println(LocalTime.now() + " – " + getClass().getSimpleName()
                + "Board aus dem Game " + jsonRequestObject.getString("gameCode") + " wurde abgefragt");


        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
    }


    @PostMapping(
            path = "/game/controller/put")
    public ResponseEntity<String> put(@RequestBody String body) {

        JSONObject jsonRequestObject = new JSONObject(body);
        String gameCode = jsonRequestObject.getString("gameCode");
        String playerUuid = jsonRequestObject.getString("playerUuid");
        int putRing = jsonRequestObject.getInt("putRing");
        int putField = jsonRequestObject.getInt("putField");
        boolean callComputer = jsonRequestObject.getBoolean("callComputer");

        Game game = GameManager.getGame(gameCode);
        Position putPosition = new Position(putRing, putField);
        int playerIndex = game.getPlayerIndexByUuid(playerUuid);
        int enemysIndex = 1-game.getPlayerIndexByUuid(playerUuid);

        if (game.getBoard().checkPut(putPosition)){
            game.getBoard().putStone(putPosition, playerIndex);
            System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Put in Spiel " + gameCode);
            System.out.println(GameManager.getGame(gameCode).getBoard());
            game.increaseRound();
        }

        if (callComputer && game.getPlayerByIndex(enemysIndex) instanceof ComputerPlayer){
            System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Put in Spiel " + gameCode);
            game.getBoard().putStone(game.getPlayerByIndex(enemysIndex).put(game.getBoard(), enemysIndex), enemysIndex);
            System.out.println(game.getBoard());
            game.increaseRound();
        }


        String boardAsString = transformBoardToString(game.getBoard());


        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("board", boardAsString);

        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());

    }

    private String transformBoardToString(Board board){
        String boardAsString = "";

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 8; j++){
                boardAsString += board.getArray()[i][j];
            }
        }

        return boardAsString;
    }

    private void colorPrint(String text, PRINTCOLOR color){
        System.out.print(color);
        System.out.println(text);
        System.out.print(PRINTCOLOR.RESET);

    }


}
