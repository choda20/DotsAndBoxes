package com.example.dotsandboxes.view;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class titleScreen {
    private Label title;
    private Button HVH;
    private Button HVA;
    private Button AVA;

    public titleScreen() {
        this.title = new Label("Dots&Boxes");
        this.HVH = new Button("Human Vs Human");
        this.HVA = new Button("Human Vs Ai");
        this.AVA = new Button("Ai Vs Ai");
        VBox root = new VBox();
        root.getChildren().addAll(title, HVH, HVA, AVA);
    }

    public 
}
