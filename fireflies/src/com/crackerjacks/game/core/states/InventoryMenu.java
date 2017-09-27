package com.crackerjacks.game.core.states;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class InventoryMenu extends GameState {

    private Scene scene;
    private GraphicsContext gc;

    // navigate counters
    private int counter = 0;
    private int maxCounter = 7; // up to 8 inventory slots
    private int minCounter = 0;

    public InventoryMenu(Scene scene, GraphicsContext graphicsContext) {
        this.scene = scene;
        gc = graphicsContext;
    }

    @Override
    void onEnter() {

        scene.setOnKeyPressed(e -> {

            //navigate through the inventory
            if (e.getCode() == KeyCode.DOWN) {
                if (counter++ <= maxCounter) {
                    counter++;
                } else if (counter++ > maxCounter) {
                    counter = minCounter;
                }
            }

            else if (e.getCode() == KeyCode.UP) {
                if (counter-- <= maxCounter) {
                    counter--;
                } else if (counter-- > maxCounter) {
                    counter = maxCounter;
                }
            }

            // use or equip item
            else if (e.getCode() == KeyCode.ENTER) {

            }

            // discard item
            else if (e.getCode() == KeyCode.DELETE) {

            }

        });

    }

    @Override
    void update(long time) {

    }

    @Override
    void onExit() {

    }
}
