package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;


import java.time.Instant;
import java.util.Random;

public class MCTS {
    private final AIBoard gameBoard; // the game board the algorithm should pick a move for
    private final int playerId; // the id of the player who asked for a move
    private final int opponentId; // the id of the opponent
    private final int computations; // the amount of computations the algorithm is allowed
    private Random generator = new Random();


    /**
     * full constructor that initializes all class fields
     * @param gameBoard the current game board
     * @param computations the amount of computations allowed
     */
    public MCTS(AIBoard gameBoard,int computations) {
        this.computations = computations;
        this.gameBoard = gameBoard;
        this.playerId = 1;
        this.opponentId = 0;
        this.generator = new Random();
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

        System.out.println("Did " + simulationCounter + " expansions/simulations within " + mills + " mills");
        System.out.println("Move was made at: " + move.getRow() + "," + move.getColumn() + " on horizontal line: " + move.getIsHorizontal() + "\n");
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
     * function that goes up from the give node to the root, while updating
     * the number of visits for each node, and setting each node's score to the highest sum
     * of its children's score.
     * @param selected the node to get to the root from
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
     * functions that simulates a full game from a given node, using the best possible
     * move available. (the move is removed from the available moves lists each time so if the node
     * will be simulated again a new move will be explored)
     * the score of each node that is simulated is set to the difference between the player scores,
     * meaning the highest rated leafs will be when the AI wins.
     * @param promisingNode a node that seems promising for exploration
     */
    private int simulateLightPlayout(MCTSNode promisingNode) {
        MCTSNode tempNode = promisingNode;
        AIBoard board;
        ModelLine bestMove;

        if (!tempNode.getBoard().isGameOngoing() &&
                tempNode.getBoard().getScoreDifference() <= 0) {
            int score = tempNode.getBoard().getScoreDifference() == 0 ?
                    Integer.MIN_VALUE/2 : Integer.MIN_VALUE;
            tempNode.getParent().setScore(score);
            return tempNode.getBoard().getLastPlayer();
        }

        while (tempNode.getBoard().isGameOngoing() &&
                tempNode.getBoard().getBestMoves().size()>0) {

            bestMove = tempNode.getBoard().getBestMove();
            board = new AIBoard(new AIBoard(tempNode.getBoard()));
            board.performMove(bestMove.getRow(), bestMove.getColumn(),
                    bestMove.getIsHorizontal());
            MCTSNode child = new MCTSNode(board);

            child.setParent(tempNode);
            tempNode.addChild(child);

            tempNode = child;
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
