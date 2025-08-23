package binary_search_tree;


import java.util.*;

public class Main_L {
    public static void main(String[] args) {
        ArrayList<Student_L> students = new ArrayList<Student_L>(List.of(new Student_L[] {
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
        }));
        Random random = new Random(42);
        Collections.shuffle(students, random);

        System.out.println("Students:");
        System.out.println(Arrays.toString(students.toArray()));

        // Add students to the tree
        StudentTree_L tree = new StudentTree_L();
        for (Student_L student : students) {
            tree.add(student);
        }

        // Print the tree
        System.out.println();
        System.out.println("Binary search tree printed depth-first:");
        tree.printDepthFirst();
        System.out.println();
        System.out.println("Binary search tree printed breadth-first:");
        tree.printBreadthFirst();
        System.out.println();

        // Print height
        System.out.println("Tree height: " + tree.height());

        // Remove three nodes
        try {
            tree.remove(1);  // remove Alexander
            tree.remove(98765432);  // remove Frank
            tree.remove(87497);    // remove Polina
        } catch (StudentDoesNotExistException_L e) {
            throw new RuntimeException(e);
        }
        System.out.println();
        System.out.println("Binary student search tree after removal:");
        tree.printDepthFirst();
        System.out.println("Tree height: " + tree.height());
    }
}
