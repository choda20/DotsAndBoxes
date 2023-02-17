package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.model.enums.PlayerNumber;

public class Game {
    Player first;
    Player second;
    PlayerNumber turn;
    GameType gameType;
    Board gameBoard;
    public Game() {
    }

    public PlayerNumber getTurn() {
        return turn;
    }
    public void swapTurn() {}
    public void startGame() {}
    public void endGame() {}
    public boolean isGameOver() {return true;}
    public void buildBoard() {} // should be board not void

}
