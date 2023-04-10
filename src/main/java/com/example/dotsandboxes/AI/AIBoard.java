package com.example.dotsandboxes.AI;

import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * class the represents a game board that the AI uses.
 */
public class AIBoard {
    private int firstScore; // score of player 1(human)
    private int secondScore; // score of player 2(the AI)
    private int currentPlayer; // signifies the player that should act now, 0 for human, 1 for AI
    private List<ModelLine> bestLines; // a list of available lines that close a box
    private List<ModelLine> worstLines; // a list of available lines that when connected will make a box closable
    private List<ModelLine> okLines; // a list of available lines that will not close or leave a box to be closed
    private ModelLine[][] horizontalLines; // (gridSize)*(gridSize-1) matrix containing all the horizontal lines in the game
    private ModelLine[][] verticalLines; // (gridSize)*(gridSize-1) matrix containing all the vertical  lines in the game
    private ModelLine lastMove; // the last move that was made on the board

    /**
     * empty constructor that initializes players.
     * for the board to work setGridSize needs to be called
     */
    public AIBoard() {
        this.firstScore = 0;
        this.secondScore = 0;
        this.currentPlayer = 0;
    }

    /**
     * sets up all line lists and arrays according to the provided gridSize
     * @param gridSize the square root of the gridSize( gridSize = n*n, so the parameter is n)
     */
    public void setGridSize(int gridSize) {
        this.horizontalLines = new ModelLine[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new ModelLine[gridSize][gridSize-1]; // array of vertical lines
        this.okLines = new ArrayList<ModelLine>();
        this.bestLines = new ArrayList<ModelLine>();
        this.worstLines = new ArrayList<ModelLine>();
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new ModelLine(i,j,true,false);
                verticalLines[i][j] = new ModelLine(i,j,false,false);
                okLines.add(horizontalLines[i][j]);
                okLines.add(verticalLines[i][j]);
            }
        }
    }

    /**
     * full constructor that copies all fields from a given board.
     * NOTE: all copies are separate objects since they will be changed, and
     * they should not change in the source board.
     * @param board the board to be copied from, typically the parent node board since
     *              the move is made after the creation of the board
     */
    public AIBoard(AIBoard board) {
        int gridSize = board.getGridSize();
        this.firstScore = board.firstScore;
        this.secondScore = board.secondScore;
        this.currentPlayer = board.currentPlayer;

        this.okLines = new ArrayList<>();
        this.worstLines = new ArrayList<>();
        this.bestLines = new ArrayList<>();
        this.horizontalLines = new ModelLine[gridSize][gridSize-1];
        this.verticalLines = new ModelLine[gridSize][gridSize-1];
        List<ModelLine>[] lists = new List[]{board.okLines,board.worstLines,board.bestLines};
        for (int i=0;i<gridSize;i++) {
            for(int j=0;j<gridSize-1;j++) {
                this.horizontalLines[i][j] = board.horizontalLines[i][j].copy();
                addLineToList(board.horizontalLines[i][j],board.horizontalLines[i][j].getIsHorizontal() ? LineType.horizontal: LineType.vertical,lists);
                this.verticalLines[i][j] = board.verticalLines[i][j].copy();
                addLineToList(board.verticalLines[i][j],board.verticalLines[i][j].getIsHorizontal() ? LineType.horizontal: LineType.vertical,lists);
            }
        }
    }

    /**
     * function that adds a new line to the appropriate list based on the list
     * it was in on the original board it was copied from
     * @param line the copied line
     * @param type the line type(horizontal or vertical), used to determine which line array to use
     * @param lists an array of the old board line lists
     */
    private void addLineToList(ModelLine line,LineType type,List<ModelLine>[] lists) {
        ModelLine[][] lineArray = type.equals(LineType.horizontal) ? horizontalLines: verticalLines;
        if (lists[0].contains(line))
            okLines.add(lineArray[line.getRow()][line.getColumn()]);
        else if (lists[1].contains(line))
            worstLines.add(lineArray[line.getRow()][line.getColumn()]);
        else if (lists[2].contains(line))
            bestLines.add(lineArray[line.getRow()][line.getColumn()]);
    }

    /**
     * function that registers a move to the game board, and updated all line lists and
     * player scores accordingly.
     * @param row the row the line is in
     * @param column the column the line is in
     * @param lineType represents the type of line the move was made on
     *                 (used to determine which line array to use)
     */
    public void performMove(int row, int column, LineType lineType) {
        ModelLine[][] lines = lineType.equals(LineType.horizontal) ? horizontalLines : verticalLines;
        ModelLine line = lines[row][column];
        if (!line.getIsConnected()) {
            line.connectLine();
            int scoreObtained = checkBoxFormed(line);
            increaseCurrentScore(scoreObtained);
            adjustLinesBasedOnMove(line);
            if (scoreObtained == 0) {
                currentPlayer = 1 - currentPlayer;
            }
            this.lastMove = line;
        }
    }

    /**
     * function that checks if a box was formed after a line was connected, and returns the
     * score obtained from the connection
     * @param line a newly connected line
     * @return the score obtained from connecting the line, ranges from 0-2.
     */
    public int checkBoxFormed(ModelLine line) {
        int score = 0;
        int[] boxesAfterMove = checkLeftBoxes(line);
        for(int box: boxesAfterMove) {
            if (box == 3)
                score++;
        }
        return score;
    }

    /**
     * function that checks if the game is in progress
     * @return true if the game has yet to end, false if it ended
     */
    public boolean isGameOngoing() {
        return !((firstScore + secondScore) == ((getGridSize()-1)*(getGridSize()-1)));
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
    public List<ModelLine> getBestMoves() {
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
     * future boards based on a line list returned by the function "getBestMoves()".
     * @return a list of "best" possible future game boards
     */
    public List<AIBoard> getAvlNextMoves() {
        List<AIBoard> avlMoves = new ArrayList<>();
        List<ModelLine> avlLines = getBestMoves();
        for(int i=0;i<avlLines.size();i++) {
            avlMoves.add(new AIBoard(this));
            avlMoves.get(i).performMove(avlLines.get(i).getRow(),avlLines.get(i).getColumn(),avlLines.get(i).getIsHorizontal() ? LineType.horizontal : LineType.vertical);
        }

        return avlMoves;
    }

    /**
     * function that returns the best possible move on the current board.
     * the moves scored are not all available moves but rather all "best"
     * moves based on a line list returned by the function "getBestMoves()". additionaly,
     * the moves are scored by the function evaluateMove.
     * @return
     */
    public ModelLine getBestMove() {
        List<ModelLine> avlLines = getBestMoves();
        ModelLine bestMove = avlLines.get(0);
        int bestScore = - 1, score;

        for (ModelLine move: avlLines) {
            score = evaluateMove(move);
            if (score > bestScore) {
                bestMove = move;
                bestScore = score;
            }
        }
        removeLine(bestMove);
        return bestMove;
    }

    /**
     * function that evaluates a move by the amount of boxes it closes, and the amount
     * of boxes it lets the opposing player close. the formula is:
     * moveScore = BoxesClosed(score obtained) - BoxesleftOpen
     * @param move the move to be scored
     * @return the score of the given move
     */
    private int evaluateMove(ModelLine move) {
        move.connectLine();
        int score = checkBoxFormed(move);
        int[] leftBoxes = checkLeftBoxes(move);
        score -= ((leftBoxes[0] == 2 ? 1 : 0) + (leftBoxes[1] == 2 ? 1 : 0));
        move.disconnectLine();
        return score;
    }

    /**
     * functions that checks how many lines are connected in boxes that
     * a recently connected line is a part of.
     * @param line the line that was connected
     * @return an int array of length 2, where each index is a box a line is a part
     * of (a line can be in 1-2 boxes), and has a value ranging from 0-3.
     * the value of each index represents how many lines are connected in the box apart
     * from the parameter line
     */
    private int[] checkLeftBoxes(ModelLine line) {
        int x = line.getRow();
        int y = line.getColumn();
        int columnLength = horizontalLines[0].length;
        int[] connectedLines = new int[2];
        if (line.getIsHorizontal()) {
            connectedLines[0] = (x>0 && x < getGridSize() && y < columnLength) ? ((horizontalLines[x-1][y].getIsConnected() ? 1 : 0) + (verticalLines[y+1][x-1].getIsConnected() ? 1 : 0) + (verticalLines[y][x-1].getIsConnected() ? 1 : 0)): 0;
            connectedLines[1] = (x < columnLength && y < columnLength) ? (horizontalLines[x+1][y].getIsConnected() ? 1 : 0) + (verticalLines[y][x].getIsConnected() ? 1 : 0) + (verticalLines[y+1][x].getIsConnected() ? 1 : 0): 0;
        }
        else {
            connectedLines[0] = (x > 0 && x < getGridSize() && y < columnLength) ? ((verticalLines[x - 1][y].getIsConnected() ? 1 : 0) + (horizontalLines[y][x - 1].getIsConnected() ? 1 : 0) + (horizontalLines[y + 1][x - 1].getIsConnected() ? 1 : 0)) : 0;
            connectedLines[1] = (x < columnLength && y < columnLength) ? (verticalLines[x + 1][y].getIsConnected() ? 1 : 0) + (horizontalLines[y][x].getIsConnected() ? 1 : 0) + (horizontalLines[y + 1][x].getIsConnected() ? 1 : 0) : 0;
        }
        return connectedLines;
    }

    /**
     * function that moves the lines in the boxes the connected line is a part of
     * to their appropriate available lines list based on the state of the box after
     * the line connection.
     * @param line the line that was connected
     */
    private void adjustLinesBasedOnMove(ModelLine line) {
        removeLine(line);
        List<List<ModelLine>> lines = getUnconnectedLines(line);

        for(int i=0;i<lines.size();i++)
            lines.get(i).stream().forEach(listLine -> removeLine(listLine));

        for (int i=0;i<lines.size();i++) {
            if (lines.get(i).size() == 2) { // 2 means adding a line will open a box, so worst moves
                worstLines.add(lines.get(i).get(0));
                worstLines.add(lines.get(i).get(1));
            } else if (lines.get(i).size() == 1) { // 1 means adding a line will close a box, so best move
                bestLines.add(lines.get(i).get(0));
            } else if (lines.get(i).size() == 3) { // 3 means that nothing notable will happen, so ok move
                okLines.add(lines.get(i).get(0));
                okLines.add(lines.get(i).get(1));
                okLines.add(lines.get(i).get(2));
            }
        }

    }

    /**
     * function that returns a 2d list of all lines effected(unconnected lines) by the connection of a line.
     * @param line the line that was connected
     * @return a 2d list of lines, where each row in the list represents a box,
     *      * and each column represents a line in the box that is not connected and needs to be moved
     *      * to a new available line list after the move.
     */
    private List<List<ModelLine>> getUnconnectedLines(ModelLine line) {
        List<List<ModelLine>> lines = new ArrayList<List<ModelLine>>();
        int x = line.getRow();
        int y = line.getColumn();
        int columnLength = horizontalLines[0].length;
        if (line.getIsHorizontal()) {
            if (x>0 && x < getGridSize() && y < columnLength) {
                List<ModelLine> otherLines = new ArrayList<>(Arrays.asList(new ModelLine[]{horizontalLines[x - 1][y], verticalLines[y + 1][x - 1], verticalLines[y][x - 1]}));
                lines.add(otherLines.stream().filter(listLine -> listLine.getIsConnected() == false).collect(Collectors.toList()));
            }
            if (x < columnLength && y < columnLength) {
                List<ModelLine> otherLines = new ArrayList<>(Arrays.asList(new ModelLine[]{horizontalLines[x+1][y], verticalLines[y][x], verticalLines[y+1][x]}));
                lines.add(otherLines.stream().filter(listLine -> listLine.getIsConnected() == false).collect(Collectors.toList()));
            }
        }
        else {
            if (x > 0 && x < getGridSize() && y < columnLength) {
                List<ModelLine> otherLines = new ArrayList<>(Arrays.asList(new ModelLine[]{verticalLines[x - 1][y], horizontalLines[y][x - 1], horizontalLines[y + 1][x - 1]}));
                lines.add(otherLines.stream().filter(listLine -> listLine.getIsConnected() == false).collect(Collectors.toList()));
            }
            if (x < columnLength && y < columnLength) {
                List<ModelLine> otherLines = new ArrayList<>(Arrays.asList(new ModelLine[]{verticalLines[x + 1][y], horizontalLines[y][x], horizontalLines[y + 1][x]}));
                lines.add(otherLines.stream().filter(listLine -> listLine.getIsConnected() == false).collect(Collectors.toList()));
            }
        }
        return lines;
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
    public int getCurrentPlayer() {return currentPlayer;}

}
