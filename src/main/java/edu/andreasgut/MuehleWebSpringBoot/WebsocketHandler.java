package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
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
                case "connect":
                    game.addToSessionList(session);
                    session.sendMessage(new TextMessage("Verbunden mit Game " + gameCode));
                    break;

                case "join":
                    game.addToSessionList(session);
                    String player2Name = jsonObject.getString("player2Name");

                    JSONObject joinJsonObject = new JSONObject();
                    joinJsonObject.put("command", "join");
                    joinJsonObject.put("player2Name", player2Name);

                    for (WebSocketSession s : sessions){
                        s.sendMessage(new TextMessage(joinJsonObject.toString()));
                    }
                    break;

                case "broadcast":
                    String broadcastPlayerUuid = jsonObject.getString("playerUuid");
                    for (WebSocketSession s : sessions){
                        s.sendMessage(new TextMessage(broadcastPlayerUuid + " sent a Message"));
                    }
                    break;

                case "update":
                    String updatePlayerUuid = jsonObject.getString("playerUuid");
                    String boardAsString = game.getBoard().getBoardAsString();
                    String action = jsonObject.getString("action");

                    JSONObject updateJsonObject = new JSONObject();
                    updateJsonObject.put("command", "update");
                    updateJsonObject.put("action", action);
                    updateJsonObject.put("playerUuid", updatePlayerUuid);
                    updateJsonObject.put("boardAsString", boardAsString);
                    updateJsonObject.put("action", action);
                    for (WebSocketSession s : sessions){
                        s.sendMessage(new TextMessage(updateJsonObject.toString()));
                    }
            }
        }
        else {
            session.sendMessage(new TextMessage("Game mit Code " + gameCode + " existiert nicht."));
        }


    }

}
