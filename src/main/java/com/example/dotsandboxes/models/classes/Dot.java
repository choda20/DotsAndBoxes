package com.example.dotsandboxes.models.classes;

public class Dot {
    private int x; // horizontal location
    private int y; // vertical location

    public Dot(int x,int y) { // constructor
        this.x = x;
        this.y = y;
    }

    public int getX() { // x getter
        return x;
    }
    public int getY() { // y getter
        return y;
    }

    boolean canFormALine(Dot other) { // checks if two dots can form a line
        return other.x == x + 1 && other.y == y || other.x == x && other.y == y + 1 ||
                other.x == x - 1 && other.y == y || other.x == x && other.y == y - 1;
    }
}
