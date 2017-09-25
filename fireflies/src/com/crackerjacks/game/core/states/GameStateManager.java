package com.crackerjacks.game.core.states;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by jm on 4/29/17.
 */
public class GameStateManager {

    private static LinkedList<GameState> stateList = new LinkedList<>();

    public static void update(long time) {
        stateList.getLast().update(time);
    }

    public static LinkedList getStateList() {
        return stateList;
    }

    public static void removeLast() {
        stateList.getLast().onExit();
        stateList.removeLast();
        stateList.getLast().onEnter();
    }


}
