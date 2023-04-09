package com.example.dotsandboxes.model.classes;

import com.example.dotsandboxes.AI.AIPlayer;
import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.MoveResult;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import javafx.util.Pair;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class Game {
    private PropertyChangeSupport pcs; // variable
    private Player first; // represents player 1
    private Player second; //represents player 2
    private PlayerNumber turn; // represents the current player, first for first and second for second
    private GameType gameType; // represents the type of the game(HVH,HVA,AVA)
    private Board gameBoard; //represents the board

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

    private boolean isGameOver() { // returns true if the game is in progress, otherwise false
        return !((first.getScore() + second.getScore()) == (gameBoard.getGridSize()-1)*(gameBoard.getGridSize()-1));}

    private void swapTurn() {
        turn = turn == PlayerNumber.first ? PlayerNumber.second : PlayerNumber.first;
    } // moves to next turn

    public void performMove(int row, int column, LineType lineType) throws CloneNotSupportedException {
        ModelLine[][] lines = lineType.equals(LineType.horizontal) ? gameBoard.getHorizontalLines() : gameBoard.getVerticalLines();
        ModelLine line = lines[row][column];
        if (!line.isConnected()) {
            line.connectLine();
            int scoreObtained = gameBoard.checkBoxFormed(line);
            getCurrent().setScore(getCurrent().getScore() + scoreObtained);
            MoveResult result = !isGameOver() ? MoveResult.gameOver : MoveResult.valid;
            PropertyChangeEvent event = new PropertyChangeEvent(this,"performMove",new Pair<ModelLine,PlayerNumber>(line,turn),result);
            if (scoreObtained == 0) {swapTurn();}
            pcs.firePropertyChange(event);
            if (gameType.equals(GameType.HumanVsAI) && turn.equals(PlayerNumber.second) && !result.equals(MoveResult.gameOver)) {
                Pair<Point,LineType> move = getCurrent().play();
                performMove(move.getKey().x,move.getKey().y,move.getValue());
            }
        }
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

        try {number = Integer.parseInt(gridSize);}
        catch (NumberFormatException e) {number = 0;}
        if (p1Name.isBlank() || p2Name.isBlank() || number <= 1 || number > 10) {
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
