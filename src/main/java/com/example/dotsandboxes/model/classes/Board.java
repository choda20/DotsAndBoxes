package com.example.dotsandboxes.model.classes;


import com.example.dotsandboxes.model.enums.LineType;

public class Board {
    private final ModelLine[][] horizontalLines; // (gridSize)*(gridSize-1)
    // matrix containing all the horizontal lines in the game
    private final ModelLine[][] verticalLines; // (gridSize)*(gridSize-1) matrix
    // containing all the vertical  lines in the game

    /**
     * A constructor that gets a gridSize parameter and creates the
     * lines arrays based on it
     * @param gridSize
     */
    public Board(int gridSize) { // constructor that
        this.horizontalLines = new ModelLine[gridSize][gridSize-1];
        // array of horizontal lines
        this.verticalLines = new ModelLine[gridSize][gridSize-1];
        // array of vertical lines
        initializeLines(gridSize);
    }

    /**
     * function that creates all the lines in the game based on the gridSize
     * parameter run time O(n/2) when n = total amount of lines in the game
     * @param gridSize
     */
    private void initializeLines(int gridSize) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new ModelLine(i,j,LineType.horizontal,
                        false);
                verticalLines[i][j] = new ModelLine(i,j,LineType.vertical,
                        false);
            }
        }
    }

    /**
     * function that gets a line as a parameter and checks how many points a
     * player will obtain if they were to connect said line
     * @param line
     * @return the score obtained from forming the line
     */
    public int checkBoxFormed(ModelLine line) {
        int x = line.getRow();
        int y = line.getColumn();
        int columnLength = horizontalLines[0].length;
        int score = 0;
        if (line.getIsHorizontal().equals(LineType.horizontal)) {
            score+=
                    (x>0 && x < getGridSize() && y < columnLength) ?
                            areAllConnected(horizontalLines[x-1][y],
                                    verticalLines[y+1][x-1],
                                    verticalLines[y][x-1]) : 0;
            score += (x < columnLength && y < columnLength) ?
                    areAllConnected(horizontalLines[x+1][y], verticalLines[y][x]
                            ,verticalLines[y+1][x]) : 0;
        }
        else {
            score +=
                    (x > 0 && x < getGridSize() && y < columnLength) ?
                            areAllConnected(verticalLines[x - 1][y],
                                    horizontalLines[y][x - 1],
                                    horizontalLines[y + 1][x - 1]) : 0;
            score += (x < columnLength && y < columnLength) ?
                    areAllConnected(verticalLines[x + 1][y],
                            horizontalLines[y][x],
                            horizontalLines[y + 1][x]) : 0;
        }
        return score;
    }

    /**
     * checks if all lines passed as parameters are connected
     * @param line
     * @param line2
     * @param line3
     * @return 1 if all parameter lines are connected, 0 otherwise
     */
    private int areAllConnected(ModelLine line, ModelLine line2,
                                ModelLine line3){
        int connectedNum = isConnected(line)+ isConnected(line2)+
                isConnected(line3);
        return connectedNum == 3 ? 1: 0;
    }

    /**
     * checks if a line is connected or not
     * @param line
     * @return 1 if the line is connected, 0 otherwise
     */
    private int isConnected(ModelLine line) {
        return line.getIsConnected() ? 1 : 0;
    }

    // general getters
    public ModelLine[][] getHorizontalLines() {return horizontalLines;}
    public ModelLine[][] getVerticalLines() {return verticalLines;}

    /**
     * since horizontalLines = ModelLine[gridSize][gridSize-1] we can get
     * the gridSize from the first dimension length of horizontalLines
     * @return board gridSize
     */
    public int getGridSize() {return horizontalLines.length;}
}
