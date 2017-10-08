package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.animator.Sprite;
import com.crackerjacks.game.core.objects.*;
import com.crackerjacks.game.core.dungeonGenerator.Generator;
import com.crackerjacks.game.core.input.InputHandler;
import com.crackerjacks.game.core.input.MainGameController;
import com.crackerjacks.game.core.interactions.Technique;
import com.crackerjacks.game.core.interactions.Type;
import com.crackerjacks.game.core.io.Save;
import com.crackerjacks.game.core.io.SaveIO;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by jm on 5/23/17.
 */
public class MainGame extends GameState {

    // 3D camera for the scene
    private PerspectiveCamera camera;

    // draw list
    private ArrayList<GameCharacter> charDrawList = new ArrayList();

    // elements for the dungeon map
    private int[][] tileMap;
    final private int mapSize = 60;
    final private int grids = 4;
    final private int roomSize = 7;

    // design map
    private int[][] designMap;
    private int[][] designMap2;

    // how fast the player slides from one tile to another
    private int playerSpeed = 4;

    private int startX = 500;
    private int startY = 500;

    private int YCharmModifier = -28;


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
    private Point goal;
    private boolean isOnGoal = false;

    // items
    private ArrayList<ItemObject2D> items = new ArrayList<>();

    // player inputHandler
    private InputHandler inputHandler;
    private MainGameController mainGameController;

    // images for the sprites
    private Image characterSprites;
    private Image tileSprites;
    // private Image background;

    // identifies if user loaded an existing save or a new game
    private boolean isNewGame;

    public MainGame(Scene scene, GraphicsContext graphicsContext, boolean isNewGame) {
        this.scene = scene;
        this.graphicsContext = graphicsContext;

        this.isNewGame = isNewGame;

        // fill history text
        for (int i = 0; i < 10; i++) {
            Global.getHistory().add((i==9?"Welcome to Ordeal of the FireFlies!": ""));
            System.out.println(Global.getHistory().get(Global.getHistory().size() - 1));
        }

        onEnter();

    }

    @Override
    void onEnter() throws IndexOutOfBoundsException {

        System.out.println(isNewGame);

        if (isNewGame) {

            // create generator for dungeons passing our tilemap as the base
            generator = new Generator(mapSize, grids, roomSize);

            // initialize tile map
            tileMap = new int[mapSize][mapSize];
            designMap = new int[mapSize][mapSize];
            designMap2 = new int[mapSize][mapSize];
            fogMap = new int[120][120];

            // generate dungeon
            System.out.println("Generating Dungeon");
            generateNewDungeon();

        } else {
            Save save = Global.getSave();

            generator = save.getGenerator();

            player = save.getPlayer();

            // initialize tile map
            tileMap = save.getTileMap();
            designMap = save.getDesignMap();
            designMap2 = save.getDesignMap2();
            fogMap = save.getFogMap();

            items = save.getItems();

            goal = new Point();
            goal.setLocation(generator.getGoalPosition().getX(), generator.getGoalPosition().getY());

            // place enemies
            enemies.addAll(save.getEnemies());
            deadEnemies.addAll(save.getDeadEnemies());
        }

        // player inputHandler
        inputHandler = new InputHandler(scene);
        mainGameController = new MainGameController();

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

    }

    @Override
    void update(long time) {

        /* handle player input */
        mainGameController.update(inputHandler, player, enemies, deadEnemies, tileMap, scene, graphicsContext);

        if (player.getCurrentHealth() <= 0) {
            Global.setSave(null);
            String sPath = System.getProperty("user.home") + "\\fireflies.sav";
            Path path = Paths.get(sPath);
            try {
                Files.delete(path);
            } catch (NoSuchFileException x) {
                System.err.format("%s: no such" + " file or directory%n", path);
            } catch (DirectoryNotEmptyException x) {
                System.err.format("%s not empty%n", path);
            } catch (IOException x) {
                // File permission problems are caught here.
                System.err.println(x);
            }
            GameStateManager.removeLast();
        }

        // check if player is in goal, if yes then generate new dungeon
        if (player.getX() == goal.getX() && player.getY() == goal.getY()) {

            if (player.getKeys() == generator.getKeysCoordinates().size()) {
                if (!inputHandler.isDisabled())
                    player.setKeys(0);
                    generateNewDungeon();
            }
        }

        // updates items in the game; checks if player is on top of the items

        for (ItemObject2D item : items) {
            if (item.update(player)) {
                // if item is a key
                if (item.getItem().getName().equals("Key")) {
                    player.setKeys(player.getKeys() + 1);
                    items.remove(item);
                    Global.addHistoryText(player.getName().toUpperCase() + " got a KEY!");
                    System.out.println("Keys = " + player.getKeys());
                }
                // else if it is a weapon crystal or potion
                else {
                    // check if inventory is full
                    if (player.getInventory().size() < 8) {
                        player.getInventory().add(item.getItem());
                        items.remove(item);
                        Global.addHistoryText(player.getName().toUpperCase() + " picked up " + item.getItem().getName().toUpperCase());
                    }
                }
            }
        }

        // DRAW
        // reset screen
        graphicsContext.setFill(Color.BLACK);
        // graphicsContext.drawImage(background,0, 0, 1920, 1920);
        graphicsContext.fillRect(150, 150, 3000, 3000);

        // update tile type where the enemies and player are standing
        for (Enemy e : enemies) {
            e.setTileType(tileMap[(int)e.getY()][(int)e.getX()]);
        }

        player.setTileType(tileMap[(int)player.getY()][(int)player.getX()]);

        // draw rooms and corridors
        for(int i = 0; i < designMap.length; i++) { // iterate through the rows
            for(int j = 0; j < designMap.length; j++) { // iterate through the columns

                // draw back layer first
                if (designMap2[i][j] == 1) {
                    graphicsContext.drawImage(tileSprites, 0, 192, 32, 32, j * tileWidth + startX,
                            i * tileHeight + startY, tileWidth, tileHeight);
                } else if (designMap2[i][j] == 2) {
                    graphicsContext.drawImage(tileSprites, 32, 192, 32, 32, j * tileWidth + startX,
                            i * tileHeight + startY, tileWidth, tileHeight);
                } else if (designMap2[i][j] == 3) {
                    graphicsContext.drawImage(tileSprites, 64, 192, 32, 32, j * tileWidth + startX,
                            i * tileHeight + startY, tileWidth, tileHeight);
                } else if (designMap2[i][j] == 4) {
                    graphicsContext.drawImage(tileSprites, 96, 32, 32, 32, j * tileWidth + startX,
                            i * tileHeight + startY, tileWidth, tileHeight);
                }

                // draw main layer next
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

        // draw goal
        graphicsContext.setFill(Color.BROWN);
        graphicsContext.fillRect(goal.getX() * tileWidth + startX, goal.getY() * tileHeight + startY,
                tileHeight, tileWidth);

        // draw items
        for (ItemObject2D item : items) {
            if (item.getItem().getName().equals("Key")) {
                graphicsContext.setFill(Color.GOLD);
                graphicsContext.fillRect(item.getX() * tileWidth + startX, item.getY() * tileHeight + startY,
                        tileHeight, tileWidth);
            } else {
                graphicsContext.setFill(Color.BLUE);
                graphicsContext.fillRect(item.getX() * tileWidth + startX, item.getY() * tileHeight + startY,
                        tileHeight, tileWidth);
            }
        }

        // draw attack side
        if (mainGameController.getAttackMode()) {
            // transparent red
            graphicsContext.setFill(new Color(1.0f, 0.0f, 0.0f, 0.5f));
            if(mainGameController.getAttackSide()== "left")
                graphicsContext.fillRect((player.getX() - 1) * tileWidth + startX,
                        player.getY() * tileHeight + startY, tileWidth, tileHeight );
            else if(mainGameController.getAttackSide()=="right")
                graphicsContext.fillRect((player.getX() + 1) * tileWidth + startX,
                        player.getY() * tileHeight + startY, tileWidth, tileHeight );
            else if(mainGameController.getAttackSide()=="up")
                graphicsContext.fillRect(player.getX() * tileWidth + startX,
                        (player.getY() - 1) * tileHeight + startY , tileWidth, tileHeight );
            else if(mainGameController.getAttackSide()=="down")
                graphicsContext.fillRect(player.getX() * tileWidth + startX,
                        (player.getY() + 1) * tileHeight + startY, tileWidth, tileHeight );
        }

        // sort game characters depending on their Y values
        charDrawList.clear();
        charDrawList.addAll(enemies);
        charDrawList.add(player);
        Collections.sort(charDrawList, Comparator.comparing(c -> c.getY()));

        // update enemy sprites
        for (Enemy e : enemies) {
            Sprite sprite = e.getSprite();
            sprite.update(time);

            // checks through the X axis
            if (sprite.getX() < e.getX()*tileWidth+startX) {
                sprite.setX(sprite.getX() + playerSpeed);
            } else if (sprite.getX() > e.getX()*tileWidth+startX) {
                sprite.setX(sprite.getX() - playerSpeed);
            }
            // checks through the Y axis
            if (sprite.getY() < e.getY()*tileWidth+startY+YCharmModifier) {
                sprite.setY(sprite.getY() + playerSpeed);
            } else if (sprite.getY() > e.getY()*tileWidth+startY+YCharmModifier) {
                sprite.setY(sprite.getY() - playerSpeed);
            }
            // checks if both X and Y coordinates of the player sprite is equal to the supposed tile placement of the
            // enemy in the 2D game space
            if ((sprite.getX() == e.getX()*tileWidth+startX)
                    && (sprite.getY() == e.getY()*tileWidth+startY+YCharmModifier)) {
                // something something
            }
        }

        // makes the player's sprite slide from one tile to another and snaps the sprite to the supposed tile placement
        player.getSprite().update(time);
        Sprite playerSprite = player.getSprite();
        // checks through the X axis
        if (playerSprite.getX() < player.getX()*tileWidth+startX) {
            playerSprite.setX(playerSprite.getX() + playerSpeed);
        } else if (playerSprite.getX() > player.getX()*tileWidth+startX) {
            playerSprite.setX(playerSprite.getX() - playerSpeed);
        }
        // checks through the Y axis
        if (playerSprite.getY() < player.getY()*tileWidth+startY+YCharmModifier) {
            playerSprite.setY(playerSprite.getY() + playerSpeed);
        } else if (playerSprite.getY() > player.getY()*tileWidth+startY+YCharmModifier) {
            playerSprite.setY(playerSprite.getY() - playerSpeed);
        }

        // checks if both X and Y coordinates of the player sprite is equal to the supposed tile placement of the
        // player in the 2D game space
        if ((playerSprite.getX() == player.getX()*tileWidth+startX)
                && (playerSprite.getY() == player.getY()*tileWidth+startY+YCharmModifier)) {
            inputHandler.setDisabled(false);
            mainGameController.unfog(player, fogMap, inputHandler);
        }

        //draw characters
        for (GameCharacter character : charDrawList) {
            // if enemy
            if (character instanceof Enemy) {
                Enemy e = (Enemy) character;
                Sprite sprite = e.getSprite();
                sprite.update(time);
                Point offset = sprite.getCurrentOffset();

                // draw enemy sprite if within player's line of sight
                if (fogMap[(int) e.getY()][(int) e.getX()] == 2) {
                    graphicsContext.drawImage(characterSprites, offset.getX(), offset.getY(),
                            sprite.getWidth(), sprite.getHeight(), sprite.getX(),
                            sprite.getY(), charWidth, charHeight);
                }
            }
            // if player
            else {
                // draw player
                Point playerOffset = playerSprite.getCurrentOffset();
                graphicsContext.drawImage(characterSprites, playerOffset.getX(), playerOffset.getY(),
                        playerSprite.getWidth(), playerSprite.getHeight(), playerSprite.getX(),
                        playerSprite.getY(), charWidth, charHeight);
            }
        }


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

        // reposition camera depending on player and map
        camera.setTranslateX(playerSprite.getX());
        camera.setTranslateY(playerSprite.getY() + 48);

        /* draw HUD */
        double hudx = camera.getTranslateX() - 356;
        double hudy = camera.getTranslateY() - 269;

        double hudh = 80;
        double hudw = 711;

        graphicsContext.setFill(Color.DARKBLUE);
        // draw hud background
        graphicsContext.fillRect(hudx, hudy, hudw, hudh);

        // health
        graphicsContext.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText("Health " + player.getCurrentHealth()+"/"+player.getMaxHealth()
                ,hudx + 10, hudy + 20);

        // equipped item
        graphicsContext.fillText("Equipped Item: "
                + (player.getEquipped()==null? " None" : player.getEquipped().getName()),hudx + 10, hudy + 40);

        // damage
        graphicsContext.fillText("Damage: " + player.getDamage() + " + " + player.getDamageMod()
                ,hudx + 10,hudy + 60);
        // attack
        graphicsContext.fillText("Attack: " + player.getAttack() + " + " + player.getAttackMod()
                ,hudx + 130, hudy + 60);
        // defense
        graphicsContext.fillText("Defense: "+ (int) player.getDefense() + " + " + player.getDefenseMod()
                ,hudx + 230, hudy + 60);

        // display proficient technique
        graphicsContext.fillText("Proficiency: " + player.getProficientTechnique().getId(),hudx + 400, hudy + 20);

//        // brute lvl and exp
//        graphicsContext.fillText("Brute lvl " + player.getLevelBrute() + " | "
//                        + player.getExperienceBrute() + " exp",hudx + 400, hudy + 20);
//
//        // stable lvl and exp
//        graphicsContext.fillText("Stable lvl " + player.getLevelStable() + " | "
//                + player.getExperienceStable() + " exp",hudx + 400, hudy + 40);
//
//        // cut lvl and exp
//        graphicsContext.fillText("Cut lvl " + player.getLevelCut() + " | "
//                + player.getExperienceCut() + " exp",hudx + 400, hudy + 60);

        // player's fireflies essence
        // graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText("FireFlies " + player.getFireflies(), hudx + 600, hudy + 20);

        // check if keys are required to beat the level
        if (generator.getKeysCoordinates().size() > 0) {
            graphicsContext.fillText("Keys: " + player.getKeys() + "/" + generator.getKeysCoordinates().size(),
                    hudx + 600, hudy + 40);
        } else {
            graphicsContext.fillText("No keys required",hudx + 600, hudy + 40);
        }
        // level
        graphicsContext.fillText("Player Level " + player.getLevel() + " | exp "
                + player.getExperience() + "/"+String.valueOf( Math.pow(player.getLevel(), 2) * 100)
                ,hudx + 150, hudy + 20);


        // MINI MAP
        // draw dungeon mini map
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap.length; j++) {
                if (tileMap[j][i] > 0 && fogMap[j][i] > 0) {
                    graphicsContext.setFill(new Color(1, 1, 1, 0.5));
                    graphicsContext.fillRect(i * 3 + hudx + 10, j * 3 + hudy + 320, 3, 3);
                }
            }
        }
        // draw player in mini map
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRect(player.getX() * 3 + hudx + 10, player.getY() * 3 + hudy + 320, 3, 3);

        // MESSAGE BOX
        // draw main box
        graphicsContext.setFill(new Color(0, 0, 0, 0.5));
        graphicsContext.fillRect(hudx + (hudw / 2 + 40), hudy + 410, 360, 120);
        // draw box border
        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.strokeRect(hudx + (hudw / 2 + 40), hudy + 410, 280, 120);
        // draw texts
        graphicsContext.setFont(Font.font("Verdana", FontWeight.NORMAL, 10));
        graphicsContext.setFill(Color.GREY);

        for (int i = 0; i < Global.getHistory().size(); i++) {

            if (i == 9)
                graphicsContext.setFill(Color.YELLOW);
            else if (i > 4)
                graphicsContext.setFill(Color.LIGHTYELLOW);

            graphicsContext.fillText(Global.getHistory().get(i), hudx + (hudw / 2 + 50), hudy + 410 + 10 + i * 11);
        }


    }

    @Override
    void onExit() {

        isNewGame = false;

        Save save = new Save();
        save.setPlayer(player);
        save.setTileMap(tileMap);
        save.setDesignMap(designMap);
        save.setFogMap(fogMap);
        save.setEnemies(enemies);
        save.setDeadEnemies(deadEnemies);
        save.setGenerator(generator);
        save.setItems(items);
        save.setDesignMap2(designMap2);

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

        // clear all items
        items.clear();

        // generate dungeon and throw current enemy population
        generator.generateDungeon(p);
        System.out.println("New Dungeon Generated");
        tileMap = generator.getDungeon();

        // clean up the previous enemy population
        deadEnemies.clear();
        enemies.clear();

        // add the new generation of enemies
        enemies.addAll(generator.getEnemies());

        // add keys to items
        ArrayList<Point> keys = generator.getKeysCoordinates();
        ArrayList<Point> loot = generator.getItemLoots();
        System.out.println("Generator keys = " + generator.getKeysCoordinates());

        for (Point key : keys) {
            System.out.println("Mayroong keys sa x="+key.x + " y="+key.y);
            items.add(new ItemObject2D(new Item("Key"), (int) key.getX(), (int)key.getY(),
                    tileWidth, tileHeight));
        }

        for (Point item : loot) {
            items.add(new ItemObject2D(new WeaponItem("Something", Type.a), (int) item.getX(), (int)item.getY(),
                    tileWidth, tileHeight));
        }


        // set up player elements
        if (isNewGame) {
            player = new Player();
            player.setName("Jean Gadot");
            player.setDamage(2);
            player.setMaxHealth(100);
            player.setCurrentHealth(100);
        }

        player.setX(generator.getPlayerPosition().getX());
        player.setY(generator.getPlayerPosition().getY());

        Sprite playerSprite = new Sprite(1, player.getX() * tileWidth + startX,
                player.getY() * tileHeight + startY + YCharmModifier, 32, 48, 400);
        playerSprite.addPoint(new Point(0, 0));
        playerSprite.addPoint(new Point(32, 0));
        playerSprite.addPoint(new Point(64, 0));
        playerSprite.addPoint(new Point(96, 0));

        player.setSprite(playerSprite);

        /*// FOR DUBUGGING
        player.setBleed(5);
        player.setBleedDamage(5);
        player.setDisarm(40);*/

        // set sprites for enemies
        for (Enemy enemy : enemies) {
            Sprite sprite = new Sprite(1, enemy.getX() * tileWidth + startX,
                    enemy.getY() * tileHeight + startY + YCharmModifier, 32, 48, 400);

            int yOffset = (enemy.getType().equals(Type.a)? 48 : (enemy.getType().equals(Type.b)? 192 : 336));

            yOffset += (enemy.getTechnique().equals(Technique.stable)? 48:
                    (enemy.getTechnique().equals(Technique.cut)? 96: 0));

            sprite.addPoint(new Point(0, yOffset));
            sprite.addPoint(new Point(32, yOffset));
            sprite.addPoint(new Point(64, yOffset));
            sprite.addPoint(new Point(96, yOffset));

            enemy.setSprite(sprite);
        }

        // goal point
        goal = new Point();
        goal.setLocation(generator.getGoalPosition().getX(), generator.getGoalPosition().getY());

        // get design mapping
        designMap = generator.getDesignLayer1();
        designMap2 = generator.getDesignLayer2();

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

        isNewGame = false;
        // System.out.println("B Enemies: " + generator.getPaperEnemyCount());
        // System.out.println("C Enemies: " + generator.getScissorsEnemyCount());
    }

}
