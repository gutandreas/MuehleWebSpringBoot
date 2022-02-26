package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WebsocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> webSocketSessions = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        webSocketSessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        webSocketSessions.remove(session);
    }

    synchronized public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println(message.getPayload() + " from " + session.getRemoteAddress());

        JSONObject jsonObject = new JSONObject(message.getPayload());

        String gameCode = jsonObject.getString("gameCode");
        String command = jsonObject.getString("command");

        if (GameManager.doesGameExist(gameCode)) {
            Game game = GameManager.getGame(gameCode);
            LinkedList<WebSocketSession> sessions = game.getSessionList();

            switch (command) {
                case "start":
                    game.addToSessionList(session);
                    break;

                case "join":
                    game.addToSessionList(session);
                    for (WebSocketSession s : sessions){
                        sendMessageWithExceptionHandling(game, s, jsonObject.toString());
                    }
                    break;

                case "watch":
                    game.addToSessionList(session);
                    JSONObject watchJsonObject = new JSONObject();
                    watchJsonObject.put("gameCode", jsonObject.getString("gameCode"));
                    watchJsonObject.put("command", "chat");
                    watchJsonObject.put("name", "Anonymer Beobachter");
                    watchJsonObject.put("message", "Ich schaue euch zu...");
                    for (WebSocketSession s : sessions){
                        sendMessageWithExceptionHandling(game, s, watchJsonObject.toString());
                    }
                    break;

                case "giveup":
                    for (WebSocketSession s : sessions){
                        sendMessageWithExceptionHandling(game, s, jsonObject.toString());
                    }
                    GameManager.removeGame(gameCode);
                    break;

                case "roboterConnection":
                    for (WebSocketSession s : sessions){
                        sendMessageWithExceptionHandling(game, s, jsonObject.toString());
                    }

                    if (jsonObject.getBoolean("connected")){
                        game.setRoboterConnected(true);
                        game.setRoboterWatching(jsonObject.getBoolean("watching"));
                        game.setRoboterPlaying(jsonObject.getBoolean("playing"));
                    }
                    else {
                        game.setRoboterConnected(false);
                        game.setRoboterWatching(false);
                        game.setRoboterPlaying(false);
                    }
                    break;

                case "chat":
                    for (WebSocketSession s : sessions){
                        sendMessageWithExceptionHandling(game, s, jsonObject.toString());
                    }
                    break;

                case "update":

                    String action = jsonObject.getString("action");
                    String playerUuid = jsonObject.getString("playerUuid");
                    int playerIndex = game.getPlayerIndexByUuid(playerUuid);
                    int enemysIndex = 1-playerIndex;
                    boolean callComputer = jsonObject.getBoolean("callComputer");

                    if (action.equals("put")){

                        game.setGameStarted(true);

                        int putRing = jsonObject.getInt("ring");
                        int putField = jsonObject.getInt("field");
                        Position putPosition = new Position(putRing, putField);

                        if (game.getBoard().isPutPossibleAt(putPosition)){
                            game.getBoard().putStone(putPosition, playerIndex);
                            game.increaseRound();
                            for (WebSocketSession s : sessions){
                                sendMessageWithExceptionHandling(game, s, jsonObject.toString());}

                            if (!game.getBoard().canPlayerMove(enemysIndex) && game.getRound() == 18){
                                GameControllerWebsocket.sendGameOverMessage(gameCode, enemysIndex, "Kein Zug mehr möglich");
                                GameManager.removeGame(gameCode);
                            }
                            else {
                                if (callComputer && game.getPlayerByIndex(enemysIndex) instanceof ComputerPlayer){
                                    triggerNextComputerStep(gameCode, playerIndex, enemysIndex);
                                }
                            }
                        }
                        else {
                            sendExceptionMessageToSender(session, "Ungültiger Put");
                        }


                    }

                    if (action.equals("move")) {


                        boolean allowedToJump = game.getBoard().numberOfStonesOf(playerIndex) == 3;

                        Position from = new Position();
                        from.setRing(jsonObject.getInt("moveFromRing"));
                        from.setField(jsonObject.getInt("moveFromField"));

                        Position to = new Position();
                        to.setRing(jsonObject.getInt("moveToRing"));
                        to.setField(jsonObject.getInt("moveToField"));

                        Move move = new Move(from, to);

                        if (game.getBoard().isMovePossibleAt(move, allowedToJump)){
                            game.getBoard().moveStone(move, playerIndex);
                            game.increaseRound();
                            for (WebSocketSession s : sessions){
                                sendMessageWithExceptionHandling(game, s, jsonObject.toString());
                            }

                            if (!game.getBoard().canPlayerMove(enemysIndex)){
                                GameControllerWebsocket.sendGameOverMessage(gameCode, enemysIndex, "Kein Zug mehr möglich");
                                GameManager.removeGame(gameCode);
                            }
                            else {
                                if (callComputer && game.getPlayerByIndex(enemysIndex) instanceof ComputerPlayer){
                                    triggerNextComputerStep(gameCode, playerIndex, enemysIndex);
                                }
                            }
                        }
                        else {
                            sendExceptionMessageToSender(session, "Ungültiger Move");
                        }

                    }

                    if (action.equals("kill")){

                        int killRing = jsonObject.getInt("ring");
                        int killField = jsonObject.getInt("field");
                        Position killPosition = new Position(killRing, killField);

                        if (game.getBoard().canPlayerKill(playerIndex) && game.getBoard().isKillPossibleAt(killPosition, enemysIndex)){
                            game.getBoard().removeStone(killPosition);
                            for (WebSocketSession s : sessions){
                                sendMessageWithExceptionHandling(game, s, jsonObject.toString());
                            }
                            if (game.getBoard().numberOfStonesOf(enemysIndex) < 3 && game.getRound() >= 18){
                                GameControllerWebsocket.sendGameOverMessage(gameCode, enemysIndex, "Weniger als 3 Steine");
                                GameManager.removeGame(gameCode);
                            }
                            else {
                                if (callComputer && game.getPlayerByIndex(enemysIndex) instanceof ComputerPlayer){
                                    triggerNextComputerStep(gameCode, playerIndex, enemysIndex);
                                }
                            }
                        }
                        else {
                            sendExceptionMessageToSender(session, "Ungültiger Kill");
                        }



                    }
            }
        }
        else {
            sendExceptionMessageToSender(session, "Game mit Code " + gameCode + " existiert nicht.");
        }


    }

    private void triggerNextComputerStep(String gameCode, int playerIndex, int enemysIndex){

        Game game = GameManager.getGame(gameCode);

        if (game.getRound() <= 18){
            GameControllerWebsocket.computerPuts(gameCode, game, playerIndex, enemysIndex);
        }
        else {
            GameControllerWebsocket.computerMoves(gameCode, game, playerIndex, enemysIndex);
        }

    }

    private void sendMessageWithExceptionHandling(Game game, WebSocketSession session, String message){
        try {
            session.sendMessage(new TextMessage(message));
        }
        catch (Exception e){
            game.removeFromSessionList(session);
        }
    }

    private void sendExceptionMessageToSender(WebSocketSession session, String details){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "exception");
        jsonObject.put("details", details);

        try {
            session.sendMessage(new TextMessage(jsonObject.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
