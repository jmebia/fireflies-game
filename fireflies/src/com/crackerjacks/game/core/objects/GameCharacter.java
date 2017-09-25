package com.crackerjacks.game.core.objects;

import com.crackerjacks.game.core.animator.Sprite;

import java.io.Serializable;

/**
 * Created by jm on 5/23/17.
 */
public class GameCharacter implements Serializable{

    private static final long serialVersionUID = 1L;

    /** Variables */

    private String name;

    private Sprite sprite;

    // leveling
    private int level;
    private double experience;

    // attributes
    private int attack;
    private int defense;
    private int maxHealth;
    private int currentHealth;
    private int damage;

    // number of turns for the disable effects
    private int bleed;
    private int root;
    private int stun;
    private int disarm;

    private int bleedDamage;

    // 2D position
    private double X;
    private double Y;

    // tile type
    int tileType;

    /** Constructor */

    public GameCharacter() {

        this.level = 1;
        this.experience = 0;

        this.attack = 5;
        this.defense = 5;
        this.maxHealth = 10;
        this.currentHealth = maxHealth;

        this.bleed = 0;
        this.root = 0;
        this.stun = 0;
        this.disarm = 0;

        bleedDamage = 0;

        // the base damage of a objects is always equal to the ceiling of
        // the 10 percent of her current level
        this.damage = (int) Math.ceil( level * .1 ) + 1;
    }

    /** Getters and Setters */

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        this.maxHealth = 10 + 10 * this.level;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int health) {
        this.maxHealth = health;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int health) {
        if (health <= maxHealth)
            this.currentHealth = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setDefaultDamage() {
        // the default damage of a objects is always equal to the ceiling of
        // the 10 percent of her current level
        this.damage = (int) Math.ceil( level * .1 ) + 1;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBleed() {
        return bleed;
    }

    public void setBleed(int bleed) {
        this.bleed = bleed;
    }

    public int getBleedDamage() {
        return bleedDamage;
    }

    public void setBleedDamage(int bleedDamage) {
        this.bleedDamage = bleedDamage;
    }

    public int getRoot() {
        return root;
    }

    public void setRoot(int root) {
        this.root = root;
    }

    public int getStun() {
        return stun;
    }

    public void setStun(int stun) {
        this.stun = stun;
    }

    public int getDisarm() {
        return disarm;
    }

    public void setDisarm(int disarm) {
        this.disarm = disarm;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setTileType(int tileType) {
        this.tileType = tileType;
    }

    public int getTileType() {
        return tileType;
    }
}
