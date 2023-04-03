package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.Board;

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
    }

    void addChild(MCTSNode node) {
        children.add(node);
    }

    //getters
    MCTSNode getChildWithMaxScore() {
        MCTSNode result = children.get(0);
        for(int i=1;i<children.size();i++) {
            if (children.get(i).getScore() > result.getScore()) {
                result = children.get(i);
            }
        }
        return result;
    }
    public AIBoard getBoard() {return board;}
    public List<MCTSNode> getChildren() {return children;}
    public int getScore() {return score;}
    public int getVisits() {return visits;}

    //setters
    public void setScore(int score) {this.score = score;}

    public void incVisits() {this.visits++;}
    public void incScore() {this.score++;}
    public void printNode() {
        int j,k;
        for (j=0;j<board.getGridSize();j++) {
            for (k=0;k<board.getGridSize()-1;k++) {
                System.out.print(board.getHorizontalLines()[j][k].isConnected() + " ");
            }
            System.out.println();
            for (k=0;k<board.getGridSize()-1;k++) {
                System.out.print(board.getVerticalLines()[j][k].isConnected() + " ");
            }
            System.out.println();
        }
    }
}
