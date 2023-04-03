package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.Box;
import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIBoard {
    private int firstScore; // player 1
    private int secondScore; // player 2
    private PlayerNumber turn; // represents the current player, first for first and second for second
    private int gridSize; // desired board size
    private AIGameStatus gameStatus;
    private List<ModelLine> avlLines;
    private ModelLine[][] horizontalLines; // (gridSize)*(gridSize-1) matrix containing all the horizontal lines in the game
    private ModelLine[][] verticalLines; // (gridSize)*(gridSize-1) matrix containing all the vertical  lines in the game
    private Box[][] boxes; // (gridSize-1)*(gridSize) matrix containing the boxes in the game
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
        this.gridSize = board.getGridSize();
        this.firstScore = board.getFirstScore();
        this.secondScore = board.getSecondScore();
        this.turn = board.getTurn();
        this.gameStatus = board.getGameStatus();
        this.generator = new Random();

        this.avlLines = new ArrayList<>();
        this.boxes = new Box[gridSize-1][gridSize-1];
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
        for (int i=0;i<gridSize-1;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                boxes[i][j] = new Box(new ArrayList<ModelLine>());
                boxes[i][j].getLines().add(verticalLines[j][i]);
                boxes[i][j].getLines().add(verticalLines[j+1][i]);
                boxes[i][j].getLines().add(horizontalLines[i][j]);
                boxes[i][j].getLines().add(horizontalLines[i+1][j]);
                for (ModelLine line: boxes[i][j].getLines()) {
                    if (line.isConnected())
                        boxes[i][j].incConnectedLines();
                }
            }
        }
    }
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
        this.boxes = new Box[gridSize-1][gridSize-1];
        this.horizontalLines = new ModelLine[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new ModelLine[gridSize][gridSize-1]; // array of vertical lines
        this.avlLines = new ArrayList<ModelLine>();
        initializeLines();
        initializeBoxes();
    }
    private void initializeLines() {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new ModelLine(i,j,true,false);
                verticalLines[i][j] = new ModelLine(i,j,false,false);
                avlLines.add(horizontalLines[i][j]);
                avlLines.add(verticalLines[i][j]);
            }
        }
    }
    private void initializeBoxes() {
        for (int i=0;i<gridSize-1;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                boxes[i][j] = new Box(new ArrayList<ModelLine>());
                boxes[i][j].getLines().add(verticalLines[j][i]);
                boxes[i][j].getLines().add(verticalLines[j+1][i]);
                boxes[i][j].getLines().add(horizontalLines[i][j]);
                boxes[i][j].getLines().add(horizontalLines[i+1][j]);
            }
        }
    }
    private int checkBoxFormed(ModelLine line) { // checks if a newly connected line completes boxes, and returns the points obtained from it(0-2)
        List<Box> parentData = getParentBoxes(line);
        int numberOfParents = parentData.size();
        int score;
        switch (numberOfParents) {
            case 1:
                parentData.get(0).incConnectedLines();
                score = parentData.get(0).getIsComplete() ? 1 : 0;
                return score;
            case 2:
                parentData.get(0).incConnectedLines();
                parentData.get(1).incConnectedLines();
                score = (parentData.get(0).getIsComplete() ? 1 : 0) + (parentData.get(1).getIsComplete() ? 1 : 0);
                return score;
            default:
                return 0;
        }
    }

    private List<Box> getParentBoxes(ModelLine line) { // returns a Box array containing the boxes a line is a part of, and an Integer containing the length of the array
        List<Box> results = new ArrayList<>();
        int resultIndex = 0;
        for (int i = 0; i < gridSize-1; i++) {
            for (int j = 0; j < gridSize-1; j++) {
                if (boxes[i][j].hasLine(line)) {
                    results.add(boxes[i][j]);
                    resultIndex+=1;
                    if (resultIndex >= 2) {
                        return results;
                    }
                }
            }
        }
        return results;
    }
    public boolean isGameOngoing() { // returns true if the game is in progress, otherwise false
        boolean ongoing =  !((firstScore + secondScore) == ((gridSize-1)*(gridSize-1)));
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
    public Box[][] getBoxes() {return boxes;}
    public ModelLine[][] getHorizontalLines() {return horizontalLines;}
    public ModelLine[][] getVerticalLines() {return verticalLines;}
    public int getFirstScore() {return firstScore;}
    public PlayerNumber getTurn() {return turn;}
    public int getSecondScore() {return secondScore;}
    public int getGridSize() {return gridSize;}
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
