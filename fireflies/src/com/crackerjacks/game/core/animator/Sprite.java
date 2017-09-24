package com.crackerjacks.game.core.animator;

import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;

public class Sprite implements Serializable {

    private static final long serialVersionUID = 1L;

    private LinkedList<Point> points;

    private double x;
    private double y;

    private int width;

    private int height;

    private int frameCount;

    private long lastUpdate;

    private int currentListOffset;

    private long duration;

    public Sprite(int frameCount, double x, double y, int width, int height, long duration) {
        this.x = x;
        this.y = y;
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
        if (time - lastUpdate > duration * 1000000) {
            if (currentListOffset < frameCount)
                currentListOffset++;
            else
                currentListOffset = 0;
            lastUpdate = time;

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

    public Point getCurrentOffset() {
        return points.get(currentListOffset);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
