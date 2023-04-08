package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.classes.Player;
import com.example.dotsandboxes.model.enums.PlayerNumber;

import java.util.ArrayList;
import java.util.List;

public class MCTSNode {
    private AIBoard board;
    private int visits;
    private int score;
    private List<MCTSNode> children = new ArrayList<>();
    MCTSNode parent = null;

    public MCTSNode(AIBoard initBoard) {
        this.board = initBoard;
        this.visits = 0;
        this.score = 0;
    }

    void addChild(MCTSNode node) {
        children.add(node);
    }

    //getters
    MCTSNode getChildWithMaxScore() {
        MCTSNode result = null;
        if (!children.isEmpty()) {
            result = children.get(0);
            for (int i = 1; i < children.size(); i++) {
                if (children.get(i).getScore() > result.getScore()) {
                    result = children.get(i);
                }
            }
        }
        return result;
    }
    public List<MCTSNode> getChildren() {return children;}
    public AIBoard getBoard() {return board;}
    public int getScore() {return score;}
    public int getVisits() {return visits;}

    //setters
    public void setScore(int score) {this.score = score;}

    public void incVisits() {this.visits++;}
    public void decVisits() {this.visits--;}
    public void incScore() {this.score++;}
    public void decScore() {this.score--;}
}
