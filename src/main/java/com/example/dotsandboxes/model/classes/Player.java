package com.example.dotsandboxes.model.classes;

public abstract class Player {
    protected String name;
    protected int score;
    public Player() {
        score = 0;
    }

    public String getName() {return name;}
    public int getScore() {return score;}

    public void setName(String name) {this.name = name;}
    public void setScore(int score) {this.score = score;}
    public abstract Board play(Board gameBoard);

}
