package com.example.dotsandboxes.controller;

import com.example.dotsandboxes.model.classes.AIPlayer;
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
import javafx.stage.Stage;

public class TitleScreenController {
    private TitleScreen view; // screen view
    private Game model; // game model
    private Stage stage; // the app window
    public TitleScreenController(TitleScreen view, Game model, Stage stage) throws Exception { // constructor
        this.model = model;
        this.view = view;
        this.stage = stage;

        setButtonActions(view.getHVH(),view.getHVA(),view.getAVA());
        setButtonStyles(view.getHVH(),view.getHVA(),view.getAVA());
        setLabelStyle(view.getTitle());
        view.start(stage); // starts the application
    }


    private void moveToSettings() throws Exception { // moves the app to the settings screen after a button was pressed
        SettingsScreen settingsView = new SettingsScreen(view.getSceneX(), view.getSceneY());
        SettingsScreenController settingsController = new SettingsScreenController(settingsView,model,stage);
    }
    private void setLabelStyle(Label title) { // styles the screen title
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-size: 50px;");
        title.setTextFill(Color.BLUE);
    }
    private void setButtonStyles(Button HVH, Button HVA, Button AVA) { // styles the Buttons on screen
        HVH.setPadding(new Insets(10,20,10,20));
        HVA.setPadding(new Insets(10,20,10,20));
        AVA.setPadding(new Insets(10,20,10,20));
    }
    private void setButtonActions(Button HVH, Button HVA, Button AVA) { // sets up button reactions to being pressed
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

        AVA.setOnAction(buttonEvent -> {
            try {
                handleAVA();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // functions that configures the buttons to set the model game type, and change the screen to the settings screen
    private void handleHVH() throws Exception {
        model.setGameType(GameType.HumanVsHuman);
        model.setFirst(new HumanPlayer());
        model.setSecond(new HumanPlayer());
        moveToSettings();
    }
    private void handleHVA() throws Exception {
        model.setGameType(GameType.HumanVsAI);
        model.setFirst(new HumanPlayer());
        model.setSecond(new AIPlayer());
        moveToSettings();
    }
    private void handleAVA() throws Exception {
        model.setGameType(GameType.AIVsAI);
        model.setFirst(new AIPlayer());
        model.setSecond(new AIPlayer());
        moveToSettings();
    }
}
