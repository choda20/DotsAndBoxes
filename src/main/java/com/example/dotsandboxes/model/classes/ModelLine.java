package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.PlayerNumber;

public class ModelLine {
    private int column;
    private int row;
    private boolean isHorizontal;
    private boolean isConnected;
    private PlayerNumber owner;

    public ModelLine(int row, int column, boolean isHorizontal, boolean isConnected) {
        this.column = column;
        this.row = row;
        this.isConnected = isConnected;
        this.isHorizontal = isHorizontal;
    }

    public ModelLine copy() {
        ModelLine newLine = new ModelLine(row,column,isHorizontal,isConnected);
        return newLine;
    }

    // getters
    public int getColumn() {return column;}
    public PlayerNumber getOwner() {return owner;}
    public int getRow() {return row;}
    public boolean isConnected() {return isConnected;}
    public boolean isHorizontal() {return isHorizontal;}

    // setters
    public void setOwner(PlayerNumber owner) {this.owner = owner;}
    public void connectLine() {this.isConnected = true;}
}
