package com.example.dotsandboxes;

import com.example.dotsandboxes.model.classes.HumanPlayer;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import com.example.dotsandboxes.view.GameScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HumanPlayer p1 = new HumanPlayer("Itay", PlayerNumber.first,8);
        HumanPlayer p2 = new HumanPlayer("Avner", PlayerNumber.first,3);
        GameScreen game = new GameScreen(5,p1,p2);
        game.start(primaryStage);
    }
}
