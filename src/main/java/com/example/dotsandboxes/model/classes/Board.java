package com.example.dotsandboxes.model.classes;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Pair;

import java.awt.*;


public class Board {
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
    }
    public Board(int gridSize) {
        this.gridSize = gridSize;
        this.horizontalLines = new Line[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new Line[gridSize][gridSize-1]; // array of vertical lines
        this.dots = new Circle[gridSize][gridSize]; // array of dots
    }

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
    public void initializeDots(double startingX,double startingY, int padding) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize; j++) {
                dots[i][j] = new Circle(startingX+(j*75),startingY+(i*75)+padding,5);
            }
        }
    }
    public boolean checkBoxFormed(Line line) {
        Pair<Integer,Point> lineData = getLineIndexes(line);
        boolean completed = false;
        boolean firstBox = false;
        boolean secondBox = false;
        int i = lineData.getValue().x;
        int j = lineData.getValue().y;
        switch (lineData.getKey()) {
            case 0:
                if (i > 0 && i < gridSize-1 && j > 0) {
                    firstBox = !horizontalLines[i - 1][j].getStroke().equals(Color.TRANSPARENT)
                            && !verticalLines[i][j - 1].getStroke().equals(Color.TRANSPARENT) &&
                            !verticalLines[i + 1][j - 1].getStroke().equals(Color.TRANSPARENT);
                }
                if (i<gridSize-1) {
                    secondBox = !horizontalLines[i + 1][j].getStroke().equals(Color.TRANSPARENT)
                            && !verticalLines[i][j].getStroke().equals(Color.TRANSPARENT) &&
                            !verticalLines[i + 1][j].getStroke().equals(Color.TRANSPARENT);
                }
                completed = firstBox || secondBox;
                System.out.println("first: " + firstBox + ", second: " + secondBox + ",combined: " + completed);
                return completed;
            case 1:
                if (i > 0 && i < gridSize-1 && j > 0) {
                    firstBox = !verticalLines[i - 1][j].getStroke().equals(Color.TRANSPARENT)
                            && !horizontalLines[i][j - 1].getStroke().equals(Color.TRANSPARENT) &&
                            !horizontalLines[i + 1][j - 1].getStroke().equals(Color.TRANSPARENT);
                }
                if (i<gridSize-1) {
                    secondBox = !verticalLines[i + 1][j].getStroke().equals(Color.TRANSPARENT)
                            && !horizontalLines[i][j].getStroke().equals(Color.TRANSPARENT) &&
                            !horizontalLines[i + 1][j].getStroke().equals(Color.TRANSPARENT);
                }
                completed = firstBox || secondBox;
                System.out.println("first: " + firstBox + ", second: " + secondBox + ",combined: " + completed);
                return completed;
            default:
                return false;
        }
    }

    public Pair<Integer,Point> getLineIndexes(Line line) { // finds the index and type of a line, 0 - horizontal, 1 - vertical, -1 for not found(should not be possible)
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                if (horizontalLines[i][j].equals(line))
                    return new Pair<>(0,new Point(i,j));
                if (verticalLines[i][j].equals(line))
                    return new Pair<>(1,new Point(i,j));
            }
        }
        return new Pair<>(-1,new Point(-1,-1));
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
}
