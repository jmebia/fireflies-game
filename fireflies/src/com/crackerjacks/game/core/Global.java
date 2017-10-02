package com.crackerjacks.game.core;

import com.crackerjacks.game.core.io.Save;

import java.util.ArrayList;

public class Global {

    private static Save save = null;

    private static ArrayList<String> history = new ArrayList<>();

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

    public static ArrayList<String> getHistory() {
        return history;
    }

    public static void addHistoryText(String text) {
        if (history.size() == 10) {
            history.remove(history.get(0));
            history.add(text);
        }
    }

}
