package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.Board;
import com.example.dotsandboxes.model.enums.PlayerNumber;

import java.time.Instant;

public class MCTS {
    private final Board gameBoard;
    private final int computations;
    private final PlayerNumber player;

    public MCTS(Board gameBoard,int computations,PlayerNumber player) {
        this.computations = computations;
        this.gameBoard = gameBoard;
        this.player = player;
    }

    public Board MCTSCalc() {
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
            if (selected.board.getStatus() == Board.GAME_IN_PROGRESS) {
                selected = expandNodeAndReturnRandom(promisingNode);
            }

            //SIMULATE
            int playoutResult = simulateLightPlayout(selected);

            //PROPAGATE
            backPropagation(playoutResult, selected);
        }

        MCTSNode best = tree.getChildWithMaxScore();

        Instant end = Instant.now();
        long milis = end.toEpochMilli() - start.toEpochMilli();

        System.out.println("Did " + counter + " expansions/simulations within " + milis + " milis");
        System.out.println("Best move scored " + best.score + " and was visited " + best.visits + " times");

        return best.board;
    }

    // if node is already a leaf, return the leaf
    private MCTSNode expandNodeAndReturnRandom(MCTSNode node) {
        MCTSNode result = node;

        Board board = node.board;

        for (Board move : board.getAllLegalNextMoves()) {
            MCTSNode child = new MCTSNode(move);
            child.parent = node;
            node.addChild(child);

            result = child;
        }

        int random = Board.RANDOM_GENERATOR.nextInt(node.children.size());

        return node.children.get(random);
    }

    private void backPropagation(int playerNumber, MCTSNode selected) {
        MCTSNode node = selected;

        while (node != null) { // look for the root
            node.visits++;
            if (node.board.getLatestMovePlayer() == playerNumber) {
                node.score++;
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
    private int simulateLightPlayout(MCTSNode promisingNode) {
        MCTSNode node = new MCTSNode(promisingNode.board);
        node.parent = promisingNode.parent;
        int boardStatus = node.board.getStatus();

        if (boardStatus == opponentId) {
            node.parent.score = Integer.MIN_VALUE;
            return node.board.getStatus();
        }

        while (node.board.getStatus() == Board.GAME_IN_PROGRESS) {
            //game.ConnectFourBoard nextMove = node.board.getWinningMoveOrElseRandom();
            Board nextMove = node.board.getRandomLegalNextMove();

            MCTSNode child = new MCTSNode(nextMove);
            child.parent = node;
            node.addChild(child);

            node = child;
        }

        return node.board.getStatus();
    }

    private MCTSNode selectPromisingNode(MCTSNode tree) {
        MCTSNode node = tree;
        while (node.children.size() != 0) {
            //if (node.children.size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }
}
}
