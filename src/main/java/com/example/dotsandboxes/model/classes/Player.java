package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.LineType;
import javafx.util.Pair;

import java.awt.*;

public abstract class Player {
    protected String name; // the player's name
    protected int score; // the player's score

    public Player() {
        score = 0;
    } // constructor

    // getters
    public String getName() {return name;}
    public int getScore() {return score;}

    //setters
    public void setName(String name) {this.name = name;}
    public void setScore(int score) {this.score = score;}

    public abstract Pair<Point, LineType> play() throws CloneNotSupportedException; // abstract function that configures how the player makes a move
}
