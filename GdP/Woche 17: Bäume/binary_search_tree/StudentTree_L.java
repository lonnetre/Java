package binary_search_tree;

import java.util.Arrays;

/**
 * A binary search tree that manages {@link binary_search_tree.Student_L} objects
 */
public class StudentTree_L {

    /** The root node of the tree */
    private StudentNode_L root;

    public StudentTree_L() {
        this.root = null;
    }


    /**
     * Retrieves the student with the matriculation number from the tree
     * @param matriculationNumber the matriculation number of the student to look for
     * @return the student whose matriculation number is the given one
     */
    public Student_L get(int matriculationNumber) {
        StudentNode_L current = root;

        // Walk through the tree to find the correct student
        while (current != null) {
            if (matriculationNumber == current.getStudent().getMatriculationNumber()) {
                return current.getStudent();
            } else if (matriculationNumber < current.getStudent().getMatriculationNumber()) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        return null;
    }


    /**
     * Adds the given student to the binary search tree
     * @param student the student to add to the tree
     */
    public void add(Student_L student) {

        StudentNode_L newNode = new StudentNode_L(student);

        // Special case if the tree is empty
        if (root == null) {
            root = newNode;
            return;
        }

        StudentNode_L current = root;

        // Walk through the tree on the correct path, until there is a node with an empty child
        while (current != null) {

            if (student.getMatriculationNumber() == current.getStudent().getMatriculationNumber()) {
                // Failure case: No two students must share the same matriculation number
                return;

            } else if (student.getMatriculationNumber() < current.getStudent().getMatriculationNumber()) {
                if (current.getLeft() == null) {
                    current.setLeft(newNode);
                    return;
                }
                current = current.getLeft();

            } else {
                if (current.getRight() == null) {
                    current.setRight(newNode);
                    return;
                }
                current = current.getRight();
            }
        }
    }


    /**
     * Prints the tree in a depth-first manner (pre-order).
     */
    public void printDepthFirst() {
        if (this.root == null) {
            return;
        }
        printDepthFirstRec(this.root, "", true);
    }

    private void printDepthFirstRec(StudentNode_L node, String prefix, boolean isRightSibling) {
        System.out.print(prefix);
        if (isRightSibling) {
            System.out.print("└── ");
        } else {
            System.out.print("├── ");
        }
        System.out.println(node.getStudent().toString());

        String childrenPrefix = prefix + (isRightSibling ? "    " : "│   ");

        if (node.getLeft() != null) {
            printDepthFirstRec(node.getLeft(), childrenPrefix, false);
        }
        if (node.getRight() != null) {
            printDepthFirstRec(node.getRight(), childrenPrefix, true);
        }
    }


    /**
     * Prints the tree in a breadth-first manner.
     */
    public void printBreadthFirst() {
        if (this.root == null) {
            return;
        }

        System.out.println(root.getStudent());

        // Collect all sibling nodes at this level
        StudentNode_L[] currentNodes = { this.root };
        boolean finishedTraversal = this.root.getLeft() == null && this.root.getRight() == null;

        // Walk through the tree level by level
        while (!finishedTraversal) {

            // Collect all the children from the current nodes
            StudentNode_L[] children = new StudentNode_L[currentNodes.length * 2];
            for (int i = 0; i < currentNodes.length; i++) {
                StudentNode_L node = currentNodes[i];
                if (node != null) {
                    children[i * 2 + 0] = node.getLeft();
                    children[i * 2 + 1] = node.getRight();
                }
            }
            currentNodes = children;

            // Check if there are any valid nodes left
            int numNodes = 0;
            for (StudentNode_L node : currentNodes) {
                if (node != null) {
                    numNodes += 1;
                }
            }

            if (numNodes == 0) {
                finishedTraversal = true;
            }

            // Print this level
            if (!finishedTraversal) {
                for (int i = 0; i < currentNodes.length; i++) {
                    StudentNode_L node = currentNodes[i];
                    if (node != null) {
                        System.out.print(node.getStudent() + " ");
                    } else {
                        System.out.print("- ");
                    }
                }
                System.out.println();
            }
        }
    }


    /**
     * Computes the height of this tree
     * @return the height of this tree
     */
    public int height() {
        return heightRec(this.root);
    }

    private int heightRec(StudentNode_L node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(heightRec(node.getLeft()), heightRec(node.getRight()));
    }


    /**
     * Removes the student with the given matriculation number from the binary search tree
     * @param matriculationNumber the matriculation number of the student to remove
     * @throws StudentDoesNotExistException_L if the student is not part of the tree
     */
    public void remove(int matriculationNumber) throws StudentDoesNotExistException_L {
        this.root = removeRec(this.root, matriculationNumber);
    }


    /**
     * Removes the given student from the subtree of which <code>current</code> is the root node.
     * @param current the root of a subtree
     * @param matriculationNumber the matriculation number of the student to remove
     * @return the updated subtree with the student removed
     */
    private StudentNode_L removeRec(StudentNode_L current, int matriculationNumber) throws StudentDoesNotExistException_L {
        if (current == null) {
            throw new StudentDoesNotExistException_L();
        }

        // Walk through the tree recursively until the correct node is found
        if (matriculationNumber == current.getStudent().getMatriculationNumber()) {

            if (current.getLeft() == null && current.getRight() == null) {
                // Both children are null -> this is a leaf
                // By removing this node, the entire subtree becomes null
                return null;

            } else if (current.getRight() == null) {
                // Only the right child is null
                // By removing this node, only the left node remains as the subtree
                return current.getLeft();

            } else if (current.getLeft() == null) {
                // Only the left child is null
                // By removing this node, only the right node remains as the subtree
                return current.getRight();

            } else {
                // Both children nodes exist -> find the smallest node in the right subtree
                StudentNode_L smallestNode = findSmallestNode(current.getRight());

                // Replace the student in the current node by the smallest student
                current.setStudent(smallestNode.getStudent());

                // Remove the smallest student from the right subtree
                current.setRight(removeRec(current.getRight(), smallestNode.getStudent().getMatriculationNumber()));

                // This subtree still begins with the current node, although its right subtree was modified
                return current;
            }

        } else if (matriculationNumber < current.getStudent().getMatriculationNumber()) {
            // Continue searching in the left subtree
            current.setLeft(removeRec(current.getLeft(), matriculationNumber));
            return current;

        } else {
            // Continue searching in the right subtree
            current.setRight(removeRec(current.getRight(), matriculationNumber));
            return current;
        }
    }


    /**
     * Finds the node with the smallest matriculation number in the subtree
     * beginning with <code>node</code>
     * @param node the root of a subtree
     * @return the node with the smallest matriculation number
     */
    private StudentNode_L findSmallestNode(StudentNode_L node) {
        if (node == null) {
            return null;
        } else if (node.getLeft() == null) {
            return node;
        } else {
            return findSmallestNode(node.getLeft());
        }
    }
}
