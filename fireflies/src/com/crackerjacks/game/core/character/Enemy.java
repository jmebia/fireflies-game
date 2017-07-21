package com.crackerjacks.game.core.character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Enemy class that contains the default GameCharacter elements and enemy specific behavior functions.
 */
public class Enemy extends GameCharacter {

    /*
    Element element;
    Behavior behavior; // AI
     */

    public void update(GameCharacter player, ArrayList<Enemy> enemies, int[][] tilemap) {

        // handle dumb A.I. here
        double playerX = player.getX();
        double playerY = player.getY();

        // if player is beside enemy, attack player
        if ( (playerX == getX() + 1 && playerY == getY()) || (playerX == getX() - 1 && playerY == getY())
                || (playerX == getX() && playerY == getY() + 1)
                || (playerX == getX() && playerY == getY()-1))
            new Interaction().attackMove(this, player);

        // if no one is around enemy, move around mindlessly
        else {
            int axis = new Random().nextInt(2);
            // 0 = x , 1 = y
            if (axis == 0) {
                double i = getX() + new Random().nextInt(3) - 1;
                System.out.println("i = " + (i - getX()));
                if (!checkCollisions(enemies, tilemap, i, getY()))
                    this.setX(i);
            } else {
                double i = getY() + new Random().nextInt(3) - 1;
                System.out.println("i = " + (i - getY()));
                if (!checkCollisions(enemies, tilemap, getX(), i))
                    this.setY(i);
            }
        }

    }

    public void draw(GraphicsContext graphicsContext, int startX, int startY, int tileHeight, int tileWidth) {

        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRect(getX()*tileHeight+startX, getY()*tileWidth+startY,
                tileHeight, tileWidth);

    }

    private boolean checkCollisions(ArrayList<Enemy> enemies, int[][] tile,  double posX, double posY) {
        boolean res = false;

        // iterates through enemies to check collision
        for (Enemy e : enemies) {
            try {
                if ( (e.getX() == posX && e.getY() == posY)
                        || (tile[(int) posY][(int) posX] == 0) ) {
                    res = true;
                    break;
                }
            } catch (IndexOutOfBoundsException event) {
                res = true;
                break;
            }
        }

        System.out.println(tile[(int) posY][(int) posX] + " - " + res);

        return res;
    }

}
