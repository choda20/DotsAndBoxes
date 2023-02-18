package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.PlayerNumber;

public class HumanPlayer extends Player{

    public HumanPlayer(String name, PlayerNumber number) {
        super();
        this.name = name;
    }
    public HumanPlayer() {
        this.score = 0;
    }

    public Board play(Board board) {
        return board;
    }
}
