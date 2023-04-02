package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.Board;
import com.example.dotsandboxes.model.classes.Player;
import com.example.dotsandboxes.model.enums.LineType;
import javafx.util.Pair;

import java.awt.*;


public class AIPlayer extends Player{
    AIModel model;
    public AIPlayer(String name,int score) {
        this.score = score;
        this.name = name;
        this.model = new AIModel();
    }
    public AIPlayer() {
        this.score = 0;
    }
    public Pair<Point, LineType> play(int p1Score, int p2Score, Board gameBoard) {
        return new Pair<Point,LineType>(new Point(-1,-1),LineType.horizontal);
    }

}
