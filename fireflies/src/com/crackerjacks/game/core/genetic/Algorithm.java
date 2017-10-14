/**
 * contains the main genetic algorithm for the game
 */

package com.crackerjacks.game.core.genetic;

import com.crackerjacks.game.core.interactions.Technique;
import com.crackerjacks.game.core.objects.Enemy;
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
        maxFitness = (maxFitness<1? 1 : maxFitness);
    }

    public void produce() {

        // keep on producing child objects while the number of offspring
        // doesn't meet the required population
        for (Enemy child: offsprings.getPopulation()) {

            Enemy parents[] = selectParents();

            child.setTechnique(crossoverElement(parents[0], parents[1]));

            // child = mutate(child);

            Technique technique = crossoverElement(parents[0], parents[1]);
            Type type = crossoverType(parents[0], parents[1]);

            child.setTechnique(technique);
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

    private Technique crossoverElement(Enemy firstParent, Enemy secondParent) {

        Technique e;
        Random r = new Random();
        int i;

        // randomize if child is gonna get first or second parent's element
        i = r.nextInt(2);

        e = (i==0? firstParent.getTechnique()
                : secondParent.getTechnique());

        // mutation check
        if (r.nextDouble() <= mutationRate) {
            // pick a random element not found in parent
            // 0 = brute, 1 = stable, 2 = cut
            while (true) {
                i = r.nextInt(3);
                if (i == 0 && !(firstParent.getTechnique().equals(Technique.brute)
                        || secondParent.getTechnique().equals(Technique.brute))) {
                    e = Technique.brute; break;
                }
                else  if (i == 1 && !(firstParent.getTechnique().equals(Technique.stable)
                        || secondParent.getTechnique().equals(Technique.stable))) {
                    e = Technique.stable; break;
                }
                else  if (i == 2 && !(firstParent.getTechnique().equals(Technique.cut)
                        || secondParent.getTechnique().equals(Technique.cut))) {
                    e = Technique.cut; break;
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
            // 0 = brute, 1 = stable, 2 = cut
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
