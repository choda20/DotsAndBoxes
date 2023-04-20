package com.example.dotsandboxes.controller;

import com.example.dotsandboxes.AI.AIPlayer;
import com.example.dotsandboxes.model.classes.Game;
import com.example.dotsandboxes.model.classes.HumanPlayer;
import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.view.SettingsScreen;
import com.example.dotsandboxes.view.TitleScreen;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;


public class TitleScreenController {
    private final TitleScreen view; // the screens view
    private final Game model; // the game model
    private final Stage stage; // the app window

    /**
     * full constructor. sets up button actions for all view buttons, styles all view buttons and labels,
     * and shows the app window
     * @param view the screen view containing all ui elements
     * @param model the game model containing the game data
     * @param stage the app windows in which the ui is displayed
     */
    public TitleScreenController(TitleScreen view, Game model, Stage stage) { // constructor
        this.model = model;
        this.view = view;
        this.stage = stage;

        setButtonActions(view.getHVH(),view.getHVA());
        setButtonStyles(new Button[]{view.getHVH(),view.getHVA()});
        setLabelStyle(view.getTitle());
        try {
            view.start(stage); // starts the application
        } catch (Exception e) {
            System.out.println("Title screen could not start.");
            System.exit(1);
        }
    }


    /**
     * function that moves the stage to the settings screen(moves to the settings screen)
     */
    private void moveToSettings()  { // moves the app to the settings screen after a button was pressed
        SettingsScreen settingsView = new SettingsScreen(view.getBackground());
        SettingsScreenController settingsController = new SettingsScreenController(settingsView,model,stage);
    }

    /**
     * function that styles the view title
     * @param title the title to be styled
     */
    private void setLabelStyle(Label title) { // styles the screen title
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-size: 100px;");
        Stop[] stops = new Stop[] { new Stop(0.4, Color.web("#CC0033")),new Stop(0.5, Color.rgb(0, 0, 255, 0.5)), new Stop(1, Color.BLUE) };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        title.setTextFill(gradient);
    }

    /**
     * function that styles all view buttons
     * @param buttons array of all view buttons
     */
    private void setButtonStyles(Button[] buttons) { // styles the Buttons on screen
        for (Button button : buttons) {
            button.setPadding(new Insets(10, 20, 10, 20));
            button.setPrefHeight(50);
            button.setPrefWidth(200);
            button.setStyle("-fx-background-color: radial-gradient(radius 50%, #FF3B61, #FE8373); -fx-background-radius: 50px; -fx-text-fill: white; -fx-font-size: 17;");
        }
    }

    /**
     * function that links button to their respective on-tap function
     * @param HVH human vs human game button
     * @param HVA human vs AI game button
     */
    private void setButtonActions(Button HVH, Button HVA) { // sets up button reactions to being pressed
        HVH.setOnAction(buttonEvent -> {
            try {
                handleHVH();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        HVA.setOnAction(buttonEvent -> {
            try {
                handleHVA();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * function that activates when human vs human button is pressed,
     * sets up the model to a HVH game and moves to the settings screen
     */
    private void handleHVH() {
        model.setGameType(GameType.HumanVsHuman);
        model.setFirst(new HumanPlayer());
        model.setSecond(new HumanPlayer());
        moveToSettings();
    }
    /**
     * function that activates when human vs AI button is pressed,
     * sets up the model to a HVA game and moves to the settings screen
     */
    private void handleHVA() {
        model.setGameType(GameType.HumanVsAI);
        model.setFirst(new HumanPlayer());
        model.setSecond(new AIPlayer());
        moveToSettings();
    }
}
