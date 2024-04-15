package GenAlgorithm.Structures;

import java.util.Objects;

public class Destination {
    private String name;
    private int maxStudents;

    public Destination(String name, int maxStudents) {
        this.name = name;
        this.maxStudents = maxStudents;
    }

    public String getName() {
        return name;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destination that = (Destination) o;
        return maxStudents == that.maxStudents && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, maxStudents);
    }

    @Override
    public String toString() {
        return "Destination{" +
                "name='" + name + '\'' +
                ", maxStudents=" + maxStudents +
                '}';
    }
}

