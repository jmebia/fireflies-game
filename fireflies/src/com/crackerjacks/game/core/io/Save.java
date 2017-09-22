package com.crackerjacks.game.core.io;

import com.crackerjacks.game.core.objects.Enemy;
import com.crackerjacks.game.core.objects.Player;
import com.crackerjacks.game.core.dungeonGenerator.Generator;

import java.io.Serializable;
import java.util.ArrayList;

public class Save implements Serializable {

    private static final long serialVersionUID = 1L;

    private Generator generator = null;

    // characters
    private Player player = null;

    private ArrayList<Enemy> enemies = null;
    private ArrayList<Enemy> deadEnemies = null;

    // map
    private int[][] tileMap = null;
    private int[][] designMap = null;
    private int[][] fogMap = null;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public ArrayList<Enemy> getDeadEnemies() {
        return deadEnemies;
    }

    public void setDeadEnemies(ArrayList<Enemy> deadEnemies) {
        this.deadEnemies = deadEnemies;
    }

    public int[][] getTileMap() {
        return tileMap;
    }

    public void setTileMap(int[][] tileMap) {
        this.tileMap = tileMap;
    }

    public int[][] getDesignMap() {
        return designMap;
    }

    public void setDesignMap(int[][] designMap) {
        this.designMap = designMap;
    }

    public int[][] getFogMap() {
        return fogMap;
    }

    public void setFogMap(int[][] fogMap) {
        this.fogMap = fogMap;
    }

    public Generator getGenerator() {
        return generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }
}
