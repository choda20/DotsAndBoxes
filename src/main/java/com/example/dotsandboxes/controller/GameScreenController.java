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
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
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
    private LinearGradient p1Gradient;
    private LinearGradient p2Gradient;


    public GameScreenController(Game model, GameScreen view, Stage stage) throws Exception { // constructor
        this.model = model;
        this.view = view;
        this.gridSize = model.getGameBoard().getGridSize();
        Stop[] stopsP1 = new Stop[] { new Stop(0, Color.web("#FE0944")), new Stop(1, Color.web("#FEAE96")) };
        this.p1Gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stopsP1);
        Stop[] stopsP2 = new Stop[] { new Stop(0, Color.web("#008FFD")), new Stop(1, Color.web("#2A2A72")) };
        this.p2Gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stopsP2);

        model.addPropertyChangeListener(this); // registers the controller as a listener to the model

        setLabels(view.getLabels()); // initializes labels
        buildViewBoard(stage.getWidth(),stage.getHeight());
        enableAllLines();

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
                    registerMove(clickedLine,row,column,lineType);
                }));
                lines[i][j].setOnMouseEntered((mouseEvent -> {
                    Line hoveredLine = (Line) mouseEvent.getSource();
                    if (hoveredLine.getStroke() != p1Gradient && hoveredLine.getStroke() != p2Gradient) {
                        Stop[] stopsHovered = new Stop[] { new Stop(0, Color.web("#FBD72B")), new Stop(1, Color.web("#F9484A")) };
                        hoveredLine.setStroke(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stopsHovered));
                    }
                }));
                lines[i][j].setOnMouseExited((mouseEvent -> {
                    Line hoveredLine = (Line) mouseEvent.getSource();
                    if (hoveredLine.getStroke() != p1Gradient && hoveredLine.getStroke() != p2Gradient) {
                        hoveredLine.setStroke(Color.TRANSPARENT);
                    }
                }));
            }
        }
    }
    public void registerMove(Line clickedLine, int row, int column, LineType lineType) {
        disableLine(clickedLine);
        model.performMove(row,column,lineType);
    }
    public void setLabels(Label[] labels) { // configures the screen labels
        // shows current player
        labels[0] = new Label();
        labels[0].setText(model.getCurrent().getName() + "'s turn");
        labels[0].setAlignment(Pos.CENTER);
        labels[0].setStyle("-fx-font-size: 75px;");
        labels[0].setTextFill(p1Gradient);

        // player 1's score
        labels[1] = new Label();
        labels[1].setText(model.getFirst().getName() + "'s score: " + model.getFirst().getScore());
        labels[1].setAlignment(Pos.CENTER);
        labels[1].setStyle("-fx-font-size: 40px;");
        labels[1].setTextFill(p1Gradient);

        // player 2's score
        labels[2] = new Label();
        labels[2].setText(model.getSecond().getName() + "'s score:  " + model.getSecond().getScore());
        labels[2].setAlignment(Pos.CENTER);
        labels[2].setStyle("-fx-font-size: 40px;");
        labels[2].setTextFill(p2Gradient);
    }

    public void buildViewBoard(double width,double height) { // initializes the game board
        double constraint = Math.min(width,height);
        double spaceBetweenDots = (constraint/gridSize)/2;
        double startingHeight = height/2;
        double startingWidth = width/4;
        double dotRadius = 7.5;
        initializeLinesDots(startingWidth,startingHeight,spaceBetweenDots,dotRadius);
    }
    // matrix initializers
    public void initializeLinesDots(double startingX,double startingY, double SBD,double dotRadius) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                view.getHorizontalLines()[i][j] = new Line(startingX+(j*SBD)+dotRadius,startingY+(i*SBD),startingX+((j+1)*SBD)- dotRadius,startingY+((i)*SBD));
                view.getVerticalLines()[i][j] = new Line(startingX+(i*SBD),startingY+(j*SBD)+dotRadius,startingX+(i*SBD),startingY+((j+1)*SBD)-dotRadius);
                view.getDots()[i][j] = new Circle(startingX+(j*SBD),startingY+(i*SBD),dotRadius);
            }
            view.getDots()[i][gridSize-1] = new Circle(startingX+((gridSize-1)*SBD),startingY+(i*SBD),dotRadius);
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

        LinearGradient lineColor = changedLine.getOwner().equals(PlayerNumber.first) ? p1Gradient : p2Gradient;
        LinearGradient turnColor = model.getTurn().equals(PlayerNumber.first) ? p1Gradient : p2Gradient;
        Line[][] lineMatrix = changedLine.isHorizontal() ? view.getHorizontalLines() : view.getVerticalLines();
        lineMatrix[changedLine.getRow()][changedLine.getColumn()].setStroke(lineColor);
        disableLine(lineMatrix[changedLine.getRow()][changedLine.getColumn()]);

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
