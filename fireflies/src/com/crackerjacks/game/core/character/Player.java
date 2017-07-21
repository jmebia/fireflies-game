package com.crackerjacks.game.core.character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player extends GameCharacter {

    /*
    Element element; // player element rock paper or scissors

     */

    /*
    public void update() {

    }
    */

    public void draw(GraphicsContext graphicsContext, int startX, int startY, int tileHeight, int tileWidth) {
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillRect(getX() * tileHeight + startY, getY() * tileWidth + startY,
                tileHeight, tileWidth);
    }

}
