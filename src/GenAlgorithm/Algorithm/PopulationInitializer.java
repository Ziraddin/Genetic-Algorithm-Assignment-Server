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

        // Continue creating assignments until the population size is reached.
        while (population.size() < populationSize) {
            // Create a new assignment.
            Assignment assignment = new Assignment();
            // Flag to indicate if any destination limit is exceeded.
            boolean exceededLimit = false;

            // Iterate over each student to assign them to a destination.
            for (Student student : students) {
                // Create a copy of the student's preferred destinations and shuffle them to randomize the assignment process.
                List<String> preferredDestinations = new ArrayList<>(student.getPreferredDestinations());
                Collections.shuffle(preferredDestinations);

                // Flag to indicate if the student has exceeded the limit.
                boolean studentExceededLimit = false;

                // Iterate over the shuffled preferred destinations.
                for (String destinationName : preferredDestinations) {
                    // Find the destination object corresponding to the destination name.
                    Destination destination = destinationFinder.findDestination(destinationName);

                    // If the destination is found, and it has not reached its maximum capacity, assign the student to it.
                    if (destination != null && assignment.getAssignedStudents(destination) < destination.getMaxStudents()) {
                        assignment.assignStudent(student, destination);
                        // Move to the next student.
                        break;
                    } else {
                        // Set the flag to true if the student has exceeded the limit.
                        studentExceededLimit = true;
                    }
                }

                // If the student has exceeded the limit, assign them to a random destination until the limit is no longer exceeded.
                if (studentExceededLimit) {
                    List<Destination> availableDestinations = destinationFinder.getDestinationsWithCapacity(assignment);

                    while (!availableDestinations.isEmpty()) {
                        Collections.shuffle(availableDestinations);
                        Destination randomDestination = availableDestinations.get(0);
                        assignment.assignStudent(student, randomDestination);

                        // If the assignment does not exceed the limit for the random destination, break the loop.
                        if (assignment.getAssignedStudents(randomDestination) < randomDestination.getMaxStudents()) {
                            break;
                        } else {
                            // Remove the random destination if it exceeds the limit.
                            availableDestinations.remove(randomDestination);
                        }
                    }

                    // If no available destinations are left, set the exceededLimit flag to true and break the loop.
                    if (availableDestinations.isEmpty()) {
                        exceededLimit = true;
                        break;
                    }
                }
            }

            if (!exceededLimit) {
                population.add(assignment);
            }
        }

        return population;
    }
}

