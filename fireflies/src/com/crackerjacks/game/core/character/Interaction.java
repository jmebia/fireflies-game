package com.crackerjacks.game.core.character;

import com.crackerjacks.game.core.character.GameCharacter;

/**
 * Created by jm on 6/1/17.
 */
public class Interaction {

    public void attackMove(GameCharacter attacker, GameCharacter defender) {

        int damage = attacker.getDamage();
        int attack = attacker.getAttack();
        int defense = (int) defender.getDefense();

        if (attack < defense)
            damage -= defense - attack;

        // if damage is less than 0 then the attack will be equal to 0 else it will be equal
        // to the total damage value
        defender.setCurrentHealth(defender.getCurrentHealth() - (damage < 0? 0 : damage));
        System.out.println(attacker.getName() + " did " + damage + " damage to " + defender.getName());
    }

}
