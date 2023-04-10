package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;


import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MCTS {
    private final AIBoard gameBoard; // the game board the algorithm should pick a move for
    private final int playerId; // the id of the player who asked for a move
    private final int opponentId; // the id of the opponent
    private final int computations; // the amount of computations the algorithm is allowed

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
    }

    /**
     * function that starts the MCTS algorithm
     * @return the line selected by the algorithm
     */
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
            simulateLightPlayout(selected);

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
            child.setScore(child.getBoard().getScoreDifference());
            node.addChild(child);
        }

        if (node.getChildren().size() == 0)
            return node;
        Random generator = new Random();
        int random = generator.nextInt(node.getChildren().size());
        return node.getChildren().get(random);
    }

    /**
     * function that goes up from the give node to the root, while updating
     * the number of visits for each node, and setting each node's score to the highest sum
     * of its children's score.
     * @param selected the node to get to the root from
     */
    private void backPropagation(MCTSNode selected) {
        MCTSNode node = selected,  previous = null;
        int currentPlayer;
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

    /**
     * functions that simulates a full game from a given node, using the best possible
     * move available. (the move is removed from the available moves lists each time so if the node
     * will be simulated again a new move will be explored)
     * the score of each node that is simulated is set to the difference between the player scores,
     * meaning the highest rated leafs will be when the AI wins.
     * @param promisingNode a node that seems promising for exploration
     */
    private void simulateLightPlayout(MCTSNode promisingNode) {
        MCTSNode node = promisingNode;
        AIBoard board;
        ModelLine bestMove;
        Random rand = new Random();

        while (node.getBoard().isGameOngoing() && node.getBoard().getBestMoves().size()>0) {

            bestMove = node.getBoard().getBestMove();
            board = new AIBoard(new AIBoard(node.getBoard()));
            board.performMove(bestMove.getRow(),bestMove.getColumn(),bestMove.getIsHorizontal() ? LineType.horizontal : LineType.vertical);
            MCTSNode child = new MCTSNode(board);

            child.setScore(board.getScoreDifference());


            child.setParent(node);
            node.addChild(child);

            node = child;
        }
    }


    /**
     * function that selects a promising node for exploration using the
     * UCT formula. the function prioritizes unexplored children, so it will
     * force pick an unexplored child without using UCT if one exists.
     * @param tree the root of the Monte carlo tree
     * @return the promising node to be explored
     */
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
