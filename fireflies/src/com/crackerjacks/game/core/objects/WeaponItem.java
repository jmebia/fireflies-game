package com.crackerjacks.game.core.objects;

import com.crackerjacks.game.core.interactions.Type;

public class WeaponItem extends Item {

    private Type type;

    // base stat modifiers
    private int maxDamage;
    private int minDamage;
    private int health;
    private int attack;
    private int defense;

    private double stun_chance;
    private double bleed_chance;
    private double disarm_chance;

    public WeaponItem(String name, Type type) {
        super(name);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
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

    public double getDisarm_chance() {
        return disarm_chance;
    }

    public void setDisarm_chance(double disarm_chance) {
        this.disarm_chance = disarm_chance;
    }

}
