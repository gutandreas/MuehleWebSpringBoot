package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.UUID;

@RestController
public class GameController {

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

        if (game.playerArrayList.get(0).getUuid().equals(playerUuid)){
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


}
