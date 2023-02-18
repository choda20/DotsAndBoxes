package com.example.dotsandboxes.model.classes;

import javafx.scene.shape.Line;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class eLine {
    private Line line; // the line
    private boolean isHorizontal; // true for horizontal, false for vertical

    public eLine(Line line) { // constructor
        this.line = line;
        this.isHorizontal = line.getStartY() == line.getEndY() ? false : true;
    }

    public Line getLine() { // returns the line
        return line;
    }

    public boolean canFormABox(eLine other) {
        if (isHorizontal) {
           return canFormABoxH(other);
        }
        return canFormABoxV(other);
    }
    private boolean canFormABoxV(eLine other) {
        if(!other.equals(this)) {
            return other.isHorizontal == this.isHorizontal && Math.abs(other.line.getStartY() - this.getLine().getStartY()) == 1
                    && other.line.getStartX() == this.getLine().getStartX() || shareDot(other);
        }
        else return false;
    }
    private boolean canFormABoxH(eLine other) {
        if(!other.equals(this)) {
            return other.isHorizontal == this.isHorizontal && Math.abs(other.line.getStartX() - this.getLine().getStartX()) == 1
                    && other.line.getStartY() == this.getLine().getStartY() || shareDot(other);
        }
        else return false;
    }

    public boolean shareDot(eLine other) {
        return line.intersects(other.line.getStartX(),other.line.getStartY(),other.line.getEndX(),other.line.getEndY());
    }
/*
    public Box findBoxes(Box[][] boxes) {
        List<Box> parentBoxes = Arrays.asList(boxes).stream().flatMap(Arrays::stream).
                filter(box -> box.hasLine(this)).collect(Collectors.toList());
        return parentBoxes.size() > 0 && parentBoxes.size() <=2 ? parentBoxes: null;
    }
*/
}

