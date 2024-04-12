package GenAlgorithm.Structures;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Assignment {
    private Map<Student, Destination> assignmentMap;
    private double fitness;

    public Assignment() {
        assignmentMap = new HashMap<>();
        fitness = 0.0;
    }

    public void assignStudent(Student student, Destination destination) {
        assignmentMap.put(student, destination);
    }

    public Destination getAssignedDestination(Student student) {
        return assignmentMap.get(student);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Map<Student, Destination> getAssignmentMap() {
        return assignmentMap;
    }

    public void setAssignmentMap(Map<Student, Destination> assignmentMap) {
        this.assignmentMap = assignmentMap;
    }

    public int getAssignedStudents(Destination destination) {
        int count = 0;
        for (Destination dest : assignmentMap.values()) {
            if (dest.equals(destination)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return Double.compare(that.fitness, fitness) == 0 && Objects.equals(assignmentMap, that.assignmentMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentMap, fitness);
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "assignmentMap=" + assignmentMap +
                ", fitness=" + fitness +
                '}';
    }
}


