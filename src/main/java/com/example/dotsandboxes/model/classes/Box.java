package com.example.dotsandboxes.model.classes;

import javafx.scene.shape.Line;

import java.util.ArrayList;

public class Box {
    private ArrayList<Line> lines;
    private int connectedLines;
    private boolean  isComplete;

    public Box(ArrayList<Line> lines) {
        this.lines = lines;
        int connectedLines = 0;
        boolean  isComplete = false;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }
    public int getConnectedLines() {
        return connectedLines;
    }
    public boolean getIsComplete() {return isComplete;}
    public void incConnectedLines() {
        connectedLines+=1;
        if(connectedLines == 4) {
            isComplete = true;
        }
    }
    public boolean hasLine(Line line) {
        return  lines.contains(line);
    }
}
