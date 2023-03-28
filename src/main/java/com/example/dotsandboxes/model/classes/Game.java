package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.MoveResult;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class Game {
    private PropertyChangeSupport pcs; // the observer
    Player first; // player 1
    Player second; // player 2
    PlayerNumber turn; // represents the current player, first for first and second for second
    GameType gameType; // represents the type of the game(HVH,HVA,AVA)
    Board gameBoard; // the board

    public Game() {
        pcs = new PropertyChangeSupport(this);
        this.turn = PlayerNumber.first;
    } // empty constructor

    public Game(Player first, Player second, GameType gameType, Board gameBoard) { // full constructor
        this.gameType = gameType;
        this.first = first;
        this.second = second;
        this.gameBoard = gameBoard;
        this.turn = PlayerNumber.first;
    }

    public boolean isGameOver() { // returns true if the game is in progress, otherwise false
        return !((first.getScore() + second.getScore()) == (gameBoard.getGridSize()-1)*(gameBoard.getGridSize()-1));}

    public void swapTurn() {
        turn = turn == PlayerNumber.first ? PlayerNumber.second : PlayerNumber.first;
    } // moves to next turn

    public MoveResult performMove(int row, int column, LineType lineType) { // returns true if a move made by a player is valid, false otherwise
        ModelLine[][] lines = lineType.equals(LineType.horizontal) ? gameBoard.getHorizontalLines() : gameBoard.getVerticalLines();
        ModelLine line = lines[row][column];
        if (!line.isConnected()) {
            line.connectLine();
            line.setOwner(turn);
            int scoreObtained = gameBoard.checkBoxFormed(line);
            if (turn == PlayerNumber.first)
                first.setScore(first.getScore() + scoreObtained);
            else
                second.setScore(second.getScore() + scoreObtained);
            if (scoreObtained == 0) {
                swapTurn();
            }
            MoveResult result = !isGameOver() ? MoveResult.gameOver : MoveResult.valid;
            PropertyChangeEvent event = new PropertyChangeEvent(this,"performMove",line,result);
            pcs.firePropertyChange(event);
            return result;
        }
        return MoveResult.invalid;
    }

    public Pair<Integer,String> getWinner() { // returns 0 if somebody won, 1 if a tie occurred. if a tie occurs a blank name is returned, otherwise the winners name is returned
        if (first.getScore() == second.getScore()) {
            return new Pair<>(1,"");
        }
        else if (first.getScore() > second.getScore()) {
            return new Pair<>(0,first.getName());
        }
        return new Pair<>(0,second.getName());
    }

    public void insertNamesAndGrid(String p1Name,String p2Name,String gridSize) {
        boolean valid = true;
        int number;

        try {
            number = Integer.parseInt(gridSize);
        }
        catch (NumberFormatException e) {
            number = 0;
        }

        if (p1Name.isBlank() || p2Name.isBlank() || number <= 1) {
            valid = false;
        } else {
            first.setName(p1Name);
            second.setName(p2Name);
            setGameBoard(new Board(number));
        }

        PropertyChangeEvent event = new PropertyChangeEvent(this,"insertNamesAndGrid","",valid);
        pcs.firePropertyChange(event);
    }
    // registers and deletes observer listeners
    public void addPropertyChangeListener(PropertyChangeListener listener) {pcs.addPropertyChangeListener(listener);}
    public void removePropertyChangeListener(PropertyChangeListener listener) {pcs.removePropertyChangeListener(listener);}

    // getters
    public PlayerNumber getTurn() {return turn;}
    public GameType getGameType() {return gameType;}
    public Board getGameBoard() {return gameBoard;}
    public Player getSecond() {return second;}
    public Player getFirst() {return first;}
    public Player getCurrent() {return turn == PlayerNumber.first ? first: second;}

    // setters
    public void setGameType(GameType type) {this.gameType = type;}
    public void setGameBoard(Board gameBoard) {this.gameBoard = gameBoard;}
    public void setFirst(Player first) {this.first = first;}
    public void setSecond(Player second) {this.second = second;}
}
