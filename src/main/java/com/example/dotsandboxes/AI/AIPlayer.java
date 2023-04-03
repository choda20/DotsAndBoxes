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
        System.out.println("Board before MTCS: ");
        printBoard();
        moveAlgorithm = new MCTS(new AIBoard(model),500, PlayerNumber.second);
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

    public void printBoard() {
        int j,k;
        for (j=0;j<model.getGridSize();j++) {
            for (k=0;k<model.getGridSize()-1;k++) {
                System.out.print(model.getHorizontalLines()[j][k].isConnected() + " ");
            }
            System.out.println();
            for (k=0;k<model.getGridSize()-1;k++) {
                System.out.print(model.getVerticalLines()[j][k].isConnected() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public AIBoard getModel() {return model;}
}
