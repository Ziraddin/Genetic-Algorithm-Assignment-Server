package GenAlgorithm.Algorithm;

import GenAlgorithm.Structures.Destination;

import java.util.List;

import GenAlgorithm.Structures.Assignment;

import java.util.ArrayList;

public class DestinationFinder {
    private List<Destination> destinations;

    public DestinationFinder(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public Destination findDestination(String destinationName) {
        for (Destination destination : destinations) {
            if (destination.getName().equals(destinationName)) {
                return destination;
            }
        }
        return null; // Destination not found
    }

    public List<Destination> getDestinationsWithCapacity(Assignment assignment) {
        List<Destination> destinationsWithCapacity = new ArrayList<>();
        for (Destination destination : destinations) {
            if (assignment.getAssignedStudents(destination) < destination.getMaxStudents()) {
                destinationsWithCapacity.add(destination);
            }
        }
        return destinationsWithCapacity;
    }
}

