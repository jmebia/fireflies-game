package com.crackerjacks.game.core.interactions;


import javafx.scene.paint.Color;

public enum Element {

    rock("rock", Color.RED),
    paper("paper", Color.BLUE),
    scissors("scissors", Color.GREEN);

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
