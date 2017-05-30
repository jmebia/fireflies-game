package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.GameCharacter;
import com.crackerjacks.game.core.generator.DungeonGenerator;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by jm on 5/23/17.
 */
public class MainGame extends GameState {

    private int[][] tileMap;

    final private int mapSize = 32;

    final private int tileHeight = 16;
    final private int tileWidth = 16;

    final private int roomCount = 7;
    final private int roomSize = 7;

    // player character
    private GameCharacter player = new GameCharacter();
    private DungeonGenerator generator;

    // enemies
    private ArrayList<GameCharacter> enemies = new ArrayList<>();

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
        tileMap = new int[mapSize][mapSize];
        // generateDungeon dungeon
        System.out.println("Generating Dungeon");
        generator.generateDungeon(tileMap, roomCount, roomSize);
        tileMap = generator.getDungeonMap();

        // text placement
        textX = tileMap.length * tileWidth + 64;
        textY = 128;

        // set player position
        player.setName("Kangkong");
        player.setX(generator.getPlayerPosition().getX());
        player.setY(generator.getPlayerPosition().getY());

        // place enemies
        int[][] enemyMap = generator.getEnemyMap();

        for (int y = 0; y < enemyMap.length ; y++) {
            for (int x = 0; x < enemyMap.length; x++) {
                if (enemyMap[y][x] == 1) {
                    GameCharacter e = new GameCharacter();
                    e.setName("Enemy");
                    e.setX(x);
                    e.setY(y);
                    enemies.add(e);
                }
            }
        }

        // player controller
        scene.setOnKeyPressed(event -> {

            String code = event.getCode().toString();

            // move up
            if(code.equals("UP")) {
                int tempY = (int) player.getY() - 1;

                if (tileMap[tempY][(int)player.getX()] > 0) {
                    player.setY(tempY);
                }
            }

            // move down
            if(code.equals("DOWN")) {
                int tempY = (int) player.getY() + 1;

                if (tileMap[tempY][(int)player.getX()] > 0) {
                    player.setY(tempY);
                }
            }

            // move left
            if(code.equals("LEFT")) {
                int tempX = (int) player.getX() - 1;

                if (tileMap[(int) player.getY()][tempX] > 0) {
                    player.setX(tempX);
                }
            }

            // move right
            if(code.equals("RIGHT")) {
                int tempX = (int) player.getX() + 1;

                if (tileMap[(int) player.getY()][tempX] > 0) {
                    player.setX(tempX);
                }
            }

            // generateDungeon new dungeon rooms
            if(code.equals("ENTER")) {
                generator.generateDungeon(tileMap, roomCount, roomSize);
                System.out.println("New Dungeon Generated");
                tileMap = generator.getDungeonMap();

                // replace player
                player.setX(generator.getPlayerPosition().getX());
                player.setY(generator.getPlayerPosition().getY());

                // replace enemies
                int[][] enemyMap2 = generator.getEnemyMap();

                enemies.clear();
                for (int y = 0; y < enemyMap2.length ; y++) {
                    for (int x = 0; x < enemyMap2.length; x++) {
                        if (enemyMap2[y][x] == 1) {
                            GameCharacter e = new GameCharacter();
                            e.setName("Enemy");
                            e.setX(x);
                            e.setY(y);
                            enemies.add(e);
                        }
                    }
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

        for(int i = 0; i < tileMap.length; i++) { // iterate through the rows
            for(int j = 0; j < tileMap.length; j++) { // iterate through the columns

                if (tileMap[i][j] == 1 || tileMap[i][j] == 3) { // if point is traversable and a room
                    graphicsContext.setFill(Color.WHITE);
                    graphicsContext.fillRect(j*tileHeight, i*tileWidth, tileHeight, tileWidth);
                }

                else if (tileMap[i][j] == 2) { // if point is traversable and a corridor
                    graphicsContext.setFill(Color.YELLOW);
                    graphicsContext.fillRect(j*tileHeight, i*tileWidth, tileHeight, tileWidth);
                }

                else if (tileMap[i][j] == -1) { // if point is not traversable and a wall
                    graphicsContext.setFill(Color.DARKGRAY);
                    graphicsContext.fillRect(j*tileHeight, i*tileWidth, tileHeight, tileWidth);
                }

            }
        }

        //draw characters in the map
        // player character
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRect(player.getX()*tileHeight, player.getY()*tileWidth, tileHeight, tileWidth);

        // enemies
        graphicsContext.setFill(Color.PINK);
        for (GameCharacter enem: enemies) {
            graphicsContext.fillRect(enem.getX()*tileHeight, enem.getY()*tileWidth, tileHeight, tileWidth);
        }

        graphicsContext.setFill(Color.ALICEBLUE);
        graphicsContext.fillText(player.getName(), textX, textY);
        graphicsContext.fillText("Health: " + player.getHealth(), textX, textY + 32);
        graphicsContext.fillText("Damage: " + player.getDamage(), textX, textY + 64);
        graphicsContext.fillText("Player Position: (" + player.getX() + ", " + player.getY() + ")",
                textX, textY + 128);
        graphicsContext.fillText("Press 'ENTER' to generate a new dungeon",
                textX - 128, textY + 32 * 14);
    }

    @Override
    void onExit() {

    }
}
