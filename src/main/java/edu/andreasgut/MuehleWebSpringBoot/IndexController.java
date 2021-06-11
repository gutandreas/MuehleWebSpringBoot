package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalTime;
import java.util.UUID;


@RestController
public class IndexController {



    @GetMapping(
            path = "/index/controller/gameHTML")
    public ModelAndView loadGameHTML() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/game.html");
        return modelAndView;}

    @GetMapping(
            path = "/index/controller/gameCompHTML")
    public ModelAndView loadGameCompHTML() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/gameComp.html");
        return modelAndView;}



        // Hier wird die gameID herausgelesen... Macht noch nicht wirklich Sinn so...
    @PostMapping(
            path = "/index/controller/waitingRoomHTML/{gameCode}")
    public @ResponseBody JSONObject loadWaitingRoomHTMLid(@PathVariable String gameCode) {
        Game game = new Game(new HumanPlayer("player1", generateRandomUUID(), STONECOLOR.BLACK), (new HumanPlayer("player2", generateRandomUUID(), STONECOLOR.WHITE)), 0);
        JSONObject jsonObject = new JSONObject(game);
        System.out.println(this.getClass().getSimpleName() + ": Das Game mit dem Code '" + gameCode + "' wurde erstellt");
        return jsonObject;}



    @PostMapping(
            path = "/index/controller/menschVsMensch/start",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> loadMenschVsMenschStart(@RequestBody String body){
        colorPrint(body, PRINTCOLOR.YELLOW);
        JSONObject jsonObject = new JSONObject(body);
        String player1Name = jsonObject.getString("player1Name");
        String gameCode = jsonObject.getString("gameCode");
        STONECOLOR player1Color;
        if(jsonObject.getString("player1Color").equals("BLACK")){
            player1Color = STONECOLOR.BLACK;}
        else {
            player1Color = STONECOLOR.WHITE;
        }

        if (GameManager.checkIfGameExists(gameCode)){
            System.out.println("Bereits vorhandener Gamecode: Dieser Gamecode wird bereits für ein anderes Spiel verwendet");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("-");
        }
        else {
            Game game = new Game(new HumanPlayer(player1Name, generateRandomUUID(), player1Color));
            GameManager.addGame(gameCode, game);

            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("player1Uuid", game.getPlayer0().getUuid());
            jsonResponseObject.put("player1Color", player1Color.name());
            jsonResponseObject.put("player1Index", 0);

            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
        }

    }

    @PostMapping(
            path = "/index/controller/menschVsMensch/getEnemy")
    private ResponseEntity<String> getEnemy(@RequestBody String body){
        colorPrint(body, PRINTCOLOR.YELLOW);
        JSONObject jsonObject = new JSONObject(body);
        String gameCode = jsonObject.getString("gameCode");

        System.out.println(this.getClass().getSimpleName() + ": Im Game mit dem Code '" + gameCode + "' wurde der Gegner abgefragt.");

        JSONObject jsonResponseObject = new JSONObject();

        if (GameManager.getGame(gameCode).getPlayer1() != null){

            jsonResponseObject.put("player2Name", GameManager.getGame(gameCode).getPlayer1().getName());

            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
        }
        else {

            jsonResponseObject.put("player2Name", "");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(jsonResponseObject.toString());
        }

    }

    @PostMapping(
            path = "/index/controller/menschVsMensch/join")
    public ResponseEntity<String> loadMenschVsMenschJoin(@RequestBody String body){
        colorPrint(body, PRINTCOLOR.YELLOW);
        JSONObject jsonObject = new JSONObject(body);
        String player2Name = jsonObject.getString("player2Name");
        String gameCode = jsonObject.getString("gameCode");

        if (GameManager.checkIfGameExists(gameCode)){
            STONECOLOR player1StoneColor = GameManager.getGame(gameCode).getPlayer0().getStonecolor();

            STONECOLOR player2StoneColor;
            if (player1StoneColor==STONECOLOR.BLACK){
                player2StoneColor = STONECOLOR.WHITE;
            }
            else {
                player2StoneColor = STONECOLOR.BLACK;
            }

            Game game = GameManager.getGame(gameCode);

            game.setPlayer1(new HumanPlayer(player2Name, generateRandomUUID(), player2StoneColor));

            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("player1Name", GameManager.getGame(gameCode).getPlayer0().getName());
            jsonResponseObject.put("player2Color", player2StoneColor);
            jsonResponseObject.put("player2Uuid", game.getPlayer1().getUuid());
            jsonResponseObject.put("player2Index", 1);

            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());

        }
        else {
            System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": GameCode falsch – Ein Spieler versuchte einem nicht existierenden Game beizutreten");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("-");
        }

    }

    @PostMapping(
            path = "/index/controller/menschVsComputer",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loadMenschVsComputer(@RequestBody String body){
        colorPrint(body, PRINTCOLOR.YELLOW);
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

        Game game = new Game(new HumanPlayer(player1Name, generateRandomUUID(), player1Color), new ComputerPlayer(computerName, generateRandomUUID(), computerColor), 0);
        String gameCode = GameManager.addGameAndGetGameCode(game);

        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("modus", modus);
        jsonResponseObject.put("player1Uuid", game.getPlayer0().getUuid());
        jsonResponseObject.put("playerIndex", 0);
        jsonResponseObject.put("gameCode", gameCode);
        jsonResponseObject.put("start", "true");


        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
    }

    @PostMapping(
            path = "/index/controller/menschVsMensch/checkIfGameComplete",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> checkIfSetComplete(@RequestBody String body){
        colorPrint(body, PRINTCOLOR.YELLOW);
        JSONObject jsonRequestObject = new JSONObject(body);
        String gameCode = jsonRequestObject.getString("gameCode");

        if(GameManager.getGame(gameCode).isGameComplete()){
            String player2Name = GameManager.getGame(gameCode).getPlayer1().getName();
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
        colorPrint(body, PRINTCOLOR.YELLOW);
        JSONObject jsonObject = new JSONObject(body);
        String computerName1 = jsonObject.getString("computerName1");
        String computerCode1 = jsonObject.getString("computerCode1");
        String computerName2 = jsonObject.getString("computerName2");
        String computerCode2 = jsonObject.getString("computerCode2");

        Game game = new Game(new ComputerPlayer(computerName1, generateRandomUUID(), STONECOLOR.BLACK), new ComputerPlayer(computerName2, generateRandomUUID(), STONECOLOR.WHITE), 0);
        String gameCode = GameManager.addGameAndGetGameCode(game);

        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("gameCode", gameCode);
        jsonResponseObject.put("uuid1", GameManager.getGame(gameCode).getPlayer0().getUuid());
        jsonResponseObject.put("uuid2", GameManager.getGame(gameCode).getPlayer1().getUuid());

        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());

    }

    @PostMapping(
            path = "/index/controller/checkGamecode")
    public ResponseEntity<String> checkGamecode(@RequestBody String body){
        colorPrint(body, PRINTCOLOR.YELLOW);
        JSONObject jsonObject = new JSONObject(body);
        String gamecode = jsonObject.getString("gamecode");

        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("gamecodeOk", !GameManager.checkIfGameExists(gamecode));

        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
    }

    private void colorPrint(String text, PRINTCOLOR color){
        System.out.print(color);
        System.out.println(text);
        System.out.print(PRINTCOLOR.RESET);

    }

    private String generateRandomUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


}
