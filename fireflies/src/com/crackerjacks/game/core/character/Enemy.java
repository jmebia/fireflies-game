package com.crackerjacks.game.core.character;

import com.crackerjacks.game.core.animator.SpriteAnimator;
import com.crackerjacks.game.core.interactions.Element;
import com.crackerjacks.game.core.interactions.Interaction;
import com.crackerjacks.game.core.interactions.Type;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Enemy.java
 *
 * Enemy class that contains the default GameCharacter elements and enemy specific
 * behavior functions.
 *
 *
 */
public class Enemy extends GameCharacter {

    // genotypes
    private Element element;
    private Type type;

    private SpriteAnimator spriteView;
    private ImageView image;

    int visionRadius = 3;

    // getters and setters
    public Element getElement() {
        return element;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setSpriteView(SpriteAnimator animator) {
        this.spriteView = animator;
    }

    public SpriteAnimator getSpriteView() {
        return this.spriteView;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public ImageView getImage() {
        return image;
    }

    // main methods

    // contains the enemy behavior tree
    public void update(Player player, ArrayList<Enemy> enemies, int[][] tilemap) {
        System.out.println("<=== Updating "+getName()+" ===>");

        // handle dumb A.I. here
        double playerX = player.getX();
        double playerY = player.getY();

        // if player is beside enemy, attack player
        if ( (playerX == getX() + 1 && playerY == getY()) || (playerX == getX() - 1 && playerY == getY())
                || (playerX == getX() && playerY == getY() + 1)
                || (playerX == getX() && playerY == getY()-1)) {
            new Interaction().attackMove(this, player);
        }

        // if player is within the vision radius
        // straight east; X++
        else if (playerX >= getX() && playerX <= getX() + visionRadius
                && playerY == getY()) {
            double i = getX() + 1;
            if (!checkCollisions(enemies, tilemap, i, getY())) {
                this.setX(i);
                System.out.println("Enemy " + getName() + " moved!");
            }
        }
        // straight west; X--
        else if (playerX <= getX() && playerX >= getX() - visionRadius
                && playerY == getY()) {
            double i = getX() - 1;
            if (!checkCollisions(enemies, tilemap, i, getY())) {
                this.setX(i);
                System.out.println("Enemy " + getName() + " moved!");
            }
        }
        // straight north; Y--
        else if (playerY <= getY() && playerY >= getY() - visionRadius
                && playerX == getX()) {
            double i = getY() - 1;
            if (!checkCollisions(enemies, tilemap, getX(), i)) {
                this.setY(i);
                System.out.println("Enemy " + getName() + " moved!");
            }
        }
        // straight south; Y++
        else if (playerY >= getY() && playerY <= getY() + visionRadius
                && playerX == getX()) {
            double i = getY() + 1;
            if (!checkCollisions(enemies, tilemap, getX(), i)) {
                this.setY(i);
                System.out.println("Enemy " + getName() + " moved!");
            }
        }
        // first quadrant
        else if(playerX >= getX() && playerX <= getX() + visionRadius
                && playerY <= getY() && playerY >= getY() - visionRadius) {
            if (Math.abs(getX() - playerX) < Math.abs(playerY - getX())) {
                // check collisions before moving through X space
                double i = getX() + 1;
                if (!checkCollisions(enemies, tilemap, i, getY())) {
                    this.setX(i);
                    System.out.println("Enemy " + getName() + " moved!");
                }
            }
            else {
                // check collisions before moving through Y space
                double i = getY() - 1;
                if (!checkCollisions(enemies, tilemap, getX(), i)) {
                    this.setY(i);
                    System.out.println("Enemy " + getName() + " moved!");
                }
            }
        }
        // second quadrant
        else if(playerX <= getX() && playerX >= getX() - visionRadius
                && playerY <= getY() && playerY >= getY() - visionRadius) {
            if (Math.abs(getX() - playerX) < Math.abs(playerY - getX())) {
                // check collisions before moving through X space
                double i = getX() - 1;
                if (!checkCollisions(enemies, tilemap, i, getY())) {
                    this.setX(i);
                    System.out.println("Enemy " + getName() + " moved!");
                }
            }
            else {
                // check collisions before moving through Y space
                double i = getY() - 1;
                if (!checkCollisions(enemies, tilemap, getX(), i)) {
                    this.setY(i);
                    System.out.println("Enemy " + getName() + " moved!");
                }
            }
        }
        // third quadrant
        else if(playerX <= getX() && playerX >= getX() - visionRadius
                && playerY >= getY() && playerY <= getY() + visionRadius) {
            if (Math.abs(getX() - playerX) < Math.abs(playerY - getX())) {
                // check collisions before moving through X space
                double i = getX() - 1;
                if (!checkCollisions(enemies, tilemap, i, getY())) {
                    this.setX(i);
                    System.out.println("Enemy " + getName() + " moved!");
                }
            }
            else {
                // check collisions before moving through Y space
                double i = getY() + 1;
                if (!checkCollisions(enemies, tilemap, getX(), i)) {
                    this.setY(i);
                    System.out.println("Enemy " + getName() + " moved!");
                }
            }
        }
        // fourth quadrant
        else if(playerX >= getX() && playerX <= getX() + visionRadius
                && playerY >= getY() && playerY <= getY() + visionRadius) {
            if (Math.abs(getX() - playerX) < Math.abs(playerY - getX())) {
                // check collisions before moving through X space
                double i = getX() + 1;
                if (!checkCollisions(enemies, tilemap, i, getY())) {
                    this.setX(i);
                    System.out.println("Enemy " + getName() + " moved!");
                }
            }
            else {
                // check collisions before moving through Y space
                double i = getY() + 1;
                if (!checkCollisions(enemies, tilemap, getX(), i)) {
                    this.setY(i);
                    System.out.println("Enemy " + getName() + " moved!");
                }
            }
        }

        // if no one is around enemy, move around mindlessly
        else {
            int axis = new Random().nextInt(2);
            // 0 = x , 1 = y
            if (axis == 0) {
                double i = getX() + new Random().nextInt(3) - 1;
                if (!checkCollisions(enemies, tilemap, i, getY())) {
                    this.setX(i);
                    System.out.println("Enemy " + getName() + " moved!");
                }
            } else if (axis == 1) {
                double i = getY() + new Random().nextInt(3) - 1;
                if (!checkCollisions(enemies, tilemap, getX(), i)) {
                    this.setY(i);
                    System.out.println("Enemy " + getName() + " moved!");
                }
            }
        }

        System.out.println("Enemy " + getName() + " updated!");
    }

    public void draw(GraphicsContext graphicsContext, Image image, int offsetX, int offsetY,
                     int startX, int startY, int tileHeight, int tileWidth) {

        // graphicsContext.setFill(getElement().getColor());
        // graphicsContext.fillRect(getX()*tileHeight+startX, getY()*tileWidth+startY,
        //       tileHeight, tileWidth);

        graphicsContext.drawImage(image, offsetX, offsetY, tileWidth, tileHeight, getX()*tileWidth+startX, getY()*tileWidth+startY, tileWidth, tileHeight);
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

        return res;
    }

}
