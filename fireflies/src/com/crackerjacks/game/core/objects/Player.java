package com.crackerjacks.game.core.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Player extends GameCharacter {

    private int rockLevel = 0;
    private int paperLevel = 0;
    private int scissorsLevel = 0;

    private boolean rock = true;
    private boolean paper = false;
    private boolean scissors = false;

    private ArrayList<Item> inventory = new ArrayList<>();

    private int fireflies = 0;

    private int lineOfSight = 5;

    // getters and setters
    public void activateRock() {
        rock = true;
        paper = false;
        scissors = false;
    }
    public void activatePaper() {
        rock = false;
        paper = true;
        scissors = false;
    }
    public void activateScissors() {
        rock = false;
        paper = false;
        scissors = true;
    }

    public boolean isRock() {
        return rock;
    }
    public boolean isPaper() {
        return paper;
    }
    public boolean isScissors() {
        return scissors;
    }

    public void draw(GraphicsContext graphicsContext, int startX, int startY, int tileHeight, int tileWidth) {
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.fillRect(getX() * tileHeight + startX, getY() * tileWidth + startY,
                tileHeight, tileWidth);
    }

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

}
