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
    private final SettingsScreen view; // the screens view
    private final Game model; // the game model
    private final Stage stage; // the app window

    /**
     * full constructor. styles all view elements, configures all fields and
     * MoveToGame button and shows the app window. also adds itself as a
     * listener to the model, so it could get updated on input field
     * validation results
     * @param view the screen view containing all ui elements
     * @param model the game model containing the game data
     * @param stage the app windows in which the ui is displayed
     */
    public SettingsScreenController(SettingsScreen view, Game model,
                                    Stage stage) { // constructor
        this.model = model;
        this.stage = stage;
        this.view = view;

        model.addPropertyChangeListener(this);

        setLabelStyle(view.getTitle(),new Label[]{view.getP1Name(),
                view.getP2Name(),view.getGridSizeTitle()},view.getErrorText());
        configureMoveToGame(view.getMoveToGame());
        configureFontForFields(new TextField[]{view.getP1Field(),
                view.getP2Field()});
        view.getGridSizeBox().setStyle("-fx-font-size: 17px;");

        try {
            view.start(stage);
        } catch (Exception e) {
            System.out.println("Settings screen could not start.");
            System.exit(1);
        }
    }

    /**
     * configures textField fonts
     * @param fields array containing all view textFields
     */
    private void configureFontForFields(TextField[] fields) {
        for (TextField field : fields) {
            field.setStyle("-fx-font-size: 17px;");
        }
    }

    /**
     * styles all on screen labels
     * @param title the window title
     * @param inputFields array of all input fields
     * @param errorText error text that shows up when validation fails
     */
    private void setLabelStyle(Label title,Label[] inputFields,Label errorText)
    { // styles the labels on screen
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-size: 75px;");
        Stop[] stops = new Stop[] { new Stop(0.4, Color.web("#CC0033")),
                new Stop(0.5, Color.rgb(0, 0, 255, 0.5)),
                new Stop(1, Color.BLUE) };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 0,
                true, CycleMethod.NO_CYCLE, stops);
        title.setTextFill(gradient);

        errorText.setVisible(false);
        errorText.setTextFill(Color.RED);
        errorText.setStyle("-fx-font-size: 30px;");

        for (Label inputField : inputFields) {
            inputField.setStyle("-fx-font-size: 17px;");
        }
    }

    /**
     * configures the button used to send input field inputs to the
     * model for validation
     * @param MTG the button in question
     */
    private void configureMoveToGame(Button MTG) {
        MTG.setPrefHeight(40);
        MTG.setPrefWidth(160);
        MTG.setStyle("-fx-background-color: radial-gradient(radius 50%, " +
                "#FF3B61, #FE8373); -fx-background-radius: 50px; " +
                "-fx-text-fill: white; -fx-font-size: 17;");
        MTG.setOnAction(actionEvent -> model.insertNamesAndGrid(
                view.getP1Input(),view.getP2Input(),view.getGridInput()));
    }

    /**
     * function that activates after model validation, if the inputs are
     * valid it will pass the stage to the game screen(move to it). if not,
     * the error text will be shown.
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     *          old value = "", new value = true if inputs are valid,
     *            otherwise  false
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue().equals(true)) {
            model.removePropertyChangeListener(this);
            GameScreen gameView =
                    new GameScreen( model.getGameBoard().getGridSize(),
                            view.getBackground());
            GameScreenController gameController =
                    new GameScreenController( model, gameView, stage);
        } else {
            view.getErrorText().setVisible(true);
        }
    }
}
