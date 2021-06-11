package edu.andreasgut.MuehleWebSpringBoot;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class GameCompController {

    @PostMapping(
            path = "/gameComp/controller/put")
    public ResponseEntity<String> putComp(@RequestBody String body) {

        JSONObject jsonRequestObject = new JSONObject(body);
        Game game = GameManager.getGame(jsonRequestObject.getString("gameCode"));
        String uuid = jsonRequestObject.getString("uuid");
        Board board = game.getBoard();
        int index = game.getPlayerIndexByUuid(uuid);

        System.out.println(board);

        Position position = game.getPlayerByUuid(uuid).put(board, index);

        if (board.checkPut(position)){
            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("ring", position.getRing());
            jsonResponseObject.put("field", position.getField());
            board.putStone(position, index);

            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
        }
        else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("-");
        }



    }


    @PostMapping(
            path = "/gameComp/controller/kill")
    public ResponseEntity<String> killComp(@RequestBody String body) {

        JSONObject jsonRequestObject = new JSONObject(body);
        Game game = GameManager.getGame(jsonRequestObject.getString("gameCode"));
        String uuid = jsonRequestObject.getString("uuid");
        Board board = game.getBoard();
        int enemyIndex = 1 - game.getPlayerIndexByUuid(uuid);

        Position position = game.getPlayerByUuid(uuid).kill(board, enemyIndex);

        if (board.checkKill(position, enemyIndex)) {

            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("ring", position.getRing());
            jsonResponseObject.put("field", position.getField());
            board.clearStone(position);

            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
        }
        else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("-");
        }
    }

    @PostMapping(
            path = "/gameComp/controller/move")
    public ResponseEntity<String> moveComp(@RequestBody String body) {

        JSONObject jsonRequestObject = new JSONObject(body);
        String gameCode = jsonRequestObject.getString("gameCode");
        String playerUuid = jsonRequestObject.getString("playerUuid");
        Game game = GameManager.getGame(gameCode);
        Board board = game.getBoard();
        int playerIndex = game.getPlayerIndexByUuid(playerUuid);
        boolean allowedToJump = board.countPlayersStones(playerIndex) == 3;

        Move move = game.getPlayerByIndex(playerIndex).move(board, playerIndex, allowedToJump);

        if (board.checkMove(move, allowedToJump)){

            JSONObject jsonResponseObject = new JSONObject();
            jsonResponseObject.put("moveFromRing", move.getFrom().getRing());
            jsonResponseObject.put("moveFromField", move.getFrom().getField());
            jsonResponseObject.put("moveToRing", move.getTo().getRing());
            jsonResponseObject.put("moveToField", move.getTo().getField());
            board.move(move, playerIndex);

            return ResponseEntity.status(HttpStatus.OK).body(jsonResponseObject.toString());
        }
        else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("-");
        }}




}