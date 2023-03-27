package com.example.dotsandboxes;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class test extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Create the labels and the matrix
        Label label1 = new Label("Label 1");
        Label label2 = new Label("Label 2");
        Label label3 = new Label("Label 3");
        int numRows = 5;
        int numCols = 5;
        GridPane matrix = new GridPane();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Circle dot = new Circle(5, Color.BLACK);
                matrix.add(dot, col, row);
            }
        }

        // Create the container for the labels
        VBox labelContainer = new VBox(label1, label2, label3);
        labelContainer.setSpacing(10);
        labelContainer.setMaxWidth(Double.MAX_VALUE);

        // Create the container for the matrix and lines
        Pane matrixContainer = new Pane();
        matrixContainer.getChildren().addAll(matrix, createLines(numCols, numRows));
        matrixContainer.setPrefSize(400, 400);
        matrixContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHgrow(matrixContainer, Priority.ALWAYS);
        GridPane.setVgrow(matrixContainer, Priority.ALWAYS);

        // Create the main container and add the label and matrix containers
        VBox mainContainer = new VBox(labelContainer, matrixContainer);
        mainContainer.setSpacing(20);
        mainContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

// Bind the sizes of the label and matrix containers to the size of the window
        labelContainer.prefWidthProperty().bind(mainContainer.widthProperty());
        matrixContainer.prefWidthProperty().bind(mainContainer.widthProperty());
        matrixContainer.prefHeightProperty().bind(mainContainer.heightProperty().subtract(labelContainer.heightProperty()));

        // Create the scene and show it
        Scene scene = new Scene(mainContainer);
        stage.setScene(scene);
        stage.show();
    }


    // Helper method to create the lines between the dots
    private Node createLines(int numCols, int numRows) {
        Pane lines = new Pane();
        double radius = 5;
        double lineWidth = 1;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                double centerX = (col + 0.5) * (2 * radius + lineWidth);
                double centerY = (row + 0.5) * (2 * radius + lineWidth);
                if (col < numCols - 1) { // add vertical line
                    Line line = new Line(centerX + radius + lineWidth/2, centerY, centerX + 3*radius + lineWidth/2, centerY);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(lineWidth);
                    lines.getChildren().add(line);
                }
                if (row < numRows - 1) { // add horizontal line
                    Line line = new Line(centerX, centerY + radius + lineWidth/2, centerX, centerY + 3*radius + lineWidth/2);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(lineWidth);
                    lines.getChildren().add(line);
                }
            }
        }
        return lines;
    }
}
