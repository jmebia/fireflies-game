package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.GameCharacter;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by jm on 5/23/17.
 */
public class MainGame extends GameState {

    private int[][] tileMap;

    private int tileHeight = 32;
    private int tileWidth = 32;

    // player character
    private GameCharacter player;

    // text placement
    int textX;
    int textY;

    public MainGame(Scene scene, GraphicsContext graphicsContext) {
        this.scene = scene;
        this.graphicsContext = graphicsContext;
        onEnter();
    }

    @Override
    void onEnter() {

        // initialize tile maps for collision/obstacles
        tileMap = new int[][] {
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,0,1,0,0,0,0,0,1,0,0,0,1},
                {1,0,0,0,0,1,0,0,0,0,0,1,0,0,0,1},
                {1,0,0,0,0,1,1,1,0,1,1,1,0,0,0,1},
                {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,1,1,0,1,1,0,0,0,1},
                {1,0,0,0,0,0,1,1,0,0,0,1,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1},
                {1,0,0,0,0,0,1,0,0,0,0,1,0,0,0,1},
                {1,0,0,0,0,0,1,1,1,0,0,1,0,0,0,1},
                {1,0,0,0,0,0,0,0,1,1,1,1,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };

        // text placement
        textX = tileMap.length * tileWidth + 64;
        textY = 128;

        // set player position
        player = new GameCharacter();
        player.setName("Kangkong");
        player.setX(3);
        player.setY(3);

        // initialize player controller
        scene.setOnKeyPressed(event -> {

            String code = event.getCode().toString();

            // move up
            if(code.equals("UP")) {
                int tempY = (int) player.getY() - 1;

                if (tileMap[tempY][(int)player.getX()] == 0) {
                    player.setY(tempY);
                }
            }

            // move down
            if(code.equals("DOWN")) {
                int tempY = (int) player.getY() + 1;

                if (tileMap[tempY][(int)player.getX()] == 0) {
                    player.setY(tempY);
                }
            }

            // move left
            if(code.equals("LEFT")) {
                int tempX = (int) player.getX() - 1;

                if (tileMap[(int) player.getY()][tempX] == 0) {
                    player.setX(tempX);
                }
            }

            // move right
            if(code.equals("RIGHT")) {
                int tempX = (int) player.getX() + 1;

                if (tileMap[(int) player.getY()][tempX] == 0) {
                    player.setX(tempX);
                }
            }

        } );

    }

    @Override
    void update() {


    }

    @Override
    void draw() {

        // reset screen
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, 800, 600);

        // draw traversable terrain from tile map for obstacles
        graphicsContext.setFill(Color.WHITE);

        for(int i = 0; i < tileMap.length; i++) { // iterate through the rows
            for(int j = 0; j < tileMap.length; j++) { // iterate through the columns

                if (tileMap[i][j] == 0) { // if point is traversable
                    graphicsContext.fillRect(j*tileHeight, i*tileWidth, tileHeight, tileWidth);

                }

            }
        }

        //draw characters in the map
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRect(player.getX()*tileHeight, player.getY()*tileWidth, tileHeight, tileWidth);

        graphicsContext.setFill(Color.ALICEBLUE);
        graphicsContext.fillText(player.getName(), textX, textY);
        graphicsContext.fillText("Health: " + player.getHealth(), textX, textY + 32);
        graphicsContext.fillText("Damage: " + player.getDamage(), textX, textY + 64);
        graphicsContext.fillText("Player Position: (" + player.getX() + ", " + player.getY() + ")",
                textX, textY + 128);

    }

    @Override
    void onExit() {

    }
}
