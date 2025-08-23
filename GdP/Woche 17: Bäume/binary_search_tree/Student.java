package binary_search_tree;

import java.util.Objects;

/**
 * A class that represents a student with a matriculation number and a name.
 */
public class Student_L implements Comparable<Student_L> {

    /** A unique ID (not monitored by the class) */
    private final int matriculationNumber;

    /** The name of the student */
    private final String name;

    /**
     * Creates a new student from the given matriculation number and name
     * @param matriculationNumber the unique ID of the student (not checked)
     * @param name the name of the student
     */
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

    /**
     * Compares two students by their matriculation numbers.
     * @param other the other student to be compared.
     * @return -1 if the matriculation number of this student is smaller than
     * the one of the other student, 0 if they are equal or 1 otherwise.
     */
    @Override
    public int compareTo(Student_L other) {
        // Ascending order by matriculation number
        return this.matriculationNumber - other.matriculationNumber;
    }

    /**
     * Checks if two students are equal, i.e., if their matriculation numbers are the same.
     * @param o
     * @return true if the students are the same or false otherwise
     */
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
