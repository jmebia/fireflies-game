package com.crackerjacks.game.core;

import com.crackerjacks.game.core.states.MainGame;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.crackerjacks.game.core.states.GameStateManager;

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

    private GameStateManager stateManager;

    @Override
    public void init() throws Exception {
        super.init();

        root = new Group();
        scene = new Scene(root, 800, 600, Color.BLACK);
        canvas = new Canvas(3000, 3000);
        root.getChildren().setAll(canvas);
        graphicsContext = canvas.getGraphicsContext2D();

        // game state init
        stateManager = new GameStateManager();
        stateManager.stateList.add(new MainGame(scene, graphicsContext));

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ordeal of the Fireflies");
        primaryStage.getIcons().add(new Image("/com/crackerjacks/game/resources/icon.png"));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                // update
                stateManager.update(now);

                // draw
                stateManager.draw();
            }

        }.start();

    }

}
