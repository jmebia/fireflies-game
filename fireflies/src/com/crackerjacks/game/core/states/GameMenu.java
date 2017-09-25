package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.io.Save;
import com.crackerjacks.game.core.objects.Player;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

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
        ;

    }

    @Override
    void update(long time) {

    }

    @Override
    void onExit() {

    }

}
