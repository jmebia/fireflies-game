package com.crackerjacks.game.core;

import com.crackerjacks.game.core.io.Save;

import java.util.LinkedList;

public class Global {

    private static Save save = null;

    private static String saveFile = System.getProperty("user.home") + "\\fireflies.sav";

    public static String getSaveFile() {
        return saveFile;
    }

    public static Save getSave() {
        return save;
    }

    public static void setSave(Save save) {
        Global.save = save;
    }

    LinkedList<String> history = new LinkedList<>();


}
