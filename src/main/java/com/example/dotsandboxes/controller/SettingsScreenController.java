package com.example.dotsandboxes.controller;

import com.example.dotsandboxes.model.classes.Game;
import com.example.dotsandboxes.view.GameScreen;
import com.example.dotsandboxes.view.SettingsScreen;
import com.example.dotsandboxes.view.TitleScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SettingsScreenController {
    private SettingsScreen view;
    private Game model;
    private Stage stage;
    public SettingsScreenController(SettingsScreen view, Game model, Stage stage) throws Exception {
        this.model = model;
        this.stage = stage;
        this.view = view;

        setLabelStyle(view.getTitle(),view.getP1Name(),view.getP2Name(),view.getGridSize(),view.getErrorText());
        setButtonStyle(view.getMoveToGame());
        setButtonAction(view.getMoveToGame(),view.getGridField(),view.getP1Field(),view.getP2Field());

        view.start(stage);
    }

    private void setButtonAction(Button moveToGame,TextField gridSize,TextField p1Name,TextField p2Name) throws Exception {
        model.getFirst().setName(p1Name.getText());
        model.getSecond().setName(p2Name.getText());
        model.getGameBoard().setGridSize(Integer.parseInt(gridSize.getText()));
        GameScreen gameView = new GameScreen(model.getGameBoard(),model.getFirst(), model.getSecond(), view.getSceneX(), view.getSceneY());
        GameScreenController gameController = new GameScreenController(model,gameView,stage);
    }
    private boolean validateFields(TextField gridSize,TextField p1Name,TextField p2Name) {
        boolean gridValidator = gridSize.getText().chars().allMatch(Character::isDigit);
        boolean p1NameValidator = p1Name.getText().isEmpty();
        boolean p2NameValidator = p2Name.getText().isEmpty();
        return gridValidator && p1NameValidator && p2NameValidator;
    }
    private void setButtonStyle(Button moveToGame) {}
    private void setLabelStyle(Label title,Label p1Name,Label p2Name,Label gridSize,Label errorText) {
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-size: 50px;");
        title.setTextFill(Color.BLUE);
        errorText.setVisible(false);
        errorText.setTextFill(Color.RED);
        errorText.setStyle("-fx-font-size: 20px;");
    }
}
