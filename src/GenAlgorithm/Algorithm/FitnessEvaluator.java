package GenAlgorithm.Algorithm;

import GenAlgorithm.Structures.Assignment;
import GenAlgorithm.Structures.Destination;
import GenAlgorithm.Structures.Student;

import java.util.List;

public class FitnessEvaluator {
    private List<Student> students;

    public FitnessEvaluator(List<Student> students) {
        this.students = students;
    }

    public void evaluateFitness(List<Assignment> population) {
        for (Assignment assignment : population) {
            double totalCost = calculateTotalCost(assignment);
            assignment.setFitness(1.0 / (1.0 + totalCost));
        }
    }

    public double calculateTotalCost(Assignment assignment) {
        double totalCost = 0;
        for (Student student : students) {
            Destination assignedDestination = assignment.getAssignedDestination(student);
            List<String> preferredDestinations = student.getPreferredDestinations();

            int preferenceIndex = preferredDestinations.indexOf(assignedDestination.getName()) + 1;
            if (preferenceIndex > 0) {
                totalCost += Math.pow(preferenceIndex - 1, 2);
            } else {
                totalCost += 10 * Math.pow(preferredDestinations.size(), 2);
            }
        }
        return totalCost;
    }

    public double getBestFitness(List<Assignment> population) {
        double bestFitness = Double.NEGATIVE_INFINITY;
        for (Assignment assignment : population) {
            if (assignment.getFitness() > bestFitness) {
                bestFitness = assignment.getFitness();
            }
        }
        return bestFitness;
    }

    public Assignment getBestAssignment(List<Assignment> population) {
        Assignment bestAssignment = population.get(0);
        double bestFitness = Double.NEGATIVE_INFINITY;
        for (Assignment assignment : population) {
            if (assignment.getFitness() > bestFitness) {
                bestFitness = assignment.getFitness();
                bestAssignment = assignment;
            }
        }
        return bestAssignment;
    }
}
