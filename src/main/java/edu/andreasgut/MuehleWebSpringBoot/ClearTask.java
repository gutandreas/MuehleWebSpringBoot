package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.util.TimerTask;

public class ClearTask extends TimerTask {

    String gameCode;

    public ClearTask(String gameCode) {
        this.gameCode = gameCode;
    }

    @Override
    public void run() {
        if (GameManager.doesGameExist(gameCode)){
            sendTimeOutMessage(gameCode);
            GameManager.removeGame(gameCode);
        }
    }

    static private void sendTimeOutMessage(String gameCode){

        for (WebSocketSession session : GameManager.getGame(gameCode).getSessionList())
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command", "timeout");

            try {
                session.sendMessage(new TextMessage(jsonObject.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
