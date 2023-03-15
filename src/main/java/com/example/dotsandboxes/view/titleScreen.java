package com.example.dotsandboxes.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class TitleScreen extends Application {
    private Label title; // window title
    private Button HVH; // button the enters a HVH game
    private Button HVA; // button the enters a HVA game
    private Button AVA; // button the enters a AVA game
    private int sceneX; // x-axis size of the app window
    private int sceneY; //y-axis size of the app window

    public TitleScreen(int sceneX,int sceneY) { // constructor
        this.title = new Label("Dots&Boxes");
        this.HVH = new Button("Human Vs Human");
        this.HVA = new Button("Human Vs Ai");
        this.AVA = new Button("Ai Vs Ai");
        this.sceneX = sceneX;
        this.sceneY = sceneY;
    }

    @Override
    public void start(Stage stage) throws Exception { // sets up the window scene
        VBox root = new VBox(title, HVH, HVA, AVA);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);

        Scene scene = new Scene(root, sceneX, sceneY); // sets the scene
        stage.setScene(scene);
        stage.show();
    }

    // getters
    public Label getTitle() {return title;}
    public Button getHVH() {return HVH;}
    public Button getHVA() {return HVA;}
    public Button getAVA() {return AVA;}
    public int getSceneX() {return sceneX;}
    public int getSceneY() {return sceneY;}
}
