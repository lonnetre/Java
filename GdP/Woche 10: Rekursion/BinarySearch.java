package search;

import java.util.Arrays;
import java.util.Comparator;

public class BinarySearch_L {

    /**
     * Finds the student with the given matriculation number in the given student array
     * @param students an array of students, sorted in nondescending order by their matriculation number
     * @param matriculationNumber the matriculation number of the wanted student
     * @return the student with the given matriculation number or null if they are not part of the array
     */
    private static Student binarySearch(Student[] students, int matriculationNumber) {
        if (students == null || students.length == 0) {
            return null;
        }

        return binarySearch(students, matriculationNumber, 0, students.length - 1);
    }

    private static Student binarySearch(Student[] students, int matriculationNumber, int startIndex, int endIndex) {

        if (startIndex > endIndex) {
            // The search failed
            return null;
        }

        // Check the student in the middle of the array
        int centerIndex = startIndex + ((endIndex - startIndex) / 2); // instead of the potential integer overflow of (s + e) / 2
        Student centerStudent = students[centerIndex];

        if (matriculationNumber == centerStudent.getMatriculationNumber()) {
            // Found the student with the given matriculation number
            return centerStudent;

        } else if (matriculationNumber < centerStudent.getMatriculationNumber()) {
            // Continue searching in the left half of the array
            endIndex = centerIndex - 1;

        } else {
            // Continue searching in the right half of the array
            startIndex = centerIndex + 1;
        }

        // Recursive search with the adjusted boundaries
        return binarySearch(students, matriculationNumber, startIndex, endIndex);
    }


    public static void main(String[] args) {
        Student[] students = new Student[]{
                new Student(1, "Alexander Friedrich"),
                new Student(346, "Emma Watschon"),
                new Student(45669, "Marie Kurier"),
                new Student(87421, "Tom Thomson"),
                new Student(87497, "Polina Port"),
                new Student(196379, "Leo Poldina"),
                new Student(893729, "Murad Merkur"),
                new Student(2745532, "Maxim Winter"),
                new Student(12345678, "Magda Martinez"),
                new Student(98765432, "Frank Franklin")
        };

        // The student array must be sorted by the matriculation number, since the binary search
        // works only for sorted data. If this not the case, use the following line to sort the array:
        //Arrays.sort(students, Comparator.comparingInt(Student::getMatriculationNumber));

        // Print the array
        // (This line of code uses Java method reference captures for brevity; it is not part of GdP)
        Arrays.stream(students).forEach(System.out::println);

        // Test implementation with each student
        for (int i = 0; i < students.length; i++) {

            // Search for i-th student
            Student student = students[i];
            Student foundStudent = binarySearch(students, student.getMatriculationNumber());

            boolean success = (foundStudent != null) && foundStudent.equals(student);
            System.out.println("Found student #" + i + ": " + success);
        }

        // Test with a matriculation number of a student who is not in the array
        Student foundStudent = binarySearch(students, 257837);
        boolean success = (foundStudent == null);
        System.out.println("Did not find student with unknown matriculation number: " + success);
    }
}
