package com.example.dotsandboxes;

import com.example.dotsandboxes.controller.TitleScreenController;
import com.example.dotsandboxes.model.classes.Game;
import com.example.dotsandboxes.view.TitleScreen;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        File imageFile = new File("src/main/java/com/example/dotsandboxes/assets/background.jpg");
        javafx.scene.image.Image image = new Image(imageFile.getCanonicalPath());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0,1.0,true,true,false,false));
        Background background = new Background(backgroundImage);
        Game model = new Game();
        TitleScreen title = new TitleScreen(background);
        TitleScreenController titleController = new TitleScreenController(title,model,primaryStage);
    }
}



