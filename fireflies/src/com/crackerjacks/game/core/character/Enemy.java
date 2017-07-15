package com.crackerjacks.game.core.character;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jm on 7/13/17.
 */
public class Enemy extends GameCharacter {

    public void updateBehavior(GameCharacter player, ArrayList<Enemy> enemies, int[][] tilemap) {

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
                if (checkCollisions(enemies, tilemap, i, getY()) == false)
                    this.setX(i);
            } else {
                double i = getY() + new Random().nextInt(3) - 1;
                System.out.println("i = " + (i - getY()));
                if (checkCollisions(enemies, tilemap, getX(), i) == false)
                    this.setY(i);
            }
        }

    }

    private boolean checkCollisions(ArrayList<Enemy> enemies, int[][] tile,  double posX, double posY) {
        boolean res = false;

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
