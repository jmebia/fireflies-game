package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.io.Save;
import com.crackerjacks.game.core.objects.Player;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameMenu extends GameState {

    private Player player;

    private Scene scene;
    private GraphicsContext gc;

    private int hudX;
    private int hudY;
    private int hudW;
    private int hudH;

    // navigate counters
    private int currentMarker = 0;
    private int maxCounter = 7; // up to 8 inventory slots
    private int minCounter = 0;


    public GameMenu(Scene scene, GraphicsContext graphicsContext) {
        Save save = Global.getSave();
        player = save.getPlayer();

        this.scene = scene;
        gc = graphicsContext;
    }

    @Override
    void onEnter() {

        scene.setOnKeyPressed(event -> {

            //navigate through the inventory
            if (event.getCode() == KeyCode.DOWN) {
                if (currentMarker + 1 <= maxCounter) {
                    currentMarker++;
                } else if (currentMarker + 1 > maxCounter) {
                    currentMarker = minCounter;
                }

                System.out.println("MinCounter = " + minCounter);
                System.out.println("MaxCounter = " + maxCounter);
                System.out.println("CurrentCounter = " + currentMarker);
            }

            else if (event.getCode() == KeyCode.UP) {
                if (currentMarker - 1 >= minCounter) {
                    currentMarker--;
                } else if (currentMarker - 1 < minCounter) {
                    currentMarker = maxCounter;
                }

                System.out.println("MinCounter = " + minCounter);
                System.out.println("MaxCounter = " + maxCounter);
                System.out.println("CurrentCounter = " + currentMarker);
            }

            // trigger command selected
            else if (event.getCode() == KeyCode.ENTER) {

            }

            // exit game menu
            else if (event.getCode() == KeyCode.ESCAPE) {
                System.out.println("Pressed Escape");
                GameStateManager.removeLast();
            }

        });



    }

    @Override
    void update(long time) {

        hudX = (int) (scene.getCamera().getTranslateX() - 310);
        hudY = (int) (scene.getCamera().getTranslateY() + 30);
        hudW = 150;
        hudH = 150;

        // main bg
        gc.setFill(new Color(0f, 0f, 1f, 0.3));
        gc.fillRect(hudX, hudY, hudW, hudH);

        // border
        gc.setStroke(Color.WHITE);
        gc.strokeRect(hudX, hudY, hudW, hudH);

    }

    @Override
    void onExit() {

    }

}
