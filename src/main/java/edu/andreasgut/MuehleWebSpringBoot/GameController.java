package edu.andreasgut.MuehleWebSpringBoot;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@RestController
public class GameController {

    HashMap<String, PlayerSet> playerSetMap = new HashMap<>();



    @GetMapping(
            path = "/game/controller/gameHTML")
    public ModelAndView loadGameHTML() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/game.html");
        return modelAndView;}

    @GetMapping(
            path = "/game/controller/waitingRoomHTML")
    public ModelAndView loadWaitingRoomHTML() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/waitingRoom.html");
        return modelAndView;}


    @PostMapping(
            path = "/game/controller/menschVsMensch/start",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public @ResponseBody PlayerSet loadMenschVsMensch(@RequestBody String body){
        System.out.println(body);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");
        boolean start = jsonObject.getBoolean("startGame");
        String player1Name = jsonObject.getString("player1Name");
        String gameCode = jsonObject.getString("gameCode");
        String player1Color = jsonObject.getString("player1Color");
        String player2Color;

        if (player1Color.equals("BLACK")){
            player2Color = "WHITE";
        }
        else {
            player2Color = "BLACK";
        }

        if (playerSetMap.containsKey(gameCode)){
            System.out.println("Bereits vorhandener Gamcode: Dieser Gamecode wird bereits für ein anderes Spiel verwendet");
            return null;
        }
        else {
            Player player1 = new HumanPlayer(player1Name);
            PlayerSet playerSet = new PlayerSet(player1, gameCode);
            playerSetMap.put(gameCode, playerSet);

            /*JSONObject jsonObjectAnswer = new JSONObject();
            jsonObjectAnswer.put("gameCode", gameCode);
            jsonObjectAnswer.put("playerSet", playerSet);
            jsonObjectAnswer.put("player", player1);
            return jsonObjectAnswer;*/

            return playerSet;
        }





    }

    @PostMapping(
            path = "/game/controller/menschVsMensch/join")
    public void loadMenschVsMenschJoin(@RequestBody String body){
        System.out.println(body);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");
        String player2Name = jsonObject.getString("player2Name");
        String gameCode = jsonObject.getString("gameCode");

        if (playerSetMap.containsKey(gameCode)){
            PlayerSet playerSet = playerSetMap.get(gameCode);
            playerSet.setPlayer2(new HumanPlayer(player2Name));
            System.out.println(playerSet.getPlayer1().getName() + " und " + playerSet.getPlayer2().getName() + " bilden ein Playerset");
        }
        else {
            System.out.println("GameCode falsch: Ein Spieler versuchte einem nicht existierenden Game beizutreten");
        }

    }

    @PostMapping(
            path = "/game/controller/menschVsComputer")
    public void loadMenschVsComputer(@RequestBody String body){
        System.out.println(body);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");
        String player1Name = jsonObject.getString("player1Name");

    }


    @PostMapping(
            path = "/game/controller/computerVsComputer")
    public void loadComputerVsComputer(@RequestBody String body){
        System.out.println(body);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");
        String player1Name = jsonObject.getString("player1Name");

    }
}
