package com.crackerjacks.game.core.character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player extends GameCharacter {

    int levelRock;
    int levelPaper;
    int levelScissors;

    // getters and setters
    public int getLevelRock() {
        return levelRock;
    }

    public void setLevelRock(int levelRock) {
        this.levelRock = levelRock;
    }

    public int getLevelPaper() {
        return levelPaper;
    }

    public void setLevelPaper(int levelPaper) {
        this.levelPaper = levelPaper;
    }

    public int getLevelScissors() {
        return levelScissors;
    }

    public void setLevelScissors(int levelScissors) {
        this.levelScissors = levelScissors;
    }

    public void draw(GraphicsContext graphicsContext, int startX, int startY, int tileHeight, int tileWidth) {
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.fillRect(getX() * tileHeight + startX, getY() * tileWidth + startY,
                tileHeight, tileWidth);
    }

}
