package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.Board;
import com.example.dotsandboxes.model.classes.Box;
import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.MoveResult;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import javafx.util.Pair;

public class AIBoard {
    private int firstScore; // player 1
    private int secondScore; // player 2
    private PlayerNumber turn; // represents the current player, first for first and second for second
    private int gridSize; // desired board size
    private ModelLine[][] horizontalLines; // (gridSize)*(gridSize-1) matrix containing all the horizontal lines in the game
    private ModelLine[][] verticalLines; // (gridSize)*(gridSize-1) matrix containing all the vertical  lines in the game
    private Box[][] boxes; // (gridSize-1)*(gridSize) matrix containing the boxes in the game

    public AIBoard(int firstScore,int secondScore,PlayerNumber turn,int gridSize,ModelLine[][] horizontalLines,ModelLine[][] verticalLines,Box[][] boxes) {
        this.firstScore = firstScore;
        this.secondScore = secondScore;
        this.turn = turn;
        this.gridSize = gridSize;
        this.boxes = boxes;
        this.horizontalLines = horizontalLines;
        this.verticalLines = verticalLines;
    }
    public AIBoard() {} // empty constructor

    public int checkBoxFormed(ModelLine line) { // checks if a newly connected line completes boxes, and returns the points obtained from it(0-2)
        Pair<Integer,Box[]> parentData = getParentBoxes(line);
        Box[] parents = parentData.getValue();
        int numberOfParents = parentData.getKey().intValue();
        int score;
        switch (numberOfParents) {
            case 1:
                parents[numberOfParents-1].incConnectedLines();
                score = parents[numberOfParents-1].getIsComplete() ? 1 : 0;
                return score;
            case 2:
                parents[numberOfParents-1].incConnectedLines();
                parents[numberOfParents-2].incConnectedLines();
                score = (parents[numberOfParents-1].getIsComplete() ? 1 : 0) + (parents[numberOfParents-2].getIsComplete() ? 1 : 0);
                return score;
            default:
                return 0;
        }
    }

    private Pair<Integer,Box[]> getParentBoxes(ModelLine line) { // returns a Box array containing the boxes a line is a part of, and an Integer containing the length of the array
        Box[] results = new Box[2];
        int resultIndex = 0;
        for (int i = 0; i < gridSize-1; i++) {
            for (int j = 0; j < gridSize-1; j++) {
                if (boxes[i][j].hasLine(line)) {
                    results[resultIndex] = boxes[i][j];
                    resultIndex+=1;
                    if (resultIndex >= 2) {
                        return new Pair<>(resultIndex,results);
                    }
                }
            }
        }
        return new Pair<>(resultIndex,results);
    }
    private boolean isGameOver() { // returns true if the game is in progress, otherwise false
        return !((firstScore + secondScore) == (gridSize-1)*(gridSize-1));}

    private void swapTurn() {
        turn = turn == PlayerNumber.first ? PlayerNumber.second : PlayerNumber.first;
    } // moves to next turn

    public MoveResult performMove(int row, int column, LineType lineType) {
        ModelLine[][] lines = lineType.equals(LineType.horizontal) ? horizontalLines : verticalLines;
        ModelLine line = lines[row][column];
        if (!line.isConnected()) {
            line.connectLine();
            line.setOwner(turn);
            int scoreObtained = checkBoxFormed(line);
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

    public void increaseCurrentScore(int scoreObtained) {
        PlayerNumber current = turn == PlayerNumber.first ? PlayerNumber.first : PlayerNumber.second;
        if (current.equals(PlayerNumber.first)) {
            firstScore += scoreObtained;
        }
        else {
            secondScore += scoreObtained;
        }
    }
    // getters
    public Box[][] getBoxes() {return boxes;}
    public ModelLine[][] getHorizontalLines() {return horizontalLines;}
    public ModelLine[][] getVerticalLines() {return verticalLines;}
    public int getGridSize() {return gridSize;}
}
