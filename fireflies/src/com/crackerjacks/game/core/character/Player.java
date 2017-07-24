package com.crackerjacks.game.core.character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player extends GameCharacter {

    boolean rock = true;
    boolean paper = false;
    boolean scissors = false;

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

}
