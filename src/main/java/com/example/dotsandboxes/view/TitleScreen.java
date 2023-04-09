package com.example.dotsandboxes.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.awt.*;


public class TitleScreen extends Application {
    private Label title; // window title
    private Button HVH; // button the enters a HVH game
    private Button HVA; // button the enters a HVA game
    private Background background; // background Image used by the screen

    /**
     * constructor that initializes all class variables
     * @param background the background image used by the screen
     */
    public TitleScreen(Background background) { // constructor
        this.title = new Label("Dots&Boxes");
        this.HVH = new Button("Human Vs Human");
        this.HVA = new Button("Human Vs Ai");
        this.background = background;
    }

    /**
     * function that sets up the app window and displays it to the user
     * @param stage the app window
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox(title, HVH, HVA);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setBackground(background);

        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        double height  = resolution.getHeight();
        double width = resolution.getWidth();
        Scale scale = new Scale(width/1920,height/1080,0,0);
        root.getTransforms().add(scale);

        Scene scene = new Scene(root); // sets the scene
        stage.setScene(scene);
        stage.show();
    }

    //general getters
    public Label getTitle() {return title;}
    public Button getHVH() {return HVH;}
    public Button getHVA() {return HVA;}
    public Background getBackground() {return background;}
}
