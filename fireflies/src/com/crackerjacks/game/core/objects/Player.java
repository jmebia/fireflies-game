package com.crackerjacks.game.core.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Player extends GameCharacter {

    private int rockExp = 0;
    private int paperExp = 0;
    private int scissorsExp = 0;

    private int keys = 0;

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


    public Item getEquipped() {
        return equipped;
    }

    public void setEquipped(Item item) {
        if (inventory.contains(item))
            equipped = item;
        else
            System.out.println("Item doesn't exist in player's inventory.");
    }

    public int getRockExp() {
        return rockExp;
    }

    public void setRockExp(int rockExp) {
        this.rockExp = rockExp;
    }

    public int getPaperExp() {
        return paperExp;
    }

    public void setPaperExp(int paperExp) {
        this.paperExp = paperExp;
    }

    public int getScissorsExp() {
        return scissorsExp;
    }

    public void setScissorsExp(int scissorsExp) {
        this.scissorsExp = scissorsExp;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public void setFireflies(int fireflies) {
        this.fireflies = fireflies;
    }

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }
}
