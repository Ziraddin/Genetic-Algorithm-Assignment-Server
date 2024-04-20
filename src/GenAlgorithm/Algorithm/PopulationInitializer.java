package GenAlgorithm.Algorithm;

import GenAlgorithm.Structures.Assignment;
import GenAlgorithm.Structures.Destination;
import GenAlgorithm.Structures.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PopulationInitializer {
    private int populationSize;
    private List<Student> students;
    private DestinationFinder destinationFinder;

    // Constructor initializes the PopulationInitializer with population size, list of students, and list of destinations.
    public PopulationInitializer(int populationSize, List<Student> students, List<Destination> destinations) {
        this.populationSize = populationSize;
        this.students = students;
        this.destinationFinder = new DestinationFinder(destinations);
    }

    // Method to initialize the population of assignments.
    public List<Assignment> initializePopulation() {
        // Create an empty list to hold the population of assignments.
        List<Assignment> population = new ArrayList<>();
        // Iterate over the population size to create each assignment.
        for (int i = 0; i < populationSize; i++) {
            // Create a new assignment.
            Assignment assignment = new Assignment();
            // Iterate over each student to assign them to a destination.
            for (Student student : students) {
                // Create a copy of the student's preferred destinations and shuffle them to randomize the assignment process.
                List<String> preferredDestinations = new ArrayList<>(student.getPreferredDestinations());
                Collections.shuffle(preferredDestinations);
                // Iterate over the shuffled preferred destinations.
                for (String destinationName : preferredDestinations) {
                    // Find the destination object corresponding to the destination name.
                    Destination destination = destinationFinder.findDestination(destinationName);
                    // If the destination is found and it has not reached its maximum capacity, assign the student to it.
                    if (destination != null && assignment.getAssignedStudents(destination) < destination.getMaxStudents()) {
                        assignment.assignStudent(student, destination);
                        // Move to the next student.
                        break;
                    }
                }
            }
            // Add the completed assignment to the population.
            population.add(assignment);
        }
        // Return the initialized population of assignments.
        return population;
    }
}
