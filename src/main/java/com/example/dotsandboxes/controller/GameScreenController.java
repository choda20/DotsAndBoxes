package com.example.dotsandboxes.controller;

import com.example.dotsandboxes.model.classes.Game;
import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.MoveResult;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import com.example.dotsandboxes.view.GameScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class GameScreenController implements PropertyChangeListener {
    private Game model; // the game model
    private GameScreen view; // the game screen view
    private int gridSize; // the grid size

    public GameScreenController(Game model, GameScreen view, Stage stage) throws Exception { // constructor
        this.model = model;
        this.view = view;
        this.gridSize = model.getGameBoard().getGridSize();

        double startingX = view.getSceneX()/2-(gridSize*50)/2; // will be used to center the board
        double startingY = view.getSceneY()/2-(gridSize*50)/2; // same as startingX

        model.addPropertyChangeListener(this); // registers the controller as a listener to the model
        buildViewBoard(startingX,startingY);
        enableAllLines();
        setLabels(view.getLabels(),startingX,startingY); // initializes labels
        view.start(stage);
    }

    public void setMouseSettings(Line[][] lines, LineType lineType) { // sets up line reactions to mouse events
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                lines[i][j].setStroke(Color.TRANSPARENT);
                lines[i][j].setStrokeWidth(5);
                final int row = i,column = j;
                lines[i][j].setOnMouseClicked((mouseEvent -> {
                    Line clickedLine = (Line) mouseEvent.getSource();
                    makeMove(clickedLine,row,column,lineType);
                }));
                lines[i][j].setOnMouseEntered((mouseEvent -> {
                    Line hoveredLine = (Line) mouseEvent.getSource();
                    if (hoveredLine.getStroke() != Color.RED && hoveredLine.getStroke() != Color.BLUE) {
                        hoveredLine.setStroke(Color.YELLOW);
                    }
                }));
                lines[i][j].setOnMouseExited((mouseEvent -> {
                    Line hoveredLine = (Line) mouseEvent.getSource();
                    if (hoveredLine.getStroke() != Color.RED && hoveredLine.getStroke() != Color.BLUE) {
                        hoveredLine.setStroke(Color.TRANSPARENT);
                    }
                }));
            }
        }
    }
    public void setLabels(Label[] labels,double startingX,double startingY) { // configures the screen labels
        // shows current player
        labels[0] = new Label();
        labels[0].setText(model.getCurrent().getName() + "'s turn");
        labels[0].setLayoutX(startingX-labels[0].getWidth()/3);
        labels[0].setLayoutY(startingY-startingY/2);
        labels[0].setAlignment(Pos.CENTER);
        labels[0].setStyle("-fx-font-size: 50px;");
        labels[0].setTextFill(Color.RED);

        // player 1's score
        labels[1] = new Label();
        labels[1].setText(model.getFirst().getName() + "'s score: " + model.getFirst().getScore());
        labels[1].setLayoutX(startingX-labels[1].getWidth()/3);
        labels[1].setLayoutY(startingY-startingY/2+ 75);
        labels[1].setAlignment(Pos.CENTER);
        labels[1].setStyle("-fx-font-size: 30px;");
        labels[1].setTextFill(Color.RED);

        // player 2's score
        labels[2] = new Label();
        labels[2].setText(model.getSecond().getName() + "'s score:  " + model.getSecond().getScore());
        labels[2].setLayoutX(startingX-labels[1].getWidth()/3);
        labels[2].setLayoutY(startingY-startingY/2 + 125);
        labels[2].setAlignment(Pos.CENTER);
        labels[2].setStyle("-fx-font-size: 30px;");
        labels[2].setTextFill(Color.BLUE);
    }

    public void makeMove(Line clickedLine,int row,int column,LineType lineType) {
        disableLine(clickedLine);
        model.performMove(row,column,lineType);
    }
    public void buildViewBoard(double sceneX,double sceneY) { // initializes the game board
        initializeLines(sceneX,sceneY,20);
        initializeDots(sceneX,sceneY,20);
    }
    // matrix initializers
    public void initializeLines(double startingX,double startingY, int padding) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                view.getHorizontalLines()[i][j] = new Line(startingX+(j*75) + 7,startingY+(i*75)+padding,startingX+((j+1)*75) - 7,startingY+((i)*75)+padding);
                view.getVerticalLines()[i][j] = new Line(startingY+(i*75),startingY+(j*75)+ 7 + padding,startingY+(i*75),startingY+((j+1)*75)- 7+ padding);
            }
        }
    }
    public void initializeDots(double startingX,double startingY, int padding) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize; j++) {
                view.getDots()[i][j] = new Circle(startingX+(j*75),startingY+(i*75)+padding,5);
            }
        }
    }
    public void disableAllLines() { // disables all mouse events
        for (int i=0;i<gridSize;i++) {
            for(int j=0;j<gridSize-1;j++) {
                view.getHorizontalLines()[i][j].setOnMouseClicked(event -> {});
                view.getHorizontalLines()[i][j].setOnMouseEntered(event -> {});
                view.getHorizontalLines()[i][j].setOnMouseExited(event -> {});
                view.getVerticalLines()[i][j].setOnMouseClicked(event -> {});
                view.getVerticalLines()[i][j].setOnMouseEntered(event -> {});
                view.getVerticalLines()[i][j].setOnMouseExited(event -> {});
            }
        }
    }
    public void disableLine(Line line) {
        line.setOnMouseClicked(event -> {});
        line.setOnMouseEntered(event -> {});
        line.setOnMouseExited(event -> {});
    }
    public void enableAllLines() {
        setMouseSettings(view.getHorizontalLines(),LineType.horizontal);
        setMouseSettings(view.getVerticalLines(),LineType.vertical);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ModelLine changedLine = (ModelLine) evt.getOldValue();
        MoveResult result = (MoveResult) evt.getNewValue();
        Color lineColor = changedLine.getOwner().equals(PlayerNumber.first) ? Color.RED : Color.BLUE;
        Color turnColor = model.getTurn().equals(PlayerNumber.first) ? Color.RED : Color.BLUE;

        if(changedLine.isHorizontal()) {
            view.getHorizontalLines()[changedLine.getRow()][changedLine.getColumn()].setStroke(lineColor);
        }
        else {
            view.getVerticalLines()[changedLine.getRow()][changedLine.getColumn()].setStroke(lineColor);
        }

        view.getLabels()[1].setText(model.getFirst().getName() + "'s score: " + model.getFirst().getScore());
        view.getLabels()[2].setText(model.getSecond().getName() + "'s score:  " + model.getSecond().getScore());
        if (result == MoveResult.gameOver) {
            Pair<Integer,String> results = model.getWinner();
            if (results.getKey().intValue() == 0) {
                view.getLabels()[0].setText(results.getValue() + " Won!");
            }
            else {
                view.getLabels()[0].setText("It's A Tie!");
            }
        }
        else {
            view.getLabels()[0].setText(model.getCurrent().getName() + "'s turn");
            view.getLabels()[0].setTextFill(turnColor);
        }
    }
}
