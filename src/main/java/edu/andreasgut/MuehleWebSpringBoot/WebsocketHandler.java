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

    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println(message.getPayload() + " from " + session.getRemoteAddress());

        JSONObject jsonObject = new JSONObject(message.getPayload());

        String gameCode = jsonObject.getString("gameCode");
        String command = jsonObject.getString("command");




        if (GameManager.checkIfGameExists(gameCode)) {
            Game game = GameManager.getGame(gameCode);
            LinkedList<WebSocketSession> sessions = game.getSessionList();

            switch (command) {
                case "start":
                    game.addToSessionList(session);
                    JSONObject startJsonObject = new JSONObject();
                    startJsonObject.put("command", "start");
                    startJsonObject.put("gameCode", gameCode);

                    session.sendMessage(new TextMessage(startJsonObject.toString()));
                    break;

                case "join":
                    game.addToSessionList(session);

                    for (WebSocketSession s : sessions){
                        sendMessageWithExceptionHandling(game, s, jsonObject.toString());
                    }
                    break;

                case "watch":
                    game.addToSessionList(session);
                    break;

                case "chat":
                    for (WebSocketSession s : sessions){
                        sendMessageWithExceptionHandling(game, s, jsonObject.toString());
                    }
                    break;

                case "update":

                    String action = jsonObject.getString("action");

                    if (action.equals("put")){

                        if (GameControllerWebsocket.put(jsonObject)){
                            for (WebSocketSession s : sessions){
                                sendMessageWithExceptionHandling(game, s, jsonObject.toString());}
                        }
                        else {
                            sendExceptionMessageToSender(session, "Ungültiger Put");
                        }
                    }

                    if (action.equals("move")) {

                        if (GameControllerWebsocket.move(jsonObject)) {
                            for (WebSocketSession s : sessions) {
                                sendMessageWithExceptionHandling(game, s, jsonObject.toString());
                            }
                        }
                        else {
                            sendExceptionMessageToSender(session, "Ungültiger Move");
                        }
                    }

                    if (action.equals("kill")){

                        if (GameControllerWebsocket.kill(jsonObject)) {
                            for (WebSocketSession s : sessions){
                                sendMessageWithExceptionHandling(game, s, jsonObject.toString());
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
