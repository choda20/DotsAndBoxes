package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.model.enums.PlayerNumber;

public class Game {
    Player first;
    Player second;
    PlayerNumber turn;
    GameType gameType;
    Board gameBoard;

    public Game() {}
    public Game(Player first,Player second,GameType gameType,Board gameBoard) {
        this.gameType = gameType;
        this.first = first;
        this.second = second;
        this.gameBoard = gameBoard;
        this.turn = PlayerNumber.first;
    }

    public PlayerNumber getTurn() {
        return turn;
    }
    public GameType getGameType(){return gameType;}
    public Board getGameBoard() {return gameBoard;}
    public Player getSecond() {return second;}
    public Player getFirst() {return first;}

    public void setGameType(GameType type) {this.gameType = type;}
    public void setGameBoard(Board gameBoard) {this.gameBoard = gameBoard;}
    public void setFirst(Player first) {this.first = first;}
    public void setSecond(Player second) {this.second = second;}

    public void swapTurn() {
        turn = turn == PlayerNumber.first ? PlayerNumber.second : PlayerNumber.first;
    }
    public Player currentPlayer() {
        return turn == PlayerNumber.first ? first : second;
    }
    public void startGame() {}
    public void endGame() {}
    public boolean isGameOver() {return true;}
    public void buildBoard() {} // should be board not void

}
