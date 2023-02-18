package com.example.dotsandboxes.controller;

import com.example.dotsandboxes.model.classes.Board;
import com.example.dotsandboxes.model.classes.Game;
import com.example.dotsandboxes.view.GameScreen;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import javax.security.auth.callback.LanguageCallback;
import java.util.Arrays;


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

        model.buildBoard(startingX,startingY);
        setMouseSettings(model.getGameBoard().getHorizontalLines());
        setMouseSettings(model.getGameBoard().getVerticalLines());
        setLabels(view.getLabels(),startingX,startingY); // initializes labels
        view.start(stage);
    }

    public void setMouseSettings(Line[][] lines) {
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                lines[i][j].setStroke(Color.TRANSPARENT);
                lines[i][j].setStrokeWidth(5);
                lines[i][j].setOnMouseClicked((mouseEvent -> {
                    Line clickedLine = (Line) mouseEvent.getSource();
                    model.checkValidMove(clickedLine);
                    updateScores(view.getLabels(),model.getFirst().getScore(),model.getSecond().getScore());
                }));
                lines[i][j].setOnMouseEntered((mouseEvent -> {
                    Line hoveredLine = (Line) mouseEvent.getSource();
                    if (hoveredLine.getStroke() != Color.RED) {
                        hoveredLine.setStroke(Color.YELLOW);
                    }
                }));
                lines[i][j].setOnMouseExited((mouseEvent -> {
                    Line hoveredLine = (Line) mouseEvent.getSource();
                    if (hoveredLine.getStroke() != Color.RED) {
                        hoveredLine.setStroke(Color.TRANSPARENT);
                    }
                }));
            }
        }
    }
    public void setLabels(Label[] labels,double startingX,double startingY) {
        labels[0] = new Label();
        labels[0].setText("itay" + view.getStaticTurnText());
        labels[0].setLayoutX(startingX-labels[0].getWidth()/3);
        labels[0].setLayoutY(startingY-startingY/2);
        labels[0].setAlignment(Pos.CENTER);
        labels[0].setStyle("-fx-font-size: 50px;");
        labels[0].setTextFill(Color.BLUE);
        labels[1] = new Label();
        labels[1].setText(view.getStaticScoreTextP1() + " " +model.getFirst().getScore());
        labels[1].setLayoutX(startingX-labels[1].getWidth()/3);
        labels[1].setLayoutY(startingY-startingY/2+ 75);
        labels[1].setAlignment(Pos.CENTER);
        labels[1].setStyle("-fx-font-size: 30px;");
        labels[1].setTextFill(Color.BLUE);
        labels[2] = new Label();
        labels[2].setText(view.getStaticScoreTextP2()+ " " + model.getSecond().getScore());
        labels[2].setLayoutX(startingX-labels[1].getWidth()/3);
        labels[2].setLayoutY(startingY-startingY/2 + 125);
        labels[2].setAlignment(Pos.CENTER);
        labels[2].setStyle("-fx-font-size: 30px;");
        labels[2].setTextFill(Color.RED);
    }

    public void updateScores(Label[] labels, int p1Score, int p2Score) {
        labels[1].setText(view.getStaticScoreTextP1() + " " + p1Score);
        labels[2].setText(view.getStaticScoreTextP2()+ " " + p2Score);
        if (!model.gameStatus()) {
            labels[0].setText(model.getWinner().getName() + " Won!");
        }
        else {
            labels[0].setText(model.getCurrent().getName() + view.getStaticTurnText());
        }
    }
}
