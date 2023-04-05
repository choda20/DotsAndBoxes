package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.Box;
import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.PlayerNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIBoard {
    private int firstScore; // player 1
    private int secondScore; // player 2
    private PlayerNumber turn; // represents the current player, first for first and second for second
    private AIGameStatus gameStatus;
    private List<ModelLine> avlLines;
    private ModelLine[][] horizontalLines; // (gridSize)*(gridSize-1) matrix containing all the horizontal lines in the game
    private ModelLine[][] verticalLines; // (gridSize)*(gridSize-1) matrix containing all the vertical  lines in the game
    private Random generator;
    private ModelLine lastMove;

    public AIBoard() {
        this.turn = PlayerNumber.first;
        this.firstScore = 0;
        this.secondScore = 0;
        this.gameStatus = AIGameStatus.GameInProgress;
        this.generator = new Random();
    } // empty constructor
    public AIBoard(AIBoard board) {
        int gridSize = board.getGridSize();
        this.firstScore = board.getFirstScore();
        this.secondScore = board.getSecondScore();
        this.turn = board.getTurn();
        this.gameStatus = board.getGameStatus();
        this.generator = new Random();

        this.avlLines = new ArrayList<>();
        this.horizontalLines = new ModelLine[gridSize][gridSize-1];
        this.verticalLines = new ModelLine[gridSize][gridSize-1];
        for (int i=0;i<gridSize;i++) {
            for(int j=0;j<gridSize-1;j++) {
                this.horizontalLines[i][j] = board.getHorizontalLines()[i][j].copy();
                this.verticalLines[i][j] = board.getVerticalLines()[i][j].copy();
                if (!horizontalLines[i][j].isConnected())
                    avlLines.add(horizontalLines[i][j]);
                if (!verticalLines[i][j].isConnected())
                    avlLines.add(verticalLines[i][j]);
            }
        }

    }
    public void setGridSize(int gridSize) {
        this.horizontalLines = new ModelLine[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new ModelLine[gridSize][gridSize-1]; // array of vertical lines
        this.avlLines = new ArrayList<ModelLine>();
        initializeLines(gridSize);
    }
    private void initializeLines(int gridSize) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new ModelLine(i,j,true,false);
                verticalLines[i][j] = new ModelLine(i,j,false,false);
                avlLines.add(horizontalLines[i][j]);
                avlLines.add(verticalLines[i][j]);
            }
        }
    }

    private int checkBoxFormed(ModelLine line) { // checks if a newly connected line completes boxes, and returns the points obtained from it(0-2)
        int x = line.getRow();
        int y = line.getColumn();
        int columnLength = horizontalLines[0].length;
        int score = 0;
        if (line.isHorizontal()) {
            if (x>0 && x < columnLength && y < columnLength && horizontalLines[x-1][y].isConnected() && verticalLines[y][x-1].isConnected() && verticalLines[y+1][x-1].isConnected())
                score += 1;
            if (x < columnLength && y < columnLength && horizontalLines[x+1][y].isConnected() && verticalLines[y][x].isConnected() && verticalLines[y+1][x].isConnected())
                score += 1;
        }
        else {
            if (x > 0 && x < columnLength && y < columnLength && verticalLines[x-1][y].isConnected() && horizontalLines[y][x-1].isConnected() && horizontalLines[y+1][x-1].isConnected())
                score += 1;
            if(x < columnLength && y < columnLength && verticalLines[x+1][y].isConnected() && horizontalLines[y][x].isConnected() && horizontalLines[y+1][x].isConnected())
                score += 1;
        }
        return score;
    }

    public boolean isGameOngoing() { // returns true if the game is in progress, otherwise false
        boolean ongoing =  !((firstScore + secondScore) == ((getGridSize()-1)*(getGridSize()-1)));
        if (!ongoing) {
            updateGameStatus();
        }
        return ongoing;
    }

    public void performMove(int row, int column, LineType lineType) {
        ModelLine[][] lines = lineType.equals(LineType.horizontal) ? horizontalLines : verticalLines;
        ModelLine line = lines[row][column];
        if (!line.isConnected()) {
            line.connectLine();
            int scoreObtained = checkBoxFormed(line);
            increaseCurrentScore(scoreObtained);
            if (scoreObtained == 0) { turn = turn == PlayerNumber.first ? PlayerNumber.second : PlayerNumber.first;}
            avlLines.remove(line);
            this.lastMove = line;
            isGameOngoing();
        }
    }

    public void updateGameStatus() { // returns 0 if tie, 1 if player 1 won, 2 if player 2 won. i
        if (firstScore == secondScore) {
            gameStatus = AIGameStatus.Tie;
        }
        else if (firstScore > secondScore) {
            gameStatus = AIGameStatus.OpponentWon;
        }
        else {
            gameStatus = AIGameStatus.AIWon;
        }
    }

    public void increaseCurrentScore(int scoreObtained) {
        PlayerNumber current = turn == PlayerNumber.first ? PlayerNumber.first : PlayerNumber.second;
        if (current.equals(PlayerNumber.first)) {
            firstScore += scoreObtained;
        }
        else {
            secondScore += scoreObtained;
        }
    }
    // getters
    public ModelLine[][] getHorizontalLines() {return horizontalLines;}
    public ModelLine[][] getVerticalLines() {return verticalLines;}
    public int getFirstScore() {return firstScore;}
    public PlayerNumber getTurn() {return turn;}
    public int getSecondScore() {return secondScore;}
    public int getGridSize() {return horizontalLines.length;}
    public AIGameStatus getGameStatus() {return gameStatus;}
    public List<ModelLine> getAvlLines() {return avlLines;}
    public ModelLine getLastMove() {return lastMove;}
    public PlayerNumber getLastPlayer() {return turn.equals(PlayerNumber.first) ? PlayerNumber.second : PlayerNumber.first;}
    public List<AIBoard> getAvlNextMoves() {
        List<AIBoard> avlMoves = new ArrayList<>();
        for(int i=0;i<avlLines.size();i++) {
            avlMoves.add(new AIBoard(this));
            avlMoves.get(i).performMove(avlLines.get(i).getRow(),avlLines.get(i).getColumn(),avlLines.get(i).isHorizontal() ? LineType.horizontal : LineType.vertical);
        }
        return avlMoves;
    }
    public AIBoard getRandomMove() {
        List<AIBoard> nextMoves = getAvlNextMoves();
        if (nextMoves.isEmpty())
            return null;
        final int randomMove = generator.nextInt(nextMoves.size());
        return nextMoves.get(randomMove);
    }


}
