package com.example.dotsandboxes.model.classes;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;


public class Board {
    private ModelLine[][] horizontalLines; // (gridSize)*(gridSize-1) matrix containing all the horizontal lines in the game
    private ModelLine[][] verticalLines; // (gridSize)*(gridSize-1) matrix containing all the vertical  lines in the game

    public Board(Board board) {
        this.horizontalLines = board.getHorizontalLines();
        this.verticalLines = board.getVerticalLines();
    }
    public Board(int gridSize) { // full constructor
        this.horizontalLines = new ModelLine[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new ModelLine[gridSize][gridSize-1]; // array of vertical lines
        initializeLines(gridSize);
    }

    private void initializeLines(int gridSize) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new ModelLine(i,j,true,false);
                verticalLines[i][j] = new ModelLine(i,j,false,false);
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


    // getters
    public ModelLine[][] getHorizontalLines() {return horizontalLines;}
    public ModelLine[][] getVerticalLines() {return verticalLines;}
    public int getGridSize() {return horizontalLines.length;}
}
