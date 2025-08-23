package search2;

import java.util.Objects;

public class Student_L implements Comparable<Student_L> {

    private final int matriculationNumber;
    private final String name;

    public Student_L(int matriculationNumber, String name) {
        this.matriculationNumber = matriculationNumber;
        this.name = name;
    }

    public int getMatriculationNumber() {
        return matriculationNumber;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "[" + this.matriculationNumber + "] " + this.name;
    }

    @Override
    public int compareTo(Student_L other) {
        // Ascending order by matriculation number
        return this.matriculationNumber - other.matriculationNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student_L student = (Student_L) o;
        return this.matriculationNumber == student.matriculationNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matriculationNumber);
    }
}
