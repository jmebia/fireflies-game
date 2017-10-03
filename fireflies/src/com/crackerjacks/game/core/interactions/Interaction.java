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
            int damage = attacker.getDamage();
            int attack = attacker.getAttack();
            int defense = (int) defender.getDefense();

            // get chance for status effect
            double chance = Math.random() * 100;

            // check native attack and defense properties
            if (attack < defense)
                damage -= defense - attack;

            // check elements, reduce or increment damage depending on compared element types
            // brute type
            if (attacker.getTechnique().getId().equals(Technique.brute.getId())) {
                // TODO: brute damage computation


                // determines if stun effect will take place
                if ( (chance -= attacker.getStunChance()) < 0 ) {
                    defender.setStun(new Random().nextInt(3 - 1) + 1);
                }

            }
            // stable
            else if (attacker.getTechnique().getId().equals(Technique.stable.getId())) {
                // TODO: stable damage computation

                // determines if disarm effect will take place
                if ( (chance -= attacker.getDisarmChance()) < 0 ) {
                    defender.setDisarm(new Random().nextInt(3 - 1) + 1);
                }

            }
            //cut
            else if (attacker.getTechnique().getId().equals(Technique.cut.getId())) {
                // TODO: cut damage computation

                // determines if bleed effect will take place
                if ( (chance -= attacker.getBleedChance()) < 0 ) {
                    defender.setBleed(new Random().nextInt(4 - 1) + 1);
                    defender.setBleedDamage( new Random().nextInt((damage / 4) - 1) + 1 );
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
            String defenderType = defender.getTechnique().getId();
            double penalty = 1;

            int damage = attacker.getDamage();
            int attack = attacker.getAttack();
            int defense = (int) defender.getDefense();
            WeaponItem weap = null;

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

            // add atk and def modifiers from weapon
            try {
                if (!weap.equals(null)) {
                    attack += weap.getAttack();
                    defense += weap.getDefense();
                }
            } catch (NullPointerException e) {

            }

            // check native attack and defense properties
            if (attack < defense)
                damage -= defense - attack;

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
                if (defenderType.equals(Technique.stable.getId())) {
                    System.out.println("Enemy is stable type");
                    damage -= penalty;
                }
                else if (defenderType.equals(Technique.cut.getId())) {
                    System.out.println("Enemy is cut type");
                    damage += penalty;
                }

                // determines if stun effect will take place
                if ( (chance -= attacker.getStunChance()) < 0 ) {
                    defender.setStun(new Random().nextInt(3 - 1) + 1);
                }

            }

            // if stable is the attack element of the player
            if (attackTechniqueID.equals(Technique.stable.getId())) {
                if (defenderType.equals(Technique.cut.getId()))
                    damage -= penalty;
                else if (defenderType.equals(Technique.brute.getId()))
                    damage += penalty;

                // determines if disarm effect will take place
                if ( (chance -= attacker.getDisarmChance()) < 0 ) {
                    defender.setDisarm(new Random().nextInt(3 - 1) + 1);
                }

            }

            // if cut is the attack element of the player
            if (attackTechniqueID.equals(Technique.cut.getId())) {
                if (defenderType.equals(Technique.brute))
                    damage -= penalty;
                else if (defenderType.equals(Technique.stable))
                    damage += penalty;

                // determines if bleed effect will take place
                if ( (chance -= attacker.getBleedChance()) < 0 ) {
                    defender.setBleed(new Random().nextInt(4 - 1) + 1);
                    defender.setBleedDamage( new Random().nextInt((damage / 4) - 1) + 1 );
                }

            }

            // if damage is less than 0 then the attack will be equal to 0 else it will be equal
            // to the total damage value
            defender.setCurrentHealth(defender.getCurrentHealth() - (damage < 0 ? 0 : damage));
            System.out.println(attacker.getName() + " did " + damage + " damage to " + defender.getName());
            Global.addHistoryText(attacker.getName().toUpperCase() + " dealt "
                    + damage + " damage to " + defender.getName().toUpperCase());
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
