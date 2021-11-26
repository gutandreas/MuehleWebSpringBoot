package edu.andreasgut.MuehleWebSpringBoot;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;


public class GameTree {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    private GameTreeNode root;


    public GameTree() {
        root = new GameTreeNode();
    }

    public void initializeRoot(Board board){
        root.setBoard(board);
        root.getChildren().clear();
    }

    public GameTreeNode getLeafWithBestScore() {
        int max = Integer.MIN_VALUE;
        GameTreeNode bestLeaf = null;

        for (GameTreeNode set : getLeaves()){
            if (set.getScore() > max){
                max = set.getScore();
                bestLeaf = set;
            }
        }

        return bestLeaf;
    }


    public LinkedList<GameTreeNode> getLeaves() {

        LinkedList<GameTreeNode> leaves = new LinkedList<>();

        getLeavesRecursive(root, leaves);

        return leaves;

    }

    private void getLeavesRecursive(GameTreeNode currentSet, LinkedList<GameTreeNode> leaves){
        if (currentSet.getChildren().isEmpty() && !leaves.contains(currentSet)) {
            leaves.add(currentSet);
        } else {
            for (GameTreeNode child : currentSet.getChildren()) {
                getLeavesRecursive(child, leaves);
            }
        }
    }

    public Position getBestPut(){
        GameTreeNode currentNode = getLeafWithBestScore();

        while (currentNode.getParent() != root){
            currentNode = currentNode.getParent();
        }


        return currentNode.getPut();

    }

    public Move getBestMove(){
        GameTreeNode currentNode = getLeafWithBestScore();

        while (currentNode.getParent() != root){
            currentNode = currentNode.getParent();
        }

        return currentNode.getMove();
    }

    public Position getBestKill(){
        GameTreeNode currentNode = getLeafWithBestScore();

        while (currentNode.getParent() != root){
            currentNode = currentNode.getParent();
        }

        return currentNode.getKill();
    }

    public void keepOnlyWorstChildren(GameTreeNode parent, int numberOfChildren){
        parent.getChildren().sort(new Comparator<GameTreeNode>() {
            @Override
            public int compare(GameTreeNode o1, GameTreeNode o2) {
                if (o1.getScore() > o2.getScore()){
                    return 1;
                }
                if (o1.getScore() == o2.getScore()){
                    return 0;
                }
                else return -1;
            }
        });

        Iterator<GameTreeNode> iterator = parent.getChildren().iterator();

        if (parent.getChildren().size() > numberOfChildren) {
            for (int i = 0; i < numberOfChildren; i++) {
                iterator.next();
            }

            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }

    }

    public void keepOnlyBestChildren(GameTreeNode parent, int numberOfChildren){
        parent.getChildren().sort(new Comparator<GameTreeNode>() {
            @Override
            public int compare(GameTreeNode o1, GameTreeNode o2) {
                if (o1.getScore() > o2.getScore()){
                    return -1;
                }
                if (o1.getScore() == o2.getScore()){
                    return 0;
                }
                else return 1;
            }
        });

        Iterator<GameTreeNode> iterator = parent.getChildren().iterator();

        if (parent.getChildren().size() > numberOfChildren) {
            for (int i = 0; i < numberOfChildren; i++) {
                iterator.next();
            }

            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }

    }



    public void addSet(GameTreeNode parent, GameTreeNode child){
        parent.getChildren().add(child);
        child.setParent(parent);
    }

    public GameTreeNode getRoot() {
        return root;
    }


    public Stack<GameTreeNode> getPathToBestLeaf(){
        Stack<GameTreeNode> path = new Stack<>();
        GameTreeNode set = getLeafWithBestScore();

        while (set!=root){
            path.push(set);
            set = set.getParent();}

        return path;
    }

    @Override
    public String toString(){
        String string = "Gametree: \n \n";

        //return toStringRecursive(root, string);

        int counter1 = 0;

        for (GameTreeNode currentNode : root.getChildren()) {
            int counter2 = 0;
            string += ANSI_GREEN + "Level: " + currentNode.getLevel() + ", Pfad: " + ++counter1 + " \n";
            if (currentNode.getPut() != null){
                if (currentNode.getKill() != null){
                    string += "Put an " + currentNode.getPut() + " mit Kill an " + currentNode.getKill() + "\n";
                }
                else {
                    string += "Put an " + currentNode.getPut() + "\n";
                }
            }
            if (currentNode.getMove() != null){
                if (currentNode.getKill() != null){
                    string += currentNode.getMove() + " mit Kill an " + currentNode.getKill() + "\n";
                }
                else {
                    string += currentNode.getMove() + "\n";
                }
            }
            string += currentNode.getBoard();
            string += currentNode.getScoreDetails() + "\n \n" + ANSI_RESET;
            for (GameTreeNode currentNode2 : currentNode.getChildren()){
                int counter3 = 0;
                string += ANSI_YELLOW + "Level: " + currentNode2.getLevel() + ", Pfad: " + counter1 + "." + ++counter2 + " \n";
                if (currentNode2.getPut() != null){
                    if (currentNode2.getKill() != null){
                        string += "Put an " + currentNode2.getPut() + " mit Kill an " + currentNode2.getKill() + "\n";
                    }
                    else {
                        string += "Put an " + currentNode2.getPut() + "\n";
                    }
                }
                if (currentNode2.getMove() != null){
                    if (currentNode2.getKill() != null){
                        string += currentNode2.getMove() + " mit Kill an " + currentNode2.getKill() + "\n";
                    }
                    else {
                        string += currentNode2.getMove() + "\n";
                    }
                }
                string += currentNode2.getBoard();
                string += currentNode2.getScoreDetails() + "\n \n" + ANSI_RESET;
                for (GameTreeNode currentNode3 : currentNode2.getChildren()){
                    int counter4 = 0;
                    string += ANSI_BLUE + "Level: " + currentNode3.getLevel() + ", Pfad: " + counter1 + "." + counter2 + "." + ++counter3 + "\n";
                    if (currentNode3.getPut() != null){
                        if (currentNode3.getKill() != null){
                            string += "Put an " + currentNode3.getPut() + " mit Kill an " + currentNode3.getKill() + "\n";
                        }
                        else {
                            string += "Put an " + currentNode3.getPut() + "\n";
                        }
                    }
                    if (currentNode3.getMove() != null){
                        if (currentNode3.getKill() != null){
                            string += currentNode3.getMove() + " mit Kill an " + currentNode3.getKill() + "\n";
                        }
                        else {
                            string += currentNode3.getMove() + "\n";
                        }
                    }
                    string += currentNode3.getBoard();
                    string += currentNode3.getScoreDetails() + "\n \n" + ANSI_RESET;
                    for (GameTreeNode currentNode4 : currentNode3.getChildren()){
                        string += ANSI_PURPLE + "Level: " + currentNode4.getLevel() + ", Pfad: " + counter1 + "." + counter2 + "." + counter3 + "." + ++counter4 +  "\n";
                        if (currentNode4.getPut() != null){
                            if (currentNode4.getKill() != null){
                                string += "Put an " + currentNode4.getPut() + " mit Kill an " + currentNode4.getKill() + "\n";
                            }
                            else {
                                string += "Put an " + currentNode4.getPut() + "\n";
                            }
                        }
                        if (currentNode.getMove() != null){
                            if (currentNode4.getKill() != null){
                                string += currentNode4.getMove() + " mit Kill an " + currentNode4.getKill() + "\n";
                            }
                            else {
                                string += currentNode4.getMove() + "\n";
                            }
                        }
                        string += currentNode4.getBoard();
                        string += currentNode4.getScoreDetails() + "\n \n"  + ANSI_RESET;

            }}}}
        return string;
    }

    private String toStringRecursive(GameTreeNode set, String string){

        for (GameTreeNode currentSet : set.getChildren()) {
            string += "Level: " + currentSet.getLevel() + "\n";
            string += currentSet.getBoard();
            string += "Resultierender Score: " + currentSet.getScore() + "\n \n";
            toStringRecursive(currentSet, string);
        }

        return string;


        }

}
