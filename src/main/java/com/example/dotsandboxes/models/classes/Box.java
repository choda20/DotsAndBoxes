package com.example.dotsandboxes.models.classes;

import com.example.dotsandboxes.models.enums.LineType;
import com.example.dotsandboxes.models.enums.PlayerNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Box {
    ArrayList<Line> lines;
    int connectedLines;
    boolean  isComplete;
    PlayerNumber owner;

    public Box(ArrayList<Line> lines) {
        this.lines = lines;
        int connectedLines = 0;
        boolean  isComplete = false;
        PlayerNumber owner = PlayerNumber.none;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }
    public int getConnectedLines() {
        return connectedLines;
    }
    public PlayerNumber getOwner() {
        return owner;
    }

    public int connectLine(Line line, PlayerNumber owner) {
        if (hasLine(line)) {
            connectedLines += 1;
            int index = lines.indexOf(line);
            Line lineToChange = lines.get(index);
            lineToChange.connectLine();
            if (connectedLines == 4) {
                isComplete = true;
            }
            return connectedLines;
        }
        return -1; // line does not exist
    }
    public boolean hasLine(Line line) {
        return  lines.indexOf(line) != -1 ? true: false;
    }
}
