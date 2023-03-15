package com.example.dotsandboxes.view;

import com.example.dotsandboxes.model.classes.Board;
import com.example.dotsandboxes.model.classes.Player;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class GameScreen extends Application {
    private Board gameBoard; // the game board
    private int sceneX; //x-axis size of the app window
    private int sceneY; //y-axis size of the app window
    private StringProperty player1; // contains the name of the first player
    private StringProperty player2; // contains the name of the second player
    private IntegerProperty p1Score; // contains the score of the first player
    private IntegerProperty p2Score; // contains the score of the second player
    private StringProperty staticTurnText; // current turn text
    private StringProperty staticScoreTextP1; // score text for player 1
    private StringProperty staticScoreTextP2; // score text for player 2
    private Label[] labels; // holds on screen text, 0 - for current turn, 1 - for player 1 score, 2 - for player 2 score


    public GameScreen() {} // empty constructor
    public GameScreen(Board gameBoard,Player p1,Player p2, int sceneX, int sceneY) { // full constructor
        this.gameBoard = gameBoard;
        this.sceneX = sceneX;
        this.sceneY = sceneY;
        this.labels = new Label[3]; // 0 - for current turn, 1 - for player 1 score, 2 - for player 2 score
        this.player1 = new SimpleStringProperty(p1.getName());
        this.player2 =  new SimpleStringProperty(p2.getName());;
        this.p1Score = new SimpleIntegerProperty(p1.getScore());
        this.p2Score = new SimpleIntegerProperty(p2.getScore());
        this.staticTurnText = new SimpleStringProperty("'s turn");
        this.staticScoreTextP1 = new SimpleStringProperty(player1.getValue() + "'s score:");
        this.staticScoreTextP2 = new SimpleStringProperty(player2.getValue() + "'s score:");
    }

    @Override
    public void start(Stage stage) throws Exception { // sets up the stage
        Group root = new Group(); // sets the root of the scene
        Scene scene = new Scene(root, sceneX, sceneY); // sets the scene

        addChildrenToRoot(root,labels,gameBoard.getDots(),gameBoard.getHorizontalLines(),gameBoard.getVerticalLines()); // adds lines and dots to the scene

        stage.setScene(scene);
        stage.show();
    }

    public void addChildrenToRoot(Group root, Label[] labels, Circle[][] dots, Line[][] horizontalLines, Line[][] verticalLines) { // adds all ui elements to the stage root
        root.getChildren().addAll(labels);
        for(int i=0;i<gameBoard.getGridSize();i++) {
            root.getChildren().addAll(dots[i]);
            root.getChildren().addAll(horizontalLines[i]);
            root.getChildren().addAll(verticalLines[i]);
        }
    }

    // getters
    public Label[] getLabels() {return labels;}
    public int getSceneX() {return sceneX;}
    public int getSceneY() {return sceneY;}
    public String getStaticScoreTextP1() {return staticScoreTextP1.get();}
    public String getStaticTurnText() {return staticTurnText.get();}
    public String getStaticScoreTextP2() {return staticScoreTextP2.get();}
}
