package com.example.dotsandboxes.model.classes;

public class ModelLine {
    private int column; // represents the column the line is in inside a line array
    private int row; // represents the row the line is in inside a line array
    private boolean isHorizontal; // represent the type of the line(horizontal if true, vertical if false)
    private boolean isConnected; // represents if the line is connected(true if yes, false otherwise)

    /**
     * full constructor
     * @param row
     * @param column
     * @param isHorizontal
     * @param isConnected
     */
    public ModelLine(int row, int column, boolean isHorizontal, boolean isConnected) {
        this.column = column;
        this.row = row;
        this.isConnected = isConnected;
        this.isHorizontal = isHorizontal;
    }

    /**
     * function that creates a copy of the current line(used for so the AI can
     * create copies of the board)
     * @return an identical copy of the current line
     */
    public ModelLine copy() {
        ModelLine newLine = new ModelLine(row, column, isHorizontal, isConnected);
        return newLine;
    }

    //general getters
    public int getColumn() {return column;}
    public int getRow() {return row;}
    public boolean getIsConnected() {return isConnected;}
    public boolean getIsHorizontal() {return isHorizontal;}

    /**
     * function that sets isConnected to true(connects the line)
     */
    public void connectLine() {
        this.isConnected = true;
    }

    /**
     * function that sets isConnected to false(disconnects the line)
     */
    public void disconnectLine() {
        this.isConnected = false;
    }
}
