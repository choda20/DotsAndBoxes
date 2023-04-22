package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import javafx.util.Pair;


import java.time.Instant;
import java.util.Random;

public class MCTS {
    private final int playerId; // the id of the player
    private final AIBoard gameBoard; // the game board the algorithm should
    // pick a move for
    private final Random generator; // used to roll random numbers


    /**
     * full constructor that initializes all class fields
     * @param gameBoard the current game board
     */
    public MCTS(AIBoard gameBoard, int playerId) {
        this.gameBoard = gameBoard;
        this.generator = new Random();
        this.playerId = playerId;
    }

    /**
     * function that starts the MCTS algorithm
     * @return the line selected by the algorithm
     */
    public ModelLine MCTSCalc() {
        System.out.println("Starting MCTS!");
        Instant start = Instant.now();

        long simulationCounter = 0L,endTime = System.currentTimeMillis() + 1500;

        MCTSNode tree = new MCTSNode(gameBoard);

        while (System.currentTimeMillis() < endTime) {
            simulationCounter++;
            //SELECT
            MCTSNode promisingNode = selectPromisingNode(tree);

            //EXPAND
            MCTSNode selected = promisingNode;
            if (selected.getBoard().isGameOngoing()) {
                selected = expandNodeAndReturnRandom(promisingNode);
            }

            //SIMULATE
            int winningPlayer = simulateLightPlayout(selected);

            //PROPAGATE
            backPropagation(selected,winningPlayer);
        }
        MCTSNode best = tree.getChildWithMaxScore();
        Instant end = Instant.now();
        long mills = end.toEpochMilli() - start.toEpochMilli();
        ModelLine move = best.getBoard().getLastMove();

        System.out.println("Did " + simulationCounter + " " +
                "expansions/simulations within " + mills + " mills");
        System.out.println("Move was made at: " + move.getRow() + "," +
                move.getColumn() + " on line type: " +
                move.getIsHorizontal() + "\n");
        return move;
    }


    /**
     * function the expands the Monte Carlo tree by adding all "best" future
     * boards available from the given node as children to said node.
     * @param node a node to be expanded
     * @return a random child of the parameter node, or the node itself if
     * it is a leaf.
     */
    private MCTSNode expandNodeAndReturnRandom(MCTSNode node) {
        MCTSNode child;
        AIBoard board = node.getBoard();

        for (AIBoard move : board.getAvlNextMoves()) {
            child = new MCTSNode(move);
            child.setParent(node);
            node.addChild(child);
        }

        if (node.getChildren().size() == 0)
            return node;
        int random = generator.nextInt(node.getChildren().size());
        return node.getChildren().get(random);
    }

    /**
     * function that climbs the tree up to the root, while increasing the
     * score of nodes that the winning player played at by 10 points.
     * @param selected the node to climb from
     * @param winningPlayer the id of the winning plater
     */
    private void backPropagation(MCTSNode selected, int winningPlayer) {
        MCTSNode tempNode = selected;

        while (tempNode != null) { // look for the root
            tempNode.incVisits();

            if (tempNode.getBoard().getLastPlayer() == winningPlayer)
                tempNode.setScore(tempNode.getScore() + 10);

            tempNode = tempNode.getParent();
        }
    }

    /**
     * functions that simulates a full game from a given node, using the best
     * possible move available. (the move is removed from the available moves
     * lists each time so if the node will be simulated again a new move will
     * be explored). the function sets simulations that end in loss to the
     * minimum value of an integer, and tie to minimum-value/2.
     * @param promisingNode
     * @return the id of the player who made the last move
     */
    private int simulateLightPlayout(MCTSNode promisingNode) {
        MCTSNode tempNode = promisingNode,child;
        AIBoard childBoard,tempBoard = tempNode.getBoard() ;
        ModelLine bestMove;

        if (!tempBoard.isGameOngoing() &&
                tempBoard.getScoreDifference() <= 0) {
            int score = tempBoard.getScoreDifference() == 0 ?
                    Integer.MIN_VALUE/2 : Integer.MIN_VALUE;
            tempNode.getParent().setScore(score);
            return tempBoard.getLastPlayer();
        }

        while (tempBoard.isGameOngoing() &&
                tempBoard.getBestMovesList().size()>0) {

            Pair<ModelLine,Integer> bestMoveAndScore= tempBoard.getBestMove();
            bestMove = bestMoveAndScore.getKey();
            childBoard = new AIBoard(tempBoard);
            childBoard.performMove(bestMove.getRow(), bestMove.getColumn(),
                    bestMove.getIsHorizontal());
            child = new MCTSNode(childBoard);

            child.setScore(bestMoveAndScore.getValue());
            child.setParent(tempNode);
            tempNode.addChild(child);

            tempNode = child;
            tempBoard = tempNode.getBoard();
        }

        return tempNode.getBoard().getLastPlayer();
    }


    /**
     * function that selects a promising node for exploration using the
     * UCT formula.
     * @param tree the root of the Monte carlo tree
     * @return the promising node to be explored
     */
    private MCTSNode selectPromisingNode(MCTSNode tree) {
        MCTSNode node = tree;
        while (node.getChildren().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }


}
