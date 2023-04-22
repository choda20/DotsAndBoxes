package com.example.dotsandboxes;

import com.example.dotsandboxes.controller.TitleScreenController;
import com.example.dotsandboxes.model.classes.Game;
import com.example.dotsandboxes.view.TitleScreen;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * function that sets app the app icon, title and background image and
     * starts the title screen
     */
    @Override
    public void start(Stage primaryStage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setWidth(screenBounds.getWidth()/3);
        primaryStage.setHeight((screenBounds.getHeight()*0.8));
        primaryStage.setTitle("Dots&Boxes by Itay Aziz Kadchoda");
        primaryStage.getIcons().add(getImageFromUrl(
                "src/main/resources/images/appicon.jpg"));


        Image backgroundImageImage = getImageFromUrl(
                "src/main/resources/images/background.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                backgroundImageImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(1.0,1.0,true,true,
                        false,false));
        Background background = new Background(backgroundImage);

        Game model = new Game();
        TitleScreen title = new TitleScreen(background);
        TitleScreenController titleController = new
                TitleScreenController(title,model,primaryStage);
    }

    /**
     * function that gets a path of a local image(inside the project) and
     * returns an Image class object based on the image.
     * @param path image path from content root
     * @return image object based on the image path
     */
    private Image getImageFromUrl(String path) {
        try {
            File imageFile = new File(path);
            return new Image(imageFile.getCanonicalPath());
        }catch (Exception e) {
            System.err.println("App cannot be launched because " +
                    "image links are not valid.");
            System.exit(1);
            return null; // quiet the compiler
        }
    }
}



