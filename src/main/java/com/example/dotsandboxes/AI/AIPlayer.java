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
        moveAlgorithm = new MCTS(new AIBoard(model),1000, PlayerNumber.second);
        moveAlgorithm.MCTSCalc();
        System.out.println("ENDED");
        return new Pair<Point,LineType>(new Point(-1,-1),LineType.horizontal);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ModelLine changedLine = (ModelLine) evt.getOldValue();
        MoveResult result = (MoveResult) evt.getNewValue();
        model.performMove(changedLine.getRow(),changedLine.getColumn(),changedLine.isHorizontal() ? LineType.horizontal : LineType.vertical);
    }

    public AIBoard getModel() {return model;}
}
