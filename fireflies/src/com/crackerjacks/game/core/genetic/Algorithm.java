/**
 * contains the main genetic algorithm for the game
 */

package com.crackerjacks.game.core.genetic;

import com.crackerjacks.game.core.objects.Enemy;
import com.crackerjacks.game.core.interactions.Element;
import com.crackerjacks.game.core.interactions.Type;

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

            child.setElement(crossoverElement(parents[0], parents[1]));

            // child = mutate(child);

            Element element = crossoverElement(parents[0], parents[1]);
            Type type = crossoverType(parents[0], parents[1]);

            child.setElement(element);
            child.setType(type);

        }

    }

    private Enemy[] selectParents() {

        Enemy parents[] = new Enemy[2];

        parents[0] = acceptReject();
        parents[1] = acceptReject();

        return parents;

    }

    private Enemy acceptReject() {

        Enemy partner;
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

    private Element crossoverElement(Enemy firstParent, Enemy secondParent) {

        Element e;
        Random r = new Random();
        int i;

        // randomize if child is gonna get first or second parent's element
        i = r.nextInt(2);

        e = (i==0? firstParent.getElement()
                : secondParent.getElement());

        // mutation check
        if (r.nextDouble() <= mutationRate) {
            // pick a random element not found in parent
            // 0 = rock, 1 = paper, 2 = scissors
            while (true) {
                i = r.nextInt(3);
                if (i == 0 && !(firstParent.getElement().equals(Element.rock)
                        || secondParent.getElement().equals(Element.rock))) {
                    e = Element.rock; break;
                }
                else  if (i == 1 && !(firstParent.getElement().equals(Element.paper)
                        || secondParent.getElement().equals(Element.paper))) {
                    e = Element.paper; break;
                }
                else  if (i == 2 && !(firstParent.getElement().equals(Element.scissors)
                        || secondParent.getElement().equals(Element.scissors))) {
                    e = Element.scissors; break;
                }
            }
        }

        return e;
    }

    private Type crossoverType(Enemy firstParent, Enemy secondParent) {

        Type e;
        Random r = new Random();
        int i;

        // randomize if child is gonna get first or second parent's element
        i = r.nextInt(2);

        e = (i==0? firstParent.getType()
                : secondParent.getType());

        // mutation check
        if (r.nextDouble() <= mutationRate) {
            // pick a random element not found in parent
            // 0 = rock, 1 = paper, 2 = scissors
            while (true) {
                i = r.nextInt(3);
                if (i == 0 && !(firstParent.getType().equals(Type.a)
                        || secondParent.getType().equals(Type.a))) {
                    e = Type.a; break;
                }
                else  if (i == 1 && !(firstParent.getType().equals(Type.b)
                        || secondParent.getType().equals(Type.b))) {
                    e = Type.b; break;
                }
                else  if (i == 2 && !(firstParent.getType().equals(Type.c)
                        || secondParent.getType().equals(Type.c))) {
                    e = Type.c; break;
                }
            }
        }

        return e;
    }

    public ArrayList<Enemy> getOffsprings() {
        return offsprings.getPopulation();
    }

}
