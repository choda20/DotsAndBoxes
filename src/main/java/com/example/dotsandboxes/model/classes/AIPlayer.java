package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.LineType;
import javafx.util.Pair;

import java.awt.*;

public class AIPlayer extends Player{
    public AIPlayer() {
        this.score = 0;
    }
    public Pair<Point, LineType> play(Board board) {
        return new Pair<Point,LineType>(new Point(-1,-1),LineType.horizontal);
    }
}
