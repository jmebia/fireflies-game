package com.crackerjacks.game.core.states;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by jm on 5/23/17.
 */
public class MainGame extends GameState {

    public MainGame(Scene scene, GraphicsContext graphicsContext) {
        this.scene = scene;
        this.graphicsContext = graphicsContext;
        onEnter();
    }

    @Override
    void onEnter() {

    }

    @Override
    void update() {

    }

    @Override
    void draw() {

    }

    @Override
    void onExit() {

    }
}
