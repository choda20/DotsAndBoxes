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
    private List<AIBoard> avlMoves;
    private List<ModelLine> avlLines;
    private ModelLine[][] horizontalLines; // (gridSize)*(gridSize-1) matrix containing all the horizontal lines in the game
    private ModelLine[][] verticalLines; // (gridSize)*(gridSize-1) matrix containing all the vertical  lines in the game
    private Box[][] boxes; // (gridSize-1)*(gridSize) matrix containing the boxes in the game

    public AIBoard() {
        this.turn = PlayerNumber.first;
        this.firstScore = 0;
        this.secondScore = 0;
        this.gameStatus = AIGameStatus.GameInProgress;
    } // empty constructor
    public AIBoard(AIBoard board) {
        this.gridSize = board.getGridSize();
        this.secondScore = board.getSecondScore();
        this.firstScore = board.getFirstScore();
        this.turn = board.getTurn();
        this.avlLines = new ArrayList<>(board.getAvlLines());
        this.boxes = new Box[gridSize-1][gridSize-1];
        this.horizontalLines = new ModelLine[gridSize][gridSize-1];
        this.verticalLines = new ModelLine[gridSize][gridSize-1];
        this.gameStatus = board.getGameStatus();
        for (int i=0;i<gridSize;i++) {
            for(int j=0;j<gridSize-1;j++) {
                this.horizontalLines[i][j] = board.getHorizontalLines()[i][j].copy();
                this.verticalLines[i][j] = board.getVerticalLines()[i][j].copy();
                if (!(i == gridSize-1)) {
                    this.boxes[i][j] = board.getBoxes()[i][j].copy();
                }
            }
        }
        this.avlMoves = new ArrayList<AIBoard>();
        initializeAvlNextMoves();
    }
    public int checkBoxFormed(ModelLine line) { // checks if a newly connected line completes boxes, and returns the points obtained from it(0-2)
        Pair<Integer,Box[]> parentData = getParentBoxes(line);
        Box[] parents = parentData.getValue();
        int numberOfParents = parentData.getKey().intValue();
        int score;
        switch (numberOfParents) {
            case 1:
                parents[numberOfParents-1].incConnectedLines();
                score = parents[numberOfParents-1].getIsComplete() ? 1 : 0;
                return score;
            case 2:
                parents[numberOfParents-1].incConnectedLines();
                parents[numberOfParents-2].incConnectedLines();
                score = (parents[numberOfParents-1].getIsComplete() ? 1 : 0) + (parents[numberOfParents-2].getIsComplete() ? 1 : 0);
                return score;
            default:
                return 0;
        }
    }

    private Pair<Integer,Box[]> getParentBoxes(ModelLine line) { // returns a Box array containing the boxes a line is a part of, and an Integer containing the length of the array
        Box[] results = new Box[2];
        int resultIndex = 0;
        for (int i = 0; i < gridSize-1; i++) {
            for (int j = 0; j < gridSize-1; j++) {
                if (boxes[i][j].hasLine(line)) {
                    results[resultIndex] = boxes[i][j];
                    resultIndex+=1;
                    if (resultIndex >= 2) {
                        return new Pair<>(resultIndex,results);
                    }
                }
            }
        }
        return new Pair<>(resultIndex,results);
    }
    public boolean isGameOngoing() { // returns true if the game is in progress, otherwise false
        boolean ongoing =  !((firstScore + secondScore) == (gridSize-1)*(gridSize-1));
        if (!ongoing) {
            System.out.println("ENDED");
            updateGameStatus();
        }
        return ongoing;
    }

    public void swapTurn() {
        turn = turn == PlayerNumber.first ? PlayerNumber.second : PlayerNumber.first;
    } // moves to next turn

    public void performMove(int row, int column, LineType lineType) {
        ModelLine[][] lines = lineType.equals(LineType.horizontal) ? horizontalLines : verticalLines;
        ModelLine line = lines[row][column];
        if (!line.isConnected()) {
            line.connectLine();
            line.setOwner(turn);
            int scoreObtained = checkBoxFormed(line);
            increaseCurrentScore(scoreObtained);
            if (scoreObtained == 0) {swapTurn();}
            avlLines.remove(line);
            initializeAvlNextMoves();
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
    private void initializeLines() {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new ModelLine(i,j,true,false);
                verticalLines[i][j] = new ModelLine(i,j,false,false);
                avlLines.add(horizontalLines[i][j]);
                avlLines.add(verticalLines[i][j]);
            }
        }
        initializeAvlNextMoves();
    }
    public void initializeAvlNextMoves() {
        if (!avlMoves.isEmpty()){
            System.out.println("Cleared");
            avlMoves.clear();
        }
        for(int i=0;i<avlLines.size();i++) {
            avlMoves.add(new AIBoard(this));
            avlMoves.get(i).performMove(avlLines.get(i).getRow(),avlLines.get(i).getColumn(),avlLines.get(i).isHorizontal() ? LineType.horizontal : LineType.vertical);
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
    // getters
    public Box[][] getBoxes() {return boxes;}
    public List<AIBoard> getAvlMoves() {return avlMoves;}
    public ModelLine[][] getHorizontalLines() {return horizontalLines;}
    public ModelLine[][] getVerticalLines() {return verticalLines;}
    public int getFirstScore() {return firstScore;}
    public PlayerNumber getTurn() {return turn;}
    public int getSecondScore() {return secondScore;}
    public int getGridSize() {return gridSize;}
    public AIGameStatus getGameStatus() {return gameStatus;}
    public List<ModelLine> getAvlLines() {return avlLines;}
    public PlayerNumber getLastPlayer() {return turn.equals(PlayerNumber.first) ? PlayerNumber.second : PlayerNumber.first;}
    public AIBoard getRandomMove() {
        System.out.println(avlMoves.size());
        Random rnd = new Random();
        int index = rnd.nextInt(avlMoves.size());
        AIBoard result = avlMoves.get(index);
        avlMoves.remove(index);
        return result;
    }

    //setters
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
        this.boxes = new Box[gridSize-1][gridSize-1];
        this.horizontalLines = new ModelLine[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new ModelLine[gridSize][gridSize-1]; // array of vertical lines
        this.avlMoves = new ArrayList<AIBoard>();
        this.avlLines = new ArrayList<ModelLine>();
        initializeBoxes();
        initializeLines();
    }

}
