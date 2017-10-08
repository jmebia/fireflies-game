package com.crackerjacks.game.core;

import com.crackerjacks.game.core.io.Save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Global {

    // Save file variables
    private static Save save = null;

    private static String saveFile = System.getProperty("user.home") + "\\fireflies.sav";

    // activity history variables
    private static ArrayList<String> history = new ArrayList<>();

    // count of enemies spawned
    private static LinkedList<HashMap<String, Integer>> enemySpawnStatistics = new LinkedList<>();


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

    public static void setHistory(ArrayList<String> history) {
        Global.history = history;
    }

    public static LinkedList<HashMap<String, Integer>> getEnemySpawnStatistics() {
        return enemySpawnStatistics;
    }

    public static void setEnemySpawnStatistics(LinkedList<HashMap<String, Integer>> enemySpawnStatistics) {
        Global.enemySpawnStatistics = enemySpawnStatistics;
    }

}
