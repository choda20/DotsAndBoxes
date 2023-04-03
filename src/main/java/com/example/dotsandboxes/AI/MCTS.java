package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.Board;
import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.PlayerNumber;

import java.time.Instant;
import java.util.Random;

public class MCTS {
    private final AIBoard gameBoard;
    private final int computations;
    private final PlayerNumber player;

    public MCTS(AIBoard gameBoard,int computations,PlayerNumber player) {
        this.computations = computations;
        this.gameBoard = gameBoard;
        this.player = player;
    }

    public AIBoard MCTSCalc() {
        System.out.println("Starting MCTS!");
        Instant start = Instant.now();

        long counter = 0L;

        MCTSNode tree = new MCTSNode(gameBoard);

        while (counter < computations) {
            System.out.println("Counter at: " + counter);
            counter++;

            //SELECT
            MCTSNode promisingNode = selectPromisingNode(tree);
            System.out.println("Selected promising node");

            //EXPAND
            MCTSNode selected = promisingNode;
            if (selected.getBoard().getGameStatus().equals(AIGameStatus.GameInProgress)) {
                selected = expandNodeAndReturnRandom(promisingNode);
            }
            System.out.println("Expanded node");

            //SIMULATE
            AIGameStatus playoutResult = simulateLightPlayout(selected);
            System.out.println("Simulated");

            //PROPAGATE
            backPropagation(playoutResult, selected);
            System.out.println("BackPropagated");
        }
        System.out.println("Ended While");
        MCTSNode best = tree.getChildWithMaxScore();

        Instant end = Instant.now();
        long milis = end.toEpochMilli() - start.toEpochMilli();

        System.out.println("Did " + counter + " expansions/simulations within " + milis + " milis");
        System.out.println("Best move scored " + best.getChildWithMaxScore() + " and was visited " + best.getVisits() + " times");

        return best.getBoard();
    }

    // if node is already a leaf, return the leaf
    private MCTSNode expandNodeAndReturnRandom(MCTSNode node) {
        MCTSNode result = node;
        AIBoard board = node.getBoard();
        Random generator = new Random();

        for (AIBoard move : board.getAvlMoves()) {
            MCTSNode child = new MCTSNode(move);
            child.parent = node;
            node.addChild(child);

            result = child;
        }
        result.printNode();
        System.out.println();
        node.printNode();
        int random = generator.nextInt(node.getChildren().size());
        return node.getChildren().get(random);
    }

    private void backPropagation(AIGameStatus playerNumber, MCTSNode selected) {
        MCTSNode node = selected;

        while (node != null) { // look for the root
            node.incVisits();
            if (node.getBoard().getLastPlayer().equals(playerNumber)) {
                node.incScore();
            }

            node = node.parent;
        }
    }

    /**
     *
     * "Light playout" is to indicate that each move is chosen totally randomly,
     * in contrast to using some heuristic
     *
     */

    private AIGameStatus simulateLightPlayout(MCTSNode promisingNode) {
        MCTSNode node = new MCTSNode(promisingNode.getBoard());
        node.parent = promisingNode.parent;
        AIGameStatus boardStatus = node.getBoard().getGameStatus();

        if (boardStatus.equals(AIGameStatus.OpponentWon)) {
            node.parent.setScore(Integer.MIN_VALUE);
            return node.getBoard().getGameStatus();
        }

        while (node.getBoard().getGameStatus().equals(AIGameStatus.GameInProgress)) {
            AIBoard move = node.getBoard().getRandomMove();
            MCTSNode child = new MCTSNode(move);
            child.parent = node;
            node.addChild(child);

            node = child;
        }

        return node.getBoard().getGameStatus();
    }

    private MCTSNode selectPromisingNode(MCTSNode tree) {
        MCTSNode node = tree;
        while (node.getChildren().size() != 0) {
            System.out.println("what");
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }
}
