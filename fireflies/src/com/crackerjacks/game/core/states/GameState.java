package com.crackerjacks.game.core.states;

import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by jm on 4/29/17.
 */

abstract class GameState {

    Scene scene;
    GraphicsContext graphicsContext;
    PerspectiveCamera camera;

    abstract void onEnter();
    abstract void update(long time);
    abstract void onExit();
}
