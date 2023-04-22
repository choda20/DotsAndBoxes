package com.example.dotsandboxes.AI;

import java.util.Collections;
import java.util.Comparator;

/**
 * UCT (Upper Confidence bounds applied to Trees), an algorithm that deals
 * with the flaw of Monte-Carlo Tree Search, and is a way to decide which
 * node in the tree to expand next during the search process.
 */
public class UCT {
    /**
     * function that is used to access the UCT algorithm.
     * the function activates UCT on all children of the provided node
     * and returns the child with the highest UCT value.
     * @param node the tree node that is to be explored
     * @return child node with highest UCT value
     */
    public static MCTSNode findBestNodeWithUCT(MCTSNode node) {
        int parentVisit = node.getVisits();
        return Collections.max(
                node.getChildren(),
                Comparator.comparing(c -> uctValue(parentVisit,
                        c.getScore(), c.getVisits())));
    }

    /**
     * the UCT algorithm, The UCT algorithm essentially balances between two
     * competing goals: choosing a node that has high win rates (exploitation),
     * and choosing a node that hasn't been explored much yet (exploration).
     * @param totalVisit the amount of times the nodes parent was visited
     * @param nodeWinScore the nodes score
     * @param nodeVisit the amount of times the node was visited
     * @return the UCT value of the node
     */
    private static double uctValue(int totalVisit, double nodeWinScore,
                                   int nodeVisit) {
        if (nodeVisit == 0) {
            return 0;
        }
        return (nodeWinScore / (double) nodeVisit)
                +(1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit));
    }
}
