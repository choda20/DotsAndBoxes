package com.example.dotsandboxes;

import com.example.dotsandboxes.controller.TitleScreenController;
import com.example.dotsandboxes.model.classes.Game;
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
        Game model = new Game();
        TitleScreen title = new TitleScreen(sceneX,sceneY);
        TitleScreenController titleController = new TitleScreenController(title,model,primaryStage);
    }
}



