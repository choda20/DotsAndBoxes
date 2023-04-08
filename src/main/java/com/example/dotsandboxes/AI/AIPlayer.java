package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.classes.Player;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.MoveResult;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import javafx.util.Pair;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class AIPlayer extends Player implements PropertyChangeListener {
    AIBoard model;
    MCTS moveAlgorithm;
    public AIPlayer(String name,int score) {
        this.score = score;
        this.name = name;
        this.model = new AIBoard();
    }
    public AIPlayer() {
        this.score = 0;
        this.model = new AIBoard();
    }
    public Pair<Point, LineType> play()  {
        moveAlgorithm = new MCTS(new AIBoard(model),2500);
        ModelLine move = moveAlgorithm.MCTSCalc();
        return new Pair<Point,LineType>(new Point(move.getRow(),move.getColumn()),move.isHorizontal() ? LineType.horizontal : LineType.vertical);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Pair<ModelLine,PlayerNumber> changedLineAndOwner = (Pair<ModelLine,PlayerNumber>) evt.getOldValue();
        ModelLine changedLine = changedLineAndOwner.getKey();
        MoveResult result = (MoveResult) evt.getNewValue();
        model.performMove(changedLine.getRow(),changedLine.getColumn(),changedLine.isHorizontal() ? LineType.horizontal : LineType.vertical);
    }

    public AIBoard getModel() {return model;}
}
