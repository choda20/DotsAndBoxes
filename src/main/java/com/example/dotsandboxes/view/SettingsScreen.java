package com.example.dotsandboxes.view;

import com.example.dotsandboxes.controller.GameScreenController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.function.Function;

public class SettingsScreen extends Application {
    private Label title;
    private Label errorText;
    private Label p1Name;
    private Label p2Name;
    private Label gridSize;
    private TextField p1Field;
    private TextField p2Field;
    private TextField gridField;
    private Button moveToGame;
    private int sceneX;
    private int sceneY;

    public SettingsScreen(int sceneX,int sceneY) {
        this.title = new Label("Settings");
        this.p1Name = new Label("Player 1 name: ");
        this.errorText = new Label("Invalid Values!");
        this.p2Name = new Label("Player 2 name: ");
        this.gridSize = new Label("GridSize(NxN): ");
        this.p1Field = new TextField();
        this.p2Field = new TextField();
        this.gridField = new TextField();
        this.moveToGame = new Button("Begin Game!");
        this.sceneX = sceneX;
        this.sceneY = sceneY;
    }

    public void start(Stage stage) throws Exception {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(25);
        root.setVgap(25);

        root.add(title,0,0,2,1);
        root.add(p1Name,0,1);
        root.add(p1Field,1,1);
        root.add(p2Name,0,2);
        root.add(p2Field,1,2);
        root.add(gridSize,0,3);
        root.add(gridField,1,3);
        root.add(moveToGame,0,4);
        root.add(errorText,0,5);

        Scene scene = new Scene(root, sceneX, sceneY); // sets the scene
        stage.setScene(scene);
        stage.show();
    }

    public int getSceneX() {return sceneX;}
    public int getSceneY() {return sceneY;}
    public int getGridInput() {
        int number;
        try {
            number = Integer.parseInt(gridField.getText());
        }
        catch (NumberFormatException e) {
            number = 0;
        }
        return number;
    }
    public String getP1Input() {return p1Field.getText();}
    public String getP2Input() {return p2Field.getText();}
    public Label getErrorText() {return errorText;}
    public Label getGridSize() {return gridSize;}
    public Label getP1Name() {return p1Name;}
    public Label getP2Name() {return p2Name;}
    public Label getTitle() {return title;}
    public TextField getGridField() {return gridField;}
    public TextField getP1Field() {return p1Field;}
    public TextField getP2Field() {return p2Field;}

    public Button getMoveToGame() {return moveToGame;}
}
