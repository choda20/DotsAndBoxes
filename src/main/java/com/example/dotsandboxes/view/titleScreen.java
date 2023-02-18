package com.example.dotsandboxes.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class TitleScreen extends Application {
    private Label title;
    private Button HVH;
    private Button HVA;
    private Button AVA;
    private int sceneX;
    private int sceneY;

    public TitleScreen(int sceneX,int sceneY) {
        this.title = new Label("Dots&Boxes");
        this.HVH = new Button("Human Vs Human");
        this.HVA = new Button("Human Vs Ai");
        this.AVA = new Button("Ai Vs Ai");
        this.sceneX = sceneX;
        this.sceneY = sceneY;
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox(title, HVH, HVA, AVA);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);

        Scene scene = new Scene(root, sceneX, sceneY); // sets the scene
        stage.setScene(scene);
        stage.show();
    }

    public Label getTitle() {return title;}
    public Button getHVH() {return HVH;}
    public Button getHVA() {return HVA;}
    public Button getAVA() {return AVA;}
    public int getSceneX() {return sceneX;}
    public int getSceneY() {return sceneY;}
}
