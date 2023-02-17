package com.example.dotsandboxes.model.classes;
import java.awt.Point;

public class Board {
    
    public boolean canFormALine(Point first, Point second) { // checks if two dots can form a line
        return first.distance(second) == 1;
    }
}
