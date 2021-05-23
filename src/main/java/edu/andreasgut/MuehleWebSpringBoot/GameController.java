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

        System.out.println(LocalTime.now() + " – " + getClass().getSimpleName() + "Spieler mit UUID " + playerUuid + " wartet");


        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
    }

    @PostMapping(
            path = "/game/controller/put")
    public void put(@RequestBody String body) {

        JSONObject jsonObject = new JSONObject(body);
        String gameCode = jsonObject.getString("gameCode");
        String playerUuid = jsonObject.getString("playerUuid");
        int putRing = jsonObject.getInt("putRing");
        int putField = jsonObject.getInt("putField");

        Game game = GameManager.getGame(gameCode);
        Position putPosition = new Position(putRing, putField);
        int playerIndex;

        if (game.getPlayerArrayList().get(0).getUuid().equals(playerUuid)){
            playerIndex = 0;
        }
        else {
            playerIndex = 1;
        }

        if (game.getBoard().checkPut(putPosition)){
            game.getBoard().putStone(putPosition, playerIndex);
            System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Put in Spiel " + gameCode);
            System.out.println(GameManager.getGame(gameCode).getBoard());
        }

    }

    private void colorPrint(String text, PRINTCOLOR color){
        System.out.print(color);
        System.out.println(text);
        System.out.print(PRINTCOLOR.RESET);

    }


}
