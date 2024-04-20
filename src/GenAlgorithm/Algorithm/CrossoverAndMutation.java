package GenAlgorithm.Algorithm;

import GenAlgorithm.Structures.Assignment;
import GenAlgorithm.Structures.Destination;
import GenAlgorithm.Structures.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrossoverAndMutation {
    private List<Student> students;
    private double mutationRate;
    private Random random;
    private DestinationFinder destinationFinder;

    // Constructor initializes the CrossoverAndMutation with the list of students, destinations, and mutation rate.
    public CrossoverAndMutation(List<Student> students, List<Destination> destinations, double mutationRate) {
        this.students = students;
        this.mutationRate = mutationRate;
        this.random = new Random();
        this.destinationFinder = new DestinationFinder(destinations);
    }

    // Method to perform crossover and mutation on a list of parent assignments to generate offspring.
    public List<Assignment> crossoverAndMutate(List<Assignment> parents, int populationSize) {
        // Create an empty list to hold the offspring.
        List<Assignment> offspring = new ArrayList<>();
        // Iterate over the parent assignments, generating two offspring for each pair of parents.
        for (int i = 0; i < populationSize; i += 2) {
            // Select two random parents from the list of parents.
            Assignment parent1 = parents.get(random.nextInt(parents.size()));
            Assignment parent2 = parents.get(random.nextInt(parents.size()));
            // Perform crossover between the two parents to create two children.
            Assignment child1 = crossover(parent1, parent2);
            Assignment child2 = crossover(parent2, parent1);
            // Mutate the children.
            mutate(child1);
            mutate(child2);
            // Add the children to the offspring list.
            offspring.add(child1);
            offspring.add(child2);
        }
        // Return the list of offspring.
        return offspring;
    }

    // Method to perform crossover between two parent assignments.
    private Assignment crossover(Assignment parent1, Assignment parent2) {
        // Create a new assignment to hold the child.
        Assignment child = new Assignment();
        // Iterate over each student.
        for (Student student : students) {
            // Randomly select genes from either parent to form the child's assignment.
            if (random.nextBoolean()) {
                Destination destination = parent1.getAssignedDestination(student);
                child.assignStudent(student, destination);
            } else {
                Destination destination = parent2.getAssignedDestination(student);
                child.assignStudent(student, destination);
            }
        }
        // Return the child assignment.
        return child;
    }

    // Method to perform mutation on an assignment.
    private void mutate(Assignment assignment) {
        // Iterate over each student in the assignment.
        for (Student student : students) {
            // Check if the mutation should occur based on the mutation rate.
            if (random.nextDouble() < mutationRate) {
                // Get the preferred destinations of the student.
                List<String> preferredDestinations = new ArrayList<>(student.getPreferredDestinations());
                // Iterate over each preferred destination.
                for (String destinationName : preferredDestinations) {
                    // Find the destination object from the name.
                    Destination destination = destinationFinder.findDestination(destinationName);
                    // If the destination is not null and the assignment can accommodate the student, assign the student to the destination.
                    if (destination != null && assignment.getAssignedStudents(destination) < destination.getMaxStudents()) {
                        assignment.assignStudent(student, destination);
                        break; // Move to the next student
                    }
                }
            }
        }
    }
}
