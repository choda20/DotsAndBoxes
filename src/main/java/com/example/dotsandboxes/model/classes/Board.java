package com.example.dotsandboxes.model.classes;
import javafx.util.Pair;
import java.util.ArrayList;


public class Board {
    private int gridSize; // desired board size
    private ModelLine[][] horizontalLines; // (gridSize)*(gridSize-1) matrix containing all the horizontal lines in the game
    private ModelLine[][] verticalLines; // (gridSize)*(gridSize-1) matrix containing all the vertical  lines in the game
    private Box[][] boxes; // (gridSize-1)*(gridSize) matrix containing the boxes in the game

    public Board() {} // empty constructor
    public Board(Board board) { // constructor to make a board from an existing board
        this.gridSize = board.getGridSize();
        this.horizontalLines = board.getHorizontalLines();
        this.verticalLines = board.getVerticalLines();
        this.boxes = board.getBoxes();
        initializeLines();
        initializeBoxes();
    }
    public Board(int gridSize) { // full constructor
        this.gridSize = gridSize;
        this.horizontalLines = new ModelLine[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new ModelLine[gridSize][gridSize-1]; // array of vertical lines
        this.boxes = new Box[gridSize-1][gridSize-1];
        initializeLines();
        initializeBoxes();
    }

    private void initializeLines() {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new ModelLine(i,j,true,false);
                verticalLines[i][j] = new ModelLine(i,j,false,false);
            }
        }
    }
    private void initializeBoxes() {
        for (int i=0;i<gridSize-1;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                boxes[i][j] = new Box(new ArrayList<ModelLine>());
                boxes[i][j].getLines().add(verticalLines[j][i]);
                boxes[i][j].getLines().add(verticalLines[j+1][i]);
                boxes[i][j].getLines().add(horizontalLines[i][j]);
                boxes[i][j].getLines().add(horizontalLines[i+1][j]);
            }
        }
    }

    public int checkBoxFormed(ModelLine line) { // checks if a newly connected line completes boxes, and returns the points obtained from it(0-2)
        Pair<Integer,Box[]> parentData = getParentBoxes(line);
        Box[] parents = parentData.getValue();
        int numberOfParents = parentData.getKey().intValue();
        switch (numberOfParents) {
            case 1:
                parents[numberOfParents-1].incConnectedLines();
                return parents[numberOfParents-1].getIsComplete() ? 1 : 0;
            case 2:
                parents[numberOfParents-1].incConnectedLines();
                parents[numberOfParents-2].incConnectedLines();
                return (parents[numberOfParents-1].getIsComplete() ? 1 : 0) + (parents[numberOfParents-2].getIsComplete() ? 1 : 0);
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

    // getters
    public Box[][] getBoxes() {return boxes;}
    public ModelLine[][] getHorizontalLines() {return horizontalLines;}
    public ModelLine[][] getVerticalLines() {return verticalLines;}
    public int getGridSize() {return gridSize;}
}
