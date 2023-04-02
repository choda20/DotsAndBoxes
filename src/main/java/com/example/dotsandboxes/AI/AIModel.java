package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.Board;
import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.classes.Player;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.MoveResult;
import com.example.dotsandboxes.model.enums.PlayerNumber;


public class AIModel {
    int firstScore; // player 1
    int secondScore; // player 2
    PlayerNumber turn; // represents the current player, first for first and second for second
    Board gameBoard; // the board

    public AIModel(int firstScore, int secondScore, Board gameBoard) { // full constructor
        this.firstScore = firstScore;
        this.secondScore = secondScore;
        this.gameBoard = gameBoard;
        this.turn = PlayerNumber.first;
    }
    public AIModel() {}
    private boolean isGameOver() { // returns true if the game is in progress, otherwise false
        return !((firstScore + secondScore) == (gameBoard.getGridSize()-1)*(gameBoard.getGridSize()-1));}

    private void swapTurn() {
        turn = turn == PlayerNumber.first ? PlayerNumber.second : PlayerNumber.first;
    } // moves to next turn

    public MoveResult performMove(int row, int column, LineType lineType) {
        ModelLine[][] lines = lineType.equals(LineType.horizontal) ? gameBoard.getHorizontalLines() : gameBoard.getVerticalLines();
        ModelLine line = lines[row][column];
        if (!line.isConnected()) {
            line.connectLine();
            line.setOwner(turn);
            int scoreObtained = gameBoard.checkBoxFormed(line);
            increaseCurrentScore(scoreObtained);
            if (scoreObtained == 0) {swapTurn();}
            MoveResult result = !isGameOver() ? MoveResult.gameOver : MoveResult.valid;
            return result;
        }
        return MoveResult.invalid;
    }

    public int getWinner() { // returns 0 if tie, 1 if player 1 won, 2 if player 2 won. i
        if (firstScore == secondScore) {
            return 0;
        }
        else if (firstScore > secondScore) {
            return 1;
        }
        return 2;
    }


    // getters
    public PlayerNumber getTurn() {return turn;}
    public Board getGameBoard() {return gameBoard;}
    public int getSecondScore() {return secondScore;}
    public int getFirstScore() {return firstScore;}
    public void increaseCurrentScore(int scoreObtained) {
        PlayerNumber current = turn == PlayerNumber.first ? PlayerNumber.first : PlayerNumber.second;
        if (current.equals(PlayerNumber.first)) {
            firstScore += scoreObtained;
        }
        else {
            secondScore += scoreObtained;
        }
    }

    // setters
    public void setGameBoard(Board gameBoard) {this.gameBoard = gameBoard;}

}
