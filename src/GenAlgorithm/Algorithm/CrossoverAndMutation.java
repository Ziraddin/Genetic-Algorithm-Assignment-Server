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

    public CrossoverAndMutation(List<Student> students, List<Destination> destinations, double mutationRate) {
        this.students = students;
        this.mutationRate = mutationRate;
        this.random = new Random();
        this.destinationFinder = new DestinationFinder(destinations);
    }

    public List<Assignment> crossoverAndMutate(List<Assignment> parents, int populationSize) {
        List<Assignment> offspring = new ArrayList<>();
        for (int i = 0; i < populationSize; i += 2) {
            Assignment parent1 = parents.get(random.nextInt(parents.size()));
            Assignment parent2 = parents.get(random.nextInt(parents.size()));
            Assignment child1 = crossover(parent1, parent2);
            Assignment child2 = crossover(parent2, parent1);
            mutate(child1);
            mutate(child2);
            offspring.add(child1);
            offspring.add(child2);
        }
        return offspring;
    }

    private Assignment crossover(Assignment parent1, Assignment parent2) {
        Assignment child = new Assignment();
        for (Student student : students) {
            if (random.nextBoolean()) {
                Destination destination = parent1.getAssignedDestination(student);
                child.assignStudent(student, destination);
            } else {
                Destination destination = parent2.getAssignedDestination(student);
                child.assignStudent(student, destination);
            }
        }
        return child;
    }

    private void mutate(Assignment assignment) {
        for (Student student : students) {
            if (random.nextDouble() < mutationRate) {
                List<String> preferredDestinations = new ArrayList<>(student.getPreferredDestinations());
                for (String destinationName : preferredDestinations) {
                    Destination destination = destinationFinder.findDestination(destinationName);
                    if (destination != null && assignment.getAssignedStudents(destination) < destination.getMaxStudents()) {
                        assignment.assignStudent(student, destination);
                        break; // Move to next student
                    }
                }
            }
        }
    }
}
