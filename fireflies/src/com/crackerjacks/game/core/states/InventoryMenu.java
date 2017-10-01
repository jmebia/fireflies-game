package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.objects.Item;
import com.crackerjacks.game.core.objects.Player;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class InventoryMenu extends GameState {

    private Scene scene;
    private GraphicsContext gc;

    // inventory hud properties
    private int hudX;
    private int hudY;
    private int hudW;
    private int hudH;

    // navigate counters
    private int currentMarker = 0;
    private int maxCounter = 7; // up to 8 inventory slots
    private int minCounter = 0;

    // player
    Player player;
    ArrayList<Item> inventory;

    public InventoryMenu(Scene scene, GraphicsContext graphicsContext) {
        this.scene = scene;
        gc = graphicsContext;
    }

    @Override
    void onEnter() {

        // get player from save file
        player = Global.getSave().getPlayer();
        inventory = player.getInventory();
        maxCounter = inventory.size() - 1;


        scene.setOnKeyPressed(e -> {

            //navigate through the inventory
            if (e.getCode() == KeyCode.DOWN) {
                if (currentMarker++ <= maxCounter) {
                    currentMarker++;
                } else if (currentMarker++ > maxCounter) {
                    currentMarker = minCounter;
                }
            }

            else if (e.getCode() == KeyCode.UP) {
                if (currentMarker-- <= maxCounter) {
                    currentMarker--;
                } else if (currentMarker-- > maxCounter) {
                    currentMarker = maxCounter;
                }
            }

            // use or equip item
            else if (e.getCode() == KeyCode.ENTER) {

            }

            // discard item
            else if (e.getCode() == KeyCode.DELETE) {

            }

            // exit inventory
            else if (e.getCode() == KeyCode.I || e.getCode() == KeyCode.ESCAPE) {
                GameStateManager.removeLast();
            }

        });

    }

    @Override
    void update(long time) {

        // TODO: Display inventory items


        // draw
        hudX = (int) (scene.getCamera().getTranslateX() - 310);
        hudY = (int) (scene.getCamera().getTranslateY() - 160);
        hudW = 400;
        hudH = 400;


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
