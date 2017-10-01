package com.crackerjacks.game.core.interactions;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.objects.Enemy;
import com.crackerjacks.game.core.objects.GameCharacter;
import com.crackerjacks.game.core.objects.Player;

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

            // check native attack and defense properties
            if (attack < defense)
                damage -= defense - attack;
            // check elements, reduce or increment damage depending on compared element types
            // brute type
            if (attacker.getTechnique().getId().equals(Technique.brute.getId())) {
                // TODO: brute damage computation

            }
            // stable
            else if (attacker.getTechnique().getId().equals(Technique.stable.getId())) {
                // TODO: stable damage computation
            }
            //cut
            else if (attacker.getTechnique().getId().equals(Technique.cut.getId())) {
                // TODO: cut damage computation
            }

            // if damage is less than 0 then the attack will be equal to 0 else it will be equal
            // to the total damage value
            defender.setCurrentHealth(defender.getCurrentHealth() - (damage < 0 ? 0 : damage));
            System.out.println(attacker.getName() + " dealt " + damage + " damage to " + defender.getName());
            Global.addHistoryText(attacker.getName() + " dealt " + damage + " damage to " + defender.getName());
        }
    }

    public void attackMove(Player attacker, Enemy defender, String attackElementID) {

        // check if attacker is not disarmed
        if (attacker.getDisarm() <= 0) {
            double elementalPenalty = 1;
            int damage = attacker.getDamage();
            int attack = attacker.getAttack();
            int defense = (int) defender.getDefense();

            // check native attack and defense properties
            if (attack < defense)
                damage -= defense - attack;

            // check elements
            // if brute is the attack element of the player
            if (attackElementID.equals(Technique.brute.getId())) {
                if (defender.getTechnique().getId().equals(Technique.stable))
                    damage -= elementalPenalty;
                else if (defender.getTechnique().getId().equals(Technique.cut))
                    damage += elementalPenalty;
            }
            // if stable is the attack element of the player
            if (attackElementID.equals(Technique.stable.getId())) {
                if (defender.getTechnique().getId().equals(Technique.cut))
                    damage -= elementalPenalty;
                else if (defender.getTechnique().getId().equals(Technique.brute))
                    damage += elementalPenalty;
            }
            // if cut is the attack element of the player
            if (attackElementID.equals(Technique.cut.getId())) {
                if (defender.getTechnique().getId().equals(Technique.brute))
                    damage -= elementalPenalty;
                else if (defender.getTechnique().getId().equals(Technique.stable))
                    damage += elementalPenalty;
            }

            // if damage is less than 0 then the attack will be equal to 0 else it will be equal
            // to the total damage value
            defender.setCurrentHealth(defender.getCurrentHealth() - (damage < 0 ? 0 : damage));
            System.out.println(attacker.getName() + " did " + damage + " damage to " + defender.getName());
            Global.addHistoryText(attacker.getName() + " dealt " + damage + " damage to " + defender.getName());
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
