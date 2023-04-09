package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AIBoard {
    private int firstScore; // player 1
    private int secondScore; // player 2
    private int currentPlayer;
    private List<ModelLine> bestLines;
    private List<ModelLine> worstLines;
    private List<ModelLine> okLines;
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

        this.okLines = new ArrayList<>();
        this.worstLines = new ArrayList<>();
        this.bestLines = new ArrayList<>();
        this.horizontalLines = new ModelLine[gridSize][gridSize-1];
        this.verticalLines = new ModelLine[gridSize][gridSize-1];
        for (int i=0;i<gridSize;i++) {
            for(int j=0;j<gridSize-1;j++) {
                this.horizontalLines[i][j] = board.horizontalLines[i][j].copy();
                this.verticalLines[i][j] = board.verticalLines[i][j].copy();
            }
        }
        int max = Integer.max(Integer.max(board.okLines.size(),board.worstLines.size()),board.bestLines.size());
        for (int i=0;i<max;i++) {
            if (i < board.okLines.size())
                okLines.add(board.okLines.get(i));
            if (i < board.worstLines.size())
                worstLines.add(board.worstLines.get(i));
            if (i < board.bestLines.size())
                bestLines.add(board.bestLines.get(i));
        }

    }
    public void setGridSize(int gridSize) {
        this.horizontalLines = new ModelLine[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new ModelLine[gridSize][gridSize-1]; // array of vertical lines
        this.okLines = new ArrayList<ModelLine>();
        this.bestLines = new ArrayList<ModelLine>();
        this.worstLines = new ArrayList<ModelLine>();
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new ModelLine(i,j,true,false);
                verticalLines[i][j] = new ModelLine(i,j,false,false);
                okLines.add(horizontalLines[i][j]);
                okLines.add(verticalLines[i][j]);
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
            adjustLinesBasedOnMove(line);
            if (scoreObtained == 0) {
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
        List<ModelLine> avlLines = getBestMoves();
        for(int i=0;i<avlLines.size();i++) {
            avlMoves.add(new AIBoard(this));
            avlMoves.get(i).performMove(avlLines.get(i).getRow(),avlLines.get(i).getColumn(),avlLines.get(i).isHorizontal() ? LineType.horizontal : LineType.vertical);
        }

        return avlMoves;
    }

    public Pair<Integer,ModelLine> getBestMove() {
        List<ModelLine> avlLines = getBestMoves();
        ModelLine bestMove = avlLines.get(0);
        int bestScore = - 1, score;

        for (ModelLine move: avlLines) {
            score = evaluateMove(move);
            if (score > bestScore) {
                bestMove = move;
                bestScore = score;
            }
        }
        adjustLinesBasedOnMove(bestMove);
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

    public void adjustLinesBasedOnMove(ModelLine line) {
        removeLine(line);
        List<List<ModelLine>> lines = getUnconnectedLines(line);
        for (int i=0;i<lines.size();i++) {
            if (lines.get(i).size() == 2) { // 2 means adding a line will open a box, so worst moves
                worstLines.add(lines.get(i).get(0));
                worstLines.add(lines.get(i).get(1));
            } else if (lines.get(i).size() == 1) { // 1 means adding a line will close a box, so best move
                bestLines.add(lines.get(i).get(0));
            } else if (lines.get(i).size() == 3) { // 3 means that nothing notable will happen, so ok move
                okLines.add(lines.get(i).get(0));
                okLines.add(lines.get(i).get(1));
                okLines.add(lines.get(i).get(2));
            }
        }

    }

    public List<List<ModelLine>> getUnconnectedLines(ModelLine line) {
        List<List<ModelLine>> lines = new ArrayList<List<ModelLine>>();
        int x = line.getRow();
        int y = line.getColumn();
        int columnLength = horizontalLines[0].length;
        if (line.isHorizontal()) {
            if (x>0 && x < getGridSize() && y < columnLength) {
                List<ModelLine> otherLines = new ArrayList<>(Arrays.asList(new ModelLine[]{horizontalLines[x - 1][y], verticalLines[y + 1][x - 1], verticalLines[y][x - 1]}));
                lines.add(otherLines.stream().filter(listLine -> listLine.isConnected() == false).collect(Collectors.toList()));
            }
            if (x < columnLength && y < columnLength) {
                List<ModelLine> otherLines = new ArrayList<>(Arrays.asList(new ModelLine[]{horizontalLines[x+1][y], verticalLines[y][x], verticalLines[y+1][x]}));
                lines.add(otherLines.stream().filter(listLine -> listLine.isConnected() == false).collect(Collectors.toList()));
            }
        }
        else {
            if (x > 0 && x < getGridSize() && y < columnLength) {
                List<ModelLine> otherLines = new ArrayList<>(Arrays.asList(new ModelLine[]{verticalLines[x - 1][y], horizontalLines[y][x - 1], horizontalLines[y + 1][x - 1]}));
                lines.add(otherLines.stream().filter(listLine -> listLine.isConnected() == false).collect(Collectors.toList()));
            }
            if (x < columnLength && y < columnLength) {
                List<ModelLine> otherLines = new ArrayList<>(Arrays.asList(new ModelLine[]{verticalLines[x + 1][y], horizontalLines[y][x], horizontalLines[y + 1][x]}));
                lines.add(otherLines.stream().filter(listLine -> listLine.isConnected() == false).collect(Collectors.toList()));
            }
        }
        for(int i=0;i<lines.size();i++)
            lines.get(i).stream().forEach(listLine -> removeLine(listLine));
        return lines;
    }
    public void removeLine(ModelLine line) {
        okLines.remove(line);
        bestLines.remove(line);
        worstLines.remove(line);
    }
    // getters
    public int getGridSize() {return horizontalLines.length;}
    public ModelLine getLastMove() {return lastMove;}
    public int getCurrentPlayer() {return currentPlayer;}
    public List<ModelLine> getOkLines() {return okLines;}
    public List<ModelLine> getWorstLines() {return worstLines;}
    public List<ModelLine> getBestLines() {return bestLines;}

    public int getScoreDifference() {return secondScore-firstScore;}
    public List<ModelLine> getBestMoves() {
        if (bestLines.isEmpty()) {
            if (okLines.isEmpty())
                return worstLines;
            return okLines;
        }
        return bestLines;
    }
}
