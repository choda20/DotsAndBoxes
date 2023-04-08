package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import javafx.util.Pair;

import java.time.Instant;
import java.util.Random;

public class MCTS {
    private final AIBoard gameBoard;
    private final int computations;

    public MCTS(AIBoard gameBoard,int computations) {
        this.computations = computations;
        this.gameBoard = gameBoard;
    }

    public ModelLine MCTSCalc() {
        System.out.println("Starting MCTS!");
        Instant start = Instant.now();

        long counter = 0L;

        MCTSNode tree = new MCTSNode(gameBoard);

        while (counter < computations) {
            counter++;
            //SELECT
            MCTSNode promisingNode = selectPromisingNode(tree);

            //EXPAND
            MCTSNode selected = promisingNode;
            if (selected.getStatus().equals(GameStatus.Player1Turn) || selected.getStatus().equals(GameStatus.Player2Turn)) {
                selected = expandNodeAndReturnRandom(promisingNode);
            }

            //SIMULATE
            GameStatus playoutResult = simulateLightPlayout(selected);

            //PROPAGATE
            backPropagation(playoutResult, selected);
        }
        MCTSNode best = tree.getChildWithMaxScore();
        Instant end = Instant.now();
        long milis = end.toEpochMilli() - start.toEpochMilli();
        ModelLine move = best.getMove();

        System.out.println("Did " + counter + " expansions/simulations within " + milis + " milis");
        System.out.println("Best move scored " + best.getChildWithMaxScore() + " and was visited " + best.getVisits() + " times");
        System.out.println("Move was made at: " + move.getRow() + "," + move.getColumn() + " on horizontal line: " + move.isHorizontal());
        return move;
    }

    // if node is already a leaf, return the leaf
    private MCTSNode expandNodeAndReturnRandom(MCTSNode node) {
        MCTSNode result = node;
        MCTSNode child;
        Random generator = new Random();

        for (AIBoard move : gameBoard.getAvlNextMoves()) {
            child = new MCTSNode(move);
            child.parent = node;
            node.addChild(child);
            if (move.leavesOpenBox()) {
                child.setScore(Integer.MIN_VALUE);
            }
            result = child;
        }
        int random = generator.nextInt(node.getChildren().size());
        return node.getChildren().get(random);
    }

    private void backPropagation(GameStatus status, MCTSNode selected) {
        MCTSNode node = selected;

        while (node != null) { // look for the root
            node.incVisits();
            if (node.getBoard().getLastGameStatus().equals(status))
                node.incScore();
            node = node.parent;
        }

    }

    /**
     *
     * "Light playout" is to indicate that each move is chosen totally randomly,
     * in contrast to using some heuristic
     *
     */
    private GameStatus simulateLightPlayout(MCTSNode promisingNode) {
        MCTSNode node = new MCTSNode(promisingNode.getBoard());
        node.parent = promisingNode.parent;
        GameStatus boardStatus = node.getBoard().getGameStatus();

        if (boardStatus.equals(GameStatus.Player1Won)) {
            node.parent.setScore(Integer.MIN_VALUE);
            return node.getBoard().getGameStatus();
        }

        AIBoard move;
        MCTSNode child;
        while (node.getBoard().getGameStatus().equals(GameStatus.Player1Turn) || node.getBoard().getGameStatus().equals(GameStatus.Player2Turn)) {
            move = node.getBoard().getRandomMove();
            child = new MCTSNode(move);
            child.parent = node;
            node.addChild(child);
            if (move.leavesOpenBox()) {
                child.setScore(Integer.MIN_VALUE);
            }
            node = child;
        }
        node.setScore(node.getBoard().getScoreDifference());
        return node.getBoard().getGameStatus();
    }

    private MCTSNode selectPromisingNode(MCTSNode tree) {
        MCTSNode node = tree;
        while (node.getChildren().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    private void adjustScore(MCTSNode node) {
        Pair<Boolean,int[]> moveResult = node.getBoard().leavesBoxOpen();
        if (moveResult.getKey()) {
            node.setScore(Integer.MIN_VALUE);
        }
        if (node.getBoard().hasOpenBox())
            node.setScore(Integer.MAX_VALUE);
    }
}
