package com.example.dotsandboxes.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;


public class GameScreen extends Application {
    private Circle[][] dots; // gridSize*gridSize matrix containing
    // all dots in the game
    private Line[][] horizontalLines; // (gridSize)*(gridSize-1) matrix
    // containing all the horizontal lines in the game
    private Line[][] verticalLines; // (gridSize)*(gridSize-1) matrix
    // containing all the vertical  lines in the game
    private int gridSize; // square root of the size of the grid
    private Label[] labels; //array that holds on-screen text, 0 - for
    // current turn, 1 - for player 1 score, 2 - for player 2 score
    private Background background; // background Image used by the screen


    /**
     * constructor that initializes all class variables
     * @param gridSize the square root of the size of the grid
     * @param background the background image used by the screen
     */
    public GameScreen(int gridSize, Background background) { // full constructor
        this.gridSize = gridSize;
        this.labels = new Label[3]; // 0 - for current turn, 1 - for player
        // 1 score, 2 - for player 2 score
        this.horizontalLines = new Line[gridSize][gridSize-1];
        // array of horizontal lines
        this.verticalLines = new Line[gridSize][gridSize-1];
        // array of vertical lines
        this.dots = new Circle[gridSize][gridSize]; // array of dots
        this.background = background;
    }

    /**
     * function that sets up the app window and displays it to the user
     * @param stage the app window
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception { // sets up the stage
        VBox root = new VBox(); // sets the root of the scene
        root.setBackground(background);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(25);

        VBox labelContainer = new VBox(labels[0],labels[1],labels[2]);
        labelContainer.setAlignment(Pos.CENTER);
        labelContainer.setSpacing(15);
        Group matrix = new Group();
        addToMatrix(matrix);

        root.getChildren().addAll(labelContainer,matrix);
        Scene scene = new Scene(root); // sets the scene
        stage.setScene(scene);
        stage.show();
    }

    /**
     * function that adds all ui elements stored in an array to a
     * group
     * @param matrix the group to store the elements in
     */
    private void addToMatrix(Group matrix) {
        for (int i=0;i<gridSize;i++) {
            for(int j=0;j<gridSize-1;j++) {
                matrix.getChildren().addAll(dots[i][j],
                        horizontalLines[i][j],verticalLines[i][j]);
            }
            matrix.getChildren().add(dots[i][gridSize-1]);
        }
    }

    //general getters
    public Label[] getLabels() {return labels;}
    public Circle[][] getDots() {return dots;}
    public Line[][] getHorizontalLines() {return horizontalLines;}
    public Line[][] getVerticalLines() {return verticalLines;}
}
