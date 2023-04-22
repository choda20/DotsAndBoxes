package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.LineType;


public class ModelLine {
    private final int column; // represents the column the line is in inside
    // a line array
    private final int row; // represents the row the line is in
    // inside a line array
    private final LineType lineType; // represent the type of the line
    // (horizontal if true, vertical if false)
    private boolean isConnected; // represents if the line is connected(true if
    // yes, false otherwise)

    /**
     * full constructor
     * @param row
     * @param column
     * @param lineType
     * @param isConnected
     */
    public ModelLine(int row, int column, LineType lineType,
                     boolean isConnected) {
        this.column = column;
        this.row = row;
        this.isConnected = isConnected;
        this.lineType = lineType;
    }

    /**
     * function that creates a copy of the current line(used for so the AI can
     * create copies of the board)
     * @return an identical copy of the current line
     */
    public ModelLine copy() {
        return new ModelLine(row, column, lineType, isConnected);
    }

    //general getters
    public int getColumn() {return column;}
    public int getRow() {return row;}
    public boolean getIsConnected() {return isConnected;}
    public LineType getIsHorizontal() {return lineType;}

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
