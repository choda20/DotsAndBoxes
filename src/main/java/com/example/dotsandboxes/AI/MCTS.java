package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;
import javafx.util.Pair;


import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MCTS {
    private final AIBoard gameBoard;
    private final int playerId;
    private final int opponentId;
    private final int computations;

    public MCTS(AIBoard gameBoard,int computations) {
        this.computations = computations;
        this.gameBoard = gameBoard;
        this.playerId = 1;
        this.opponentId = 0;
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
            if (selected.getBoard().isGameOngoing()) {
                selected = expandNodeAndReturnRandom(promisingNode);
            }

            //SIMULATE
            int playoutResult = simulateLightPlayout(selected);

            //PROPAGATE
            backPropagation(selected);
        }
        MCTSNode best = tree.getChildWithMaxScore();
        Instant end = Instant.now();
        long mills = end.toEpochMilli() - start.toEpochMilli();
        ModelLine move = best.getBoard().getLastMove();

        System.out.println("Did " + counter + " expansions/simulations within " + mills + " mills");
        System.out.println("Best move scored " + best.getChildWithMaxScore() + " and was visited " + best.getVisits() + " times");
        System.out.println("Move was made at: " + move.getRow() + "," + move.getColumn() + " on horizontal line: " + move.getIsHorizontal() + "\n");
        return move;
    }

    // if node is already a leaf, return the leaf
    private MCTSNode expandNodeAndReturnRandom(MCTSNode node) {
        MCTSNode child;
        AIBoard board = node.getBoard();
        Random generator = new Random();

        for (AIBoard move : board.getAvlNextMoves()) {
            child = new MCTSNode(move);
            child.setParent(node);
            child.setScore(child.getBoard().getScoreDifference());
            node.addChild(child);
        }

        if (node.getChildren().size() == 0)
            return node;
        int random = generator.nextInt(node.getChildren().size());
        return node.getChildren().get(random);
    }

    private void backPropagation(MCTSNode selected) {
        MCTSNode node = selected,  previous = null;
        int currentPlayer, penalty = selected.getBoard().getCurrentPlayer() == playerId ? 5: -7, accumulated = penalty;
        while (node != null) { // look for the root
            node.incVisits();
            currentPlayer = 1 - node.getBoard().getCurrentPlayer();
            if (currentPlayer == playerId) {
                node.incScore();
            }
            if (previous != null && previous.getScore() > node.getScore())
                node.setScore(previous.getScore());
            previous = node;
            node = node.getParent();
        }
    }

    private int simulateLightPlayout(MCTSNode promisingNode) {
        MCTSNode node = promisingNode;
        AIBoard board;
        ModelLine bestMove;
        Random rand = new Random();
        boolean best = true;
        while (node.getBoard().isGameOngoing() && node.getBoard().getBestMoves().size()>0) {
            bestMove = node.getBoard().getBestMove();
            board = new AIBoard(new AIBoard(node.getBoard()));
            board.performMove(bestMove.getRow(),bestMove.getColumn(),bestMove.getIsHorizontal() ? LineType.horizontal : LineType.vertical);
            MCTSNode child = new MCTSNode(board);
            child.setScore(board.getScoreDifference());
            if (best) {
                child.setScore(Integer.MAX_VALUE / 2 + board.getScoreDifference());
                best = false;
            }
            child.setParent(node);
            node.addChild(child);

            node = child;
        }
        return node.getBoard().getCurrentPlayer();
    }


    private MCTSNode selectPromisingNode(MCTSNode tree) {
        MCTSNode node = tree;
        List<MCTSNode> unexploredChildren;

        while (node.getChildren().size() != 0) {
            unexploredChildren = node.getChildren().stream().filter(c -> c.getVisits() == 0).collect(Collectors.toList());
            if (unexploredChildren.size() > 0) {
                return unexploredChildren.get(0);
            }
            node = UCT.findBestNodeWithUCT(node);
        }

        return node;
    }


}
