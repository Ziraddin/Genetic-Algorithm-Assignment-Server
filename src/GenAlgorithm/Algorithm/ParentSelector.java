package GenAlgorithm.Algorithm;

import GenAlgorithm.Structures.Assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParentSelector {
    private Random random;

    public ParentSelector() {
        this.random = new Random();
    }

    public List<Assignment> selectParents(List<Assignment> population) {
        List<Assignment> parents = new ArrayList<>();
        double totalFitness = getTotalFitness(population);

        while (parents.size() < population.size()) {
            double roulette = random.nextDouble() * totalFitness;
            double sum = 0;
            for (Assignment assignment : population) {
                sum += assignment.getFitness();
                if (sum >= roulette) {
                    parents.add(assignment);
                    break;
                }
            }
        }

        return parents;
    }

    private double getTotalFitness(List<Assignment> population) {
        double totalFitness = 0;
        for (Assignment assignment : population) {
            totalFitness += assignment.getFitness();
        }
        return totalFitness;
    }
}
