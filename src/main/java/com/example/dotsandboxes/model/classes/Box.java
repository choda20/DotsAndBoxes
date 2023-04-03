package com.example.dotsandboxes.model.classes;

import java.util.ArrayList;

public class Box {
    private ArrayList<ModelLine> lines; // lines forming the box
    private int connectedLines; // number of connected lines
    private boolean  isComplete; // indicates if all the lines are connected

    public Box(ArrayList<ModelLine> lines) { // constructor
        this.lines = lines;
        int connectedLines = 0;
        boolean  isComplete = false;
    }
    public Box(ArrayList<ModelLine> lines,int connectedLines,boolean isComplete) {
        this.lines = new ArrayList<ModelLine>(lines);
        this.connectedLines = connectedLines;
        this.isComplete = isComplete;
    }

    public Box copy() {
        Box newBox = new Box(lines,connectedLines,isComplete);
        return newBox;
    }

    // getters
    public ArrayList<ModelLine> getLines() {
        return lines;
    }
    public int getConnectedLines() {
        return connectedLines;
    }
    public boolean getIsComplete() {return isComplete;}

    public int incConnectedLines() { // increases number of connected lines, and sets isComplete to true if the box is complete
        connectedLines+=1;
        if(connectedLines == 4) {
            isComplete = true;
        }
        return connectedLines;
    }
    public boolean hasLine(ModelLine line) {
        return  lines.contains(line);
    } // returns true if a line is in the box, and false otherwise
}
