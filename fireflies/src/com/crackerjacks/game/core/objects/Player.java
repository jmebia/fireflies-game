package com.crackerjacks.game.core.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Player extends GameCharacter {

    private int rockLevel = 0;
    private int paperLevel = 0;
    private int scissorsLevel = 0;

    private ArrayList<Item> inventory = new ArrayList<>();
    private Item equipped = null;

    private int fireflies = 0;

    private int lineOfSight = 4;

    // getters and setters

    public void addFireflies(int count) {
        fireflies += count;
    }

    public void removeFireflies(int count) {
        if (!(fireflies - count < 0))
            fireflies += count;
        else
            System.out.println("Player's chips cannot be lower than zero!");
    }

    public int getFireflies() {
        return fireflies;
    }

    public void setLineOfSight(int los) {
        this.lineOfSight = los;
    }

    public int getLineOfSight() {
        return this.lineOfSight;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public int getRockLevel() {
        return rockLevel;
    }

    public void setRockLevel(int rockLevel) {
        this.rockLevel = rockLevel;
    }

    public int getPaperLevel() {
        return paperLevel;
    }

    public void setPaperLevel(int paperLevel) {
        this.paperLevel = paperLevel;
    }

    public int getScissorsLevel() {
        return scissorsLevel;
    }

    public void setScissorsLevel(int scissorsLevel) {
        this.scissorsLevel = scissorsLevel;
    }

    public Item getEquipped() {
        return equipped;
    }

    public void setEquipped(Item item) {
        if (inventory.contains(item))
            equipped = item;
        else
            System.out.println("Item doesn't exist in player's inventory.");
    }

}
