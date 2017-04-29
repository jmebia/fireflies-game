package com.crackerjacks.game.core.states;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by jm on 4/29/17.
 */
public class MainMenu extends GameState {

    public MainMenu(Scene scene, GraphicsContext graphicsContext) {
        this.scene = scene;
        this.graphicsContext = graphicsContext;
    }

    @Override
    void onEnter() {

    }

    @Override
    void update() {
        System.out.println("Yep, it's updating something");
    }

    @Override
    void draw() {
        System.out.println("Yep, it's drawing something");
        graphicsContext.fillText("It's Working!", 250, 250);
    }

    @Override
    void onExit() {

    }

}
