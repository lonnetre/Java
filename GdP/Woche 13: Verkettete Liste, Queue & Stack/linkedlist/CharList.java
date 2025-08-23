package linkedlist;

public interface CharList_L {

    /**
     * Adds the symbol to the end of the list
     * @param symbol the symbol to add to the list
     */
    void append(char symbol);

    /**
     * Returns the symbols in the list as one string
     * @return the contained symbols as one string
     */
    String toString();

    /**
     * Returns the symbol at the given index
     * @param index the position (beginning at zero) of the symbol
     * @return the symbol at the specified index
     */
    char get(long index);

    /**
     * Returns the number of symbols in this list
     * @return the total number of symbols
     */
    long size();

    /**
     * Inserts the given symbol at the given index.
     * For example: inserting 'X' at index 2 into a list containing ABBAA, yields: ABXBAA
     * @param symbol the symbol to insert into the list
     * @param index the position (beginning at 0) in the list, where to insert the symbol
     */
    void insert(char symbol, long index);

    /**
     * Deletes the symbol at the given index from the list
     * For example: removing the symbol at index 2 from a list containing ABXBAA, yields ABBAA and returns 'X'
     * @param index the position (beginning at zero) of the symbol that should be removed
     * @return the removed symbol
     */
    char remove(long index);
}
