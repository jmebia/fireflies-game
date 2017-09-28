package com.crackerjacks.game.core.objects;

public class ItemObject2D {

    int x;
    int y;
    int width;
    int height;

    Item item;

    public ItemObject2D(Item item, int x, int y, int w, int h) {
        this.item = item;
        this.x = x;
        this.y = y;
        width = w;
        height = h;
    }

    public boolean update(Player player) {

        boolean isGettable = false;

        // checks if player is standing above the item
        if (player.getX() == this.x && player.getY() == y) {
            // checks if player inventory is full
            if (player.getInventory().size() == 8)
                isGettable = true;
        }

        return isGettable;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
