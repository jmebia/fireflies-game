package com.crackerjacks.game.core.states;

import javax.swing.plaf.nimbus.State;
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

    public static void addState(GameState state) {
        stateList.getLast().onExit();
        stateList.add(state);
        stateList.getLast().onEnter();
    }

    public static void removeLast2() {
        stateList.getLast().onExit();
        stateList.removeLast();
        stateList.getLast().onEnter();
        stateList.getLast().onExit();
        stateList.removeLast();
        stateList.getLast().onEnter();
    }

}
