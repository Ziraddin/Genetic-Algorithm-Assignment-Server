package GenAlgorithm.Algorithm;

import GenAlgorithm.Structures.Destination;

import java.util.List;

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
}
