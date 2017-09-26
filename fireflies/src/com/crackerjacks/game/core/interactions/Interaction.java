package com.crackerjacks.game.core.interactions;

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
            double elementPenalty = 1;
            int damage = attacker.getDamage();
            int attack = attacker.getAttack();
            int defense = (int) defender.getDefense();

            // check native attack and defense properties
            if (attack < defense)
                damage -= defense - attack;
            // check elements, reduce or increment damage depending on compared element types
            // brute type
            if (attacker.getElement().getId().equals(Element.brute.getId())) {
                if (defender.isPaper())
                    damage -= elementPenalty;
                else if (defender.isScissors())
                    damage += elementPenalty;
            }
            // stable
            else if (attacker.getElement().getId().equals(Element.stable.getId())) {
                if (defender.isScissors())
                    damage -= elementPenalty;
                else if (defender.isRock())
                    damage += elementPenalty;
            }
            //cut
            else if (attacker.getElement().getId().equals(Element.cut.getId())) {
                if (defender.isRock())
                    damage -= elementPenalty;
                else if (defender.isPaper())
                    damage += elementPenalty;
            }

            // if damage is less than 0 then the attack will be equal to 0 else it will be equal
            // to the total damage value
            defender.setCurrentHealth(defender.getCurrentHealth() - (damage < 0 ? 0 : damage));
            System.out.println(attacker.getName() + " did " + damage + " damage to " + defender.getName());
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
            if (attackElementID.equals(Element.brute.getId())) {
                if (defender.getElement().getId().equals(Element.stable))
                    damage -= elementalPenalty;
                else if (defender.getElement().getId().equals(Element.cut))
                    damage += elementalPenalty;
            }
            // if stable is the attack element of the player
            if (attackElementID.equals(Element.stable.getId())) {
                if (defender.getElement().getId().equals(Element.cut))
                    damage -= elementalPenalty;
                else if (defender.getElement().getId().equals(Element.brute))
                    damage += elementalPenalty;
            }
            // if cut is the attack element of the player
            if (attackElementID.equals(Element.cut.getId())) {
                if (defender.getElement().getId().equals(Element.brute))
                    damage -= elementalPenalty;
                else if (defender.getElement().getId().equals(Element.stable))
                    damage += elementalPenalty;
            }

            // if damage is less than 0 then the attack will be equal to 0 else it will be equal
            // to the total damage value
            defender.setCurrentHealth(defender.getCurrentHealth() - (damage < 0 ? 0 : damage));
            System.out.println(attacker.getName() + " did " + damage + " damage to " + defender.getName());
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
