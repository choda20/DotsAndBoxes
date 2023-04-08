package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIBoard {
    private int firstScore; // player 1
    private int secondScore; // player 2
    private int currentPlayer;
    private List<ModelLine> avlLines;
    private ModelLine[][] horizontalLines; // (gridSize)*(gridSize-1) matrix containing all the horizontal lines in the game
    private ModelLine[][] verticalLines; // (gridSize)*(gridSize-1) matrix containing all the vertical  lines in the game
    private Random generator;
    private ModelLine lastMove;

    public AIBoard() {
        this.firstScore = 0;
        this.secondScore = 0;
        this.currentPlayer = 0;
        this.generator = new Random();
    } // empty constructor
    public AIBoard(AIBoard board) {
        int gridSize = board.getGridSize();
        this.firstScore = board.firstScore;
        this.secondScore = board.secondScore;
        this.currentPlayer = board.currentPlayer;
        this.generator = new Random();

        this.avlLines = new ArrayList<>();
        this.horizontalLines = new ModelLine[gridSize][gridSize-1];
        this.verticalLines = new ModelLine[gridSize][gridSize-1];
        for (int i=0;i<gridSize;i++) {
            for(int j=0;j<gridSize-1;j++) {
                this.horizontalLines[i][j] = board.horizontalLines[i][j].copy();
                this.verticalLines[i][j] = board.verticalLines[i][j].copy();
                if (!horizontalLines[i][j].isConnected())
                    avlLines.add(horizontalLines[i][j]);
                if (!verticalLines[i][j].isConnected())
                    avlLines.add(verticalLines[i][j]);
            }
        }

    }
    public void setGridSize(int gridSize) {
        this.horizontalLines = new ModelLine[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new ModelLine[gridSize][gridSize-1]; // array of vertical lines
        this.avlLines = new ArrayList<ModelLine>();
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new ModelLine(i,j,true,false);
                verticalLines[i][j] = new ModelLine(i,j,false,false);
                avlLines.add(horizontalLines[i][j]);
                avlLines.add(verticalLines[i][j]);
            }
        }
    }

    public int checkBoxFormed(ModelLine line) { // checks if a newly connected line completes boxes, and returns the points obtained from it(0-2)
        int x = line.getRow();
        int y = line.getColumn();
        int columnLength = horizontalLines[0].length;
        int score = 0;
        if (line.isHorizontal()) {
            if (x>0 && x < getGridSize() && y < columnLength && horizontalLines[x-1][y].isConnected() && verticalLines[y][x-1].isConnected() && verticalLines[y+1][x-1].isConnected())
                score += 1;
            if (x < columnLength && y < columnLength && horizontalLines[x+1][y].isConnected() && verticalLines[y][x].isConnected() && verticalLines[y+1][x].isConnected())
                score += 1;
        }
        else {
            if (x > 0 && x < getGridSize() && y < columnLength && verticalLines[x-1][y].isConnected() && horizontalLines[y][x-1].isConnected() && horizontalLines[y+1][x-1].isConnected())
                score += 1;
            if(x < columnLength && y < columnLength && verticalLines[x+1][y].isConnected() && horizontalLines[y][x].isConnected() && horizontalLines[y+1][x].isConnected())
                score += 1;
        }
        return score;
    }

    public boolean isGameOngoing() { // returns true if the game is in progress, otherwise false
        return !((firstScore + secondScore) == ((getGridSize()-1)*(getGridSize()-1)));
    }

    public void performMove(int row, int column, LineType lineType) {
        ModelLine[][] lines = lineType.equals(LineType.horizontal) ? horizontalLines : verticalLines;
        ModelLine line = lines[row][column];
        if (!line.isConnected()) {
            line.connectLine();
            int scoreObtained = checkBoxFormed(line);
            increaseCurrentScore(scoreObtained);
            avlLines.remove(line);
            if (scoreObtained == 0 && !isGameOngoing()) {
                currentPlayer = 1 - currentPlayer;
            }
            this.lastMove = line;
        }
    }


    public void increaseCurrentScore(int scoreObtained) {
        if (currentPlayer == 0) {
            firstScore += scoreObtained;
        }
        else {
            secondScore += scoreObtained;
        }
    }
    public List<AIBoard> getAvlNextMoves() {
        List<AIBoard> avlMoves = new ArrayList<>();
        for(int i=0;i<avlLines.size();i++) {
            avlMoves.add(new AIBoard(this));
            avlMoves.get(i).performMove(avlLines.get(i).getRow(),avlLines.get(i).getColumn(),avlLines.get(i).isHorizontal() ? LineType.horizontal : LineType.vertical);
        }
        return avlMoves;
    }
    public AIBoard getRandomMove() {
        List<AIBoard> nextMoves = getAvlNextMoves();
        if (nextMoves.isEmpty())
            return null;
        final int randomMove = generator.nextInt(nextMoves.size());
        return nextMoves.get(randomMove);
    }
    public Pair<Integer,ModelLine> getBestMove() {
        ModelLine bestMove = avlLines.get(0);
        int bestScore = - 1, score;

        for (ModelLine move: avlLines) {
            score = evaluateMove(move);
            if (score > bestScore) {
                bestMove = move;
                bestScore = score;
            }
        }
        avlLines.remove(bestMove);
        return new Pair<Integer, ModelLine>(bestScore,bestMove);
    }
    private int evaluateMove(ModelLine move) {
        move.connectLine();
        int score = checkBoxFormed(move);
        int[] leftBoxes = checkLeftBoxes(move);
        score -= ((leftBoxes[0] == 2 ? 1 : 0) + (leftBoxes[1] == 2 ? 1 : 0));
        move.disconnectLine();
        return score;
    }

    public int[] checkLeftBoxes(ModelLine line) {
        int x = line.getRow();
        int y = line.getColumn();
        int columnLength = horizontalLines[0].length;
        int[] connectedLines = new int[2];
        if (line.isHorizontal()) {
            connectedLines[0] = (x>0 && x < getGridSize() && y < columnLength) ? ((horizontalLines[x-1][y].isConnected() ? 1 : 0) + (verticalLines[y+1][x-1].isConnected() ? 1 : 0) + (verticalLines[y][x-1].isConnected() ? 1 : 0)): 0;
            connectedLines[1] = (x < columnLength && y < columnLength) ? (horizontalLines[x+1][y].isConnected() ? 1 : 0) + (verticalLines[y][x].isConnected() ? 1 : 0) + (verticalLines[y+1][x].isConnected() ? 1 : 0): 0;
        }
        else {
            connectedLines[0] = (x > 0 && x < getGridSize() && y < columnLength) ? ((verticalLines[x - 1][y].isConnected() ? 1 : 0) + (horizontalLines[y][x - 1].isConnected() ? 1 : 0) + (horizontalLines[y + 1][x - 1].isConnected() ? 1 : 0)) : 0;
            connectedLines[1] = (x < columnLength && y < columnLength) ? (verticalLines[x + 1][y].isConnected() ? 1 : 0) + (horizontalLines[y][x].isConnected() ? 1 : 0) + (horizontalLines[y + 1][x].isConnected() ? 1 : 0) : 0;
        }
        return connectedLines;
    }

    public Pair<Boolean,int[]> leavesBoxOpen() {
        int[] lastMoveBoxes = checkLeftBoxes(lastMove);
        if (lastMoveBoxes[0]++ == 2 || lastMoveBoxes[1]++ == 2)
            return new Pair<>(true,lastMoveBoxes);
        return new Pair<>(false,lastMoveBoxes);
    }
    // getters

    public int getGridSize() {return horizontalLines.length;}
    public ModelLine getLastMove() {return lastMove;}
    public int getCurrentPlayer() {return currentPlayer;}
}
