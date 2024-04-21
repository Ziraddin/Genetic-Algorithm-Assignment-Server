package Tests;

import GenAlgorithm.Algorithm.GeneticAlgorithm;
import GenAlgorithm.Structures.Assignment;
import GenAlgorithm.Structures.Destination;
import GenAlgorithm.Structures.Student;

import java.util.Arrays;
import java.util.List;

public class TestGeneticAlgorithm {
    public static void main(String[] args) {
        // Create students
        List<Student> students = Arrays.asList(
                new Student("S1", Arrays.asList("D1", "D2")),
                new Student("S2", Arrays.asList("D1"))
        );

        // Create destinations
        List<Destination> destinations = Arrays.asList(
                new Destination("D1", 1),
                new Destination("D2", 1)
        );

        long startTime = System.currentTimeMillis();

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(10, 0.3, students, destinations);

        Assignment bestAssignment = geneticAlgorithm.optimize();

        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;

        // Display best assignment
        System.out.println("Best Assignment:");
        for (Student student : students) {
            Destination destination = bestAssignment.getAssignedDestination(student);
            System.out.println(student.getName() + " -> " + destination.getName());
        }

        // Display time elapsed
        System.out.println("Time Elapsed: " + timeElapsed + " milliseconds");
    }
}
