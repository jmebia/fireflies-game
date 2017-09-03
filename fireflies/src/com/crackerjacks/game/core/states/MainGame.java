package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.animator.SpriteAnimator;
import com.crackerjacks.game.core.character.Enemy;
import com.crackerjacks.game.core.character.Player;
import com.crackerjacks.game.core.dungeonGenerator.Generator;
import com.crackerjacks.game.core.input.InputHandler;
import com.crackerjacks.game.core.input.Controller;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.awt.*;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.crackerjacks.game.core.Game.root;

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

    // how fast the player slides from one tile to another
    int playerSpeed = 4;

    int startX = 0;
    int startY = 0;


    // tile map for the fog of war
    private int[][] fogMap;

    // size of the dungeon when drawn on screen
    final private int tileHeight = 32;
    final private int tileWidth = 32;

    // player character
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

    // canvas for hud
    javafx.scene.canvas.Canvas hud;
    GraphicsContext gcHud;

    // images for the sprites
    Image characterSprites;
    Image roomSprite;
    Image corridorSprite;
    Image background;

    // image container
    ImageView playerSpriteView;
    SpriteAnimator animator;

    // hud switch
    boolean isHudOn = false;

    public MainGame(Scene scene, GraphicsContext graphicsContext) {
        this.scene = scene;
        this.graphicsContext = graphicsContext;

        // new canvas
        hud = new Canvas(500, 300);
        gcHud = hud.getGraphicsContext2D();
        root.getChildren().addAll(hud);
        hud.toFront();

        // set up camera
        camera.setTranslateZ(-1000);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(21);
        scene.setCamera(camera);

        onEnter();

    }

    @Override
    void onEnter() throws IndexOutOfBoundsException {

        // create generator for dungeons passing our tilemap as the base
        generator = new Generator(mapSize, grids, roomSize);

        // initialize tile map
        tileMap = new int[mapSize][mapSize];
        fogMap = new int[120][120];

        // generateDungeon dungeon
        System.out.println("Generating Dungeon");

        generateNewDungeon();

        // player inputHandler
        inputHandler = new InputHandler(scene);
        controller = new Controller();

        // place enemies
        enemies.addAll(generator.getEnemies());

        // load the images of the sprites
        characterSprites = new Image(getClass().getResourceAsStream("../../resources/char_spritesheet.png"));
        roomSprite = new Image(getClass().getResourceAsStream("../../resources/tile_room.png"));
        corridorSprite = new Image(getClass().getResourceAsStream("../../resources/tile_corridor.png"));
        background = new Image(getClass().getResourceAsStream("../../resources/space-background.png"));

        // setting up the animator
        playerSpriteView = new ImageView(characterSprites);
        playerSpriteView = new ImageView(characterSprites);
        playerSpriteView.setViewport(new Rectangle2D(0, 0, 200, 200));
        playerSpriteView.setFitHeight(tileHeight);
        playerSpriteView.setFitWidth(tileWidth);
        playerSpriteView.setTranslateX(player.getX()*tileWidth+startX);
        playerSpriteView.setTranslateY(player.getY()*tileWidth+startY);
        root.getChildren().add(playerSpriteView);

        animator = new SpriteAnimator(playerSpriteView, Duration.millis(700),
                2, 2, 0, 0, 200, 200);
        animator.setCycleCount(Animation.INDEFINITE);
        animator.play();

    }

    @Override
    void update(long time) {

        /* handle player input */
        controller.update(inputHandler, player, enemies, deadEnemies, tileMap, fogMap, camera);

        if (player.getCurrentHealth() <= 0) {
            generateNewDungeon();
        }

        // check if player is in goal, if yes then generate new dungeon
        if (player.getX() == goal.getX() && player.getY() == goal.getY()) {
            generateNewDungeon();
        }

        // reposition hud
        hud.setTranslateX(camera.getTranslateX() - 250);
        hud.setTranslateY(camera.getTranslateY() - 186);

    }

    @Override
    void draw() {

        // reset screen
        // graphicsContext.setFill(Color.BLACK);
        graphicsContext.drawImage(background,0
                , 0
                , 1920,
                1920);

        // draw rooms and corridors
        for(int i = 0; i < tileMap.length; i++) { // iterate through the rows
            for(int j = 0; j < tileMap.length; j++) { // iterate through the columns

                if (tileMap[i][j] == 1 || tileMap[i][j] == 3) { // if point is traversable and a room
                    //graphicsContext.setFill(Color.DARKGRAY);
                    //graphicsContext.fillRect(j*tileHeight+startY, i*tileWidth + startX, tileHeight, tileWidth);
                    graphicsContext.drawImage(roomSprite, j * tileWidth + startX, i * tileHeight + startY,
                            tileWidth, tileHeight);
                }

                else if (tileMap[i][j] == 2) { // if point is traversable and a corridor
                    //graphicsContext.setFill(Color.GRAY);
                    //graphicsContext.fillRect(j*tileHeight + startX, i*tileWidth + startY, tileHeight, tileWidth);
                    graphicsContext.drawImage(corridorSprite, j * tileWidth + startX, i * tileHeight + startY,
                            tileWidth, tileHeight);
                }
            }
        }

        /* draw characters in the map */
        /*/ draw player character
        player.draw(graphicsContext, startX, startY, tileHeight, tileWidth);
*/
        // draw enemies
        for (Enemy enemy : enemies) {
            if (fogMap[(int) enemy.getY()][(int) enemy.getX()] == 2) {
                enemy.draw(graphicsContext, startX, startY, tileHeight, tileWidth);
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
                    graphicsContext.setFill(new Color(0f,0f,0f,0.8));
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

        /* draw HUD */
        // draw static hud texts
        if (!isHudOn) {
            gcHud.setFill(new Color(0.0f, 0.0f, 1.0f, 0.5f));
            // draw hud background
            gcHud.fillRect(0, 0, 500, 70);
            isHudOn = true;

            // health
            gcHud.setFill(Color.WHITE);
            gcHud.fillText("Health ",10, 20);
            // player's chips
            gcHud.setFill(Color.WHITE);
            gcHud.fillText("Chips ", 250, 20);
        }

        // dynamic hud elements
        // health bar
        gcHud.setFill(Color.RED);
        gcHud.fillRect(60, 10,
                player.getMaxHealth(), 10);
        gcHud.setFill(Color.GREEN);
        gcHud.fillRect(60, 10,
                player.getCurrentHealth(), 10);


        // makes the player's sprite slide from one tile to another and snaps the sprite to the supposed tile placement
        // checks through the X axis
        if (playerSpriteView.getTranslateX() < player.getX()*tileWidth+startX) {
            playerSpriteView.setTranslateX(playerSpriteView.getTranslateX() + playerSpeed);
        } else if (playerSpriteView.getTranslateX() > player.getX()*tileWidth+startX) {
            playerSpriteView.setTranslateX(playerSpriteView.getTranslateX() - playerSpeed);
        }
        // checks through the Y axis
        if (playerSpriteView.getTranslateY() < player.getY()*tileWidth+startY) {
            playerSpriteView.setTranslateY(playerSpriteView.getTranslateY() + playerSpeed);
        } else if (playerSpriteView.getTranslateY() > player.getY()*tileWidth+startY) {
            playerSpriteView.setTranslateY(playerSpriteView.getTranslateY() - playerSpeed);
        }
        // checks if both X and Y coordinates of the player sprite is equal to the supposed tile placement of the
        // player in the 2D game space
        if ((playerSpriteView.getTranslateX() == player.getX()*tileWidth+startX)
                && (playerSpriteView.getTranslateY() == player.getY()*tileWidth+startY)) {
            inputHandler.setDisabled(false);
        }

        // reposition camera depending on player
        camera.setTranslateX(playerSpriteView.getTranslateX());
        camera.setTranslateY(playerSpriteView.getTranslateY());

        // playerSpriteView.setTranslateX(player.getX()*tileWidth+startX);
        // playerSpriteView.setTranslateY(player.getY()*tileHeight+startY-10);

    }

    @Override
    void onExit() {

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

        // display number of generated enemy types
        System.out.println("Rock Enemies: " + generator.getRockEnemyCount());
        System.out.println("Paper Enemies: " + generator.getPaperEnemyCount());
        System.out.println("Scissors Enemies: " + generator.getScissorsEnemyCount());
    }

}
