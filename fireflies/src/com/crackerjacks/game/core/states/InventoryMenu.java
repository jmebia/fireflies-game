package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.objects.Item;
import com.crackerjacks.game.core.objects.Player;
import com.crackerjacks.game.core.objects.PotionItem;
import com.crackerjacks.game.core.objects.WeaponItem;
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
                if (currentMarker + 1 <= maxCounter) {
                    currentMarker++;
                } else if (currentMarker + 1 > maxCounter) {
                    currentMarker = minCounter;
                }

                System.out.println("MinCounter = " + minCounter);
                System.out.println("MaxCounter = " + maxCounter);
                System.out.println("CurrentCounter = " + currentMarker);
            }

            else if (e.getCode() == KeyCode.UP) {
                if (currentMarker - 1 >= minCounter) {
                    currentMarker--;
                } else if (currentMarker - 1 < minCounter) {
                    currentMarker = maxCounter;
                }

                System.out.println("MinCounter = " + minCounter);
                System.out.println("MaxCounter = " + maxCounter);
                System.out.println("CurrentCounter = " + currentMarker);
            }

            // use or equip item
            else if (e.getCode() == KeyCode.ENTER) {
                System.out.println("Pressed Enter");
                // check if an item exists in an inventory
                if (maxCounter > -1) {
                    Item usable = inventory.get(currentMarker);
                    // do the equip method if item is a weapon
                    if (usable instanceof WeaponItem) {
                        System.out.println(usable.getName());
                        player.setEquipped((WeaponItem) usable);
                    }

                    // else if it is a potion item
                    else if (usable instanceof PotionItem) {
                        System.out.println(usable.getName());
                        player.useItem((PotionItem) usable);
                    }
                }
            }

            // discard item
            else if (e.getCode() == KeyCode.DELETE) {
                // check if an item exists in the inventory
                if (maxCounter > -1) {
                    Item usable = inventory.get(currentMarker);

                    player.getInventory().remove(usable);
                    System.out.println("Removed " + usable.getName() + " from your inventory.");
                    inventory = player.getInventory();
                    maxCounter = inventory.size() - 1;
                    currentMarker = minCounter;
                }
            }

            // exit inventory
            else if (e.getCode() == KeyCode.I || e.getCode() == KeyCode.ESCAPE) {
                GameStateManager.removeLast();
            }

        });

    }

    @Override
    void update(long time) {

        // draw
        hudX = (int) (scene.getCamera().getTranslateX() - 310);
        hudY = (int) (scene.getCamera().getTranslateY() - 160);
        hudW = 400;
        hudH = 400;


        // main bg
        gc.setFill(new Color(0f, 0f, 1f, 0.3));
        gc.fillRect(hudX, hudY, hudW, hudH);


        // TODO: Display inventory items
        for (int i = 0; i < inventory.size(); i++) {

            // get item
            Item item = inventory.get(i);

            // print item name in inventory
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Verdana", FontWeight.NORMAL,
                    (currentMarker == i? 20 : 12)));
            gc.fillText(item.getName(), hudX + 10, hudY + 30 + i * 15);

        }



        // border
        gc.setStroke(Color.WHITE);
        gc.strokeRect(hudX, hudY, hudW, hudH);

    }

    @Override
    void onExit() {

    }
}
