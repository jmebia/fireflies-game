package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.character.Enemy;
import com.crackerjacks.game.core.character.Player;
import com.crackerjacks.game.core.dungeonGenerator.Generator;
import com.crackerjacks.game.core.input.Controller;
import com.crackerjacks.game.core.input.Mover;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jm on 5/23/17.
 */
public class MainGame extends GameState {

    // 3D camera for the scene
    private PerspectiveCamera camera = new PerspectiveCamera(true);

    // elements for the dungeon map
    private int[][] tileMap;
    final private int mapSize = 60;
    final private int grids = 4;
    final private int roomSize = 7;

    // size of the dungeon when drawn on screen
    final private int tileHeight = 16;
    final private int tileWidth = 16;

    // player character
    private Player player;
    private Generator generator;

    // enemies
    private ArrayList<Enemy> enemies = new ArrayList<>();

    // goal
    Point goal;

    // player controller
    private Controller controller;
    private Mover mover;

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

        generateNewDungeon();

        // player controller
        controller = new Controller(scene);
        mover = new Mover();

        // place enemies
        enemies.addAll(generator.getEnemies());

    }

    @Override
    void update(long time) {

        /* handle player input */
        mover.update(controller, player, enemies, tileMap, camera);


        if (player.getCurrentHealth() <= 0) {
            generateNewDungeon();
        }

        // check if player is in goal, if yes then generate new dungeon
        if (player.getX() == goal.getX() && player.getY() == goal.getY()) {
            generateNewDungeon();
        }

        // reposition camera depending on player position
        camera.setTranslateX(player.getX() * tileWidth + 500);
        camera.setTranslateY(player.getY() * tileHeight + 500);


    }

    private void generateNewDungeon() {
        generator.generateDungeon();
        System.out.println("New Dungeon Generated");
        tileMap = generator.getDungeon();
        enemies.clear();
        enemies.addAll(generator.getEnemies());
        player = new Player();
        player.setName("Jean Gadot");
        player.setX(generator.getPlayerPosition().getX());
        player.setY(generator.getPlayerPosition().getY());
        player.setDamage(5);
        goal = new Point();
        goal.setLocation(generator.getGoalPosition().getX(), generator.getGoalPosition().getY());
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

        /* draw characters in the map */
        // draw player character
        player.draw(graphicsContext, startX, startY, tileHeight, tileWidth);

        // draw enemies
        for (Enemy enemy : enemies) {
            enemy.draw(graphicsContext, startX, startY, tileHeight, tileWidth);
        }

        // draw goal
        graphicsContext.setFill(Color.BROWN);
        graphicsContext.fillRect(goal.getX() * tileWidth + startY, goal.getY() * tileHeight + startY,
                tileHeight, tileWidth);

        // draw attack side
        if (mover.getAttackMode()) {
            // transparent red
            graphicsContext.setFill(new Color(1.0f, 0.0f, 0.0f, 0.5f));
            if(mover.getAttackSide()== "left")
                graphicsContext.fillRect((player.getX() - 1) * tileWidth + startX,
                        player.getY() * tileHeight + startY, tileWidth, tileHeight );
            else if(mover.getAttackSide()=="right")
                graphicsContext.fillRect((player.getX() + 1) * tileWidth + startX,
                        player.getY() * tileHeight + startY, tileWidth, tileHeight );
            else if(mover.getAttackSide()=="up")
                graphicsContext.fillRect(player.getX() * tileWidth + startX,
                        (player.getY() - 1) * tileHeight + startY , tileWidth, tileHeight );
            else if(mover.getAttackSide()=="down")
                graphicsContext.fillRect(player.getX() * tileWidth + startX,
                        (player.getY() + 1) * tileHeight + startY, tileWidth, tileHeight );
        }

        /* draw HUD */
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
