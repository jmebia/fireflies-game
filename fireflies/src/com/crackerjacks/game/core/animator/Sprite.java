package com.crackerjacks.game.core.animator;

import java.awt.*;
import java.util.LinkedList;

public class Sprite {

    private LinkedList<Point> points;

    private int width;

    private int height;

    private int frameCount;

    private long lastUpdate;

    private int currentListOffset;

    private long duration;

    public Sprite(int frameCount, int width, int height, long duration) {
        this.frameCount = frameCount;
        this.height = height;
        this.width = width;
        this.duration = duration;
        points = new LinkedList<>();
        lastUpdate = 0;
        currentListOffset = 0;
    }

    public void update(long time) {
        // check if the duration has passed since the last update of the sprite
        if (time - lastUpdate > duration) {
            if (currentListOffset < frameCount)
                currentListOffset++;
            else
                currentListOffset = 0;
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getDuration() {
        return duration;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public LinkedList<Point> getPoints() {
        return points;
    }

    public void setCurrentListOffset(int currentListOffset) {
        this.currentListOffset = currentListOffset;
    }

    public int getCurrentListOffset() {
        return currentListOffset;
    }
}
