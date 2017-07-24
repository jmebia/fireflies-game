package com.crackerjacks.game.core.interactions;

import com.crackerjacks.game.core.character.Enemy;
import com.crackerjacks.game.core.character.GameCharacter;
import com.crackerjacks.game.core.character.Player;

/**
 * Created by jm on 6/1/17.
 */
public class Interaction {

    public void attackMove(Enemy attacker, Player defender) {

        double elementPenalty = 1;
        int damage = attacker.getDamage();
        int attack = attacker.getAttack();
        int defense = (int) defender.getDefense();

        // check native attack and defense properties
        if (attack < defense)
            damage -= defense - attack;
        // check elements, reduce or increment damage depending on compared element types
        // rock type
        if (attacker.getElement().getId().equals(Element.rock.getId())) {
            if(defender.isPaper())
                damage -= elementPenalty;
            else if (defender.isScissors())
                damage += elementPenalty;
        }
        // paper
        else if (attacker.getElement().getId().equals(Element.paper.getId())) {
            if(defender.isScissors())
                damage -= elementPenalty;
            else if (defender.isRock())
                damage += elementPenalty;
        }
        //scissors
        else if (attacker.getElement().getId().equals(Element.scissors.getId())) {
            if (defender.isRock())
                damage -= elementPenalty;
            else if (defender.isPaper())
                damage += elementPenalty;
        }

        // if damage is less than 0 then the attack will be equal to 0 else it will be equal
        // to the total damage value
        defender.setCurrentHealth(defender.getCurrentHealth() - (damage < 0? 0 : damage));
        System.out.println(attacker.getName() + " did " + damage + " damage to " + defender.getName());
    }

    public void attackMove(Player attacker, Enemy defender, String attackElementID) {

        double elementalPenalty = 1;
        int damage = attacker.getDamage();
        int attack = attacker.getAttack();
        int defense = (int) defender.getDefense();

        // check native attack and defense properties
        if (attack < defense)
            damage -= defense - attack;

        // check elements
        // if rock is the attack element of the player
        if (attackElementID.equals(Element.rock.getId())) {
            if (defender.getElement().getId().equals(Element.paper))
                damage -= elementalPenalty;
            else if (defender.getElement().getId().equals(Element.scissors))
                damage += elementalPenalty;
        }
        // if paper is the attack element of the player
        if (attackElementID.equals(Element.paper.getId())) {
            if (defender.getElement().getId().equals(Element.scissors))
                damage -= elementalPenalty;
            else if (defender.getElement().getId().equals(Element.rock))
                damage += elementalPenalty;
        }
        // if scissors is the attack element of the player
        if (attackElementID.equals(Element.scissors.getId())) {
            if (defender.getElement().getId().equals(Element.rock))
                damage -= elementalPenalty;
            else if (defender.getElement().getId().equals(Element.paper))
                damage += elementalPenalty;
        }

        // if damage is less than 0 then the attack will be equal to 0 else it will be equal
        // to the total damage value
        defender.setCurrentHealth(defender.getCurrentHealth() - (damage < 0? 0 : damage));
        System.out.println(attacker.getName() + " did " + damage + " damage to " + defender.getName());
    }

}
