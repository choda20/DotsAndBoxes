package com.example.dotsandboxes.AI;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * a class that represents a node in a Monte Carlo tree
 */
public class MCTSNode {
    private AIBoard board;  // the state of the game in the board
    private int visits; // the number of times the node was explored
    private double score; // the score of the node
    private List<MCTSNode> children; // a list of all possible actions on the board
    private MCTSNode parent; // the node that led to this board

    /**
     * full constructor that sets up the current board.
     * @param initBoard the game board
     */
    public MCTSNode(AIBoard initBoard) {
        this.board = initBoard;
        this.visits = 0;
        this.score = 0;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    /**
     * function that adds the node parameter as a child to the current node
     * @param node node to be made a child
     */
    public void addChild(MCTSNode node) {
        children.add(node);
    }

    /**
     * function that returns the child with the highest score
     * @return highest scored child
     * run time: O(n) when n=number of children
     */
    public MCTSNode getChildWithMaxScore() {
        return children.stream().max(Comparator.comparingDouble(MCTSNode::getScore)).orElse(null);
    }

    /**
     * function that increase the amount of times the node was visited by 1
     */
    public void incVisits() {this.visits++;}

    /**
     * function that increase the nodes score by 1
     */
    public void incScore() {this.score++;}

    //general getters
    public List<MCTSNode> getChildren() {return children;}
    public AIBoard getBoard() {return board;}
    public double getScore() {return score;}
    public int getVisits() {return visits;}
    public MCTSNode getParent() {return parent;}

    //general setters
    public void setScore(double score) {this.score = score;}
    public void setParent(MCTSNode parent) {this.parent = parent;}
}
