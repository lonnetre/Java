package search2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BinarySearch_L {

    /**
     * Finds the student with the given matriculation number in the given student list
     * @param students a list of students, sorted in nondescending order by their matriculation number
     * @param matriculationNumber the matriculation number of the wanted student
     * @return the student with the given matriculation number or null if they are not part of the list
     */
    private static Student_L binarySearch(List<Student_L> students, int matriculationNumber) {
        if (students == null || students.size() == 0) {
            return null;
        }

        // Sort the list before performing the search.
        // Note that this solution is time-costly if searches are performed multiple times
        // without changing the list, since the list is sorted again for no reason.
        Collections.sort(students);

        return binarySearch(students, matriculationNumber, 0, students.size() - 1);
    }

    private static Student_L binarySearch(List<Student_L> students, int matriculationNumber, int startIndex, int endIndex) {

        if (startIndex > endIndex) {
            // The search failed
            return null;
        }

        // Check the student in the middle of the list
        int centerIndex = startIndex + ((endIndex - startIndex) / 2); // instead of the potential integer overflow of (s + e) / 2
        Student_L centerStudent = students.get(centerIndex);

        if (matriculationNumber == centerStudent.getMatriculationNumber()) {
            // Found the student with the given matriculation number
            return centerStudent;

        } else if (matriculationNumber < centerStudent.getMatriculationNumber()) {
            // Continue searching in the left half of the list
            endIndex = centerIndex - 1;

        } else {
            // Continue searching in the right half of the list
            startIndex = centerIndex + 1;
        }

        // Recursive search with the adjusted boundaries
        return binarySearch(students, matriculationNumber, startIndex, endIndex);
    }


    public static void main(String[] args) {

        // Create an ArrayList to store the data, such that it is cost-efficient to read
        // from it a lot of times (-> binary search), but costly to add or remove students
        // (occurs rarely in this program).
        // Note that it would also be possible, of course, to add the students with add() one after another
        List<Student_L> students = new ArrayList<Student_L>(Arrays.asList(
                new Student_L(1, "Alexander Friedrich"),
                new Student_L(346, "Emma Watschon"),
                new Student_L(45669, "Marie Kurier"),
                new Student_L(87421, "Tom Thomson"),
                new Student_L(87497, "Polina Port"),
                new Student_L(196379, "Leo Poldina"),
                new Student_L(893729, "Murad Merkur"),
                new Student_L(2745532, "Maxim Winter"),
                new Student_L(12345678, "Magda Martinez"),
                new Student_L(98765432, "Frank Franklin")
        ));


        // Print the list
        // (This line of code uses Java method reference captures for brevity; it is not part of GdP)
        students.forEach(System.out::println);

        // Test implementation with each student
        for (int i = 0; i < students.size(); i++) {

            // Search for i-th student
            Student_L student = students.get(i);
            Student_L foundStudent = binarySearch(students, student.getMatriculationNumber());

            boolean success = (foundStudent != null) && foundStudent.equals(student);
            System.out.println("Found student #" + i + ": " + success);
        }

        // Test with a matriculation number of a student who is not in the list
        Student_L foundStudent = binarySearch(students, 257837);
        boolean success = (foundStudent == null);
        System.out.println("Did not find student with unknown matriculation number: " + success);

        // Add a new student to the list
        Student_L emmy = new Student_L(666, "Emmy Evil");
        students.add(0, emmy);
        foundStudent = binarySearch(students, 666);
        success = (foundStudent != null) && foundStudent.equals(emmy);
        System.out.println("Found Emmy Evil" + ": " + success);

        // Equals test:
        System.out.println();
        System.out.println("Equals-test:");
        Student_L anna = new Student_L(1, "Anna");
        Student_L anna2 = new Student_L(1, "anna");
        System.out.println("anna == anna2: " + (anna == anna2)); // false
        System.out.println("anna.equals(anna2): " + anna.equals(anna2)); // true
    }
}
