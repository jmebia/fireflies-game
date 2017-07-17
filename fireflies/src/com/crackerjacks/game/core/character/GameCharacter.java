package com.crackerjacks.game.core.character;

/**
 * Created by jm on 5/23/17.
 */
public class GameCharacter {

    /** Variables */

    private String name;

    // leveling
    private int level;
    private double experience;

    // attributes
    private int attack;
    private int defense;
    private int maxHealth;
    private int currentHealth;
    private int damage;

    // 2D position
    private double X;
    private double Y;

    /** Constructor */

    public GameCharacter() {

        this.level = 1;
        this.experience = 0;

        this.attack = 5;
        this.defense = 5;
        this.maxHealth = 10;
        this.currentHealth = maxHealth;

        // the base damage of a character is always equal to the ceiling of
        // the 10 percent of her current level
        this.damage = (int) Math.ceil( level * .1 );
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
}
