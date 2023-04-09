package com.example.dotsandboxes.model.classes;



public class Board {
    private ModelLine[][] horizontalLines; // (gridSize)*(gridSize-1) matrix containing all the horizontal lines in the game
    private ModelLine[][] verticalLines; // (gridSize)*(gridSize-1) matrix containing all the vertical  lines in the game

    /**
     * A constructor that gets a gridSize parameter and creates the lines arrays based on it
     * @param gridSize
     */
    public Board(int gridSize) { // constructor that
        this.horizontalLines = new ModelLine[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new ModelLine[gridSize][gridSize-1]; // array of vertical lines
        initializeLines(gridSize);
    }

    /**
     * function that creates all the lines in the game based on the gridSize parameter
     * run time O(n/2) when n = total amount of lines in the game
     * @param gridSize
     */
    private void initializeLines(int gridSize) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new ModelLine(i,j,true,false);
                verticalLines[i][j] = new ModelLine(i,j,false,false);
            }
        }
    }

    /**
     * function that gets a line as a parameter and checks how many points a player
     * will obtain if they were to connect said line
     * @param line
     * @return the score obtained from forming the line
     */
    public int checkBoxFormed(ModelLine line) {
        int x = line.getRow();
        int y = line.getColumn();
        int columnLength = horizontalLines[0].length;
        int score = 0;
        if (line.getIsHorizontal()) {
            if (x>0 && x < getGridSize() && y < columnLength && horizontalLines[x-1][y].getIsConnected() && verticalLines[y][x-1].getIsConnected() && verticalLines[y+1][x-1].getIsConnected())
                score += 1;
            if (x < columnLength && y < columnLength && horizontalLines[x+1][y].getIsConnected() && verticalLines[y][x].getIsConnected() && verticalLines[y+1][x].getIsConnected())
                score += 1;
        }
        else {
            if (x > 0 && x < getGridSize() && y < columnLength && verticalLines[x-1][y].getIsConnected() && horizontalLines[y][x-1].getIsConnected() && horizontalLines[y+1][x-1].getIsConnected())
                score += 1;
            if(x < columnLength && y < columnLength && verticalLines[x+1][y].getIsConnected() && horizontalLines[y][x].getIsConnected() && horizontalLines[y+1][x].getIsConnected())
                score += 1;
        }
        return score;
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
