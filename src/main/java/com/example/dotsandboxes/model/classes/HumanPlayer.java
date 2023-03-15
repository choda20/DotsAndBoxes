package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.PlayerNumber;

public class HumanPlayer extends Player{

    public HumanPlayer(String name, PlayerNumber number) { // full constructor
        super();
        this.name = name;
    }
    public HumanPlayer() {
        this.score = 0;
    } // empty constructor

    public Board play(Board board) {
        return board;
    } // function that configures a human players move, will be implemented when AI is added
}
