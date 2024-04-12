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

    public PopulationInitializer(int populationSize, List<Student> students, List<Destination> destinations) {
        this.populationSize = populationSize;
        this.students = students;
        this.destinationFinder = new DestinationFinder(destinations);
    }

    public List<Assignment> initializePopulation() {
        List<Assignment> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Assignment assignment = new Assignment();
            for (Student student : students) {
                List<String> preferredDestinations = new ArrayList<>(student.getPreferredDestinations());
                Collections.shuffle(preferredDestinations); // Shuffle to randomize preferences
                for (String destinationName : preferredDestinations) {
                    Destination destination = destinationFinder.findDestination(destinationName);
                    if (destination != null && assignment.getAssignedStudents(destination) < destination.getMaxStudents()) {
                        assignment.assignStudent(student, destination);
                        break; // Move to next student
                    }
                }
            }
            population.add(assignment);
        }
        return population;
    }
}
