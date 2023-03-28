package com.example.dotsandboxes.view;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;


public class GameScreen extends Application {
    private Circle[][] dots; // gridSize*gridSize matrix containing all dots in the game
    private Line[][] horizontalLines; // (gridSize)*(gridSize-1) matrix containing all the horizontal lines in the game
    private Line[][] verticalLines; // (gridSize)*(gridSize-1) matrix containing all the vertical  lines in the game
    private int gridSize;
    private Label[] labels; // holds on-screen text, 0 - for current turn, 1 - for player 1 score, 2 - for player 2 score
    private Background background;

    public GameScreen() {} // empty constructor
    public GameScreen(int gridSize, Background background) { // full constructor
        this.gridSize = gridSize;
        this.labels = new Label[3]; // 0 - for current turn, 1 - for player 1 score, 2 - for player 2 score
        this.horizontalLines = new Line[gridSize][gridSize-1]; // array of horizontal lines
        this.verticalLines = new Line[gridSize][gridSize-1]; // array of vertical lines
        this.dots = new Circle[gridSize][gridSize]; // array of dots
        this.background = background;
    }

    @Override
    public void start(Stage stage) throws Exception { // sets up the stage
        VBox root = new VBox(); // sets the root of the scene
        root.setBackground(background);

        VBox labelContainer = new VBox(labels[0],labels[1],labels[2]);
        labelContainer.setSpacing(10);



        Scene scene = new Scene(root,stage.getWidth(),stage.getHeight()); // sets the scene
        stage.setScene(scene);
        stage.show();
    }

    /*
    public void addChildrenToRoot(Group root) { // adds all ui elements to the stage root
        root.getChildren().addAll(labels);
        for(int i=0;i<gridSize;i++) {
            root.getChildren().addAll(dots[i]);
            root.getChildren().addAll(horizontalLines[i]);
            root.getChildren().addAll(verticalLines[i]);
        }
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
     */

    // getters
    public Label[] getLabels() {return labels;}
    public Circle[][] getDots() {return dots;}
    public Line[][] getHorizontalLines() {return horizontalLines;}
    public Line[][] getVerticalLines() {return verticalLines;}
}
