package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.AI.AIModel;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.PlayerNumber;
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
    public Pair<Point, LineType> play(int p1Score, int p2Score, Board gameBoard){return new Pair<Point,LineType>(new Point(-1,-1),LineType.horizontal);} // function that configures a human players move, will be implemented when AI is added
}
