package com.example.dotsandboxes.controller;

import com.example.dotsandboxes.model.classes.Game;
import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.MoveResult;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import com.example.dotsandboxes.view.GameScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class GameScreenController implements PropertyChangeListener {
    private Game model; // the game model
    private GameScreen view; // the screens view
    private int gridSize; // the grid size
    private LinearGradient p1Gradient; // text gradient for player 1
    private LinearGradient p2Gradient; // text gradient for player 2

    /**
     * partial constructor that initializes all class fields, adds the object as a listener to the
     * model(also adds the AI if it is HVA), styles and configures all view elements and shows the app window
     *
     * @param view  the screen view containing all ui elements
     * @param model the game model containing the game data
     * @param stage the app windows in which the ui is displayed
     * @throws Exception
     */
    public GameScreenController(Game model, GameScreen view, Stage stage) throws Exception { // constructor
        this.model = model;
        this.view = view;
        this.gridSize = model.getGameBoard().getGridSize();
        Stop[] stopsP1 = new Stop[]{new Stop(0, Color.web("#FE0944")), new Stop(1, Color.web("#FEAE96"))};
        this.p1Gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stopsP1);
        Stop[] stopsP2 = new Stop[]{new Stop(0, Color.web("#008FFD")), new Stop(1, Color.web("#2A2A72"))};
        this.p2Gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stopsP2);

        model.addPropertyChangeListener(this); // registers the controller as a listener to the model
        if (model.getGameType().equals(GameType.HumanVsAI)) {
            model.addPropertyChangeListener((PropertyChangeListener) model.getSecond());
        }

        setLabels(view.getLabels()); // initializes labels
        buildViewBoard(stage.getWidth(), stage.getHeight());
        setMouseSettings(view.getHorizontalLines(), LineType.horizontal);
        setMouseSettings(view.getVerticalLines(), LineType.vertical);

        view.start(stage);

    }

    /**
     * function that configures line reactions to being pressed, hovered on, and hover on exited.
     *
     * @param lines    2D array of lines
     * @param lineType the type of lines in the array
     */
    public void setMouseSettings(Line[][] lines, LineType lineType) { // sets up line reactions to mouse events
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                lines[i][j].setStroke(Color.TRANSPARENT);
                lines[i][j].setStrokeWidth(5);
                final int row = i, column = j;
                lines[i][j].setOnMouseClicked((mouseEvent -> {
                    Line clickedLine = (Line) mouseEvent.getSource();
                    registerMove(clickedLine, row, column, lineType);
                }));
                lines[i][j].setOnMouseEntered((mouseEvent -> {
                    Line hoveredLine = (Line) mouseEvent.getSource();
                    if (hoveredLine.getStroke() != p1Gradient && hoveredLine.getStroke() != p2Gradient) {
                        Stop[] stopsHovered = new Stop[]{new Stop(0, Color.web("#FBD72B")), new Stop(1, Color.web("#F9484A"))};
                        hoveredLine.setStroke(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stopsHovered));
                    }
                }));
                lines[i][j].setOnMouseExited((mouseEvent -> {
                    Line hoveredLine = (Line) mouseEvent.getSource();
                    if (hoveredLine.getStroke() != p1Gradient && hoveredLine.getStroke() != p2Gradient) {
                        hoveredLine.setStroke(Color.TRANSPARENT);
                    }
                }));
            }
        }
    }

    /**
     * function that activates when an unconnected line is pressed,
     * the function disable all line reactions and preforms a move on the line
     * in the game model
     *
     * @param clickedLine the line that was clicked on
     * @param row         the lines row in a line array
     * @param column      the lines column in a line array
     * @param lineType    the type of the line
     */
    public void registerMove(Line clickedLine, int row, int column, LineType lineType) {
        disableLine(clickedLine);
        model.performMove(row, column, lineType);
    }

    /**
     * function that configures all on-screen labels
     *
     * @param labels all view labels
     */
    public void setLabels(Label[] labels) {
        // shows current player
        labels[0] = new Label();
        labels[0].setText(model.getCurrent().getName() + "'s turn");
        labels[0].setAlignment(Pos.CENTER);
        labels[0].setStyle("-fx-font-size: 75px;");
        labels[0].setTextFill(p1Gradient);

        // player 1's score
        labels[1] = new Label();
        labels[1].setText(model.getFirst().getName() + "'s score: " + model.getFirst().getScore());
        labels[1].setAlignment(Pos.CENTER);
        labels[1].setStyle("-fx-font-size: 40px;");
        labels[1].setTextFill(p1Gradient);

        // player 2's score
        labels[2] = new Label();
        labels[2].setText(model.getSecond().getName() + "'s score:  " + model.getSecond().getScore());
        labels[2].setAlignment(Pos.CENTER);
        labels[2].setStyle("-fx-font-size: 40px;");
        labels[2].setTextFill(p2Gradient);
    }

    /**
     * function that configures all view matrix(sets their location on the screen)
     *
     * @param width  screen width
     * @param height screen height
     */
    public void buildViewBoard(double width, double height) { // initializes the game board
        double constraint = Math.min(width, height);
        double spaceBetweenDots = (constraint / gridSize) / 2;
        double startingHeight = height / 2;
        double startingWidth = width / 4;
        double dotRadius = 7.5;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                view.getHorizontalLines()[i][j] = new Line(startingWidth + (j * spaceBetweenDots) + dotRadius, startingHeight + (i * spaceBetweenDots), startingWidth + ((j + 1) * spaceBetweenDots) - dotRadius, startingHeight + ((i) * spaceBetweenDots));
                view.getVerticalLines()[i][j] = new Line(startingWidth + (i * spaceBetweenDots), startingHeight + (j * spaceBetweenDots) + dotRadius, startingWidth + (i * spaceBetweenDots), startingHeight + ((j + 1) * spaceBetweenDots) - dotRadius);
                view.getDots()[i][j] = new Circle(startingWidth + (j * spaceBetweenDots), startingHeight + (i * spaceBetweenDots), dotRadius);
            }
            view.getDots()[i][gridSize - 1] = new Circle(startingWidth + ((gridSize - 1) * spaceBetweenDots), startingHeight + (i * spaceBetweenDots), dotRadius);
        }
    }

    /**
     * function that disables all reactions for a line
     *
     * @param line the line to be disabled
     */
    public void disableLine(Line line) {
        line.setOnMouseClicked(event -> {
        });
        line.setOnMouseEntered(event -> {
        });
        line.setOnMouseExited(event -> {
        });
    }

    /**
     * function that activates when a move was made on the model board.
     * the function updates the color of the clicked line, the on screen scores and turn.
     * if the game eneded the function will update the turn text to show the result.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     *            old value = Pair<ModelLine,PlayerNumber> the clicked line and who clicked it,
     *            new value = the result of the line connection(if the game ended)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Pair<ModelLine, PlayerNumber> changedLineAndOwner = (Pair<ModelLine, PlayerNumber>) evt.getOldValue();
        ModelLine changedLine = changedLineAndOwner.getKey();
        MoveResult result = (MoveResult) evt.getNewValue();

        LinearGradient lineColor = changedLineAndOwner.getValue().equals(PlayerNumber.first) ? p1Gradient : p2Gradient;
        LinearGradient turnColor = model.getTurn().equals(PlayerNumber.first) ? p1Gradient : p2Gradient;
        Line[][] lineMatrix = changedLine.getIsHorizontal() ? view.getHorizontalLines() : view.getVerticalLines();
        lineMatrix[changedLine.getRow()][changedLine.getColumn()].setStroke(lineColor);
        disableLine(lineMatrix[changedLine.getRow()][changedLine.getColumn()]);

        view.getLabels()[1].setText(model.getFirst().getName() + "'s score: " + model.getFirst().getScore());
        view.getLabels()[2].setText(model.getSecond().getName() + "'s score:  " + model.getSecond().getScore());
        if (result.equals(MoveResult.gameOver)) {
            Pair<Integer, String> results = model.getWinner();
            if (results.getKey().intValue() == 0) {
                LinearGradient color = results.getKey().equals(PlayerNumber.first) ? p1Gradient : p2Gradient;
                view.getLabels()[0].setText(results.getValue() + " Won!");
                view.getLabels()[0].setTextFill(color);
            } else {
                view.getLabels()[0].setText("It's A Tie!");
            }
        } else {
            view.getLabels()[0].setText(model.getCurrent().getName() + "'s turn");
            view.getLabels()[0].setTextFill(turnColor);
        }
    }
}
