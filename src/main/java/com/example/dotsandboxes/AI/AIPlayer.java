package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.classes.Player;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class AIPlayer extends Player implements PropertyChangeListener {
    private final AIBoard model; // the AI's game board

    /**
     * empty constructor that sets Player score to 0, and initializes
     * the game board
     */
    public AIPlayer() {
        this.score = 0;
        this.model = new AIBoard();
    }

    /**
     * Function that activates the algorithm the AI uses to choose a move and
     * returns the chosen move
     * function run-time is O(l*(n^3)), as explained in MCTSCalc
     * @return a Pair that holds information on the line the move
     * should be made on
     */
    public ModelLine makeMove()  {
        // the algorithm the AI uses to choose a move
        MCTS moveAlgorithm = new MCTS(new AIBoard(model));
        ModelLine move = moveAlgorithm.MCTSCalc();
        return move;
    }

    /**
     * An observer function that activates whenever the main model of the game
     * sends a message that an update has occurred in its board
     * @param evt A PropertyChangeEvent object describing the event source
     * and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Pair<ModelLine,PlayerNumber> changedLineAndOwner =
                (Pair<ModelLine,PlayerNumber>) evt.getOldValue();
        ModelLine changedLine = changedLineAndOwner.getKey();
        model.performMove(changedLine.getRow(),changedLine.getColumn(),
                changedLine.getIsHorizontal());
    }

    // general getters
    public AIBoard getModel() {return model;}
}
