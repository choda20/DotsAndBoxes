package com.example.dotsandboxes.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class SettingsScreen extends Application {
    private final Label title; // the screen title
    private final Label errorText; // error text that shows up when
    // the user enters invalid values
    private final Label p1Name; // p1Field title
    private final Label p2Name; // p2Field title
    private final Label gridSizeTitle; // gridSize text
    private final TextField p1Field; // field that gets the name of
    // the first player as an input
    private final TextField p2Field; // field that gets the name of
    // the second player as an input
    private final ComboBox gridSizeBox; // drop-down that gets
    // the grid size as an input
    private final Button moveToGame; // button that moves the stage
    // to the game screen if all inputs are valid
    private final Background background; // background Image used by the screen

    /**
     * constructor that initializes all class variables
     * @param background the background image used by the screen
     */
    public SettingsScreen(Background background) { // constructor
        String[] gridSizes = {"2","3","4","5","6","7","8","9"};

        this.title = new Label("Settings");
        this.p1Name = new Label("Player 1 name: ");
        this.errorText = new Label("Invalid Values!");
        this.p2Name = new Label("Player 2 name: ");
        this.gridSizeTitle = new Label("Grid-Size(NxN): ");
        this.p1Field = new TextField();
        this.p2Field = new TextField();
        this.gridSizeBox = new ComboBox(
                FXCollections.observableArrayList(gridSizes));
        this.moveToGame = new Button("Begin Game!");
        this.background = background;
    }

    /**
     * function that sets up the app window and displays it to the user
     * @param stage the app window
     */
    public void start(Stage stage) { // sets up the stage
        // and shows the screen
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
        inputFields.add(gridSizeTitle,0,3);
        inputFields.add(gridSizeBox,1,3);

        root.getChildren().addAll(title,inputFields,moveToGame, errorText);
        Scene scene = new Scene(root); // sets the scene
        stage.setScene(scene);
        stage.show();
    }

    //general getters
    public String getP1Input() {return p1Field.getText();}
    public String getP2Input() {return p2Field.getText();}
    public String getGridInput() {return (String)gridSizeBox.getValue();}
    public TextField getP1Field() {return p1Field;}
    public TextField getP2Field() {return p2Field;}
    public Label getErrorText() {return errorText;}
    public Label getP1Name() {return p1Name;}
    public Label getP2Name() {return p2Name;}
    public Label getTitle() {return title;}
    public Label getGridSizeTitle() {return gridSizeTitle;}

    public Button getMoveToGame() {return moveToGame;}
    public Background getBackground() {return background;}
    public ComboBox getGridSizeBox() {return gridSizeBox;}
}
