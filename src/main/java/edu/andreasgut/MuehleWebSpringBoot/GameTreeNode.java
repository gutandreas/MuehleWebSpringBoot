package edu.andreasgut.MuehleWebSpringBoot;


import java.util.LinkedList;

public class GameTreeNode {

    private int level;
    private Board board;
    private Position put;
    private Move move;
    private Position kill;
    private int score;
    private int inheritedScore;
    private GameTreeNode parent;
    private LinkedList<GameTreeNode> children = new LinkedList<>();
    private String scoreDetails;
    private boolean visited = false;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Position getPut() {
        return put;
    }

    public void setPut(Position put) {
        this.put = put;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public Position getKill() {
        return kill;
    }

    public void setKill(Position kill) {
        this.kill = kill;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getInheritedScore() {
        return inheritedScore;
    }

    public void setInheritedScore(int inheritedScore) {
        this.inheritedScore = inheritedScore;
    }

    public GameTreeNode getParent() {
        return parent;
    }

    public void setParent(GameTreeNode parent) {
        this.parent = parent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public LinkedList<GameTreeNode> getChildren() {
        return children;
    }

    public void setChildren(LinkedList<GameTreeNode> children) {
        this.children = children;
    }

    public String getScoreDetails() {
        return scoreDetails;
    }

    public void setScoreDetails(String scoreDetails) {
        this.scoreDetails = scoreDetails;
    }

    @Override
    public String toString() {


        if (put != null){
            if (kill == null){
                return  "Level " + level + ", Put an " + put + ", Score: " + score + ", Inherited Score: " + inheritedScore + "\n" + board;
            }
            else {
                return  "Level " + level + ", Put an " + put + " mit Kill an " + kill + ", Score: " + score + ", Inherited Score: " + inheritedScore + "\n" + board;
            }
        }

        if (move != null){
            if (kill == null) {
                return "Level " + level + ", " + move + ", Score: " + score + ", Inherited Score: " + inheritedScore + "\n" + board;
            }
            else {
                return  "Level " + level + ", Move an " + move + " mit Kill an " + kill + ", Score: " + ", Inherited Score: " + inheritedScore + score + "\n" + board;
            }
        }


        return null;}
}
