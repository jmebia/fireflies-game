package com.crackerjacks.game.core.interactions;


import javafx.scene.paint.Color;

public enum Element {

    brute("brute", Color.RED),
    stable("stable", Color.BLUE),
    cut("cut", Color.GREEN);

    private String id;
    private Color color;

    Element (String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

}
