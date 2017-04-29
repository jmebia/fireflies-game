package com.crackerjacks.game.core.states;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by jm on 4/29/17.
 */
public class GameStateManager {

    public LinkedList<GameState> stateList = new LinkedList<>();

    public void update() {
        stateList.getFirst().update();
    }

    public void draw() {
        stateList.getFirst().draw();
    }

}
