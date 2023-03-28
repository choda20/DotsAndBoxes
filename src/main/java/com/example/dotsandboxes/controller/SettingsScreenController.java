package com.example.dotsandboxes.controller;

import com.example.dotsandboxes.model.classes.Game;
import com.example.dotsandboxes.view.GameScreen;
import com.example.dotsandboxes.view.SettingsScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SettingsScreenController implements PropertyChangeListener {
    private SettingsScreen view; // screen view
    private Game model; // game model
    private Stage stage; // the app window
    public SettingsScreenController(SettingsScreen view, Game model, Stage stage) throws Exception { // constructor
        this.model = model;
        this.stage = stage;
        this.view = view;

        model.addPropertyChangeListener(this);

        setLabelStyle(view.getTitle(),new Label[]{view.getP1Name(),view.getP2Name(),view.getGridSize()},view.getErrorText());
        configureMoveToGame(view.getMoveToGame());
        configureFontForFields(new TextField[]{view.getP1Field(),view.getP2Field(),view.getGridField()});

        view.start(stage);
    }

    private void configureFontForFields(TextField[] fields) {
        for (int i=0;i<fields.length;i++) {
            fields[i].setStyle("-fx-font-size: 17px;");
        }
    }

    private void setLabelStyle(Label title,Label[] inputFields,Label errorText) { // styles the labels on screen
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-size: 75px;");
        Stop[] stops = new Stop[] { new Stop(0.4, Color.web("#CC0033")),new Stop(0.5, Color.rgb(0, 0, 255, 0.5)), new Stop(1, Color.BLUE) };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        title.setTextFill(gradient);

        errorText.setVisible(false);
        errorText.setTextFill(Color.RED);
        errorText.setStyle("-fx-font-size: 30px;");

        for (int i=0;i<inputFields.length;i++) {
            inputFields[i].setStyle("-fx-font-size: 17px;");
        }
    }

    private void configureMoveToGame(Button MTG) {
        MTG.setPrefHeight(40);
        MTG.setPrefWidth(160);
        MTG.setStyle("-fx-background-color: radial-gradient(radius 50%, #FF3B61, #FE8373); -fx-background-radius: 50px; -fx-text-fill: white; -fx-font-size: 17;");
        MTG.setOnAction(actionEvent -> {model.insertNamesAndGrid(view.getP1Input(),view.getP2Input(),view.getGridInput());});
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue().equals(true)) {
            try {
                model.removePropertyChangeListener(this);
                GameScreen gameView = new GameScreen(model.getGameBoard().getGridSize(),view.getBackground());
                GameScreenController gameController = new GameScreenController(model, gameView, stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            view.getErrorText().setVisible(true);
        }
    }
}
