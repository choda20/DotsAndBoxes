package com.example.dotsandboxes.controller;

import com.example.dotsandboxes.model.classes.Board;
import com.example.dotsandboxes.model.classes.Game;
import com.example.dotsandboxes.view.GameScreen;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class GameScreenController {
    private Game model;
    private GameScreen view;
    private int gridSize;

    public GameScreenController(Game model, GameScreen view, Stage stage) throws Exception {
        this.model = model;
        this.view = view;
        this.gridSize = model.getGameBoard().getGridSize();

        double startingX = view.getSceneX()/2-(gridSize*50)/2; // will be used to center the board
        double startingY = view.getSceneY()/2-(gridSize*50)/2; // same as startingX
        Board gameBoard = model.getGameBoard();

        setLabels(view.getLabels(),startingX,startingY); // initializes labels
        setDots(gameBoard.getDots(),startingX,startingY,20); // initializes dots
        setLines(gameBoard.getHorizontalLines(),gameBoard.getVerticalLines(),startingX,startingY,20); // initializes lines

        view.start(stage);
    }
    public void setDots(Circle[][] dots,double startingX,double startingY, int padding) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize; j++) {
                dots[i][j] = new Circle(startingX+(j*75),startingY+(i*75)+padding,5);
            }
        }
    }
    public void setLines(Line[][] horizontalLines,Line[][] verticalLines,double startingX,double startingY, int padding) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize-1; j++) {
                horizontalLines[i][j] = new Line(startingX+(j*75) + 7,startingY+(i*75)+padding,startingX+((j+1)*75) - 7,startingY+((i)*75)+padding);
                setMouseSettings(horizontalLines[i][j]);
                verticalLines[i][j] = new Line(startingY+(i*75),startingY+(j*75)+ 7 + padding,startingY+(i*75),startingY+((j+1)*75)- 7+ padding);
                setMouseSettings(verticalLines[i][j]);
            }
        }
    }
    public void setMouseSettings(Line line) {
        line.setStroke(Color.TRANSPARENT);
        line.setStrokeWidth(5);
        line.setOnMouseClicked((mouseEvent -> {
            Line clickedLine = (Line) mouseEvent.getSource();
            clickedLine.setStroke(Color.RED);
        }));
        line.setOnMouseEntered((mouseEvent -> {
            Line hoveredLine = (Line) mouseEvent.getSource();
            if (hoveredLine.getStroke() != Color.RED) {
                hoveredLine.setStroke(Color.YELLOW);
            }
        }));
        line.setOnMouseExited((mouseEvent -> {
            Line hoveredLine = (Line) mouseEvent.getSource();
            if (hoveredLine.getStroke() != Color.RED) {
                hoveredLine.setStroke(Color.TRANSPARENT);
            }
        }));
    }
    public void setLabels(Label[] labels,double startingX,double startingY) {
        labels[0] = new Label();
        labels[0].textProperty().bind(Bindings.concat(model.currentPlayer().getName(),view.getStaticTurnText()));
        labels[0].setLayoutX(startingX-labels[0].getWidth()/3);
        labels[0].setLayoutY(startingY-startingY/2);
        labels[0].setAlignment(Pos.CENTER);
        labels[0].setStyle("-fx-font-size: 50px;");
        labels[0].setTextFill(Color.BLUE);
        labels[1] = new Label();
        labels[1].textProperty().bind(Bindings.concat(view.getStaticScoreTextP1(),model.getFirst().getScore()));
        labels[1].setLayoutX(startingX-labels[1].getWidth()/3);
        labels[1].setLayoutY(startingY-startingY/2+ 75);
        labels[1].setAlignment(Pos.CENTER);
        labels[1].setStyle("-fx-font-size: 30px;");
        labels[1].setTextFill(Color.BLUE);
        labels[2] = new Label();
        labels[2].textProperty().bind(Bindings.concat(view.getStaticScoreTextP2(),model.getSecond().getScore()));
        labels[2].setLayoutX(startingX-labels[1].getWidth()/3);
        labels[2].setLayoutY(startingY-startingY/2 + 125);
        labels[2].setAlignment(Pos.CENTER);
        labels[2].setStyle("-fx-font-size: 30px;");
        labels[2].setTextFill(Color.RED);
    }
}
