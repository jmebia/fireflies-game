package com.crackerjacks.game.core.objects;

import java.io.Serializable;

public class Item implements Serializable {

    private int level;

    private String name;

    // base stat modifiers
    private int damage;
    private int health;
    private int attack;
    private int defense;

    private double stun_chance;
    private double bleed_chance;
    private double root_chance;

    // CONSTRUCTOR
    public Item(String name) {
        this.name = name;

        this.damage = 0;
        this.health = 0;
        this.attack = 0;
        this.defense = 0;

        this.stun_chance = 0;
        this.bleed_chance = 0;
        this.root_chance = 0;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public double getStun_chance() {
        return stun_chance;
    }

    public void setStun_chance(double stun_chance) {
        this.stun_chance = stun_chance;
    }

    public double getBleed_chance() {
        return bleed_chance;
    }

    public void setBleed_chance(double bleed_chance) {
        this.bleed_chance = bleed_chance;
    }

    public double getRoot_chance() {
        return root_chance;
    }

    public void setRoot_chance(double root_chance) {
        this.root_chance = root_chance;
    }

}
