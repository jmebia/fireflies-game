package com.crackerjacks.game.core.io;

import com.crackerjacks.game.core.objects.Enemy;
import com.crackerjacks.game.core.objects.ItemObject2D;
import com.crackerjacks.game.core.objects.Player;
import com.crackerjacks.game.core.dungeonGenerator.Generator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Save implements Serializable {

    private static final long serialVersionUID = 1L;

    private LinkedList<HashMap<String, Integer>> generations = null;
    private ArrayList<String> proficiency = null;
    private ArrayList<HashMap<String, Integer>> killedEnemies = null;

    private Generator generator = null;

    // characters
    private Player player = null;

    private ArrayList<Enemy> enemies = null;
    private ArrayList<Enemy> deadEnemies = null;

    private ArrayList<ItemObject2D> items = null;

    // map
    private int[][] tileMap = null;
    private int[][] designMap = null;
    private int[][] designMap2 = null;
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

    public ArrayList<ItemObject2D> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemObject2D> items) {
        this.items = items;
    }

    public int[][] getDesignMap2() {
        return designMap2;
    }

    public void setDesignMap2(int[][] designMap2) {
        this.designMap2 = designMap2;
    }

    public LinkedList<HashMap<String, Integer>> getGenerations() {
        return generations;
    }

    public void setGenerations(LinkedList<HashMap<String, Integer>> generations) {
        this.generations = generations;
    }

    public ArrayList<String> getProficiency() {
        return proficiency;
    }

    public void setProficiency(ArrayList<String> proficiency) {
        this.proficiency = proficiency;
    }

    public ArrayList<HashMap<String, Integer>> getKilledEnemies() {
        return killedEnemies;
    }

    public void setKilledEnemies(ArrayList<HashMap<String, Integer>> killedEnemies) {
        this.killedEnemies = killedEnemies;
    }
}
