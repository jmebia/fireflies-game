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
    private double root_chance;

    public WeaponItem(String name, Type type) {
        super(name);
    }

}
