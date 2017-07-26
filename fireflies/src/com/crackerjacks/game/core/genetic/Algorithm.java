/**
 * contains the main genetic algorithm for the game
 */

package com.crackerjacks.game.core.genetic;

import com.crackerjacks.game.core.character.Enemy;
import com.crackerjacks.game.core.interactions.Element;

import java.util.ArrayList;
import java.util.Random;

public class Algorithm {

    Population parents;

    Population offsprings;

    int populationSize;

    double mutationRate = 0.1f;
    double maxFitness;

    // Constructor
    public Algorithm(ArrayList<Enemy> parentPopulation, ArrayList<Enemy> childPopulation) {
        parents = new Population(parentPopulation);
        offsprings = new Population(childPopulation);
        populationSize = childPopulation.size();
        maxFitness = parents.getMaxFitness();
    }

    public void produce() {

        // keep on producing child objects while the number of offspring
        // doesn't meet the required population
        for (Enemy child: offsprings.getPopulation()) {

            Enemy parents[] = selectParents();

            child.setElement(crossover(parents[0], parents[1]));

            // child = mutate(child);

        }

    }

    private Enemy[] selectParents() {

        Enemy parents[] = new Enemy[2];

        parents[0] = acceptReject();
        parents[1] = acceptReject();

        return parents;

    }

    private Enemy acceptReject() {

        Enemy partner = null;
        Random rand = new Random();

        int failSafe = 0;

        while (true) {
            int index = rand.nextInt(parents.getPopulation().size());
            partner = parents.population.get(index);
            int r = rand.nextInt((int)maxFitness);
            if ( r < (partner.getCurrentHealth()/partner.getMaxHealth()) ) {
                break;
            }
            // to prevent a game breaking infinite loop
            failSafe++;
            if (failSafe == 1000) {
                partner = parents.getFittest();
                break;
            }

        }

        return partner;

    }

    private Element crossover(Enemy firstParent, Enemy secondParent) {

        // randomize if child is gonna get first or second parent's element
        int r = new Random().nextInt(2);

        return (r==0? firstParent.getElement()
                : secondParent.getElement());

    }

    /*
    private Enemy mutate(Enemy child) {
        Enemy c = child;

        return c;

    }
    */

    public ArrayList<Enemy> getOffsprings() {
        return offsprings.getPopulation();
    }

}
