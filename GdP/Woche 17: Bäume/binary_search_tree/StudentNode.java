package binary_search_tree;

/**
 * A node class for a binary tree that encapsulates a {@link binary_search_tree.Student_L}.
 */
public class StudentNode_L {

    /** The student payload of the node */
    private Student_L student;

    /** A reference to the left child node */
    private StudentNode_L left;

    /** A reference to the right child node */
    private StudentNode_L right;


    /** Constructs a new node from the given student payload */
    public StudentNode_L(Student_L student) {
        this.student = student;
        this.left = null;
        this.right = null;
    }

    public Student_L getStudent() {
        return student;
    }

    public void setStudent(Student_L student) {
        this.student = student;
    }

    public StudentNode_L getLeft() {
        return left;
    }

    public void setLeft(StudentNode_L left) {
        this.left = left;
    }

    public StudentNode_L getRight() {
        return right;
    }

    public void setRight(StudentNode_L right) {
        this.right = right;
    }
}
