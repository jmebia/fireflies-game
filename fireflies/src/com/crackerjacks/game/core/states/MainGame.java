package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.character.Enemy;
import com.crackerjacks.game.core.character.Interaction;
import com.crackerjacks.game.core.dungeonGenerator.Generator;
import com.crackerjacks.game.core.input.Controller;
import com.crackerjacks.game.core.character.GameCharacter;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by jm on 5/23/17.
 */
public class MainGame extends GameState {

    // 3D camera for the scene
    private PerspectiveCamera camera = new PerspectiveCamera(true);

    // elements for the dungeon map
    private int[][] tileMap;
    final private int mapSize = 40;
    final private int grids = 4;
    final private int roomSize = 5;

    // size of the dungeon when drawn on screen
    final private int tileHeight = 16;
    final private int tileWidth = 16;

    // player character
    private GameCharacter player = new GameCharacter();
    private Generator generator;

    // enemies
    private ArrayList<Enemy> enemies = new ArrayList<>();

    // player controller
    private Controller controller;

    // text placement
    private int textX;
    private int textY = 128;

    public MainGame(Scene scene, GraphicsContext graphicsContext) {
        this.scene = scene;
        this.graphicsContext = graphicsContext;

        // set up camera
        camera.setTranslateZ(-1000);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(20);
        scene.setCamera(camera);

        onEnter();
    }

    @Override
    void onEnter() throws IndexOutOfBoundsException {

        // create generator for dungeons passing our tilemap as the base
        generator = new Generator(mapSize, grids, roomSize);

        // initialize tile map
        tileMap = new int[mapSize][mapSize];
        // generateDungeon dungeon
        System.out.println("Generating Dungeon");
        generator.generateDungeon();
        tileMap = generator.getDungeon();

        // text placement
        textX = tileMap.length * tileWidth + 64;
        textY = 128;

        // set player position
        player.setName("Jean Gadot");
        player.setX(5);
        player.setY(5);
        player.setDamage(5);

        // player controller
        controller = new Controller(scene);

        // place enemies
        enemies.addAll(generator.getEnemies());

    }

    @Override
    void update(long time) {

        /** handle player input */

        LinkedList input = controller.getInputs();

        try {

            GameCharacter enemy = null;

            // move up
            if (input.getLast().equals("UP")) {
                int tempY = (int) player.getY() - 1;

                //check if there is an enemy in the direction
                for(GameCharacter e: enemies) {
                    if (e.getX() == player.getX() && e.getY() == tempY) {
                        enemy = e;
                        break;
                    }
                }

                if (enemy != null) {
                    new Interaction().attackMove(player, enemy);
                    if (enemy.getCurrentHealth() <= 0)
                        enemies.remove(enemy);
                }
                else if (tileMap[tempY][(int) player.getX()] > 0) {
                    player.setY(tempY);
                }

                updateEnemy();
            }

            // move down
            else if (input.getLast().equals("DOWN")) {
                int tempY = (int) player.getY() + 1;

                //check if there is an enemy in the direction
                for(GameCharacter e: enemies) {
                    if (e.getX() == player.getX() && e.getY() == tempY) {
                        enemy = e;
                        break;
                    }
                }

                if (enemy != null) {
                    new Interaction().attackMove(player, enemy);
                    if (enemy.getCurrentHealth() <= 0)
                        enemies.remove(enemy);
                }
                else if (tileMap[tempY][(int) player.getX()] > 0) {
                    player.setY(tempY);
                }

                updateEnemy();
            }

            // move left
            else if (input.getLast().equals("LEFT")) {
                int tempX = (int) player.getX() - 1;

                //check if there is an enemy in the direction
                for(GameCharacter e: enemies) {
                    if (e.getX() == tempX && e.getY() == player.getY()) {
                        enemy = e;
                        break;
                    }
                }

                if (enemy != null) {
                    new Interaction().attackMove(player, enemy);
                    if (enemy.getCurrentHealth() <= 0)
                        enemies.remove(enemy);
                }
                else if (tileMap[(int) player.getY()][tempX] > 0) {
                    player.setX(tempX);
                }

                updateEnemy();
            }

            // move right
            else if (input.getLast().equals("RIGHT")) {
                int tempX = (int) player.getX() + 1;

                //check if there is an enemy in the direction
                for(GameCharacter e: enemies) {
                    if (e.getX() == tempX && e.getY() == player.getY()) {
                        enemy = e;
                        break;
                    }
                }

                if (enemy != null) {
                    new Interaction().attackMove(player, enemy);
                    if (enemy.getCurrentHealth() <= 0)
                        enemies.remove(enemy);
                }
                else if (tileMap[(int) player.getY()][tempX] > 0) {
                    player.setX(tempX);
                }

                updateEnemy();
            }

            // generateDungeon new dungeon rooms
            if (input.getLast().equals("ENTER")) {
                generator.generateDungeon();
                System.out.println("New Dungeon Generated");
                tileMap = generator.getDungeon();
                enemies.clear();
                enemies.addAll(generator.getEnemies());
            }

            // zoom camera in
            if (input.getLast().equals("X")) {
                camera.setFieldOfView(camera.getFieldOfView() - 5);
            }
            // zoom camera out of dungeon
            if (input.getLast().equals("Z")) {
                camera.setFieldOfView(camera.getFieldOfView() + 5);
            }

            controller.clearInputs();

        } catch (NoSuchElementException e) {
            // handle
        }

        // reposition camera depending on player position
        camera.setTranslateX(player.getX() * tileWidth + 500);
        camera.setTranslateY(player.getY() * tileHeight + 500);


    }

    public void updateEnemy() {
        // update enemies
        for (Enemy enemy: enemies) {
            enemy.updateBehavior(player, enemies, tileMap);
        }

    }

    @Override
    void draw() {
        int startX = 500;
        int startY = 500;

        // reset screen
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, graphicsContext.getCanvas().getWidth(),
                graphicsContext.getCanvas().getHeight());

        for(int i = 0; i < tileMap.length; i++) { // iterate through the rows
            for(int j = 0; j < tileMap.length; j++) { // iterate through the columns

                if (tileMap[i][j] == 1 || tileMap[i][j] == 3) { // if point is traversable and a room
                    graphicsContext.setFill(Color.DARKGRAY);
                    graphicsContext.fillRect(j*tileHeight+startY, i*tileWidth + startX, tileHeight, tileWidth);
                }

                else if (tileMap[i][j] == 2) { // if point is traversable and a corridor
                    graphicsContext.setFill(Color.GRAY);
                    graphicsContext.fillRect(j*tileHeight + startY, i*tileWidth + startX, tileHeight, tileWidth);
                }
            }
        }

        /** draw characters in the map */
        // player character
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillRect(player.getX()*tileHeight+startY, player.getY()*tileWidth+startY,
                tileHeight, tileWidth);

        // draw enemies
        for (GameCharacter enemy : enemies) {
            graphicsContext.setFill(Color.GREEN);
            graphicsContext.fillRect(enemy.getX()*tileHeight+startX, enemy.getY()*tileWidth+startY,
                    tileHeight, tileWidth);
        }

        /** draw HUD */
        graphicsContext.setFill(Color.DARKBLUE);
        // draw hud background
        graphicsContext.fillRect(camera.getTranslateX() - 240, camera.getTranslateY() - 180,
                500, 50);

        graphicsContext.setFill(Color.WHITE);
        // health
        graphicsContext.fillText("HP : " + player.getCurrentHealth(),
                camera.getTranslateX() - 200,
                camera.getTranslateY() - 140 );


    }

    @Override
    void onExit() {

    }
}
