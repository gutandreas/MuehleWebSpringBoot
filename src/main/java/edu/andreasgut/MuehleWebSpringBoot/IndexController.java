package edu.andreasgut.MuehleWebSpringBoot;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.time.LocalTime;
import java.util.Timer;
import java.util.UUID;


@RestController
public class IndexController {

    private final int TIMELIMIT = 12 * 60 * 60 * 1000;


    @GetMapping(
            path = "/index/loadIndex")
    public ModelAndView loadIndex() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/index.html");
        return modelAndView;
    }


    @PostMapping(
            path = "/index/controller/menschVsMensch/start",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loadMenschVsMenschStart(@RequestBody String body) {


        colorPrint(body, PRINTCOLOR.YELLOW);
        JSONObject jsonObject = new JSONObject(body);
        String player1Name = jsonObject.getString("player1Name");
        String gameCode = jsonObject.getString("gameCode");
        STONECOLOR player1Color;
        if (jsonObject.getString("player1Color").equals("BLACK")) {
            player1Color = STONECOLOR.BLACK;
        } else {
            player1Color = STONECOLOR.WHITE;
        }

        if (GameManager.doesGameExist(gameCode)) {
            System.out.println(LocalTime.now() + " – " + "IndexController: Bereits vorhandener Gamecode. " +
                    "Dieser Gamecode wird bereits für ein anderes Spiel verwendet.");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Dieser Gamecode wird bereits " +
                    "für ein anderes Spiel verwendet.");
        } else {
            Game game = new Game(new HumanPlayer(player1Name, generateRandomUUID(), player1Color));
            game.setGameCode(gameCode);
            GameManager.addGame(gameCode, game);
            new Timer().schedule(new ClearTask(gameCode), TIMELIMIT);

            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("player1Uuid", game.getPlayer0().getUuid());
            jsonResponseObject.put("player1Color", player1Color.name());
            jsonResponseObject.put("player1Index", 0);
            jsonResponseObject.put("html", getHTMLContent("game"));

            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
        }

    }


    @PostMapping(
            path = "/index/controller/menschVsMensch/join")
    public ResponseEntity<String> loadMenschVsMenschJoin(@RequestBody String body) {
        colorPrint(body, PRINTCOLOR.YELLOW);
        JSONObject jsonObject = new JSONObject(body);
        String player2Name = jsonObject.getString("player2Name");
        String gameCode = jsonObject.getString("gameCode");

        if (GameManager.doesGameExist(gameCode) && !GameManager.getGame(gameCode).isGameComplete()) {
            STONECOLOR player1StoneColor = GameManager.getGame(gameCode).getPlayer0().getStonecolor();

            STONECOLOR player2StoneColor;
            if (player1StoneColor == STONECOLOR.BLACK) {
                player2StoneColor = STONECOLOR.WHITE;
            } else {
                player2StoneColor = STONECOLOR.BLACK;
            }

            Game game = GameManager.getGame(gameCode);

            game.setPlayer1(new HumanPlayer(player2Name, generateRandomUUID(), player2StoneColor));

            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("player1Name", GameManager.getGame(gameCode).getPlayer0().getName());
            jsonResponseObject.put("player2Color", player2StoneColor);
            jsonResponseObject.put("player2Uuid", game.getPlayer1().getUuid());
            jsonResponseObject.put("player2Index", 1);
            jsonResponseObject.put("roboterConnected", GameManager.getGame(gameCode).isRoboterConnected());
            jsonResponseObject.put("roboterWatching", GameManager.getGame(gameCode).isRoboterWatching());
            jsonResponseObject.put("roboterPlaying", GameManager.getGame(gameCode).isRoboterPlaying());
            jsonResponseObject.put("html", getHTMLContent("game"));

            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());

        } else {
            System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName()
                    + ": GameCode falsch – Ein Spieler versuchte einem nicht existierenden " +
                    "oder schon kompletten Game beizutreten");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Dieser Gamecode existiert nicht " +
                    "oder das Game ist bereits komplett.");
        }
    }

    @PostMapping(
            path = "/index/controller/menschVsComputer",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loadMenschVsComputer(@RequestBody String body) {
        colorPrint(body, PRINTCOLOR.YELLOW);
        JSONObject jsonObject = new JSONObject(body);
        String modus = jsonObject.getString("modus");

        String player1Name = jsonObject.getString("player1Name");
        STONECOLOR player1Color = STONECOLOR.valueOf(jsonObject.getString("player1Color"));

        String computerName = jsonObject.getString("computerName");
        STONECOLOR computerColor;
        if (player1Color.equals(STONECOLOR.BLACK)) {
            computerColor = STONECOLOR.WHITE;
        } else {
            computerColor = STONECOLOR.BLACK;
        }

        ScorePoints putPoints = new ScorePoints(2000, 1000, 30, 200, 300, 3, -2000, -1000, -30, -200, -300, -3);
        ScorePoints movePoints = new ScorePoints(2000, 300, 250, 200, 300, 3, -2000, -300, -250, -200, -300, -3);
        int levelLimit = Integer.parseInt(jsonObject.getString("computerLevel"));

        ComputerPlayer computerPlayer = new ComputerPlayer(computerName, computerColor, generateRandomUUID(),
                putPoints, movePoints, levelLimit);
        Game game = new Game(new HumanPlayer(player1Name, generateRandomUUID(), player1Color), computerPlayer);
        computerPlayer.setGame(game);

        String gameCode = GameManager.generateGameCode();
        GameManager.addGame(gameCode, game);
        game.setGameCode(gameCode);
        new Timer().schedule(new ClearTask(gameCode), TIMELIMIT);

        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("modus", modus);
        jsonResponseObject.put("player1Uuid", game.getPlayer0().getUuid());
        jsonResponseObject.put("playerIndex", 0);
        jsonResponseObject.put("gameCode", gameCode);
        jsonResponseObject.put("start", "true");
        jsonResponseObject.put("html", getHTMLContent("game"));

        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
    }


    @PostMapping(
            path = "/index/controller/gameWatch")
    public ResponseEntity<String> loadGameWatch(@RequestBody String body) {
        colorPrint(body, PRINTCOLOR.YELLOW);
        JSONObject jsonObject = new JSONObject(body);
        String gameCode = jsonObject.getString("gameCode");

        if (!GameManager.doesGameExist(gameCode) || GameManager.hasGameAlreadyStarted(gameCode)) {
            System.out.println(LocalTime.now() + " – " + this.getClass().getSimpleName()
                    + ": GameCode falsch – Ein Spieler versuchte ein nicht existierenden " +
                    "oder schon gestartetes Game zu beobachten");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Dieser Gamecode existiert nicht " +
                    "oder das Game wurde bereits gestartet und kann deshalb nicht mehr beobachtet werden.");
        }

        Game game = GameManager.getGame(gameCode);
        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("player1Name", game.getPlayer0().getName());
        if (game.getPlayer1() != null) {
            jsonResponseObject.put("player2Name", game.getPlayer1().getName());
        } else {
            jsonResponseObject.put("player2Name", "");
        }
        jsonResponseObject.put("player1Color", game.getPlayer0().getStonecolor());
        jsonResponseObject.put("player1Index", 0);
        jsonResponseObject.put("gameCodeWatch", gameCode);
        jsonResponseObject.put("html", getHTMLContent("gameWatch"));

        return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
    }


    private void colorPrint(String text, PRINTCOLOR color) {
        System.out.print(color);
        System.out.println(text);
        System.out.print(PRINTCOLOR.RESET);
    }

    private String generateRandomUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private String getHTMLContent(String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        String string;

        BufferedReader in;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/public/" + filename + ".html");
            in = new BufferedReader(new InputStreamReader(inputStream));

            while ((string = in.readLine()) != null)
                stringBuilder.append(string);

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String content = stringBuilder.toString();

        return content;
    }

}
