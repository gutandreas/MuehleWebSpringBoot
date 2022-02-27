package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.time.LocalTime;

public class GameControllerWebsocket {


    public static void computerPuts(String gameCode, Game game, int playerIndex, int enemysIndex) {
        System.out.println(LocalTime.now() + " – " + "GameCotrollerWebsocket: Put in Spiel " + gameCode);
        Position putPositionComp = game.getPlayerByIndex(enemysIndex).put(game.getBoard(), enemysIndex);
        game.getBoard().putStone(putPositionComp, enemysIndex);
        System.out.println(game.getBoard());
        computerSendsPutMessage(gameCode, game.getPlayerByIndex(enemysIndex), putPositionComp);

        if (!game.getBoard().canPlayerMove(playerIndex) && game.getRound() == 18){
            GameControllerWebsocket.sendGameOverMessage(gameCode, playerIndex, "Kein Zug mehr möglich");
        }

        if (game.getBoard().isPositionPartOfMorris(putPositionComp) && game.getBoard().canPlayerKill(enemysIndex)){
            computerKills(gameCode, game, playerIndex, enemysIndex);
        }

        game.increaseRound();
    }

    public static void computerMoves(String gameCode, Game game, int playerIndex, int enemysIndex){
        System.out.println(LocalTime.now() + "GameControllerWebsocket: Move in Spiel " + gameCode);
        Move moveComp = game.getPlayerByIndex(enemysIndex).move(game.getBoard(), enemysIndex, game.getBoard().numberOfStonesOf(enemysIndex) == 3);
        game.getBoard().moveStone(moveComp, enemysIndex);
        System.out.println(game.getBoard());
        computerSendsMoveMessage(gameCode, game.getPlayerByIndex(enemysIndex), moveComp);

        if (!game.getBoard().canPlayerMove(playerIndex)){
            GameControllerWebsocket.sendGameOverMessage(gameCode, playerIndex, "Kein Zug mehr möglich");
        }

        if (game.getBoard().isPositionPartOfMorris(moveComp.getTo()) && game.getBoard().canPlayerKill(enemysIndex)){
            computerKills(gameCode, game, playerIndex, enemysIndex);
            if (game.getBoard().numberOfStonesOf(playerIndex) < 3 && game.getRound() >18){
                GameControllerWebsocket.sendGameOverMessage(gameCode, playerIndex, "Weniger als 3 Steine");
                GameManager.removeGame(gameCode);
            }
        }

        game.increaseRound();
    }

    public static void computerKills(String gameCode, Game game, int playerIndex, int enemysIndex) {
        Position killPositionComp = game.getPlayerByIndex(enemysIndex).kill(game.getBoard(), playerIndex);
        if (game.getBoard().isKillPossibleAt(killPositionComp, playerIndex)){
            game.getBoard().removeStone(killPositionComp);
            System.out.println(LocalTime.now() + " – " + "GameCotrollerWebsocket: Kill in Spiel " + gameCode);
            System.out.println(game.getBoard());
            computerSendsKillMessage(gameCode, game.getPlayerByIndex(enemysIndex), killPositionComp);
        }
    }

    static private void computerSendsPutMessage(String gameCode, Player computerPlayer, Position position){
        String uuid = computerPlayer.getUuid();

        for (WebSocketSession session : GameManager.getGame(gameCode).getSessionList()) {

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

    static private void computerSendsMoveMessage(String gameCode, Player computerPlayer, Move move){
        String uuid = computerPlayer.getUuid();
        Position from = move.getFrom();
        Position to = move.getTo();

        for (WebSocketSession session : GameManager.getGame(gameCode).getSessionList()) {

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


    static private void computerSendsKillMessage(String gameCode, Player computerPlayer, Position position){
        String uuid = computerPlayer.getUuid();

        for (WebSocketSession session : GameManager.getGame(gameCode).getSessionList()) {

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


    public static void sendGameOverMessage(String gameCode, int loserIndex, String details){

        for (WebSocketSession session : GameManager.getGame(gameCode).getSessionList()) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gameCode", gameCode);
            jsonObject.put("command", "gameOver");
            jsonObject.put("playerIndex", loserIndex);
            jsonObject.put("details", details);

            try {
                session.sendMessage(new TextMessage(jsonObject.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
