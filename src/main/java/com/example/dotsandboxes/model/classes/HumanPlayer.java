package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.LineType;
import javafx.util.Pair;

import java.awt.*;

public class HumanPlayer extends Player{

    public HumanPlayer(String name, int score) { // full constructor
        this.score = score;
        this.name = name;
    }
    public HumanPlayer() {
        this.score = 0;
    } // empty constructor
    public Pair<Point, LineType> play(){return new Pair<Point,LineType>(new Point(-1,-1),LineType.horizontal);}
}
