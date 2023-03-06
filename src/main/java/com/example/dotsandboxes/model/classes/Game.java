package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Pair;

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
    public GameType getGameType() {return gameType;}
    public Board getGameBoard() {return gameBoard;}
    public Player getSecond() {return second;}
    public Player getFirst() {return first;}

    public Player getCurrent() {return turn == PlayerNumber.first ? first: second;}
    public boolean gameStatus() {return !((first.getScore() + second.getScore()) == (gameBoard.getGridSize()-1)*(gameBoard.getGridSize()-1));} // true for ongoing, false for ended

    public void setGameType(GameType type) {this.gameType = type;}
    public void setGameBoard(Board gameBoard) {this.gameBoard = gameBoard;}
    public void setFirst(Player first) {this.first = first;}
    public void setSecond(Player second) {this.second = second;}

    public void swapTurn() {
        turn = turn == PlayerNumber.first ? PlayerNumber.second : PlayerNumber.first;
    }
    public boolean checkValidMove(Line line) {
        if (line.getStroke() == Color.YELLOW || line.getStroke() == Color.TRANSPARENT) {
            line.setStroke(Color.RED);
            int scoreObtained = gameBoard.checkBoxFormed(line);
            if (turn == PlayerNumber.first)
                first.setScore(first.getScore() + scoreObtained);
            else
                second.setScore(second.getScore() + scoreObtained);
            if (scoreObtained == 0) {
                swapTurn();
            }
            isGameOver();
            return true;
        } else {
            return false;
        }
    }
    public boolean isGameOver() {
        if (!gameStatus()) {
            gameBoard.disableAllLines();
            return true;
        }
        return false;
    }
    public Pair<Integer,String> getWinner() { // 0 means somebody won, 1 means a tie
        if (first.getScore() == second.getScore()) {
            return new Pair<>(1,"");
        }
        else if (first.getScore() > second.getScore()) {
            return new Pair<>(0,first.getName());
        }
        return new Pair<>(0,second.getName());
    }
    public void buildBoard(double sceneX,double sceneY) {
        gameBoard.initializeLines(sceneX,sceneY,20);
        gameBoard.initializeDots(sceneX,sceneY,20);
        gameBoard.initializeBoxes();
    }

}
