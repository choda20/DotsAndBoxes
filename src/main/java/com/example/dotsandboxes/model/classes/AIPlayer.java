package com.example.dotsandboxes.model.classes;

public class AIPlayer extends Player{
    public AIPlayer() {
        this.score = 0;
    }
    public Board play(Board board) {
        return board;
    }
}
