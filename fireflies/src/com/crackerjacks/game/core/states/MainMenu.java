package com.crackerjacks.game.core.states;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by jm on 4/29/17.
 */
public class MainMenu extends GameState {

    // Menu variables
    int currentMarker = 1;
    int maxMarker = 3;
    int minMarker = 1;

    public MainMenu(Scene scene, GraphicsContext graphicsContext) {
        this.scene = scene;
        this.graphicsContext = graphicsContext;
        onEnter();
    }

    @Override
    void onEnter() {
        scene.setOnKeyPressed(event -> {

            // choose menu option
            if (event.getCode() == KeyCode.DOWN) {
                if (currentMarker < maxMarker) currentMarker++;
                else currentMarker = minMarker;
            } else if (event.getCode() == KeyCode.UP) {
                if (currentMarker > minMarker) currentMarker--;
                else currentMarker = maxMarker;
            }

            // trigger
            if (event.getCode() == KeyCode.ENTER) {
                switch (currentMarker) {
                    case 1:
                        System.out.println("New Game Selected!");
                        // StateStack.push("room");
                        break;
                    case 2:
                        System.out.println("Load Game Selected!");
                        break;
                    case 3:
                        System.out.println("Exit Selected!");
                        Platform.exit();
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            }
        });
    }

    @Override
    void update() {
        System.out.println("Yep, it's updating something");
    }

    @Override
    void draw() {
        System.out.println("Yep, it's drawing something");
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0,0,512,512);

        graphicsContext.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        graphicsContext.setFill((currentMarker == 1? Color.BLUE : Color.BLACK));
        graphicsContext.fillText("New Game", 64, 384);

        graphicsContext.setFill((currentMarker == 2? Color.BLUE : Color.BLACK));
        graphicsContext.fillText("Load Game", 64, 416);

        graphicsContext.setFill((currentMarker == 3? Color.BLUE : Color.BLACK));
        graphicsContext.fillText("Exit", 64, 448);
    }

    @Override
    void onExit() {

    }

}
