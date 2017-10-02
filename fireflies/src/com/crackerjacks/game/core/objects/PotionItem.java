package com.crackerjacks.game.core.objects;

import com.crackerjacks.game.core.interactions.Type;

public class PotionItem extends Item {

    private int health;
    private int durationHealth;

    public PotionItem(String name) {
        super(name);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDurationHealth() {
        return durationHealth;
    }

    public void setDurationHealth(int durationHealth) {
        this.durationHealth = durationHealth;
    }
}
