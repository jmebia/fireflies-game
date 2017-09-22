/**
 *  This class handles everything that concerns the genetic program's population.
 */
package com.crackerjacks.game.core.genetic;

import com.crackerjacks.game.core.objects.Enemy;

import java.util.ArrayList;

public class Population {

    // the list containing the population of objects
    ArrayList<Enemy> population;

    public Population(ArrayList<Enemy> population) {
        this.population = population;
    }

    public Enemy getFittest() {
        Enemy enemy = population.get(0);

        // fitness is scored by how much life they have at the end of the level
        for (Enemy e : population) {
            if ((e.getCurrentHealth()/e.getMaxHealth())
                    > (enemy.getCurrentHealth()/e.getMaxHealth()))
                enemy = e;
        }

        return enemy;
    }

    public double getMaxFitness() {
        int max = 0;

        for (Enemy enemy : population) {
            max += enemy.getCurrentHealth();
        }

        return max;
    }

    public void add(Enemy enemy) {
        population.add(enemy);
    }

    public ArrayList<Enemy> getPopulation() {
        return population;
    }

}
