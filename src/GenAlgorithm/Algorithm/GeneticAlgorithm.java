package GenAlgorithm.Algorithm;

import GenAlgorithm.Structures.Assignment;
import GenAlgorithm.Structures.Destination;
import GenAlgorithm.Structures.Student;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private List<Student> students;
    private List<Destination> destinations;

    public GeneticAlgorithm(int populationSize, double mutationRate, List<Student> students, List<Destination> destinations) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.students = students;
        this.destinations = destinations;
    }

    public void updateStudents(List<Student> updatedStudents) {
        this.students = updatedStudents;
    }

    public Assignment optimize() {
        PopulationInitializer populationInitializer = new PopulationInitializer(populationSize, students, destinations);
        FitnessEvaluator fitnessEvaluator = new FitnessEvaluator(students);
        ParentSelector parentSelector = new ParentSelector();
        CrossoverAndMutation crossoverAndMutation = new CrossoverAndMutation(students, destinations, mutationRate);

        // Initialization
        List<Assignment> population = populationInitializer.initializePopulation();

        // Plateau Detection
        int plateauCount = 0;
        double previousBestFitness = Double.NEGATIVE_INFINITY;

        // Evolution loop
        int generation = 1;

        while (plateauCount < 10) { // Plateau threshold: 10 generations
            fitnessEvaluator.evaluateFitness(population);

            // Print generation assignment costs
            System.out.println("Generation " + generation + " Assignment Costs:");
            for (Assignment assignment : population) {
                double totalCost = fitnessEvaluator.calculateTotalCost(assignment);
                System.out.println("Assignment Cost: " + totalCost);
                if (totalCost == 0.00) {
                    // If the cost is 0.00, stop the optimization process
                    System.out.println("Best Assignment Cost: " + totalCost);
                    System.out.println("Number of Generations: " + generation);
                    return assignment;
                }
            }

            List<Assignment> parents = parentSelector.selectParents(population);

            List<Assignment> offspring = crossoverAndMutation.crossoverAndMutate(parents, populationSize);

            population = offspring;

            // Plateau Detection
            double bestFitness = fitnessEvaluator.getBestFitness(population);
            if (bestFitness <= previousBestFitness) {
                plateauCount++;
            } else {
                plateauCount = 0;
                previousBestFitness = bestFitness;
            }

            generation++;
        }

        // Find and print the best assignment
        Assignment bestAssignment = fitnessEvaluator.getBestAssignment(population);
        double bestAssignmentCost = fitnessEvaluator.calculateTotalCost(bestAssignment);
        System.out.println("Best Assignment Cost: " + bestAssignmentCost);
        System.out.println("Number of Generations: " + generation);

        return bestAssignment;
    }
}
