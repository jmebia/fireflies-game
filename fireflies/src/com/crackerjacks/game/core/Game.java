package com.crackerjacks.game.core;

import com.crackerjacks.game.core.states.MainMenu;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.crackerjacks.game.core.states.GameStateManager;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by jm on 4/23/17.
 *
 * This class contains the main game loop
 *
 */

public class Game extends Application {

    public static Group root;
    private Scene scene;
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    private ClassLoader classLoader = getClass().getClassLoader();

    @Override
    public void init() throws Exception {
        super.init();

        root = new Group();
        scene = new Scene(root, 800, 600, Color.BLACK);
        canvas = new Canvas(4000, 4000);
        root.getChildren().setAll(canvas);
        graphicsContext = canvas.getGraphicsContext2D();

        // game state init
        GameStateManager.getStateList().add(new MainMenu(scene, graphicsContext));



    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setScene(scene);
        primaryStage.setTitle("Fireflies");
        primaryStage.getIcons().add(new Image(classLoader.getResource("icons/icon.png").toString()));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                // update game
                GameStateManager.update(now);
            }

        }.start();

    }

}
