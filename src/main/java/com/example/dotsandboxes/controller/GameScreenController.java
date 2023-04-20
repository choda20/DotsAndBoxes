package com.example.dotsandboxes.controller;

import com.example.dotsandboxes.model.classes.Game;
import com.example.dotsandboxes.model.classes.ModelLine;
import com.example.dotsandboxes.model.classes.Player;
import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.model.enums.LineType;
import com.example.dotsandboxes.model.enums.MoveResult;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import com.example.dotsandboxes.view.GameScreen;
import javafx.application.Platform;
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

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;


public class GameScreenController implements PropertyChangeListener {
    private final Game model; // the game model
    private final GameScreen view; // the screens view
    private final int gridSize; // the grid size
    private final Hashtable<PlayerNumber,LinearGradient> playerGradients;


    /**
     * partial constructor that initializes all class fields, adds the object as a listener to the
     * model(also adds the AI if it is HVA), styles and configures all view elements and shows the app window
     *
     * @param view  the screen view containing all ui elements
     * @param model the game model containing the game data
     * @param stage the app windows in which the ui is displayed
     */
    public GameScreenController(Game model, GameScreen view, Stage stage) { // constructor
        this.model = model;
        this.view = view;
        this.gridSize = model.getGameBoard().getGridSize();
        this.playerGradients = new Hashtable<>();

        Stop[] stopsP1 = new Stop[]{new Stop(0, Color.web("#FE0944")), new Stop(1, Color.web("#FEAE96"))};
        LinearGradient p1Gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stopsP1);
        Stop[] stopsP2 = new Stop[]{new Stop(0, Color.web("#008FFD")), new Stop(1, Color.web("#2A2A72"))};
        LinearGradient p2Gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stopsP2);
        playerGradients.put(PlayerNumber.first,p1Gradient);
        playerGradients.put(PlayerNumber.second,p2Gradient);

        model.addPropertyChangeListener(this); // registers the controller as a listener to the model
        if (model.getGameType().equals(GameType.HumanVsAI)) {
            model.addPropertyChangeListener((PropertyChangeListener) model.getSecond());
        }

        setLabels(view.getLabels()); // initializes labels
        drawViewBoard(stage.getWidth(), stage.getHeight());
        setMouseSettings(view.getHorizontalLines(), LineType.horizontal);
        setMouseSettings(view.getVerticalLines(), LineType.vertical);

        try {
            view.start(stage);
        } catch (Exception e) {
            System.out.println("Game screen could not start.");
            System.exit(1);
        }
    }

    /**
     * function that configures line reactions to being pressed, hovered on, and hover on exited.
     *
     * @param lines    2D array of lines
     * @param lineType the type of lines in the array
     */
    private void setMouseSettings(Line[][] lines, LineType lineType) { //sets up line reactions to mouse events

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                if (isLineNotConnected(lines[i][j])) {
                    lines[i][j].setStroke(Color.TRANSPARENT);
                    lines[i][j].setStrokeWidth(7);

                    final int row = i, column = j;

                    lines[i][j].setOnMouseClicked((mouseEvent -> {
                        Line clickedLine = (Line) mouseEvent.getSource();
                        executeMove(clickedLine, row, column, lineType);
                    }));

                    lines[i][j].setOnMouseEntered((mouseEvent -> {
                        Line hoveredLine = (Line) mouseEvent.getSource();
                        Stop[] stopsHovered = new Stop[]{new Stop(0, Color.web("#FBD72B")), new Stop(1, Color.web("#F9484A"))};
                        hoveredLine.setStroke(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stopsHovered));
                    }));

                    lines[i][j].setOnMouseExited((mouseEvent -> {
                        Line hoveredLine = (Line) mouseEvent.getSource();
                        hoveredLine.setStroke(Color.TRANSPARENT);
                    }));
                }
            }
        }
    }

    private void disableMouseSettings(Line[][] lines) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                if (!playerGradients.contains(lines[i][j].getStroke()))
                    disableLine(lines[i][j]);
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
    public void executeMove(Line clickedLine, int row, int column, LineType lineType) {
        disableLine(clickedLine);
        model.performMove(row, column, lineType);
    }

    /**
     * function that configures all on-screen labels
     *
     * @param labels all view labels
     */
    private void setLabels(Label[] labels) {
        // shows current player
        labels[0] = new Label();
        labels[0].setText(model.getCurrent().getName() + "'s turn");
        labels[0].setAlignment(Pos.CENTER);
        labels[0].setStyle("-fx-font-size: 75px;");
        labels[0].setTextFill(playerGradients.get(PlayerNumber.first));

        // player 1's score
        labels[1] = new Label();
        labels[1].setText(model.getFirst().getName() + "'s score: " + model.getFirst().getScore());
        labels[1].setAlignment(Pos.CENTER);
        labels[1].setStyle("-fx-font-size: 40px;");
        labels[1].setTextFill(playerGradients.get(PlayerNumber.first));

        // player 2's score
        labels[2] = new Label();
        labels[2].setText(model.getSecond().getName() + "'s score:  " + model.getSecond().getScore());
        labels[2].setAlignment(Pos.CENTER);
        labels[2].setStyle("-fx-font-size: 40px;");
        labels[2].setTextFill(playerGradients.get(PlayerNumber.second));
    }

    /**
     * function that configures all view matrix(sets their location on the screen)
     *
     * @param width  screen width
     * @param height screen height
     */
    private void drawViewBoard(double width, double height) {
        double constraint = Math.min(width, height);
        double spaceBetweenDots = (constraint / gridSize)*0.8;
        double startingHeight = height / 2;
        double startingWidth = width / 4;
        double dotRadius = 9;

        drawHorizontalLines(view.getHorizontalLines(),startingWidth,startingHeight,spaceBetweenDots,dotRadius);
        drawVerticalLines(view.getVerticalLines(),startingWidth,startingHeight,spaceBetweenDots,dotRadius);
        drawDots(view.getDots(),startingWidth,startingHeight,spaceBetweenDots,dotRadius);
    }

    private void drawHorizontalLines(Line[][] lines, double startingWidth, double startingHeight, double spaceBetweenDots, double dotRadius) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                double startX = startingWidth + (j * spaceBetweenDots) + dotRadius;
                double startY = startingHeight + (i * spaceBetweenDots);
                double endX = startingWidth + ((j + 1) * spaceBetweenDots) - dotRadius;
                double endY = startingHeight + ((i) * spaceBetweenDots);
                lines[i][j] = new Line(startX, startY, endX, endY);
            }
        }
    }

    private void drawVerticalLines(Line[][] lines, double startingWidth, double startingHeight, double spaceBetweenDots, double dotRadius) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                double startX = startingWidth + (i * spaceBetweenDots);
                double startY = startingHeight + (j * spaceBetweenDots) + dotRadius;
                double endX = startingWidth + (i * spaceBetweenDots);
                double endY = startingHeight + ((j + 1) * spaceBetweenDots) - dotRadius;
                lines[i][j] = new Line(startX, startY, endX, endY);
            }
        }
    }

    private void drawDots(Circle[][] dots, double startingWidth, double startingHeight, double spaceBetweenDots, double dotRadius) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double x = startingWidth + (j * spaceBetweenDots);
                double y = startingHeight + (i * spaceBetweenDots);
                dots[i][j] = new Circle(x, y, dotRadius);
            }
        }
    }

    /**
     * function that disables all reactions for a line
     *
     * @param line the line to be disabled
     */
    private void disableLine(Line line) {
        line.setOnMouseClicked(event -> {
        });
        line.setOnMouseEntered(event -> {
        });
        line.setOnMouseExited(event -> {
        });
    }

    private boolean isLineNotConnected(Line line) {
        return !playerGradients.containsValue(line.getStroke());
    }

    /**
     * function that activates when a move was made on the model board.
     * the function updates the color of the clicked line(updateLine function),
     * the on-screen scores and turn and if the game ended the function will
     * update the turn text to show the result(updateLabels function).
     * additionally, if the game has an AI(checkAiTurn function) this
     * function asks the AI to perform a move and sends it to the model    
     * (RunAi() function)
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

        updateLine(changedLine, changedLineAndOwner.getValue());
        updateLabels(result);

        if (checkAiTurn(model.getGameType(),model.getTurn(),result))
            RunAi();
    }

    private boolean checkAiTurn(GameType gameType, PlayerNumber currentTurn, MoveResult result) {
        boolean gameHasAi = gameType.equals(GameType.HumanVsAI);
        boolean aiTurn = currentTurn.equals(PlayerNumber.second);
        boolean ongoingGame = result.equals(MoveResult.valid);
        return (gameHasAi && aiTurn && ongoingGame);
    }
    private void RunAi() {
        Player ai = model.getCurrent();

        disableMouseSettings(view.getHorizontalLines());
        disableMouseSettings(view.getVerticalLines());

        Thread aiThread = new Thread(() -> {
            Pair<Point, LineType> result = ai.play();
            Point lineRC = result.getKey();
            Platform.runLater(() -> {

                model.performMove(lineRC.x, lineRC.y, result.getValue());

                setMouseSettings(view.getVerticalLines(),LineType.vertical);
                setMouseSettings(view.getHorizontalLines(),LineType.horizontal);
            });
        });
        aiThread.start();
    }

    private void updateLine(ModelLine line,PlayerNumber owner){
        LinearGradient lineColor = playerGradients.get(owner);
        Line[][] lineMatrix = line.getIsHorizontal() ? view.getHorizontalLines() : view.getVerticalLines();
        lineMatrix[line.getRow()][line.getColumn()].setStroke(lineColor);
        disableLine(lineMatrix[line.getRow()][line.getColumn()]);
    }

    private void updateLabels(MoveResult result) {
        LinearGradient turnColor = playerGradients.get(model.getTurn());

        view.getLabels()[1].setText(model.getFirst().getName() + "'s score: " + model.getFirst().getScore());
        view.getLabels()[2].setText(model.getSecond().getName() + "'s score:  " + model.getSecond().getScore());
        view.getLabels()[0].setTextFill(turnColor);

        if (result.equals(MoveResult.gameOver)) {
            Pair<Integer, String> results = model.getWinner();
            view.getLabels()[0].setText(results.getValue());
        } else {
            view.getLabels()[0].setText(model.getCurrent().getName() + "'s turn");
        }

    }

}
