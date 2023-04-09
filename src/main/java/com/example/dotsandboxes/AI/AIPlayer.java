package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.classes.Player;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import javafx.util.Pair;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class AIPlayer extends Player implements PropertyChangeListener {
    private AIBoard model; // the AI's game board
    private MCTS moveAlgorithm; // the algorithm the AI uses to choose a move

    /**
     * empty constructor that sets Player score to 0, and initializes the game board
     */
    public AIPlayer() {
        this.score = 0;
        this.model = new AIBoard();
    }

    /**
     * Function that activates the algorithm the AI uses to choose a move and returns the chosen move
     * @return a Pair that holds information on the line the move should be made on
     */
    public Pair<Point, LineType> play()  {
        moveAlgorithm = new MCTS(new AIBoard(model),500);
        ModelLine move = moveAlgorithm.MCTSCalc();
        return new Pair<Point,LineType>(new Point(move.getRow(),move.getColumn()),move.isHorizontal() ? LineType.horizontal : LineType.vertical);
    }

    /**
     * An observer function that activates whenever the main model of the game sends a message
     * that an update has occurred in its board
     * @param evt A PropertyChangeEvent object describing the event source
     * and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Pair<ModelLine,PlayerNumber> changedLineAndOwner = (Pair<ModelLine,PlayerNumber>) evt.getOldValue();
        ModelLine changedLine = changedLineAndOwner.getKey();
        model.performMove(changedLine.getRow(),changedLine.getColumn(),changedLine.isHorizontal() ? LineType.horizontal : LineType.vertical);
    }

    // general getters
    public AIBoard getModel() {return model;}
}
