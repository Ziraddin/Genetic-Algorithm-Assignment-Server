package GenAlgorithm.Algorithm;

import GenAlgorithm.Structures.Assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParentSelector {
    private Random random;

    // Constructor initializes the ParentSelector with a Random object.
    public ParentSelector() {
        this.random = new Random();
    }

    // Method to select parents from a population of assignments using roulette wheel selection.
    public List<Assignment> selectParents(List<Assignment> population) {
        // Create an empty list to hold the selected parents.
        List<Assignment> parents = new ArrayList<>();
        // Calculate the total fitness of the population.
        double totalFitness = getTotalFitness(population);

        // Continue selecting parents until the number of selected parents equals the population size.
        while (parents.size() < population.size()) {
            // Generate a random value within the range of total fitness.
            double roulette = random.nextDouble() * totalFitness;
            double sum = 0;
            // Iterate over each assignment in the population.
            for (Assignment assignment : population) {
                // Add the fitness of the current assignment to the running sum.
                sum += assignment.getFitness();
                // If the sum exceeds the roulette value, select the current assignment as a parent.
                if (sum >= roulette) {
                    parents.add(assignment);
                    break;
                }
            }
        }

        // Return the list of selected parents.
        return parents;
    }

    // Method to calculate the total fitness of a population of assignments.
    private double getTotalFitness(List<Assignment> population) {
        double totalFitness = 0;
        // Iterate over each assignment and add its fitness to the total.
        for (Assignment assignment : population) {
            totalFitness += assignment.getFitness();
        }
        return totalFitness;
    }
}
