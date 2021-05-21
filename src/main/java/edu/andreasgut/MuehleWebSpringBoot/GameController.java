package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class GameController {

    @PostMapping(
            path = "/game/controller/put")
    public void put(@RequestBody String body) {



        JSONObject jsonObject = new JSONObject(body);
        String gameCode = jsonObject.getString("gameCode");
        int putRing = jsonObject.getInt("putRing");
        int putField = jsonObject.getInt("putField");



        if (GameManager.getGame(gameCode).getBoard().checkPut(new Position(putRing, putField))){
            GameManager.getGame(gameCode).getBoard().putStone(new Position(putRing, putField), 0);
            System.out.println(GameManager.getGame(gameCode).getBoard());
        }

    }

    private String generateRandomUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
