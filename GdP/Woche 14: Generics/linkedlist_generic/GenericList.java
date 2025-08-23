package linkedlist_generic;

public interface GenericList_L<T> {

    /**
     * Adds the element to the end of the list
     * @param element the element to add to the list
     */
    void append(T element);

    /**
     * Returns the elements in the list as one string
     * @return the contained elements as one string
     */
    String toString();

    /**
     * Returns the element at the given index
     * @param index the position (beginning at zero) of the element
     * @return the element at the specified index
     */
    T get(long index);

    /**
     * Returns the number of elements in this list
     * @return the total number of elements
     */
    long size();

    /**
     * Inserts the given element at the given index.
     * For example: inserting 'X' at index 2 into a list containing ABBAA, yields: ABXBAA
     * @param element the element to insert into the list
     * @param index the position (beginning at 0) in the list, where to insert the element
     */
    void insert(T element, long index);

    /**
     * Deletes the element at the given index from the list
     * For example: removing the element at index 2 from a list containing ABXBAA, yields ABBAA and returns 'X'
     * @param index the position (beginning at zero) of the element that should be removed
     * @return the removed element
     */
    T remove(long index);
}
