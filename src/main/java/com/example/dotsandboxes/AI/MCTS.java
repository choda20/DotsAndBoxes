package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import javafx.util.Pair;


import java.time.Instant;
import java.util.Random;

public class MCTS {
    private final AIBoard gameBoard; // the game board the algorithm should
    // pick a move for
    private final Random generator; // used to roll random numbers


    /**
     * full constructor that initializes all class fields
     * @param gameBoard the current game board
     */
    public MCTS(AIBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.generator = new Random();
    }

    /**
     * function that starts the MCTS algorithm
     * in order to simplify the run-time of the function, lets consider a
     * case where m and k are equal to n(not really a possible scenario since
     * n will always be bigger than both), in which case the run time will be
     * O((n^3)*I), where I = the number of iterations made, and n = the depth
     * of the tree.
     * @return the line selected by the algorithm
     */
    public ModelLine MCTSCalc() {
        long endTime = System.currentTimeMillis() + 2000;
        MCTSNode tree = new MCTSNode(gameBoard);

        while (System.currentTimeMillis() < endTime) {
            // select a node to explore
            MCTSNode promisingNode = selectPromisingNode(tree); // function
            // run-time is O(n*m) where N = the depth of the tree

            //expands the selected node if possible
            MCTSNode selected = promisingNode;
            if (selected.getBoard().isGameOngoing()) {
                selected = expandNodeAndReturnRandom(promisingNode); //
                // function run-time is O(m), where m = number of
                // possible best moves and
            }

            //simulates a game on the node
            int winningPlayer = simulateLightPlayout(selected); // function
            // run-time is O(n*m*k), where k = the chain each move is a part of

            //back propagates the results of the playout
            backPropagation(selected,winningPlayer); // function run-time is
            // O(n)
        }

        MCTSNode best = tree.getChildWithMaxScore();
        ModelLine move = best.getBoard().getLastMove();
        return move;
    }


    /**
     * function the expands the Monte Carlo tree by adding all "best" future
     * boards available from the given node as children to said node.
     * function run-time is O(n), where n=  the number of possible best moves
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
     * function run-time is O(n) where n=number of possible moves from the
     * start of the game(the tree depth basically)
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
     * function run-time is O(n*m*k) as described below
     * @param promisingNode
     * @return the id of the player who made the last move
     */
    // name needs to be changed to simulatePlayout, it is not changed to avoid
    // confusion with the project book where it is named simulateLightPlayout
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

        while (tempBoard.isGameOngoing()) { // O(n), where n = is the depth
            // of the game tree

            Pair<ModelLine,Integer> bestMoveAndScore=
                    tempBoard.getBestMove(); // O(m*k), n = number of
            // possible best moves and k = the chain each move is a part of
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
     * function run-time is O(n*m) as described below
     * @param tree the root of the Monte carlo tree
     * @return the promising node to be explored
     */
    private MCTSNode selectPromisingNode(MCTSNode tree) {
        MCTSNode node = tree;
        while (node.getChildren().size() != 0) { // O(n) since the tree depth
            // is equal or greater to the number of possible moves
            node = UCT.findBestNodeWithUCT(node); // O(m), m = the number of
            // children the node has
        }
        return node;
    }


}
