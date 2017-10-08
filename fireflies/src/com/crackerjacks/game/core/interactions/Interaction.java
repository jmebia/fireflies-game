package com.crackerjacks.game.core.interactions;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.objects.Enemy;
import com.crackerjacks.game.core.objects.GameCharacter;
import com.crackerjacks.game.core.objects.Player;
import com.crackerjacks.game.core.objects.WeaponItem;

import java.util.Random;

/**
 * Created by jm on 6/1/17.
 */
public class Interaction {

    public void attackMove(Enemy attacker, Player defender) {

        // check if attacker is not disarmed
        if (attacker.getDisarm() <= 0) {
            // get defending player's proficient technique
            Technique playerTech = defender.getProficientTechnique();

//            int playerBrute = defender.getLevelBrute();
//            int playerStable = defender.getLevelStable();
//            int playerCut = defender.getLevelCut();
//
//            if (playerBrute > playerStable && playerBrute > playerCut)
//                playerTech = Technique.brute;
//            else if (playerStable > playerBrute && playerStable > playerCut)
//                playerTech = Technique.stable;
//            else if (playerCut > playerBrute && playerCut > playerStable)
//                playerTech = Technique.cut;

            // get base combat stats of enemy
            int damage = attacker.getDamage();
            int attack = attacker.getAttack();
            int defense = (int) defender.getDefense();

            // penalty
            double penalty = damage / 2;

            // get chance for status effect
            double chance = Math.random() * 100;

            System.out.println("====================> "+attacker.getName().toUpperCase()+" CHANCE = " + chance + " <====================");

            System.out.println("===================> e.chance - stun chance = " + (chance - attacker.getStunChance() * 100));

            // check native attack and defense properties
            if (attack < defense)
                damage -= defense - attack;

            // if character is in a corridor
            if (isInNarrowSpace(defender))
                damage += new Random().nextInt((int)penalty);

            // check elements, reduce or increment damage depending on compared element types
            // brute type
            if (attacker.getTechnique().getId().equals(Technique.brute.getId())) {
                // brute damage computation
                if (playerTech.equals(Technique.stable)) {
                    System.out.println("Enemy is stable type");
                    damage -= penalty;
                }
                else if (playerTech.equals(Technique.cut)) {
                    System.out.println("Enemy is cut type");
                    damage += penalty;
                }

                // determines if stun effect will take place
                if ( (chance - attacker.getStunChance() * 100) < 0 ) {
                    // if defender is cut type then stun turns is higher
                    defender.setStun(new Random().nextInt(3 - 1) + (playerTech.equals(Technique.cut)? 2 : 1));
                    System.out.println(defender.getName().toUpperCase() + " got STUNNED!");
                    Global.addHistoryText(defender.getName().toUpperCase() + " got STUNNED!");
                }

            }
            // stable
            else if (attacker.getTechnique().getId().equals(Technique.stable.getId())) {
                // stable damage computation
                if (playerTech.equals(Technique.cut))
                    damage -= penalty;
                else if (playerTech.equals(Technique.brute))
                    damage += penalty;

                // determines if disarm effect will take place
                if ( (chance - attacker.getDisarmChance() * 100) < 0 ) {
                    // if defender is brute type then disarm turns is higher
                    defender.setDisarm(new Random().nextInt(3 - 1) + (playerTech.equals(Technique.brute)? 2 : 1));
                    Global.addHistoryText(defender.getName().toUpperCase() + " got DISARMED!");
                }

            }
            //cut
            else if (attacker.getTechnique().getId().equals(Technique.cut.getId())) {
                // cut damage computation
                if (playerTech.equals(Technique.brute))
                    damage -= penalty;
                else if (playerTech.equals(Technique.stable))
                    damage += penalty;

                // determines if bleed effect will take place
                if ( (chance - attacker.getBleedChance() * 100) < 0 ) {
                    defender.setBleed(new Random().nextInt(4 - 1) + 1);
                    // if defender is stable type then bleed damage is higher
                    defender.setBleedDamage((int)(playerTech.equals(Technique.stable)? penalty + 1 : penalty));
                    Global.addHistoryText(defender.getName().toUpperCase() + " is BLEEDING!");
                }

            }

            // if damage is less than 0 then the attack will be equal to 0 else it will be equal
            // to the total damage value
            defender.setCurrentHealth(defender.getCurrentHealth() - (damage < 0 ? 0 : damage));
            System.out.println(attacker.getName() + " dealt " + damage + " damage to " + defender.getName());
            Global.addHistoryText(attacker.getName().toUpperCase() + " dealt "
                    + damage + " damage to " + defender.getName().toUpperCase());
        }
    }

    public void attackMove(Player attacker, Enemy defender, String attackTechniqueID) {

        // check if attacker is not disarmed
        if (attacker.getDisarm() <= 0) {
            // get defender's type
            Technique defenderType = defender.getTechnique();

            int damage = attacker.getDamage();
            int attack = attacker.getAttack();
            int defense = (int) defender.getDefense();
//            int minAttack = new Double(attack * 0.7).intValue();
//            int minDefense = new Double(defense * 0.7).intValue();
//            int finAttack = new Random().nextInt(attack - minAttack) + minAttack;
//            int finDefense = new Random().nextInt(defense - minDefense) + minDefense;
            WeaponItem weap = null;

            double penalty = damage / 2;

            // get weapon from player
            try {
                if (!attacker.getEquipped().equals(null)) {
                    weap = (WeaponItem) attacker.getEquipped();
                }
            } catch (NullPointerException e) {
                // handle event
            }

            // get chance for status effect
            double chance = Math.random() * 100;
            System.out.println("====================> PLAYER CHANCE = " + chance + " <====================");
            System.out.println("===================> p.chance - stun chance = " + (chance - attacker.getStunChance() * 100)
                    + " <=====================");

            // add atk and def modifiers from weapon
            try {
                if (!weap.equals(null)) {
                    attack += weap.getAttack();
                    defense += weap.getDefense();
                }
            } catch (NullPointerException e) {

            }

            // check native attack and defense properties
            // if attack is greater than defense, attack will hit, else it will miss
            if (attack < defense)
                damage -= defense - attack;

            // if character is in a corridor
            if (isInNarrowSpace(defender))
                damage += new Random().nextInt((int)penalty);

            // add damage modifier from weapon
            try {
                if (!weap.equals(null)) {
                    damage += new Random().nextInt(weap.getMaxDamage() - weap.getMinDamage()) + weap.getMinDamage();
                }
            } catch (NullPointerException e) {

            }

            // check elements
            // if brute is the attack element of the player
            if (attackTechniqueID.equals(Technique.brute.getId())) {
                System.out.println("Attack is brute");
                if (defenderType.equals(Technique.stable)) {
                    System.out.println("Enemy is stable type");
                    damage -= penalty;
                }
                else if (defenderType.equals(Technique.cut)) {
                    System.out.println("Enemy is cut type");
                    damage += penalty;
                }

                // determines if stun effect will take place
                if ( (chance - attacker.getStunChance() * 100) < 0 ) {
                    // if defender is cut type then stun turns is higher
                    defender.setStun(new Random().nextInt(3 - 1) + (defenderType.equals(Technique.cut)? 2 : 1));
                    System.out.println(defender.getName().toUpperCase() + " got STUNNED!");
                    Global.addHistoryText(defender.getName().toUpperCase() + " got STUNNED!");
                }
            }

            // if stable is the attack element of the player
            else if (attackTechniqueID.equals(Technique.stable.getId())) {
                if (defenderType.equals(Technique.cut.getId()))
                    damage -= penalty;
                else if (defenderType.equals(Technique.brute))
                    damage += penalty;

                // determines if disarm effect will take place
                if ( (chance - attacker.getDisarmChance() * 100) < 0 ) {
                    // if defender is brute type then disarm turns are higher
                    defender.setDisarm(new Random().nextInt(3 - 1) + (defenderType.equals(Technique.brute)? 2 : 1));
                    Global.addHistoryText(defender.getName().toUpperCase() + " got DISARMED!");
                }

            }

            // if cut is the attack element of the player
            else if (attackTechniqueID.equals(Technique.cut.getId())) {
                if (defenderType.equals(Technique.brute))
                    damage -= penalty;
                else if (defenderType.equals(Technique.stable))
                    damage += penalty;

                // determines if bleed effect will take place
                if ( (chance - attacker.getBleedChance() * 100) < 0 ) {
                    defender.setBleed(new Random().nextInt(4 - 1) + 1);
                    // if defender is stable type then bleed damage is higher
                    defender.setBleedDamage( new Random().nextInt(((int)penalty) - 1)
                            + (defenderType.equals(Technique.stable)? 2 : 1));
                    Global.addHistoryText(defender.getName().toUpperCase() + " is BLEEDING!");
                }

            }

            // if damage is less than 0 then the attack will be equal to 0 else it will be equal
            // to the total damage value
            defender.setCurrentHealth(defender.getCurrentHealth() - (damage < 0 ? 0 : damage));
            System.out.println(attacker.getName() + " did " + damage + " damage to " + defender.getName());
            Global.addHistoryText(attacker.getName().toUpperCase() + " dealt "
                    + damage + " damage to " + defender.getName().toUpperCase());

            if (defender.getCurrentHealth() <= 0) {
                // level up attacker (player)
                double levelReq = Math.pow(attacker.getLevel(), 2) * 100;
                double exp = levelReq / (attacker.getLevel() * 4);
                attacker.setExperience(attacker.getExperience() + exp);
                Global.addHistoryText(defender.getName().toUpperCase() + " died. You gained " + exp + " exp!");
                attacker.updateLevel();
            }
        }
    }

    private boolean isInNarrowSpace(GameCharacter character) {

        boolean isNarrow = false;

        // if player is standing in a corridor
        if (character.getTileType() == 2) {
            // check if there are more than 2 movable tiles around the character
            // int tiles = 0;

            // if (character)

            //if (tiles > 2)
            isNarrow = true;
        }

        return isNarrow;

    }

}
