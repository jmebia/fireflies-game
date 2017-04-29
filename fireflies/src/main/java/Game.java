package main.java;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by jm on 4/23/17.
 *
 * This class contains the main game loop
 *
 */

public class Game extends Application {

    private Group root;
    private Scene scene;
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    @Override
    public void init() throws Exception {
        super.init();

        root = new Group();
        scene = new Scene(root, 800, 600);
        scene.setFill(Color.BLACK);
        canvas = new Canvas(1000, 1000);
        root.getChildren().addAll(canvas);
        graphicsContext = canvas.getGraphicsContext2D();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ordeal of the Fireflies");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                // update


                // draw

            }

        }.start();

    }

}
