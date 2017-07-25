package com.crackerjacks.game.core.states;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

public class GameMenu extends GameState {


    public GameMenu(Scene scene, GraphicsContext graphicsContext) {
        this.scene = scene;
        this.graphicsContext = graphicsContext;

        // set up camera
        camera.setTranslateZ(-1000);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(20);
        scene.setCamera(camera);

        onEnter();
    }


    @Override
    void onEnter() {

    }

    @Override
    void update(long time) {

    }

    @Override
    void draw() {

    }

    @Override
    void onExit() {

    }

}
