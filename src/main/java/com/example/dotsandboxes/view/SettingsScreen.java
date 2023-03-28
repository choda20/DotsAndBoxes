package com.example.dotsandboxes.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;


public class SettingsScreen extends Application {
    private Label title; // windows title
    private Label errorText; // error text that shows up when the user enters invalid values
    private Label p1Name; // p1Field title
    private Label p2Name; // p2Field title
    private Label gridSize; // gridField title
    private TextField p1Field; // field that gets the name of the first player as an input
    private TextField p2Field; // field that gets the name of the second player as an input
    private TextField gridField; // field that gets the grid size as an input
    private Button moveToGame; // moves the stage to the game screen
    private Background background;

    public SettingsScreen(Background background) { // constructor
        this.title = new Label("Settings");
        this.p1Name = new Label("Player 1 name: ");
        this.errorText = new Label("Invalid Values!");
        this.p2Name = new Label("Player 2 name: ");
        this.gridSize = new Label("GridSize(NxN): ");
        this.p1Field = new TextField();
        this.p2Field = new TextField();
        this.gridField = new TextField();
        this.moveToGame = new Button("Begin Game!");
        this.background = background;
    }

    public void start(Stage stage) throws Exception { // sets up the stage and shows the screen
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setBackground(background);
        root.setSpacing(25);

        GridPane inputFields = new GridPane();
        inputFields.setAlignment(Pos.CENTER);
        inputFields.setHgap(25);
        inputFields.setVgap(25);
        inputFields.add(p1Name,0,1);
        inputFields.add(p1Field,1,1);
        inputFields.add(p2Name,0,2);
        inputFields.add(p2Field,1,2);
        inputFields.add(gridSize,0,3);
        inputFields.add(gridField,1,3);

        root.getChildren().addAll(title,inputFields,moveToGame,errorText);
        Scene scene = new Scene(root); // sets the scene
        stage.setScene(scene);
        stage.show();
    }

    // getters
    public String getP1Input() {return p1Field.getText();}
    public String getP2Input() {return p2Field.getText();}
    public String getGridInput() {return gridField.getText();}
    public TextField getGridField() {return gridField;}
    public TextField getP1Field() {return p1Field;}
    public TextField getP2Field() {return p2Field;}
    public Label getErrorText() {return errorText;}
    public Label getGridSize() {return gridSize;}
    public Label getP1Name() {return p1Name;}
    public Label getP2Name() {return p2Name;}
    public Label getTitle() {return title;}
    public Button getMoveToGame() {return moveToGame;}
    public Background getBackground() {return background;}

}
