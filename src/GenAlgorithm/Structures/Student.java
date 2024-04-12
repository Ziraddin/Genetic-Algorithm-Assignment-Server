package GenAlgorithm.Structures;

import java.util.List;
import java.util.Objects;

public class Student {
    private String name;
    private List<String> preferredDestinations;

    public Student(String name, List<String> preferredDestinations) {
        this.name = name;
        this.preferredDestinations = preferredDestinations;
    }

    public String getName() {
        return name;
    }

    public List<String> getPreferredDestinations() {
        return preferredDestinations;
    }

    public void updatePreferredDestinations(List<String> newPreferences) {
        preferredDestinations = newPreferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name) && Objects.equals(preferredDestinations, student.preferredDestinations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, preferredDestinations);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", preferredDestinations=" + preferredDestinations +
                '}';
    }
}
