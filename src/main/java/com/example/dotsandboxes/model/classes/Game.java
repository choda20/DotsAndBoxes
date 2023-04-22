package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.AI.AIPlayer;
import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.MoveResult;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class Game {
    private final PropertyChangeSupport pcs; // holds the observer that is used
    // to notify listeners when changes in the board occur
    private Player first; // represents player 1
    private Player second; //represents player 2
    private PlayerNumber turn; // represents the current player,
    // first for first and second for second
    private GameType gameType; // represents the type of the game(HVH,HVA,AVA)
    private Board gameBoard; //represents the board

    /**
     * empty constructor that initializes the observer and turn
     */
    public Game() {
        pcs = new PropertyChangeSupport(this);
        this.turn = PlayerNumber.first;
    } // empty constructor

    /**
     * function that determines if the game has yet to end
     * @return true if the game is in progress, otherwise false
     */
    private boolean isGameInProgress() { // returns true if the game
        // is in progress, otherwise false
        return !((first.getScore() + second.getScore()) ==
                (gameBoard.getGridSize()-1)*(gameBoard.getGridSize()-1));}

    /**
     * function that swaps the current turn, practically moves to the next turn
     */
    private void swapTurn() {
        turn = turn == PlayerNumber.first ?
                PlayerNumber.second : PlayerNumber.first;
    }

    /**
     * function that registers a move to the game board and updates player
     * scores and the current turn accordingly. this function also notifies all
     * listeners that a move was made.
     * @param row the row the line is in
     * @param column the column the line is in
     * @param lineType represents the type of line the move was made on
     *                 (used to determine which line array to use)
     */
    public void performMove(int row, int column, LineType lineType) {
        ModelLine[][] lines = lineType.equals(LineType.horizontal) ?
                gameBoard.getHorizontalLines() : gameBoard.getVerticalLines();
        if (row < lines.length && column < lines[0].length) {
            ModelLine line = lines[row][column];
            if (!line.getIsConnected()) {
                line.connectLine();
                int scoreObtained = gameBoard.checkBoxFormed(line);
                getCurrent().setScore(getCurrent().getScore() + scoreObtained);
                MoveResult result = !isGameInProgress() ? MoveResult.gameOver : MoveResult.valid;
                PropertyChangeEvent event = new PropertyChangeEvent(this, "performMove", new Pair<>(line, turn), result);
                if (scoreObtained == 0) {
                    swapTurn();
                }
                pcs.firePropertyChange(event);
            }
        }
    }

    /**
     * function that is used to get the current winner of the game
     * (can be called to see which player is leading)
     * @return a Pair that hold an integer that represents the outcome of the
     * game(0 if somebody won, 1 if a tie occurred) as well as a String with
     * the Winners name(if there is a tie a blank name is returned)
     */
    public Pair<Integer,String> getWinner() {
        String winText = " won!";
        if (first.getScore() == second.getScore()) {
            return new Pair<>(1,"It's a tie!");
        }
        else if (first.getScore() > second.getScore()) {
            return new Pair<>(0,first.getName() + winText);
        }
        return new Pair<>(0,second.getName()+ winText);
    }

    /**
     * function that gets all setting form field inputs and validates them.
     * if the inputs are valid it enters them to their respective variable.
     * @param p1Name the entered name of the first player, can be anything as
     *              long as it is not an empty string
     * @param p2Name the entered name of the second player, can be anything
     *               as long as it is not an empty string
     * @param gridSize the entered gridSize,
     *                 can be a number ranging from 2-10 (included)
     */
    public void insertNamesAndGrid(String p1Name,String p2Name,String gridSize){
        boolean valid = true;
        int number;

        try {number = Integer.parseInt(gridSize);}
        catch (NumberFormatException e) {number = 0;}
        if (p1Name.isBlank() || p2Name.isBlank() || number <= 1 || number > 10){
            valid = false;
        } else {
            first.setName(p1Name);
            second.setName(p2Name);
            setGameBoard(new Board(number));
            if (gameType.equals(GameType.HumanVsAI)) {
                AIPlayer aiPlayer = (AIPlayer) second;
                aiPlayer.getModel().setGridSize(number);
            }
        }

        PropertyChangeEvent event = new PropertyChangeEvent(this,
                "insertNamesAndGrid","",valid);
        pcs.firePropertyChange(event);
    }

    /**
     * registers a new listener to the observer notify list
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {pcs.addPropertyChangeListener(listener);}

    /**
     * removes a listener from the observer notify list
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {pcs.removePropertyChangeListener(listener);}

    /**
     * function that returns the playerNumber of the leading player
     * @return first if first is leading, second otherwise
     */
    public PlayerNumber getLeadingPlayer() {
        return first.getScore() > second.getScore() ? PlayerNumber.first :
                PlayerNumber.second;
    }

    //general getters
    public PlayerNumber getTurn() {return turn;}
    public GameType getGameType() {return gameType;}
    public Board getGameBoard() {return gameBoard;}
    public Player getSecond() {return second;}
    public Player getFirst() {return first;}
    public Player getCurrent() {return turn == PlayerNumber.first ?
            first: second;}

    //general setters
    public void setGameType(GameType type) {this.gameType = type;}
    public void setGameBoard(Board gameBoard) {this.gameBoard = gameBoard;}
    public void setFirst(Player first) {this.first = first;}
    public void setSecond(Player second) {this.second = second;}
}
