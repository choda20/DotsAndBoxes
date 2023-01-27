package com.example.dotsandboxes.models.classes;

import com.example.dotsandboxes.models.enums.LineType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Line {
    private Dot start; // dot at the start of the line
    private Dot end; // dot at the end of the line
    private boolean isConnected; // indicates if the line is connected
    private LineType type;

    public Line(Dot start,Dot end) { // constructor
        this.start = start;
        this.end = end;
        this.isConnected = false;
        this.type = start.getX() == end.getX() ? LineType.horizontal : LineType.vertical;
    }

    public LineType getType() {
        return type;
    }
    public Dot getEnd() { // end getter
        return end;
    }
    public Dot getStart() { // start getter
        return start;
    }
    public Boolean getIsConnected() { // isConnected getter
        return isConnected;
    }

    public void disconnectLine() { // disconnects the line
        isConnected = false;
    }
    public void connectLine() { // connects the line
        isConnected = true;
    }

    public boolean canFormABox(Line other) { // checks if two lines can be in a box together
        if (!other.equals(this)) {
            if (type == LineType.horizontal) {
                    return other.getType() == LineType.horizontal && Math.abs(other.getStart().getX() - start.getX()) == 1
                            && other.getStart().getY() == start.getY() || other.getType() == LineType.vertical && shareDot(other);
                }
            else
                return other.getType() == LineType.vertical && Math.abs(other.getStart().getY() - start.getY()) == 1
                        && other.getStart().getX() == start.getX() || other.getType() == LineType.horizontal && shareDot(other);
            }
        return false;
        }

    public boolean shareDot(Line other) {
        Dot[] otherDots = {other.getStart(),other.getEnd()};
        return Arrays.stream(otherDots).anyMatch(dot -> dot.equals(start) || dot.equals(end));
    }
    public List<Box> findBoxes(Box[][] boxes) {
        List<Box> parentBoxes = Arrays.asList(boxes).stream().flatMap(Arrays::stream).
                filter(box -> box.hasLine(this)).collect(Collectors.toList());
        return parentBoxes.size() > 0 && parentBoxes.size() <=2 ? parentBoxes: null;
    }
}
