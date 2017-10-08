package com.crackerjacks.game.core.objects;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.interactions.Technique;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.xml.bind.annotation.XmlElementDecl;
import java.util.ArrayList;

public class Player extends GameCharacter {

    private int healTurns = 0;
    private int healAmout = 0;

    private int rockExp = 0;
    private int paperExp = 0;
    private int scissorsExp = 0;

    private Technique proficientTechnique = Technique.brute;

    private int techniqueCost = 2;

    private int damageMod = 0;
    private int attackMod = 0;
    private int defenseMod = 0;

    private int regenTurns = 0;

    private int keys = 0;

    private ArrayList<Item> inventory = new ArrayList<>();
    private WeaponItem equipped = null;

    private int fireflies = 0;

    private int lineOfSight = 4;

    // getters and setters

    public void addFireflies(int count) {
        fireflies += count;
    }

    public void removeFireflies(int count) {
        if (!(fireflies - count < 0))
            fireflies += count;
        else
            System.out.println("Player's chips cannot be lower than zero!");
    }

    public int getFireflies() {
        return fireflies;
    }

    public void setLineOfSight(int los) {
        this.lineOfSight = los;
    }

    public int getLineOfSight() {
        return this.lineOfSight;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }


    public Item getEquipped() {
        return equipped;
    }

    public void setEquipped(WeaponItem weaponItem) {
        if (inventory.contains(weaponItem))
            equipped = weaponItem;
        else
            System.out.println("Item doesn't exist in player's inventory.");
    }

    public void useItem(PotionItem potionItem) {

    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public void setFireflies(int fireflies) {
        this.fireflies = fireflies;
    }

    public int getRockExp() {
        return rockExp;
    }

    public void setRockExp(int rockExp) {
        this.rockExp = rockExp;
    }

    public int getPaperExp() {
        return paperExp;
    }

    public void setPaperExp(int paperExp) {
        this.paperExp = paperExp;
    }

    public int getScissorsExp() {
        return scissorsExp;
    }

    public void setScissorsExp(int scissorsExp) {
        this.scissorsExp = scissorsExp;
    }

    public int getDamageMod() {
        return damageMod;
    }

    public void setDamageMod(int damageMod) {
        this.damageMod = damageMod;
    }

    public int getAttackMod() {
        return attackMod;
    }

    public void setAttackMod(int attackMod) {
        this.attackMod = attackMod;
    }

    public int getDefenseMod() {
        return defenseMod;
    }

    public void setDefenseMod(int defenseMod) {
        this.defenseMod = defenseMod;
    }

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }

    public int getRegenTurns() {
        return regenTurns;
    }

    public void setRegenTurns(int regenTurns) {
        this.regenTurns = regenTurns;
    }


    public void updateStatus() {

        if (getStun() > 0) {
            setStun(getStun()-1);
        }

        // if disarmed, minus one
        if (getDisarm() > 0)
            setDisarm(getDisarm() - 1);

        // check if enemy has bleed effect
        if (this.getBleed() > 0) {
            // subtract health with the bleed damage
            this.setCurrentHealth(getCurrentHealth() - getBleedDamage());
            this.setBleed(getBleed() - 1);
            Global.addHistoryText(getName().toUpperCase() + " bleeds " + getBleedDamage() + " HP");
        }

        // heal player
        if (getHealTurns() > 0) {
            if (getCurrentHealth() + getHealAmout() >= getMaxHealth())
                setCurrentHealth(getMaxHealth());
            else
                setCurrentHealth(getCurrentHealth() + getHealAmout());

            setHealTurns(getHealTurns() - 1);
        }
    }

    public void updateLevel() {

        // required exp
//        double expBrute = getLevelBrute();
//        double expStable = getLevelStable();
//        double expCut = getLevelCut();
        double reqExperience = Math.pow(getLevel(), 2) * 100;

        System.out.println("Player level req = ");

        if (getExperience() >= reqExperience) {
            // level up
            setLevel(getLevel() + 1);
            setExperience(getExperience() - reqExperience);
            // update health value
            setMaxHealth(getMaxHealth() + 10);
            setCurrentHealth(getCurrentHealth() + 10);
            // update damage
            setDamage(getDamage() + 1);

            Global.addHistoryText(getName().toUpperCase() + " reached level " + getLevel() + "!");
        }

    }

    public int getHealTurns() {
        return healTurns;
    }

    public void setHealTurns(int healTurns) {
        this.healTurns = healTurns;
    }

    public int getHealAmout() {
        return healAmout;
    }

    public void setHealAmout(int healAmout) {
        this.healAmout = healAmout;
    }

    public Technique getProficientTechnique() {
        return proficientTechnique;
    }

    public void setProficientTechnique(Technique proficientTechnique) {
        this.proficientTechnique = proficientTechnique;
    }

    public int getTechniqueCost() {
        return techniqueCost;
    }

    public void setTechniqueCost(int techniqueCost) {
        this.techniqueCost = techniqueCost;
    }
}
