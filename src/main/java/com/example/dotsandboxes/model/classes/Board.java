package com.example.dotsandboxes.model.classes;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;


public class Board {
    private Box[][] boxes;
    private int gridSize;
    private Circle[][] dots;
    private Line[][] horizontalLines;
    private Line[][] verticalLines;
    public Board() {}
    public Board(Board board) {
        this.gridSize = board.getGridSize();
        this.dots = board.getDots();
        this.horizontalLines = board.getHorizontalLines();
        this.verticalLines = board.getVerticalLines();
        this.boxes = board.getBoxes();
    }
    public Board(int gridSize) {
        this.gridSize = gridSize;
        this.horizontalLines = new Line[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new Line[gridSize][gridSize-1]; // array of vertical lines
        this.dots = new Circle[gridSize][gridSize]; // array of dots
        this.boxes = new Box[gridSize-1][gridSize-1];
    }

    public Box[][] getBoxes() {return boxes;}
    public Circle[][] getDots() {return dots;}
    public Line[][] getHorizontalLines() {return horizontalLines;}
    public Line[][] getVerticalLines() {return verticalLines;}
    public int getGridSize() {return gridSize;}

    public void setDots(Circle[][] dots) {this.dots = dots;}
    public void setHorizontalLines(Line[][] horizontalLines) {this.horizontalLines = horizontalLines;}
    public void setVerticalLines(Line[][] verticalLines) {this.verticalLines = verticalLines;}

    public void setGridSize(int gridSize) {this.gridSize = gridSize;}
    public void initializeLines(double startingX,double startingY, int padding) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new Line(startingX+(j*75) + 7,startingY+(i*75)+padding,startingX+((j+1)*75) - 7,startingY+((i)*75)+padding);
                verticalLines[i][j] = new Line(startingY+(i*75),startingY+(j*75)+ 7 + padding,startingY+(i*75),startingY+((j+1)*75)- 7+ padding);
            }
        }
    }
    public void initializeBoxes() {
        for (int i=0;i<gridSize-1;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                boxes[i][j] = new Box(new ArrayList<Line>());
                boxes[i][j].getLines().add(verticalLines[j][i]);
                boxes[i][j].getLines().add(verticalLines[j+1][i]);
                boxes[i][j].getLines().add(horizontalLines[i][j]);
                boxes[i][j].getLines().add(horizontalLines[i+1][j]);
            }
        }
    }
    public void initializeDots(double startingX,double startingY, int padding) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize; j++) {
                dots[i][j] = new Circle(startingX+(j*75),startingY+(i*75)+padding,5);
            }
        }
    }
    public int checkBoxFormed(Line line) {
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
    public void disableAllLines() {
        for (int i=0;i<gridSize;i++) {
            for(int j=0;j<gridSize-1;j++) {
                horizontalLines[i][j].setOnMouseClicked(event -> {});
                horizontalLines[i][j].setOnMouseEntered(event -> {});
                horizontalLines[i][j].setOnMouseExited(event -> {});
                verticalLines[i][j].setOnMouseClicked(event -> {});
                verticalLines[i][j].setOnMouseEntered(event -> {});
                verticalLines[i][j].setOnMouseExited(event -> {});
            }
        }
    }
    public Pair<Integer,Box[]> getParentBoxes(Line line) {
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
}
