package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.PlayerNumber;

public abstract class Player {
    protected String name;
    protected int score;
    protected PlayerNumber number;
    public Player() {
        score = 0;
    }
    public abstract Board play(Board gameBoard);
    public String getName() {return name;}
    public int getScore() {return score;}
    public PlayerNumber getNumber() {return number;}
}
