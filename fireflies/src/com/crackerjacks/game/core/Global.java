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
    private static ArrayList<String> history2 = new ArrayList<>();

    // count of enemies spawned
    private static LinkedList<HashMap<String, Integer>> enemySpawnStatistics = new LinkedList<>();
    private static ArrayList<String> proficiency = new ArrayList<>();
    private static ArrayList<HashMap<String, Integer>> killedEnemies = new ArrayList<>();


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

        history2.add(text);
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

    public static ArrayList<String> getProficiency() {
        return proficiency;
    }

    public static void setProficiency(ArrayList<String> proficiency) {
        Global.proficiency = proficiency;
    }

    public static ArrayList<HashMap<String, Integer>> getKilledEnemies() {
        return killedEnemies;
    }

    public static void setKilledEnemies(ArrayList<HashMap<String, Integer>> killedEnemies) {
        Global.killedEnemies = killedEnemies;
    }

    public static void setSaveFile(String saveFile) {
        Global.saveFile = saveFile;
    }

    public static ArrayList<String> getHistory2() {
        return history2;
    }

    public static void setHistory2(ArrayList<String> history2) {
        Global.history2 = history2;
    }
}
