package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.Board;

import java.util.ArrayList;
import java.util.List;

public class MCTSNode {
    public Board board;
    int visits;
    int score;
    int maxChildIndex;
    List<MCTSNode> children = new ArrayList<>();

    MCTSNode parent = null;

    public MCTSNode(Board initBoard) {
        this.board = initBoard;
        this.maxChildIndex = -1;
    }

    MCTSNode getChildWithMaxScore() {
        return children.get(maxChildIndex);
    }

    void addChild(MCTSNode node) {
        children.add(node);
        if (maxChildIndex >= 0 && children.get(maxChildIndex).score < node.score) {
            maxChildIndex = children.indexOf(node);
        }
    }
}
