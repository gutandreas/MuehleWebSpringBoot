package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;



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

        // Hier wird die gameID herausgelesen... Macht noch nicht wirklich Sinn so...
    @PostMapping(
            path = "/game/controller/waitingRoomHTML/{gameCode}")
    public @ResponseBody JSONObject loadWaitingRoomHTMLid(@PathVariable String gameCode) {
        Game game = new Game(gameCode);
        JSONObject jsonObject = new JSONObject(game);
        System.out.println("Gamecontroller: Das Game mit dem Code '" + gameCode + "' wurde erstellt");
        return jsonObject;}

    @PostMapping(
            path = "/game/controller/menschVsMensch/checkIfSetComplete",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> checkIfSetComplete(@RequestBody String body){
        JSONObject jsonRequestObject = new JSONObject(body);
        String gameCode = jsonRequestObject.getString("gameCode");

        if(playerSetMap.get(gameCode).getPlayer2() != null){
            String player2Name = playerSetMap.get(gameCode).getPlayer2().getName();
            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("gameCode", gameCode);
            jsonResponseObject.put("player2Name", player2Name);


            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("-");
    }

    @PostMapping(
            path = "/game/controller/menschVsMensch/start",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> loadMenschVsMensch(@RequestBody String body){
        System.out.println(body);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");
        boolean start = jsonObject.getBoolean("startGame");
        String player1Name = jsonObject.getString("player1Name");
        String gameCode = jsonObject.getString("gameCode");
        STONECOLOR player1Color;
        if(jsonObject.getString("player1Color").equals("BLACK")){
            player1Color = STONECOLOR.BLACK;}
        else {
            player1Color = STONECOLOR.WHITE;
        }

        if (playerSetMap.containsKey(gameCode)){
            System.out.println("Bereits vorhandener Gamecode: Dieser Gamecode wird bereits für ein anderes Spiel verwendet");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("-");
        }
        else {
            Player player1 = new HumanPlayer(player1Name);
            PlayerSet playerSet = new PlayerSet(player1, gameCode, player1Color);
            playerSetMap.put(gameCode, playerSet);

            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("gameCode", gameCode);
            jsonResponseObject.put("player1Name", player1Name);
            jsonResponseObject.put("player1Color", player1Color.name());

            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
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
            System.out.println("Gamecontroller: " + playerSet.getPlayer1().getName() + " und " + playerSet.getPlayer2().getName() + " bilden ein Playerset mit dem Gamecode " + gameCode);
        }
        else {
            System.out.println("GameCode falsch: Ein Spieler versuchte einem nicht existierenden Game beizutreten");
        }

    }

    @PostMapping(
            path = "/game/controller/menschVsComputer")
    public JSONObject loadMenschVsComputer(@RequestBody String body){
        System.out.println(body);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");
        String player1Name = jsonObject.getString("player1Name");

        jsonObject.put("modus", modus);
        jsonObject.put("player1Name", player1Name);

        return jsonObject;
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
