package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Game {
    Player first;
    Player second;
    PlayerNumber turn;
    GameType gameType;
    Board gameBoard;

    public Game() {
        this.turn = PlayerNumber.first;
    }

    public Game(Player first, Player second, GameType gameType, Board gameBoard) {
        this.gameType = gameType;
        this.first = first;
        this.second = second;
        this.gameBoard = gameBoard;
        this.turn = PlayerNumber.first;
    }

    public PlayerNumber getTurn() {
        return turn;
    }

    public GameType getGameType() {
        return gameType;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public Player getSecond() {
        return second;
    }

    public Player getFirst() {
        return first;
    }

    public void setGameType(GameType type) {
        this.gameType = type;
    }

    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void setFirst(Player first) {
        this.first = first;
    }

    public void setSecond(Player second) {
        this.second = second;
    }

    public void swapTurn() {
        turn = turn == PlayerNumber.first ? PlayerNumber.second : PlayerNumber.first;
    }

    public Player currentPlayer() {
        return turn == PlayerNumber.first ? first : second;
    }

    public boolean checkValidMove(Line line) {
        if (line.getStroke() == Color.YELLOW || line.getStroke() == Color.TRANSPARENT) {
            line.setStroke(Color.RED);
            if (gameBoard.checkBoxFormed(line))
                currentPlayer().incScore();
                System.out.println("result == true, score: " + currentPlayer().getScore() + ",name: " + currentPlayer().getName());
            swapTurn();
            return true;
        } else {
            return false;
        }
    }

    public void isGameOver() {
        if (first.getScore() + second.getScore() == Math.sqrt(gameBoard.getGridSize()-1))
            gameBoard.disableAllLines();
    }
    public void buildBoard(double sceneX,double sceneY) {
        gameBoard.initializeLines(sceneX,sceneY,20);
        gameBoard.initializeDots(sceneX,sceneY,20);
    }

}
