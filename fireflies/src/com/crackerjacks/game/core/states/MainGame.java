package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.animator.SpriteAnimator;
import com.crackerjacks.game.core.objects.Enemy;
import com.crackerjacks.game.core.objects.Player;
import com.crackerjacks.game.core.dungeonGenerator.Generator;
import com.crackerjacks.game.core.input.InputHandler;
import com.crackerjacks.game.core.input.Controller;
import com.crackerjacks.game.core.interactions.Element;
import com.crackerjacks.game.core.interactions.Type;
import com.crackerjacks.game.core.io.Save;
import com.crackerjacks.game.core.io.SaveIO;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.crackerjacks.game.core.Game.root;

/**
 * Created by jm on 5/23/17.
 */
public class MainGame extends GameState {

    // 3D camera for the scene
    private PerspectiveCamera camera;

    // elements for the dungeon map
    private int[][] tileMap;
    final private int mapSize = 60;
    final private int grids = 4;
    final private int roomSize = 7;

    // design map
    private int[][] designMap;

    // how fast the player slides from one tile to another
    int playerSpeed = 4;

    int startX = 500;
    int startY = 500;

    int YCharmModifier = -28;


    // tile map for the fog of war
    private int[][] fogMap;

    // size of the dungeon when drawn on screen
    final private int tileHeight = 32;
    final private int tileWidth = 32;

    // size of the dungeon when drawn on screen
    final private int charHeight = 48;
    final private int charWidth = 32;

    // player objects
    private Player player;
    private Generator generator;

    // enemies
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Enemy> deadEnemies = new ArrayList<>();

    // goal
    Point goal;

    // player inputHandler
    private InputHandler inputHandler;
    private Controller controller;

    // images for the sprites
    Image characterSprites;
    Image tileSprites;
    // Image background;

    // image container
    ImageView playerSpriteView;

    // identifies if user loaded an existing save or a new game
    private boolean isNewGame;

    public MainGame(Scene scene, GraphicsContext graphicsContext, boolean isNewGame) {
        this.scene = scene;
        this.graphicsContext = graphicsContext;

        this.isNewGame = isNewGame;

        onEnter();

    }

    @Override
    void onEnter() throws IndexOutOfBoundsException {

        if (isNewGame) {

            // create generator for dungeons passing our tilemap as the base
            generator = new Generator(mapSize, grids, roomSize);

            // initialize tile map
            tileMap = new int[mapSize][mapSize];
            designMap = new int[mapSize][mapSize];
            fogMap = new int[120][120];

            // generate dungeon
            System.out.println("Generating Dungeon");
            generateNewDungeon();

            // place enemies
            enemies.addAll(generator.getEnemies());

        } else {
            Save save = Global.getSave();

            generator = save.getGenerator();

            player = save.getPlayer();

            // initialize tile map
            tileMap = save.getTileMap();
            designMap = save.getDesignMap();
            fogMap = save.getFogMap();

            goal = new Point();
            goal.setLocation(generator.getGoalPosition().getX(), generator.getGoalPosition().getY());

            // place enemies
            enemies.addAll(save.getEnemies());
            deadEnemies.addAll(save.getDeadEnemies());
        }

        // player inputHandler
        inputHandler = new InputHandler(scene);
        controller = new Controller();

        // set up camera
        camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-1000);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(30);
        scene.setCamera(camera);

        // load the images of the sprites
        ClassLoader classLoader = getClass().getClassLoader();
        characterSprites = new Image(classLoader.getResource("sprites/char-spritesheet.png").toString());
        tileSprites = new Image(classLoader.getResource("sprites/tiles-spritesheet.png").toString());
        // background = new Image(classLoader.getResource("sprites/space-background.png").toString());

        // setting up the animator
        playerSpriteView = new ImageView(characterSprites);
        playerSpriteView.setViewport(new Rectangle2D(0, 0, charWidth, charHeight));
        playerSpriteView.setTranslateX(player.getX()*tileWidth+startX);
        playerSpriteView.setTranslateY(player.getY()*tileWidth+startY+YCharmModifier);
        root.getChildren().add(playerSpriteView);

    }

    @Override
    void update(long time) {

        /* handle player input */
        controller.update(inputHandler, player, enemies, deadEnemies, tileMap, fogMap, camera);

        if (player.getCurrentHealth() <= 0) {
            generateNewDungeon();
            playerSpriteView.setTranslateX(player.getX()*tileWidth+startX);
            playerSpriteView.setTranslateY(player.getY()*tileHeight+startY+YCharmModifier);
        }

        // check if player is in goal, if yes then generate new dungeon
        if (player.getX() == goal.getX() && player.getY() == goal.getY()) {
            generateNewDungeon();
            playerSpriteView.setTranslateX(player.getX()*tileWidth+startX);
            playerSpriteView.setTranslateY(player.getY()*tileHeight+startY+YCharmModifier);
        }

        // DRAW
        // reset screen
        graphicsContext.setFill(Color.BLACK);
        // graphicsContext.drawImage(background,0, 0, 1920, 1920);
        graphicsContext.fillRect(200, 200, 3000, 3000);

        // draw rooms and corridors
        for(int i = 0; i < designMap.length; i++) { // iterate through the rows
            for(int j = 0; j < designMap.length; j++) { // iterate through the columns

                // ROOM
                if (tileMap[i][j] == 1) {
                    if (designMap[i][j] == generator.getROOM_TOP_LEFT()) {
                        graphicsContext.drawImage(tileSprites, 0, 96, 32, 32, j * tileWidth + startX,
                                i * tileHeight + startY, tileWidth, tileHeight);
                    } else if (designMap[i][j] == generator.getROOM_TOP_CENTER()) {
                        graphicsContext.drawImage(tileSprites, 32, 96, 32, 32, j * tileWidth + startX,
                                i * tileHeight + startY, tileWidth, tileHeight);
                    } else if (designMap[i][j] == generator.getROOM_TOP_RIGHT()) {
                        graphicsContext.drawImage(tileSprites, 64, 96, 32, 32, j * tileWidth + startX,
                                i * tileHeight + startY, tileWidth, tileHeight);
                    } else if (designMap[i][j] == generator.getROOM_LEFT()) {
                        graphicsContext.drawImage(tileSprites, 0, 128, 32, 32, j * tileWidth + startX,
                                i * tileHeight + startY, tileWidth, tileHeight);
                    } else if (designMap[i][j] == generator.getROOM_CENTER()) {
                        graphicsContext.drawImage(tileSprites, 32, 128, 32, 32, j * tileWidth + startX,
                                i * tileHeight + startY, tileWidth, tileHeight);
                    } else if (designMap[i][j] == generator.getROOM_RIGHT()) {
                        graphicsContext.drawImage(tileSprites, 64, 128, 32, 32, j * tileWidth + startX,
                                i * tileHeight + startY, tileWidth, tileHeight);
                    } else if (designMap[i][j] == generator.getROOM_BOTTOM_LEFT()) {
                        graphicsContext.drawImage(tileSprites, 0, 160, 32, 32, j * tileWidth + startX,
                                i * tileHeight + startY, tileWidth, tileHeight);
                    } else if (designMap[i][j] == generator.getROOM_BOTTOM_CENTER()) {
                        graphicsContext.drawImage(tileSprites, 32, 160, 32, 32, j * tileWidth + startX,
                                i * tileHeight + startY, tileWidth, tileHeight);
                    } else if (designMap[i][j] == generator.getROOM_BOTTOM_RIGHT()) {
                        graphicsContext.drawImage(tileSprites, 64, 160, 32, 32, j * tileWidth + startX,
                                i * tileHeight + startY, tileWidth, tileHeight);
                    }
                }
                // CORRIDOR
                else if (tileMap[i][j] == 2) {
                    graphicsContext.drawImage(tileSprites, 96, 0, 32, 32, j * tileWidth + startX,
                            i * tileHeight + startY, tileWidth, tileHeight);
                }
            }
        }

        // draw enemies
        for (Enemy e : enemies) {
            if (fogMap[(int) e.getY()][(int) e.getX()] == 2) {
                // initial offsetY depends on the enemy type,
                // the offsetY modifier depends on the technique/element

                int offsetY = 0;

                // type
                if (e.getType() == Type.a)
                    offsetY = charHeight;
                else if (e.getType() == Type.b)
                    offsetY = charHeight * 4;
                else if (e.getType() == Type.c)
                    offsetY = charHeight * 7;

                // element
                if (e.getElement() == Element.brute)
                    offsetY += 0;
                else if (e.getElement() == Element.stable)
                    offsetY += charHeight;
                else if (e.getElement() == Element.cut)
                    offsetY += charHeight * 2;

                e.draw(graphicsContext, characterSprites, 0, offsetY, startX, startY + YCharmModifier, charHeight, charWidth);

                /*
                e.getImage().setTranslateX(e.getX()*tileWidth+startX);
                e.getImage().setTranslateY(e.getY()*tileWidth+startY+YCharmModifier);
                */
            }
        }

        // draw goal
        graphicsContext.setFill(Color.BROWN);
        graphicsContext.fillRect(goal.getX() * tileWidth + startX, goal.getY() * tileHeight + startY,
                tileHeight, tileWidth);

        // draw fog of war
        for (int y = 0; y < fogMap.length; y++) {
            for (int x = 0; x < fogMap.length; x++) {
                if (fogMap[y][x] == 0) {
                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.fillRect(x * tileWidth + startX, y * tileHeight + startY,
                            tileHeight, tileWidth);
                } else if (fogMap[y][x] == 1) {
                    graphicsContext.setFill(new Color(0f,0f,0f,0.5));
                    graphicsContext.fillRect(x * tileWidth + startX, y * tileHeight + startY,
                            tileHeight, tileWidth);
                }

            }
        }

        // draw attack side
        if (controller.getAttackMode()) {
            // transparent red
            graphicsContext.setFill(new Color(1.0f, 0.0f, 0.0f, 0.5f));
            if(controller.getAttackSide()== "left")
                graphicsContext.fillRect((player.getX() - 1) * tileWidth + startX,
                        player.getY() * tileHeight + startY, tileWidth, tileHeight );
            else if(controller.getAttackSide()=="right")
                graphicsContext.fillRect((player.getX() + 1) * tileWidth + startX,
                        player.getY() * tileHeight + startY, tileWidth, tileHeight );
            else if(controller.getAttackSide()=="up")
                graphicsContext.fillRect(player.getX() * tileWidth + startX,
                        (player.getY() - 1) * tileHeight + startY , tileWidth, tileHeight );
            else if(controller.getAttackSide()=="down")
                graphicsContext.fillRect(player.getX() * tileWidth + startX,
                        (player.getY() + 1) * tileHeight + startY, tileWidth, tileHeight );
        }


        // makes the player's sprite slide from one tile to another and snaps the sprite to the supposed tile placement
        // checks through the X axis
        if (playerSpriteView.getTranslateX() < player.getX()*tileWidth+startX) {
            playerSpriteView.setTranslateX(playerSpriteView.getTranslateX() + playerSpeed);
        } else if (playerSpriteView.getTranslateX() > player.getX()*tileWidth+startX) {
            playerSpriteView.setTranslateX(playerSpriteView.getTranslateX() - playerSpeed);
        }
        // checks through the Y axis
        if (playerSpriteView.getTranslateY() < player.getY()*tileWidth+startY+YCharmModifier) {
            playerSpriteView.setTranslateY(playerSpriteView.getTranslateY() + playerSpeed);
        } else if (playerSpriteView.getTranslateY() > player.getY()*tileWidth+startY+YCharmModifier) {
            playerSpriteView.setTranslateY(playerSpriteView.getTranslateY() - playerSpeed);
        }
        // checks if both X and Y coordinates of the player sprite is equal to the supposed tile placement of the
        // player in the 2D game space
        if ((playerSpriteView.getTranslateX() == player.getX()*tileWidth+startX)
                && (playerSpriteView.getTranslateY() == player.getY()*tileWidth+startY+YCharmModifier)) {
            inputHandler.setDisabled(false);
            controller.unfog(player, fogMap, inputHandler);
        }


        // reposition camera depending on player and map
        camera.setTranslateX(playerSpriteView.getTranslateX());
        camera.setTranslateY(playerSpriteView.getTranslateY());

        /* draw HUD */
        double hudx = camera.getTranslateX() - 356;
        double hudy = camera.getTranslateY() - 269;
        graphicsContext.setFill(Color.DARKBLUE);
        // draw hud background
        graphicsContext.fillRect(hudx, hudy, 800, 70);

        // health
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText("Health ",hudx + 10, hudy + 20);
        // player's fireflies essence
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText("FireFlies ", hudx + 250, hudy + 20);

        // player's fireflies essence
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText(""+player.getFireflies(), hudx + 330, hudy + 20);

        // health
        graphicsContext.fillText(player.getCurrentHealth()+"/"+player.getMaxHealth()
                , hudx + 80, hudy + 20);

    }

    @Override
    void onExit() {

        Save save = new Save();
        save.setPlayer(player);
        save.setTileMap(tileMap);
        save.setDesignMap(designMap);
        save.setFogMap(fogMap);
        save.setEnemies(enemies);
        save.setDeadEnemies(deadEnemies);
        save.setGenerator(generator);

        System.out.println(Global.getSave());
        Global.setSave(save);
        System.out.println("New Save file: " + Global.getSave());

        // serialize save file of player
        try {
            new SaveIO().serializeAddress(Global.getSave());
            System.out.println("Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Enemy e : enemies) {
            root.getChildren().remove(e.getImage());
        }


    }

    private void generateNewDungeon() {

        // fill fog map
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                fogMap[i][j] = 0;
            }
        }

        // create population for enemies
        ArrayList p = new ArrayList(enemies);
        p.addAll(deadEnemies);

        // generate dungeon and throw current enemy population
        generator.generateDungeon(p);
        System.out.println("New Dungeon Generated");
        tileMap = generator.getDungeon();

        // clean up the previous enemy population
        deadEnemies.clear();
        enemies.clear();

        // add the new generation of enemies
        enemies.addAll(generator.getEnemies());

        // set up player elements
        player = new Player();
        player.setName("Jean Gadot");
        player.setX(generator.getPlayerPosition().getX());
        player.setY(generator.getPlayerPosition().getY());
        player.setDamage(2);
        player.setMaxHealth(100);
        player.setCurrentHealth(100);

        // goal point
        goal = new Point();
        goal.setLocation(generator.getGoalPosition().getX(), generator.getGoalPosition().getY());

        // get design mapping
        designMap = generator.getDesignLayer1();

        // display number of generated enemy types
        int[][] enemyStats = generator.getEnemyStats();

        System.out.println("Rock + A Enemies: " + enemyStats[0][0]);
        System.out.println("Rock + B Enemies: " + enemyStats[0][1]);
        System.out.println("Rock + C Enemies: " + enemyStats[0][2]);

        System.out.println("Paper + A Enemies: " + enemyStats[1][0]);
        System.out.println("Paper + B Enemies: " + enemyStats[1][1]);
        System.out.println("Paper + C Enemies: " + enemyStats[1][2]);

        System.out.println("Scissors + A Enemies: " + enemyStats[2][0]);
        System.out.println("Scissors + B Enemies: " + enemyStats[2][1]);
        System.out.println("Scissors + C Enemies: " + enemyStats[2][2]);

        System.out.println("TOTAL Rock Enemies: " + (enemyStats[0][0] + enemyStats[0][1] + enemyStats[0][2]));
        System.out.println("TOTAL Paper Enemies: " + (enemyStats[1][0] + enemyStats[1][1] + enemyStats[1][2]));
        System.out.println("TOTAL Scissors Enemies: " + (enemyStats[2][0] + enemyStats[2][1] + enemyStats[2][2]));

        System.out.println("TOTAL A Enemies: " + (enemyStats[0][0] + enemyStats[1][0] + enemyStats[2][0]));
        System.out.println("TOTAL B Enemies: " + (enemyStats[0][1] + enemyStats[1][1] + enemyStats[2][1]));
        System.out.println("TOTAL C Enemies: " + (enemyStats[0][2] + enemyStats[1][2] + enemyStats[2][2]));


        // System.out.println("B Enemies: " + generator.getPaperEnemyCount());
        // System.out.println("C Enemies: " + generator.getScissorsEnemyCount());
    }

}
