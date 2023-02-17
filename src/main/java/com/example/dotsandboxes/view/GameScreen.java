package com.example.dotsandboxes.view;

import com.example.dotsandboxes.model.classes.Player;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.Arrays;

public class GameScreen extends Application {
    private int gridSize;
    private int padding;
    private StringProperty player1;
    private StringProperty player2;
    private StringProperty currentPlayer;
    private IntegerProperty p1Score;
    private IntegerProperty p2Score;
    private StringProperty staticTurnText;
    private StringProperty staticScoreTextP1;
    private StringProperty staticScoreTextP2;

    public GameScreen() {}
    public GameScreen(int gridSize, Player p1,Player p2) {
        this.gridSize = gridSize;
        this.padding = 20;
        this.player1 = new SimpleStringProperty(p1.getName());
        this.player2 =  new SimpleStringProperty(p2.getName());;
        this.currentPlayer = player1;
        this.p1Score = new SimpleIntegerProperty(p1.getScore());
        this.p2Score = new SimpleIntegerProperty(p2.getScore());
        this.staticTurnText = new SimpleStringProperty("'s turn");
        this.staticScoreTextP1 = new SimpleStringProperty(player1.getValue() + "'s score:");
        this.staticScoreTextP2 = new SimpleStringProperty(player2.getValue() + "'s score:");
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group(); // sets the root of the scene
        Scene scene = new Scene(root, 1000, 1000); // sets the scene
        double startingX = scene.getWidth()/2-(gridSize*50)/2; // will be used to center the board
        double startingY = scene.getHeight()/2-(gridSize*50)/2; // same as startingX

        Line[][] horizontalLines = new Line[gridSize][gridSize-1]; // array of horizontal lines
        Line[][] verticalLines = new Line[gridSize][gridSize-1]; // array of vertical lines
        Circle[][] dots = new Circle[gridSize][gridSize]; // array of dots
        Label[] labels = new Label[3]; // 0 - for current turn, 1 - for player 1 score, 2 - for player 2 score

        setLabels(labels,startingX,startingY);
        setDots(dots,startingX,startingY,padding); // initializes dots
        setLines(horizontalLines,verticalLines,startingX,startingY,padding); // initializes lines

        addChildrenToRoot(root,labels,dots,horizontalLines,verticalLines); // adds lines and dots to the scene
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    public void addChildrenToRoot(Group root,Label[] labels, Circle[][] dots, Line[][] horizontalLines, Line[][] verticalLines) {
        root.getChildren().addAll(labels);
        for(int i=0;i<gridSize;i++) {
            root.getChildren().addAll(dots[i]);
            root.getChildren().addAll(horizontalLines[i]);
            root.getChildren().addAll(verticalLines[i]);
        }
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
        labels[0].textProperty().bind(Bindings.concat(currentPlayer,staticTurnText));
        labels[0].setLayoutX(startingX-labels[0].getWidth()/3);
        labels[0].setLayoutY(startingY-startingY/2);
        labels[0].setAlignment(Pos.CENTER);
        labels[0].setStyle("-fx-font-size: 50px;");
        labels[0].setTextFill(Color.BLUE);
        labels[1] = new Label();
        labels[1].textProperty().bind(Bindings.concat(staticScoreTextP1,p1Score.getValue()));
        labels[1].setLayoutX(startingX-labels[1].getWidth()/3);
        labels[1].setLayoutY(startingY-startingY/2+ 75);
        labels[1].setAlignment(Pos.CENTER);
        labels[1].setStyle("-fx-font-size: 30px;");
        labels[1].setTextFill(Color.BLUE);
        labels[2] = new Label();
        labels[2].textProperty().bind(Bindings.concat(staticScoreTextP2,p2Score.getValue()));
        labels[2].setLayoutX(startingX-labels[1].getWidth()/3);
        labels[2].setLayoutY(startingY-startingY/2 + 125);
        labels[2].setAlignment(Pos.CENTER);
        labels[2].setStyle("-fx-font-size: 30px;");
        labels[2].setTextFill(Color.RED);
    }

}
