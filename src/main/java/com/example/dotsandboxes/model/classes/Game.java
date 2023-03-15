package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Pair;

public class Game {
    Player first; // player 1
    Player second; // player 2
    PlayerNumber turn; // represents the current player, first for first and second for second
    GameType gameType; // represents the type of the game(HVH,HVA,AVA)
    Board gameBoard; // the board

    public Game() {
        this.turn = PlayerNumber.first;
    } // empty constructor

    public Game(Player first, Player second, GameType gameType, Board gameBoard) { // full constructor
        this.gameType = gameType;
        this.first = first;
        this.second = second;
        this.gameBoard = gameBoard;
        this.turn = PlayerNumber.first;
    }

    // getters
    public PlayerNumber getTurn() {
        return turn;
    }
    public GameType getGameType() {return gameType;}
    public Board getGameBoard() {return gameBoard;}
    public Player getSecond() {return second;}
    public Player getFirst() {return first;}
    public Player getCurrent() {return turn == PlayerNumber.first ? first: second;}

    // setters
    public void setGameType(GameType type) {this.gameType = type;}
    public void setGameBoard(Board gameBoard) {this.gameBoard = gameBoard;}
    public void setFirst(Player first) {this.first = first;}
    public void setSecond(Player second) {this.second = second;}

    public boolean gameStatus() { // returns true if the game is in progress, otherwise false
        return !((first.getScore() + second.getScore()) == (gameBoard.getGridSize()-1)*(gameBoard.getGridSize()-1));}
    public void swapTurn() {
        turn = turn == PlayerNumber.first ? PlayerNumber.second : PlayerNumber.first;
    } // moves to next turn
    public boolean checkValidMove(Line line) { // returns true if a move made by a player is valid, false otherwise
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
            gameOver();
            return true;
        } else {
            return false;
        }
    }
    public void gameOver() { // disables all ui elements that can be clicked if the game has ended
        if (!gameStatus()) {
            gameBoard.disableAllLines();
        }
    }

    public Pair<Integer,String> getWinner() { // returns 0 if somebody won, 1 if a tie occurred. if a tie occurs a blank name is returned, otherwise the winners name is returned
        if (first.getScore() == second.getScore()) {
            return new Pair<>(1,"");
        }
        else if (first.getScore() > second.getScore()) {
            return new Pair<>(0,first.getName());
        }
        return new Pair<>(0,second.getName());
    }
    public void buildBoard(double sceneX,double sceneY) { // initializes the game board
        gameBoard.initializeLines(sceneX,sceneY,20);
        gameBoard.initializeDots(sceneX,sceneY,20);
        gameBoard.initializeBoxes();
    }

}
