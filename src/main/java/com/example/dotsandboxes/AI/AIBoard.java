package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * class the represents a game board that the AI uses.
 */
public class AIBoard {
    private int firstScore; // score of player 1(human)
    private int secondScore; // score of player 2(the AI)
    private int currentPlayer; // signifies the player that should act now,
    // 0 for human, 1 for AI
    private int lastPlayer; // represents the last player, same as currentPlayer
    private List<ModelLine> bestLines; // a list of available lines that close
    // a box
    private List<ModelLine> worstLines; // a list of available lines that
    // when connected will make a box closable
    private List<ModelLine> okLines; // a list of available lines that will not
    // close or leave a box to be closed
    private ModelLine[][] horizontalLines; // (gridSize)*(gridSize-1) matrix
    // containing all the horizontal lines in the game
    private ModelLine[][] verticalLines; // (gridSize)*(gridSize-1) matrix
    // containing all the vertical  lines in the game
    private ModelLine lastMove; // the last move that was made on the board

    /**
     * empty constructor that initializes players.
     * for the board to work setGridSize needs to be called
     */
    public AIBoard() {
        this.firstScore = 0;
        this.secondScore = 0;
        this.currentPlayer = 0;
        this.lastPlayer = 0;
    }

    /**
     * sets up all line lists and arrays according to the provided gridSize
     * @param gridSize the square root of the gridSize( gridSize = n*n,
     *                 so the parameter is n)
     */
    public void setGridSize(int gridSize) {
        this.horizontalLines = new ModelLine[gridSize][gridSize-1];
        // array of horizontal lines
        this.verticalLines = new ModelLine[gridSize][gridSize-1];
        // array of vertical lines
        this.okLines = new ArrayList<>();
        this.bestLines = new ArrayList<>();
        this.worstLines = new ArrayList<>();
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new ModelLine(i,j,LineType.horizontal,
                        false);
                verticalLines[i][j] = new ModelLine(i,j,LineType.vertical,
                        false);
                okLines.add(horizontalLines[i][j]);
                okLines.add(verticalLines[i][j]);
            }
        }
    }

    /**
     * full constructor that copies all fields from a given board.
     * NOTE: all copies are separate objects since they will be changed, and
     * they should not change in the source board.
     * @param board the board to be copied from, typically the parent node
     *              board since the move is made after the creation of the board
     */
    public AIBoard(AIBoard board) {
        int gridSize = board.getGridSize();
        this.firstScore = board.firstScore;
        this.secondScore = board.secondScore;
        this.currentPlayer = board.currentPlayer;
        this.lastPlayer = board.lastPlayer;
        this.okLines = new ArrayList<>();
        this.worstLines = new ArrayList<>();
        this.bestLines = new ArrayList<>();
        this.horizontalLines = new ModelLine[gridSize][gridSize-1];
        this.verticalLines = new ModelLine[gridSize][gridSize-1];
        List[] lists = new List[]{board.okLines,board.worstLines,
                board.bestLines};
        for (int i=0;i<gridSize;i++) {
            for(int j=0;j<gridSize-1;j++) {
                this.horizontalLines[i][j] = board.horizontalLines[i][j].copy();
                addLineToList(board.horizontalLines[i][j],
                        board.horizontalLines[i][j].getIsHorizontal(),lists);
                this.verticalLines[i][j] = board.verticalLines[i][j].copy();
                addLineToList(board.verticalLines[i][j],
                        board.verticalLines[i][j].getIsHorizontal(),lists);
            }
        }
    }

    /**
     * function that adds a new line to the appropriate list based on the list
     * it was in on the original board it was copied from
     * @param line the copied line
     * @param type the line type(horizontal or vertical), used to determine
     *             which line array to use
     * @param lists an array of the old board line lists
     */
    private void addLineToList(ModelLine line,LineType type,List<ModelLine>[]
            lists) {
        ModelLine[][] lineArray = type.equals(LineType.horizontal) ?
                horizontalLines: verticalLines;
        if (lists[0].contains(line))
            okLines.add(lineArray[line.getRow()][line.getColumn()]);
        else if (lists[1].contains(line))
            worstLines.add(lineArray[line.getRow()][line.getColumn()]);
        else if (lists[2].contains(line))
            bestLines.add(lineArray[line.getRow()][line.getColumn()]);
    }

    /**
     * function that registers a move to the game board, and updated
     * all line lists and player scores accordingly.
     * @param row the row the line is in
     * @param column the column the line is in
     * @param lineType represents the type of line the move was made on
     *                 (used to determine which line array to use)
     */
    public void performMove(int row, int column, LineType lineType) {
        ModelLine[][] lines = lineType.equals(LineType.horizontal) ?
                horizontalLines : verticalLines;
        if (row < lines.length && column < lines[0].length) {
            ModelLine line = lines[row][column];
            if (!line.getIsConnected()) {
                line.connectLine();
                int scoreObtained = checkBoxFormed(line)[0];
                increaseCurrentScore(scoreObtained);
                adjustLinesBasedOnMove(line);
                lastPlayer = currentPlayer;
                if (scoreObtained == 0) {
                    currentPlayer = 1 - currentPlayer;
                }
                this.lastMove = line;
            }
        }
    }

    /**
     * function that checks if a box was formed after a line was connected,
     * and returns the score obtained from the connection
     * the function run-time is technically O(n), when n=the number of boxes
     * the line is a part of. But, a line is in 2 boxes at maximum, so the
     * function run-time is O(1) in practice
     * @param line a newly connected line
     * @return an int array of length 3, where each index 1 is the number of connected
     * boxes the line is a part of, and indexes 1-2 are contain the amount of connected lines
     * in each box the line is a part of(excluding itself)
     */
    public int[] checkBoxFormed(ModelLine line) {
        int score = 0;
        int[] boxesAfterMove = checkLeftBoxes(line);
        for(int box: boxesAfterMove) {
            if (box == 3)
                score++;
        }
        return new int[]{score,boxesAfterMove[0],boxesAfterMove[1]};
    }

    /**
     * function that checks if the game is in progress
     * @return true if the game has yet to end, false if it ended
     */
    public boolean isGameOngoing() {
        return !((firstScore + secondScore) ==
                ((getGridSize()-1)*(getGridSize()-1)));
    }

    /**
     * function that adds the parameter scoreObtained to the current
     * players score. used to increase the players score after a move.
     * @param scoreObtained to amount of points to increase the players score by
     */
    private void increaseCurrentScore(int scoreObtained) {
        if (currentPlayer == 0) {
            firstScore += scoreObtained;
        }
        else {
            secondScore += scoreObtained;
        }
    }

    /**
     * getter function that returns the highest ranking(non-empty) line list by
     * the following hierarchy: bestLines > okLines > worstLines.
     * @return the best line list
     */
    public List<ModelLine> getBestMovesList() {
        if (bestLines.isEmpty()) {
            if (okLines.isEmpty())
                return worstLines;
            return okLines;
        }
        return bestLines;
    }

    /**
     * function that returns a list of available future game boards .
     * the list DOES NOT hold all future game boards, but rather all "best"
     * future boards based on a line list returned by the function
     * "getBestMoves()".
     * run-time is O(n), n = size of bestMoves list
     * @return a list of "best" possible future game boards
     */
    public List<AIBoard> getAvlNextMoves() {
        List<AIBoard> avlMoves = new ArrayList<>();
        List<ModelLine> avlLines = getBestMovesList();
        for(int i=0;i<avlLines.size();i++) {
            avlMoves.add(new AIBoard(this));
            avlMoves.get(i).performMove(avlLines.get(i).getRow(),
                    avlLines.get(i).getColumn(),
                    avlLines.get(i).getIsHorizontal());
        }

        return avlMoves;
    }

    /**
     * function that returns the best possible move on the current board.
     * the moves scored are not all available moves but rather all "best"
     * moves based on a line list returned by the function "getBestMoves()".
     * additionally,the moves are scored by the function evaluateMove.
     * if there are multiple "best moves" a random move will be returned
     * function run-time is O(n*k).
     * @return the best move that was found and its score
     */
    public Pair<ModelLine,Integer> getBestMove() {
        List<ModelLine> avlLines = getBestMovesList(),
                bestMoves = new ArrayList<>();
        int bestScore = Integer.MIN_VALUE, score;

        for (ModelLine move: avlLines) { // O(N), n = number of possible
            // best moves
            score = evaluateMove(move); // O(k), k = the length of the chain
            // the line is a part of
            if (score == bestScore) {
                bestMoves.add(move);
            }
            else if (score > bestScore) {
                bestMoves.clear();
                bestMoves.add(move);
                bestScore = score;
            }
        }

        Random generator = new Random();
        ModelLine bestMove = bestMoves.get(
                generator.nextInt(bestMoves.size()));
        removeLine(bestMove);
        return new Pair<>(bestMove,bestScore);
    }

    /**
     function that evaluates a move and assigns it a score. The
     score is based on the number of boxes that the move closes and the chain
     of moves it enables to close(the function is summoned for each
     unconnected line in the boxes the line is a part of). If the player
     gains points from the move the score will be positive, and if the player
     does not gain move it will be negative(represents evaluation for the
     opposing player's moves).
     function run time is O(k), where k = the length of the chain the line is
     a part of. this applies when the line completes a box or is a 3rd line
     to be connected in a box. otherwise, the run time is O(1).
     * @param move
     * @return the score of the move
     */
    private int evaluateMove(ModelLine move) {
        move.connectLine();

        List<ModelLine> leftLines = getUnconnectedLines(move);
        int[] boxState = checkBoxFormed(move);
        int score = boxState[0], reducedScore;

        if (score > 0){
            for (ModelLine line: leftLines) { // at most 3 lines so negligible
                score += evaluateMove(line);
            }
        } else {
            reducedScore = (boxState[1] == 2 ? 1 : 0) +
                    (boxState[2] == 2 ? 1 :0); // checks if the line is the third
                    // connected line in the box

            if (reducedScore != 0) { // happens when a line is the third in a box
                for (ModelLine line: leftLines) {
                    score +=(Math.abs(evaluateMove(line))*(-1));
                }
           }

            score += Math.abs(reducedScore)*(-1);
        }

        move.disconnectLine();
        return score*10;
    }

    /**
     * functions that checks how many lines are connected in boxes that
     * a recently connected line is a part of.
     * @param line the line that was connected
     * @return an int array of length 2, where each index is a box a line is
     * a part of (a line can be in 1-2 boxes), and has a value ranging from 0-3.
     * the value of each index represents how many lines are connected in the
     * box apart from the parameter line
     */
    private int[] checkLeftBoxes(ModelLine line) {
        int x = line.getRow();
        int y = line.getColumn();
        int columnLength = horizontalLines[0].length;
        int[] connectedLines = new int[2];
        if (line.getIsHorizontal().equals(LineType.horizontal)) {
            connectedLines[0] =
                    (x>0 && x < getGridSize() && y < columnLength) ?
                            returnConnectedNum(horizontalLines[x-1][y],
                                    verticalLines[y+1][x-1],
                                    verticalLines[y][x-1]): 0;
            connectedLines[1] = (x < columnLength && y < columnLength) ?
                    returnConnectedNum(horizontalLines[x+1][y],
                            verticalLines[y][x],verticalLines[y+1][x]): 0;
        }
        else {
            connectedLines[0] =
                    (x > 0 && x < getGridSize() && y < columnLength) ?
                            returnConnectedNum(verticalLines[x - 1][y],
                                    horizontalLines[y][x - 1],
                                    horizontalLines[y + 1][x - 1]) : 0;
            connectedLines[1] = (x < columnLength && y < columnLength) ?
                    returnConnectedNum(verticalLines[x + 1][y],
                            horizontalLines[y][x],
                            horizontalLines[y + 1][x]) : 0;
        }
        return connectedLines;
    }

    /**
     * returns the number of connected lines from the lines given as parameters
     * @param line
     * @param line2
     * @param line3
     * @return the number of connected lines
     */
    private int returnConnectedNum(ModelLine line, ModelLine line2,
                                   ModelLine line3){
        return isConnected(line)+isConnected(line2)+isConnected(line3);
    }

    /**
     * checks if a line is connected or not
     * @param line
     * @return 1 if the line is connected, 0 otherwise
     */
    private int isConnected(ModelLine line) {
        return line.getIsConnected() ? 1 : 0;
    }

    /**
     * function that moves the lines in the boxes the connected line is a
     * part of to their appropriate available lines list based on the state
     * of the box after the line connection.
     * @param line the line that was connected
     */
    private void adjustLinesBasedOnMove(ModelLine line) {
        removeLine(line);
        List<ModelLine> lines = getUnconnectedLines(line); // will be 6 lines
        // at most
        int[] connectedLines;

        for(int i=0;i<lines.size();i++)
            lines.forEach(this::removeLine);

        for (ModelLine modelLine : lines) {
            connectedLines = checkLeftBoxes(modelLine);
            if (connectedLines[0] == 3 || connectedLines[1] == 3)
                bestLines.add(modelLine);
            else if (connectedLines[0] == 2 || connectedLines[1] == 2)
                worstLines.add(modelLine);
            else okLines.add(modelLine);
        }
    }

    /**
     * function that returns a 2d list of all lines effected(unconnected lines)
     * by the connection of a line.
     * @param line the line that was connected
     * @return a 2d list of lines, where each row in the list represents a box,
     *      * and each column represents a line in the box that is not
     *      connected and needs to be moved to a new available line list
     *      after the move.
     */
    private List<ModelLine> getUnconnectedLines(ModelLine line) {
        List<ModelLine> lines = new ArrayList<>();
        int x = line.getRow();
        int y = line.getColumn();
        int columnLength = horizontalLines[0].length;

        if (line.getIsHorizontal().equals(LineType.horizontal)) {
            if (x>0 && x < getGridSize() && y < columnLength) {
                List<ModelLine> otherLines = createLineList(
                        horizontalLines[x - 1][y],verticalLines[y + 1][x - 1],
                                verticalLines[y][x - 1]);
                lines.addAll(otherLines.stream().filter(listLine ->
                        !listLine.getIsConnected()).toList());
            }
            if (x < columnLength && y < columnLength) {
                List<ModelLine> otherLines =
                        createLineList(horizontalLines[x+1][y],
                                verticalLines[y][x], verticalLines[y+1][x]);
                lines.addAll(otherLines.stream().filter(listLine ->
                        !listLine.getIsConnected()).toList());
            }
        }
        else {
            if (x > 0 && x < getGridSize() && y < columnLength) {
                List<ModelLine> otherLines =
                        createLineList(verticalLines[x - 1][y],
                        horizontalLines[y][x - 1],
                        horizontalLines[y + 1][x - 1]);
                lines.addAll(otherLines.stream().filter(listLine ->
                        !listLine.getIsConnected()).toList());
            }
            if (x < columnLength && y < columnLength) {
                List<ModelLine> otherLines =
                        createLineList(verticalLines[x + 1][y],
                                horizontalLines[y][x],
                                horizontalLines[y + 1][x]);
                lines.addAll(otherLines.stream().filter(listLine ->
                        !listLine.getIsConnected()).toList());
            }
        }
        return lines;
    }

    /**
     * creates a list of ModelLines from the given lines
     * @param line
     * @param line2
     * @param line3
     * @return a list of type ModelLine containing all parameter lines
     */
    private List<ModelLine> createLineList(ModelLine line,ModelLine line2,
    ModelLine line3) {
        return new ArrayList<>(Arrays.asList(line, line2, line3));
    }
    /**
     * function that removes a line from all available line lists.
     * Note: a line will be in only one list, but since removal does not
     * throw an error when the element passed is not in the list an if check
     * would be pointless when you can just remove the line from all lists.
     * @param line the line to be removed
     */
    private void removeLine(ModelLine line) {
        okLines.remove(line);
        bestLines.remove(line);
        worstLines.remove(line);
    }

    /**
     * getter function that returns the difference in scores between
     * the two players(from the AI's perspective)
     * @return the score difference
     */
    public int getScoreDifference() {return secondScore-firstScore;}

    //general getters
    public int getGridSize() {return horizontalLines.length;}
    public ModelLine getLastMove() {return lastMove;}
    public int getLastPlayer() {return lastPlayer;}
}
