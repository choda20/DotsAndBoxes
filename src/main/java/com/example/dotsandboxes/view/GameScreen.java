package com.example.dotsandboxes.view;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;


public class GameScreen extends Application {
    private Circle[][] dots; // gridSize*gridSize matrix containing all dots in the game
    private Line[][] horizontalLines; // (gridSize)*(gridSize-1) matrix containing all the horizontal lines in the game
    private Line[][] verticalLines; // (gridSize)*(gridSize-1) matrix containing all the vertical  lines in the game
    private int gridSize;
    private int sceneX; //x-axis size of the app window
    private int sceneY; //y-axis size of the app window
    private Label[] labels; // holds on-screen text, 0 - for current turn, 1 - for player 1 score, 2 - for player 2 score


    public GameScreen() {} // empty constructor
    public GameScreen(int sceneX, int sceneY, int gridSize) { // full constructor
        this.sceneX = sceneX;
        this.sceneY = sceneY;
        this.gridSize = gridSize;
        this.labels = new Label[3]; // 0 - for current turn, 1 - for player 1 score, 2 - for player 2 score
        this.horizontalLines = new Line[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new Line[gridSize][gridSize-1]; // array of vertical lines
        this.dots = new Circle[gridSize][gridSize]; // array of dots
    }

    @Override
    public void start(Stage stage) throws Exception { // sets up the stage
        Group root = new Group(); // sets the root of the scene
        Scene scene = new Scene(root, sceneX, sceneY); // sets the scene

        addChildrenToRoot(root); // adds lines and dots to the scene

        stage.setScene(scene);
        stage.show();
    }

    public void addChildrenToRoot(Group root) { // adds all ui elements to the stage root
        root.getChildren().addAll(labels);
        for(int i=0;i<gridSize;i++) {
            root.getChildren().addAll(dots[i]);
            root.getChildren().addAll(horizontalLines[i]);
            root.getChildren().addAll(verticalLines[i]);
        }
    }

    // getters
    public Label[] getLabels() {return labels;}
    public int getSceneX() {return sceneX;}
    public int getSceneY() {return sceneY;}
    public Circle[][] getDots() {return dots;}
    public Line[][] getHorizontalLines() {return horizontalLines;}
    public Line[][] getVerticalLines() {return verticalLines;}

}
