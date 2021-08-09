package edu.andreasgut.MuehleWebSpringBoot;

import org.springframework.web.socket.WebSocketSession;

import java.time.LocalTime;
import java.util.HashMap;

public class GameManager {

    static HashMap<String, Game> gameMap = new HashMap<>();
    static int autoGeneratedGameCode = 1;

    static public synchronized void addGame(String gameCode, Game game){
        gameMap.put(gameCode, game);
        System.out.println(LocalTime.now() + " – " + "Gamemanager: Game mit dem Gamecode " + gameCode + " hinzugefügt");
    }

    static public synchronized String addGameAndGetGameCode(Game game){
        String gameCode = "autogenerated" + autoGeneratedGameCode++;
        gameMap.put(gameCode, game);
        System.out.println(LocalTime.now() + " – " + "Gamemanager: Game mit dem Gamecode " + gameCode + " hinzugefügt");
        return gameCode;
    }

    static public synchronized Game getGame(String gameCode){
        return gameMap.get(gameCode);
    }

   static public synchronized boolean checkIfGameExists(String gameCode){
        return gameMap.containsKey(gameCode);
   }
}
