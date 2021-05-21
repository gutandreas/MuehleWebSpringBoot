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
            path = "/index/controller/waitingRoomHTML")
    public ModelAndView loadWaitingRoomHTML() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/waitingRoom.html");
        return modelAndView;}

        // Hier wird die gameID herausgelesen... Macht noch nicht wirklich Sinn so...
    @PostMapping(
            path = "/index/controller/waitingRoomHTML/{gameCode}")
    public @ResponseBody JSONObject loadWaitingRoomHTMLid(@PathVariable String gameCode) {
        Game game = new Game(new HumanPlayer("player1", generateRandomUUID(), STONECOLOR.BLACK), (new HumanPlayer("player2", generateRandomUUID(), STONECOLOR.WHITE)));
        JSONObject jsonObject = new JSONObject(game);
        System.out.println(this.getClass().getSimpleName() + ": Das Game mit dem Code '" + gameCode + "' wurde erstellt");
        return jsonObject;}



    @PostMapping(
            path = "/index/controller/menschVsMensch/start",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> loadMenschVsMensch(@RequestBody String body){
        colorPrint(body, PRINTCOLOR.YELLOW);
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

        if (GameManager.checkIfGameExists(gameCode)){
            System.out.println("Bereits vorhandener Gamecode: Dieser Gamecode wird bereits für ein anderes Spiel verwendet");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("-");
        }
        else {
            Game game = new Game(new HumanPlayer(player1Name, generateRandomUUID(), player1Color));
            GameManager.addGame(gameCode, game);

            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("gameCode", gameCode);
            jsonResponseObject.put("player1Name", player1Name);
            jsonResponseObject.put("player1Uuid", game.getPlayer0().getUuid());
            jsonResponseObject.put("player1Color", player1Color.name());

            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
        }

    }

    @PostMapping(
            path = "/index/controller/menschVsMensch/join")
    public void loadMenschVsMenschJoin(@RequestBody String body){
        colorPrint(body, PRINTCOLOR.YELLOW);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");
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

            GameManager.getGame(gameCode).setPlayer1(new HumanPlayer(player2Name, generateRandomUUID(), player2StoneColor));
        }
        else {
            System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName() + ": GameCode falsch – Ein Spieler versuchte einem nicht existierenden Game beizutreten");
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

        Game game = new Game(new HumanPlayer(player1Name, generateRandomUUID(), player1Color), new ComputerPlayer(computerName, generateRandomUUID(), computerColor));
        String gameCode = GameManager.addGameAndGetGameCode(game);

        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("modus", modus);
        jsonResponseObject.put("player1Uuid", game.getPlayer0().getUuid());
        jsonResponseObject.put("gameCode", gameCode);


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
        String modus = jsonObject.getString("modus");
        String computerName1 = jsonObject.getString("computerName1");
        String computerName2 = jsonObject.getString("computerName2");
        String player1Color = jsonObject.getString("player1Color");

        Game game = new Game(new ComputerPlayer(computerName1, generateRandomUUID(), STONECOLOR.BLACK), new ComputerPlayer(computerName2, generateRandomUUID(), STONECOLOR.WHITE));
        String gameCode = GameManager.addGameAndGetGameCode(game);

        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("modus", modus);
        jsonResponseObject.put("gameCode", gameCode);
        jsonResponseObject.put("computerName1", computerName1);
        jsonResponseObject.put("computerName2", computerName2);
        jsonResponseObject.put("player1Color", player1Color);

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
