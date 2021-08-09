package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalTime;

@RestController
public class GameController {


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
                + ": Board aus dem Game " + jsonRequestObject.getString("gameCode") + " wurde abgefragt");


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

            if (game.getRound() <= 2 * game.getNUMBEROFSTONES()){
                computerPuts(gameCode, game, playerIndex, enemysIndex);
            }
            else {
                computerMoves(gameCode, game, playerIndex, enemysIndex);
            }
        }


        String boardAsString = transformBoardToString(game.getBoard());


        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("board", boardAsString);

        for (WebSocketSession session : game.getSessionList())
        {
            try {
                session.sendMessage(new TextMessage("Put an Stelle" + putPosition));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());

    }

    @PostMapping(
            path = "/game/controller/move")
    public ResponseEntity<String> move(@RequestBody String body) {

        JSONObject jsonRequestObject = new JSONObject(body);
        String gameCode = jsonRequestObject.getString("gameCode");
        String playerUuid = jsonRequestObject.getString("playerUuid");
        Game game = GameManager.getGame(gameCode);
        int playerIndex = game.getPlayerIndexByUuid(playerUuid);
        int enemysIndex = 1-playerIndex;

        Position from = new Position();
        from.setRing(jsonRequestObject.getInt("moveFromRing"));
        from.setField(jsonRequestObject.getInt("moveFromField"));

        Position to = new Position();
        to.setRing(jsonRequestObject.getInt("moveToRing"));
        to.setField(jsonRequestObject.getInt("moveToField"));

        Move move = new Move();
        move.setFrom(from);
        move.setTo(to);

        boolean callComputer = jsonRequestObject.getBoolean("callComputer");




        if (game.getBoard().checkMove(move, game.getBoard().countPlayersStones(playerIndex) == 3)){
            game.getBoard().move(move, playerIndex);
            System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Move in Spiel " + gameCode);
            System.out.println(GameManager.getGame(gameCode).getBoard());
            game.increaseRound();
        }

        if (callComputer && game.getPlayerByIndex(enemysIndex) instanceof ComputerPlayer){

            if (game.getRound() <= 2 * game.getNUMBEROFSTONES()){
                computerPuts(gameCode, game, playerIndex, enemysIndex);
            }
            else {
                computerMoves(gameCode, game, playerIndex, enemysIndex);
            }
        }


        String boardAsString = transformBoardToString(game.getBoard());


        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("board", boardAsString);

        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());

    }

    @PostMapping(
            path = "/game/controller/kill")
    public ResponseEntity<String> kill(@RequestBody String body) {

        JSONObject jsonRequestObject = new JSONObject(body);
        String gameCode = jsonRequestObject.getString("gameCode");
        String playerUuid = jsonRequestObject.getString("playerUuid");
        int killRing = jsonRequestObject.getInt("killRing");
        int killField = jsonRequestObject.getInt("killField");
        boolean callComputer = jsonRequestObject.getBoolean("callComputer");

        Game game = GameManager.getGame(gameCode);
        Position killPosition = new Position(killRing, killField);
        int playerIndex = game.getPlayerIndexByUuid(playerUuid);
        int enemysIndex = 1-game.getPlayerIndexByUuid(playerUuid);

        if (game.getBoard().checkKill(killPosition, enemysIndex)){
            game.getBoard().clearStone(killPosition);
            System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Kill in Spiel " + gameCode);
            System.out.println(GameManager.getGame(gameCode).getBoard());
        }


        if (callComputer && game.getPlayerByIndex(enemysIndex) instanceof ComputerPlayer){

            if (game.getRound() <= 2 * game.getNUMBEROFSTONES()){
                computerPuts(gameCode, game, playerIndex, enemysIndex);
            }
            else {
                computerMoves(gameCode, game, playerIndex, enemysIndex);
            }
        }

        String boardAsString = transformBoardToString(game.getBoard());


        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("board", boardAsString);

        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
    }

    private void computerPuts(String gameCode, Game game, int playerIndex, int enemysIndex) {
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Put in Spiel " + gameCode);
        Position putPositionComp = game.getPlayerByIndex(enemysIndex).put(game.getBoard(), enemysIndex);
        game.getBoard().putStone(putPositionComp, enemysIndex);
        System.out.println(game.getBoard());

        if (game.getBoard().checkMorris(putPositionComp)){
            computerKills(gameCode, game, playerIndex, enemysIndex);
        }
        game.increaseRound();
    }

    private void computerMoves(String gameCode, Game game, int playerIndex, int enemysIndex){
        System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Move in Spiel " + gameCode);
        Move moveComp = game.getPlayerByIndex(enemysIndex).move(game.getBoard(), enemysIndex, game.getBoard().countPlayersStones(enemysIndex) == 3);
        game.getBoard().move(moveComp, enemysIndex);
        System.out.println(game.getBoard());

        if (game.getBoard().checkMorris(moveComp.getTo())){
            computerKills(gameCode, game, playerIndex, enemysIndex);
        }
        game.increaseRound();


    }

    private void computerKills(String gameCode, Game game, int playerIndex, int enemysIndex) {
        Position killPositionComp = game.getPlayerByIndex(enemysIndex).kill(game.getBoard(), playerIndex);
        if (game.getBoard().checkKill(killPositionComp, playerIndex)){
            game.getBoard().clearStone(killPositionComp);
            System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": Kill in Spiel " + gameCode);
            System.out.println(game.getBoard());
        }
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
