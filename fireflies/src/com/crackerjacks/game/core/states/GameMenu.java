package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.io.Save;
import com.crackerjacks.game.core.objects.Player;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameMenu extends GameState {

    private Player player;

    private Scene scene;
    private GraphicsContext gc;

    public GameMenu(Scene scene, GraphicsContext graphicsContext) {
        Save save = Global.getSave();
        player = save.getPlayer();

        this.scene = scene;
        gc = graphicsContext;
    }

    @Override
    void onEnter() {

        scene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {
                System.out.println("Pressed ENTER");
                GameStateManager.removeLast();
            }

        });

    }

    @Override
    void update(long time) {

        gc.setFill(Color.DARKGREY);
        gc.fillRect(0, 0, 4000, 4000);

        System.out.println("Update GameMenu");

    }

    @Override
    void onExit() {

    }

}
