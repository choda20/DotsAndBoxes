package com.example.dotsandboxes;

import com.example.dotsandboxes.controller.GameScreenController;
import com.example.dotsandboxes.controller.TitleScreenController;
import com.example.dotsandboxes.model.classes.Board;
import com.example.dotsandboxes.model.classes.Game;
import com.example.dotsandboxes.model.classes.HumanPlayer;
import com.example.dotsandboxes.model.classes.Player;
import com.example.dotsandboxes.model.enums.GameType;
import com.example.dotsandboxes.model.enums.PlayerNumber;
import com.example.dotsandboxes.view.TitleScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        int sceneX = 1000;
        int sceneY = 1000;
        Player p1 = new HumanPlayer("Itay", PlayerNumber.first,8);
        Player p2 = new HumanPlayer("Avner", PlayerNumber.second,3);
        Game model = new Game(p1,p2,GameType.HumanVsHuman,new Board(4));
        TitleScreen title = new TitleScreen(sceneX,sceneY);
        TitleScreenController titleController = new TitleScreenController(title,model,primaryStage);
    }
}
