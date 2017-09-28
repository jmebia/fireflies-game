package com.crackerjacks.game.core.objects;

import com.crackerjacks.game.core.interactions.Type;

import java.io.Serializable;

public class Item implements Serializable {

    private int level;

    private String name;

    // CONSTRUCTOR
    public Item(String name) {
        this.name = name;
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

}
