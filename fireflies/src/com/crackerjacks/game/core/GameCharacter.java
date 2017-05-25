package com.crackerjacks.game.core;

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
    private double attack;
    private double defense;
    private double health;
    private double damage;

    // 2D position
    private double X;
    private double Y;

    /** Constructor */

    public GameCharacter() {

        this.level = 1;
        this.experience = 0;

        this.attack = 5;
        this.defense = 5;
        this.health = 10;

        // the base damage of a character is always equal to the ceiling of
        // the 10 percent of her current level
        this.damage = Math.ceil( (double)level * .1 );
    }

    /** Getters and Setters */

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
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
