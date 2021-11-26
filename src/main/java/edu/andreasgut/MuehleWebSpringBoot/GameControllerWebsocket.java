package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalTime;

public class GameControllerWebsocket {

    static public boolean checkPutAndPutIfPossible(JSONObject jsonRequestObject) {

        String gameCode = jsonRequestObject.getString("gameCode");
        String playerUuid = jsonRequestObject.getString("playerUuid");
        int putRing = jsonRequestObject.getInt("ring");
        int putField = jsonRequestObject.getInt("field");
        boolean callComputer = jsonRequestObject.getBoolean("callComputer");

        Game game = GameManager.getGame(gameCode);
        Position putPosition = new Position(putRing, putField);
        int playerIndex = game.getPlayerIndexByUuid(playerUuid);
        int enemysIndex = 1-game.getPlayerIndexByUuid(playerUuid);

        if (game.getBoard().checkPut(putPosition)){
            game.getBoard().putStone(putPosition, playerIndex);
            System.out.println(LocalTime.now() + " – " + "GameCotrollerWebsocket: Put in Spiel " + gameCode);
            System.out.println(GameManager.getGame(gameCode).getBoard());
            game.increaseRound();
        }
        else {
            return false;
        }

        if (callComputer && game.getPlayerByIndex(enemysIndex) instanceof ComputerPlayer){

            if (game.getRound() <= 2 * game.getNUMBEROFSTONES()){
                computerPuts(gameCode, game, playerIndex, enemysIndex);
            }
            else {
                computerMoves(gameCode, game, playerIndex, enemysIndex);
            }
        }


        return true;

    }

    static public boolean checkMoveAndMoveAndMoveIfPossible(JSONObject jsonRequestObject) {


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
            System.out.println(LocalTime.now() + " – " + "GameCotrollerWebsocket: Move in Spiel " + gameCode);
            System.out.println(GameManager.getGame(gameCode).getBoard());
            game.increaseRound();
        }
        else {
            return false;
        }

        if (callComputer && game.getPlayerByIndex(enemysIndex) instanceof ComputerPlayer){

            if (game.getRound() <= 2 * game.getNUMBEROFSTONES()){
                computerPuts(gameCode, game, playerIndex, enemysIndex);
            }
            else {
                computerMoves(gameCode, game, playerIndex, enemysIndex);
            }


        }


        return true;

    }

    static public boolean checkKillAndKillIfPossible(JSONObject jsonRequestObject) {

        String gameCode = jsonRequestObject.getString("gameCode");
        String playerUuid = jsonRequestObject.getString("playerUuid");
        int killRing = jsonRequestObject.getInt("ring");
        int killField = jsonRequestObject.getInt("field");
        boolean callComputer = jsonRequestObject.getBoolean("callComputer");

        Game game = GameManager.getGame(gameCode);
        Position killPosition = new Position(killRing, killField);
        int playerIndex = game.getPlayerIndexByUuid(playerUuid);
        int enemysIndex = 1-game.getPlayerIndexByUuid(playerUuid);

        if (game.getBoard().checkKill(killPosition, enemysIndex)){
            game.getBoard().clearStone(killPosition);
            System.out.println(LocalTime.now() + " – " + "GameControllerWebsocket: Kill in Spiel " + gameCode);
            System.out.println(GameManager.getGame(gameCode).getBoard());
        }
        else {
            return false;
        }


        if (callComputer && game.getPlayerByIndex(enemysIndex) instanceof ComputerPlayer){

            if (game.getRound() <= 2 * game.getNUMBEROFSTONES()){
                computerPuts(gameCode, game, playerIndex, enemysIndex);
            }
            else {
                computerMoves(gameCode, game, playerIndex, enemysIndex);
            }

        }


        return true;
    }

    static private void computerPuts(String gameCode, Game game, int playerIndex, int enemysIndex) {
        System.out.println(LocalTime.now() + " – " + "GameCotrollerWebsocket: Put in Spiel " + gameCode);
        Position putPositionComp = game.getPlayerByIndex(enemysIndex).put(game.getBoard(), enemysIndex);
        game.getBoard().putStone(putPositionComp, enemysIndex);
        System.out.println(game.getBoard());
        computerSendPutMessage(gameCode, game.getPlayerByIndex(enemysIndex), putPositionComp);

        if (game.getBoard().checkMorris(putPositionComp) && game.getBoard().isThereStoneToKill(playerIndex)){
            computerKills(gameCode, game, playerIndex, enemysIndex);
        }
        game.increaseRound();
    }

    static private void computerMoves(String gameCode, Game game, int playerIndex, int enemysIndex){
        System.out.println(LocalTime.now() + "GameCotrollerWebsocket: Move in Spiel " + gameCode);
        Move moveComp = game.getPlayerByIndex(enemysIndex).move(game.getBoard(), enemysIndex, game.getBoard().countPlayersStones(enemysIndex) == 3);
        game.getBoard().move(moveComp, enemysIndex);
        System.out.println(game.getBoard());
        computerSendMoveMessage(gameCode, game.getPlayerByIndex(enemysIndex), moveComp);


        if (game.getBoard().checkMorris(moveComp.getTo()) && game.getBoard().isThereStoneToKill(playerIndex)){
            computerKills(gameCode, game, playerIndex, enemysIndex);
        }
        game.increaseRound();


    }

    static private void computerKills(String gameCode, Game game, int playerIndex, int enemysIndex) {
        Position killPositionComp = game.getPlayerByIndex(enemysIndex).kill(game.getBoard(), playerIndex);
        if (game.getBoard().checkKill(killPositionComp, playerIndex)){
            game.getBoard().clearStone(killPositionComp);
            System.out.println(LocalTime.now() + " – " + "GameCotrollerWebsocket: Kill in Spiel " + gameCode);
            System.out.println(game.getBoard());
            computerSendKillMessage(gameCode, game.getPlayerByIndex(enemysIndex), killPositionComp);

        }
    }

    static private void computerSendPutMessage(String gameCode, Player computerPlayer, Position position){
        String uuid = computerPlayer.getUuid();

        for (WebSocketSession session : GameManager.getGame(gameCode).getSessionList())
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("playerUuid", uuid);
            jsonObject.put("gameCode", gameCode);
            jsonObject.put("command", "update");
            jsonObject.put("action", "put");
            jsonObject.put("ring", position.getRing());
            jsonObject.put("field", position.getField());
            jsonObject.put("playerIndex", 1);
            jsonObject.put("triggerAxidraw", true);
            jsonObject.put("boardAsString", GameManager.getGame(gameCode).getBoard().getBoardAsString());

            try {
                session.sendMessage(new TextMessage(jsonObject.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static private void computerSendMoveMessage(String gameCode, Player computerPlayer, Move move){
        String uuid = computerPlayer.getUuid();
        Position from = move.getFrom();
        Position to = move.getTo();

        for (WebSocketSession session : GameManager.getGame(gameCode).getSessionList())
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("playerUuid", uuid);
            jsonObject.put("gameCode", gameCode);
            jsonObject.put("command", "update");
            jsonObject.put("action", "move");
            jsonObject.put("moveFromRing", from.getRing());
            jsonObject.put("moveFromField", from.getField());
            jsonObject.put("moveToRing", to.getRing());
            jsonObject.put("moveToField", to.getField());
            jsonObject.put("playerIndex", 1);
            jsonObject.put("triggerAxidraw", true);
            jsonObject.put("boardAsString", GameManager.getGame(gameCode).getBoard().getBoardAsString());

            try {
                session.sendMessage(new TextMessage(jsonObject.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static private void computerSendKillMessage(String gameCode, Player computerPlayer, Position position){
        String uuid = computerPlayer.getUuid();

        for (WebSocketSession session : GameManager.getGame(gameCode).getSessionList())
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("playerUuid", uuid);
            jsonObject.put("gameCode", gameCode);
            jsonObject.put("command", "update");
            jsonObject.put("action", "kill");
            jsonObject.put("ring", position.getRing());
            jsonObject.put("field", position.getField());
            jsonObject.put("playerIndex", 1);
            jsonObject.put("triggerAxidraw", true);
            jsonObject.put("boardAsString", GameManager.getGame(gameCode).getBoard().getBoardAsString());

            try {
                session.sendMessage(new TextMessage(jsonObject.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static private String transformBoardToString(Board board){
        String boardAsString = "";

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 8; j++){
                boardAsString += board.getArray()[i][j];
            }
        }

        return boardAsString;
    }
}
