package edu.andreasgut.MuehleWebSpringBoot;


import java.util.Stack;

public class ComputerPlayer extends Player {


    ScorePoints putPoints;
    ScorePoints movePoints;
    int levelLimit;
    Game game;

    public ComputerPlayer(String name, STONECOLOR stonecolor, String uuid, ScorePoints putPoints, ScorePoints movePoints, int levelLimit) {
        super(name, uuid, stonecolor);
        this.putPoints = putPoints;
        this.movePoints = movePoints;
        this.levelLimit = levelLimit;
    }

    GameTree gameTree = new GameTree();

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    Position put(Board board, int playerIndex) {

        gameTree.initializeRoot(board);

        recursivePutBfs(gameTree.getRoot(), putPoints, movePoints, playerIndex, playerIndex, levelLimit);

        System.out.println(gameTree);

        Stack<GameTreeNode> winningPath = gameTree.getPathToBestLeaf();
        System.out.println("Gewinnerpfad:");
        while (!winningPath.isEmpty()){
            System.out.println(winningPath.pop());
        }

        System.out.println("Gesetzter Stein: " + gameTree.getBestPut());
        return gameTree.getBestPut();
    }

    private void recursivePutBfs(GameTreeNode set, ScorePoints putPoints, ScorePoints movePoints, int scorePlayerIndex, int currentPlayerIndex, int levelLimit){

        if (set.getLevel()==levelLimit){
            return;
        }

        int tempCurrentPlayerIndex;

        if (set.getLevel()%2 == 0){
            tempCurrentPlayerIndex = scorePlayerIndex;
        }
        else {
            tempCurrentPlayerIndex = 1 - scorePlayerIndex;
        }

        for (Position freeField : Advisor.getAllFreeFields(set.getBoard())){
            pretendPut(set.getBoard(), freeField, putPoints, set, scorePlayerIndex, tempCurrentPlayerIndex, set.getLevel()+1);
        }

        if (set.getLevel()%2 == 0){
            gameTree.keepOnlyBestChildren(set, 10);}
        else {
            gameTree.keepOnlyWorstChildren(set, 1);
        }

        for (GameTreeNode child : set.getChildren()){
            if (game.getRound() + child.getLevel() < 18){
                recursivePutBfs(child, putPoints, movePoints, scorePlayerIndex, tempCurrentPlayerIndex, levelLimit);
            }
            else {
                recursiveMoveBfs(child, movePoints, scorePlayerIndex, tempCurrentPlayerIndex, levelLimit);
            }

        }
    }


    private void pretendPut(Board board, Position put, ScorePoints scorePoints, GameTreeNode parent, int scorePlayerIndex, int currentPlayerIndex, int level){

        GameTreeNode gameTreeNode1 = new GameTreeNode();
        gameTreeNode1.setPut(put);
        gameTreeNode1.setLevel(level);

        Board clonedBoard1 = (Board) board.clone();
        clonedBoard1.putStone(put, currentPlayerIndex);

        gameTreeNode1.setBoard(clonedBoard1);
        gameTreeNode1.setScore(Advisor.getScore(gameTreeNode1, scorePoints, scorePlayerIndex, false));



        if (gameTreeNode1.getBoard().checkMorris(gameTreeNode1.getPut())){
            for (Position killPosition : Advisor.getAllPossibleKills(clonedBoard1,currentPlayerIndex)){
                GameTreeNode gameTreeNode2 = new GameTreeNode();
                gameTreeNode2.setPut(put);
                gameTreeNode2.setLevel(level);
                gameTreeNode2.setKill(killPosition);

                Board clonedBoard2 = (Board) clonedBoard1.clone();
                clonedBoard2.clearStone(killPosition);

                gameTreeNode2.setBoard(clonedBoard2);
                gameTreeNode2.setScore(Advisor.getScore(gameTreeNode2, scorePoints, scorePlayerIndex, false));
                gameTree.addSet(parent, gameTreeNode2);
            }
        }
        else {
            gameTree.addSet(parent, gameTreeNode1);
        }

    }

    @Override
    Move move(Board board, int playerIndex, boolean allowedToJump) {


        gameTree.initializeRoot(board);

        recursiveMoveBfs(gameTree.getRoot(), movePoints, playerIndex, playerIndex, levelLimit);

        System.out.println(gameTree);

        Stack<GameTreeNode> winningPath = gameTree.getPathToBestLeaf();
        System.out.println("Gewinnerpfad:");
        while (!winningPath.isEmpty()){
            System.out.println(winningPath.pop());
        }

        System.out.println("Getätigter Zug: " + gameTree.getBestMove());
        return gameTree.getBestMove();

    }

    private void recursiveMoveBfs(GameTreeNode set, ScorePoints scorePoints, int scorePlayerIndex, int currentPlayerIndex, int levelLimit){

        if (set.getLevel()==levelLimit){
            return;
        }

        int tempCurrentPlayerIndex;

        if (set.getLevel()%2 == 0){
            tempCurrentPlayerIndex = scorePlayerIndex;
        }
        else {
            tempCurrentPlayerIndex = 1 - scorePlayerIndex;
        }

        for (Move move : Advisor.getAllPossibleMoves(set.getBoard(), tempCurrentPlayerIndex)){
            pretendMove(set.getBoard(), move, scorePoints, set, scorePlayerIndex, tempCurrentPlayerIndex, set.getLevel()+1);
        }

        if (set.getLevel()%2 == 0){
            gameTree.keepOnlyBestChildren(set, 3);}
        else {
            gameTree.keepOnlyWorstChildren(set, 1);
        }


        for (GameTreeNode child : set.getChildren()){
            recursiveMoveBfs(child, scorePoints, scorePlayerIndex, tempCurrentPlayerIndex, levelLimit);
        }
    }


    private void pretendMove(Board board, Move move, ScorePoints scorePoints, GameTreeNode parent, int scorePlayerIndex, int currentPlayerIndex, int level){

        GameTreeNode gameTreeNode1 = new GameTreeNode();
        gameTreeNode1.setMove(move);
        gameTreeNode1.setLevel(level);

        Board clonedBoard1 = (Board) board.clone();
        clonedBoard1.move(move, currentPlayerIndex);

        gameTreeNode1.setBoard(clonedBoard1);
        gameTreeNode1.setScore(Advisor.getScore(gameTreeNode1, scorePoints, scorePlayerIndex, false));



        if (gameTreeNode1.getBoard().checkMorris(gameTreeNode1.getMove().getTo())){
            for (Position killPosition : Advisor.getAllPossibleKills(clonedBoard1,currentPlayerIndex)){
                GameTreeNode gameTreeNode2 = new GameTreeNode();
                gameTreeNode2.setMove(move);
                gameTreeNode2.setLevel(level);
                gameTreeNode2.setKill(killPosition);

                Board clonedBoard2 = (Board) clonedBoard1.clone();
                clonedBoard2.clearStone(killPosition);

                gameTreeNode2.setBoard(clonedBoard2);
                gameTreeNode2.setScore(Advisor.getScore(gameTreeNode2, scorePoints, scorePlayerIndex, false));
                gameTree.addSet(parent, gameTreeNode2);
            }
        }
        else {
            gameTree.addSet(parent, gameTreeNode1);
        }
    }


    @Override
    Position kill(Board board,  int otherPlayerIndex) {

        return gameTree.getBestKill();

    }






}
