package com.example.dotsandboxes.model.classes;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class Board {
    private int gridSize;
    private Circle[][] dots;
    private Line[][] horizontalLines;
    private Line[][] verticalLines;
    public Board() {}
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
    /*
    public boolean canFormALine(Point first, Point second) { // checks if two dots can form a line
        return first.distance(second) == 1;
    }
     */
}
