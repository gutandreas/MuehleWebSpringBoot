package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalTime;
import java.util.HashMap;



@RestController
public class IndexController {

    GameManager gameManager = new GameManager();

    public GameManager getGameManager() {
        return gameManager;
    }

    @GetMapping(
            path = "/index/controller/gameHTML")
    public ModelAndView loadGameHTML() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/game.html");
        return modelAndView;}

    @GetMapping(
            path = "/index/controller/waitingRoomHTML")
    public ModelAndView loadWaitingRoomHTML() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/waitingRoom.html");
        return modelAndView;}

        // Hier wird die gameID herausgelesen... Macht noch nicht wirklich Sinn so...
    @PostMapping(
            path = "/index/controller/waitingRoomHTML/{gameCode}")
    public @ResponseBody JSONObject loadWaitingRoomHTMLid(@PathVariable String gameCode) {
        Game game = new Game(new HumanPlayer("player1", STONECOLOR.BLACK), (new HumanPlayer("player2", STONECOLOR.WHITE)));
        JSONObject jsonObject = new JSONObject(game);
        System.out.println(this.getClass().getSimpleName() + ": Das Game mit dem Code '" + gameCode + "' wurde erstellt");
        return jsonObject;}



    @PostMapping(
            path = "/index/controller/menschVsMensch/start",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> loadMenschVsMensch(@RequestBody String body){
        System.out.println(body);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");
        String player1Name = jsonObject.getString("player1Name");
        String gameCode = jsonObject.getString("gameCode");
        STONECOLOR player1Color;
        if(jsonObject.getString("player1Color").equals("BLACK")){
            player1Color = STONECOLOR.BLACK;}
        else {
            player1Color = STONECOLOR.WHITE;
        }

        if (gameManager.checkIfGameExists(gameCode)){
            System.out.println("Bereits vorhandener Gamecode: Dieser Gamecode wird bereits für ein anderes Spiel verwendet");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("-");
        }
        else {
            Game game = new Game(new HumanPlayer(player1Name, player1Color));
            gameManager.addGame(gameCode, game);

            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("gameCode", gameCode);
            jsonResponseObject.put("player1Name", player1Name);
            jsonResponseObject.put("player1Color", player1Color.name());

            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
        }

    }

    @PostMapping(
            path = "/index/controller/menschVsMensch/join")
    public void loadMenschVsMenschJoin(@RequestBody String body){
        System.out.println(body);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");
        String player2Name = jsonObject.getString("player2Name");
        String gameCode = jsonObject.getString("gameCode");

        if (gameManager.checkIfGameExists(gameCode)){
            STONECOLOR player1StoneColor = gameManager.getGame(gameCode).getPlayer1().getStonecolor();

            STONECOLOR player2StoneColor;
            if (player1StoneColor==STONECOLOR.BLACK){
                player2StoneColor = STONECOLOR.WHITE;
            }
            else {
                player2StoneColor = STONECOLOR.BLACK;
            }

            gameManager.getGame(gameCode).setPlayer2(new HumanPlayer(player2Name, player2StoneColor));
        }
        else {
            System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": GameCode falsch – Ein Spieler versuchte einem nicht existierenden Game beizutreten");
        }

    }

    @PostMapping(
            path = "/index/controller/menschVsComputer",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loadMenschVsComputer(@RequestBody String body){
        System.out.println(body);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");

        String player1Name = jsonObject.getString("player1Name");
        STONECOLOR player1Color = STONECOLOR.valueOf(jsonObject.getString("player1Color"));

        String computerName = jsonObject.getString("computerName");
        STONECOLOR computerColor;
        if (player1Color.equals(STONECOLOR.BLACK)){
            computerColor = STONECOLOR.WHITE;
        }
        else {
            computerColor = STONECOLOR.BLACK;
        }

        Game game = new Game(new HumanPlayer(player1Name, player1Color), new ComputerPlayer(computerName, computerColor));
        String gameCode = gameManager.addGameAndGetGameCode(game);

        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("modus", modus);
        jsonResponseObject.put("player1Name", player1Name);
        jsonResponseObject.put("computerName", computerName);
        jsonResponseObject.put("gameCode", gameCode);

        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
    }

    @PostMapping(
            path = "/index/controller/menschVsMensch/checkIfGameComplete",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> checkIfSetComplete(@RequestBody String body){
        JSONObject jsonRequestObject = new JSONObject(body);
        String gameCode = jsonRequestObject.getString("gameCode");

        if(gameManager.getGame(gameCode).isGameComplete()){
            String player2Name = gameManager.getGame(gameCode).getPlayer2().getName();
            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("gameCode", gameCode);
            jsonResponseObject.put("player2Name", player2Name);


            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("-");
    }


    @PostMapping(
            path = "/index/controller/computerVsComputer")
    public ResponseEntity<String> loadComputerVsComputer(@RequestBody String body){
        System.out.println(body);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");
        String computerName1 = jsonObject.getString("computerName1");
        String computerName2 = jsonObject.getString("computerName2");

        Game game = new Game(new ComputerPlayer(computerName1, STONECOLOR.BLACK), new ComputerPlayer(computerName2, STONECOLOR.WHITE));
        String gameCode = gameManager.addGameAndGetGameCode(game);

        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("modus", modus);
        jsonResponseObject.put("gameCode", gameCode);
        jsonResponseObject.put("computerName1", computerName1);
        jsonResponseObject.put("computerName2", computerName2);

        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());

    }
}
