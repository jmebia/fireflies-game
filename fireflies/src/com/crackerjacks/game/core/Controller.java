package com.crackerjacks.game.core;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jm on 5/30/17.
 */
public class Controller {

    private LinkedList<String> input;
    private boolean disabled = false;

    // constructor

    public Controller(Scene scene) {

        input = new LinkedList<>();

        scene.setOnKeyPressed(e -> {
            System.out.println("Pressed " + e.getCode().toString());
            String keyCode = e.getCode().toString();
            if (!disabled && !input.contains(keyCode))
                input.add(keyCode);

        });

        scene.setOnKeyReleased(e -> {
            System.out.println("Released " + e.getCode().toString());
            String keyCode = e.getCode().toString();
            if (input.contains(keyCode))
                input.remove(keyCode);

        });
    }

    // public methods

    public LinkedList<String> getInputs() {
        return input;
    }

    public void clearInputs() {
        input.clear();
    }

    public void setDisabled(boolean b) {
        this.disabled = b;
    }

    public boolean isDisabled() {
        return disabled;
    }

}
