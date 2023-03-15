package com.example.dotsandboxes.model.classes;

import javafx.scene.shape.Line;

import java.util.ArrayList;

public class Box {
    private ArrayList<Line> lines; // lines forming the box
    private int connectedLines; // number of connected lines
    private boolean  isComplete; // indicates if all the lines are connected

    public Box(ArrayList<Line> lines) { // constructor
        this.lines = lines;
        int connectedLines = 0;
        boolean  isComplete = false;
    }

    // getters
    public ArrayList<Line> getLines() {
        return lines;
    }
    public int getConnectedLines() {
        return connectedLines;
    }
    public boolean getIsComplete() {return isComplete;}

    public void incConnectedLines() { // increases number of connected lines, and sets isComplete to true if the box is complete
        connectedLines+=1;
        if(connectedLines == 4) {
            isComplete = true;
        }
    }
    public boolean hasLine(Line line) {
        return  lines.contains(line);
    } // returns true if a line is in the box, and false otherwise
}
