package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.PlayerNumber;

import java.util.ArrayList;

public class Box {
    private ArrayList<eLine> lines;
    private int connectedLines;
    private boolean  isComplete;

    public Box(ArrayList<eLine> lines) {
        this.lines = lines;
        int connectedLines = 0;
        boolean  isComplete = false;
    }

    public ArrayList<eLine> getLines() {
        return lines;
    }
    public int getConnectedLines() {
        return connectedLines;
    }
    public boolean getIsComplete() {return isComplete;}

    public int connectLine(eLine line, PlayerNumber owner) {
        if (hasLine(line)) {
            connectedLines += 1;
            int index = lines.indexOf(line);
            eLine lineToChange = lines.get(index);
            if (connectedLines == 4) {
                isComplete = true;
            }
            return connectedLines;
        }
        return -1; // line does not exist
    }
    public boolean hasLine(eLine line) {
        return  lines.contains(line);
    }
}
