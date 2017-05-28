package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.GameCharacter;
import com.crackerjacks.game.core.Generator.DungeonGenerator;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * Created by jm on 5/23/17.
 */
public class MainGame extends GameState {

    private int[][] tileMap;

    final private int tileHeight = 32;
    final private int tileWidth = 32;

    final private int roomCount = 4;

    // player character
    private GameCharacter player = new GameCharacter();
    private DungeonGenerator generator;

    // text placement
    int textX;
    int textY = 128;

    public MainGame(Scene scene, GraphicsContext graphicsContext) {
        this.scene = scene;
        this.graphicsContext = graphicsContext;

        onEnter();
    }

    @Override
    void onEnter() throws IndexOutOfBoundsException {

        // create generator for dungeons passing our tilemap as the base
        generator = new DungeonGenerator();

        // initialize tile map
        tileMap = new int[16][16];
        // generate dungeon
        tileMap = generator.generate(tileMap, roomCount);

        // print array map
        for (int i = 0; i < tileMap.length; i++) { // y
            for (int j = 0; j < tileMap.length; j++) { // x
                System.out.print(tileMap[i][j]);
            }
            System.out.println();
        }

        // text placement
        textX = tileMap.length * tileWidth + 64;
        textY = 128;

        // set player position
        player.setName("Kangkong");
        player.setX(3);
        player.setY(3);

        // initialize player controller
        scene.setOnKeyPressed(event -> {

            String code = event.getCode().toString();

            // move up
            if(code.equals("UP")) {
                int tempY = (int) player.getY() - 1;

                if (tileMap[tempY][(int)player.getX()] != 0) {
                    player.setY(tempY);
                }
            }

            // move down
            if(code.equals("DOWN")) {
                int tempY = (int) player.getY() + 1;

                if (tileMap[tempY][(int)player.getX()] != 0) {
                    player.setY(tempY);
                }
            }

            // move left
            if(code.equals("LEFT")) {
                int tempX = (int) player.getX() - 1;

                if (tileMap[(int) player.getY()][tempX] != 0) {
                    player.setX(tempX);
                }
            }

            // move right
            if(code.equals("RIGHT")) {
                int tempX = (int) player.getX() + 1;

                if (tileMap[(int) player.getY()][tempX] != 0) {
                    player.setX(tempX);
                }
            }

            // generate new dungeon rooms
            if(code.equals("ENTER")) {
                tileMap = generator.generate(tileMap, roomCount);
                System.out.println("New Dungeon Generated");
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

        for(int i = 0; i < tileMap.length; i++) { // iterate through the rows
            for(int j = 0; j < tileMap.length; j++) { // iterate through the columns

                if (tileMap[i][j] == 1) { // if point is traversable and a room
                    graphicsContext.setFill(Color.WHITE);
                    graphicsContext.fillRect(j*tileHeight, i*tileWidth, tileHeight, tileWidth);
                }

                if (tileMap[i][j] == 2) { // if point is traversable and a corridor
                    graphicsContext.setFill(Color.YELLOW);
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
        graphicsContext.fillText("Press 'ENTER' to generate new rooms",
                textX - 128, textY + 32 * 14);
    }

    @Override
    void onExit() {

    }
}
