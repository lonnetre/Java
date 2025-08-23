package linkedlist;

import org.junit.jupiter.api.*;

class RunLengthEncodedListTest_L {

    private CharList_L list;

    @BeforeEach
    void setUp() {
        // Start each testcase with a new list
        list = new RunLengthEncodedList_L();
    }

    /**
     * Helper method to append many symbols at once
     * @param symbols
     */
    private void appendAll(char... symbols) {
        for (int i = 0; i < symbols.length; i++) {
            list.append(symbols[i]);
        }
    }

    @Nested
    @DisplayName("append()")
    class Append {

        @Test
        @DisplayName("Should append symbol to empty list")
        void shouldAppendSymbolToEmptyList() {
            list.append('A');
            String result = list.toString();
            Assertions.assertEquals("A", result);
        }

        @Test
        @DisplayName("Should append same symbol to list of size one")
        void shouldAppendSameSymbolToListOfSizeOne() {
            list.append('A');
            list.append('A');
            String result = list.toString();
            Assertions.assertEquals("AA", result);
        }

        @Test
        @DisplayName("Should append different symbol to list of size one")
        void shouldAppendDifferentSymbolToListOfSizeOne() {
            list.append('A');
            list.append('B');
            String result = list.toString();
            Assertions.assertEquals("AB", result);
        }

        @Test
        @DisplayName("Should append different symbol to list")
        void shouldAppendDifferentSymbolToList() {
            appendAll('A', 'B', 'B', 'C');
            list.append('X');
            String result = list.toString();
            Assertions.assertEquals("ABBCX", result);
        }

        @Test
        @DisplayName("Should append streak symbol to list")
        void shouldAppendStreakSymbolToList() {
            appendAll('A', 'B', 'B', 'C');
            list.append('C');
            String result = list.toString();
            Assertions.assertEquals("ABBCC", result);
        }
    }

    @Nested
    @DisplayName("toString()")
    class ToString {
        @Test
        @DisplayName("Should return empty String for empty list")
        void shouldReturnEmptyStringForEmptyList() {
            String result = list.toString();
            Assertions.assertEquals("", result);
        }

        @Test
        @DisplayName("Should return symbol String for list of size one")
        void shouldReturnSymbolStringForListOfSizeOne() {
            list.append('A');
            String result = list.toString();
            Assertions.assertEquals("A", result);
        }

        @Test
        @DisplayName("Should return repeated symbol String for list containing symbol streak")
        void shouldReturnRepeatedSymbolStringForListContainingSymbolMultipleTimes() {
            list.append('A');
            list.append('A');
            String result = list.toString();
            Assertions.assertEquals("AA", result);
        }

        @Test
        @DisplayName("Should return symbol String for list containing different symbols")
        void shouldReturnSymbolStringForListContainingDifferentSymbols() {
            appendAll('A', 'B', 'B', 'C');
            String result = list.toString();
            Assertions.assertEquals("ABBC", result);
        }
    }

    @Nested
    @DisplayName("get()")
    class Get {
        @Test
        @DisplayName("Should throw IndexOutOfBoundsException when getting an element of empty list")
        void shouldThrowIndexOutOfBoundsExceptionWhenGettingAnElementOfEmptyList() {
            Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
                list.get(0);
            });
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException when asking for a negative index")
        void shouldThrowIndexOutOfBoundsExceptionWhenAskingForANegativeIndex() {
            list.append('A');
            Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
                list.get(-1);
            });
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException when no element exists at the requested index")
        void shouldThrowIndexOutOfBoundsExceptionWhenNoElementExistsAtTheRequestedIndex() {
            list.append('A');
            Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
                list.get(1);
            });
        }

        @Test
        @DisplayName("Should return the first symbol when asking for index zero")
        void shouldReturnTheFirstSymbolWhenAskingForIndexZero() {
            appendAll('A', 'B');
            char result = list.get(0);
            Assertions.assertEquals('A', result);
        }

        @Test
        @DisplayName("Should return the symbol at the requested index when the list contains only the same symbol")
        void shouldReturnTheSymbolAtTheRequestedIndexWhenTheListContainsOnlyTheSameSymbol() {
            appendAll('A', 'A', 'A');
            char result = list.get(2);
            Assertions.assertEquals('A', result);
        }

        @Test
        @DisplayName("Should return the symbol at the requested index when the list contains mixed symbols")
        void shouldReturnTheSymbolAtTheRequestedIndexWhenTheListContainsMixedSymbols() {
            appendAll('A', 'B', 'B', 'C');
            char result = list.get(3);
            Assertions.assertEquals('C', result);
        }
    }

    @Nested
    @DisplayName("size()")
    class Size {
        @Test
        @DisplayName("Should have size zero for new list")
        void shouldHaveSizeZeroForNewList() {
            long size = list.size();
            Assertions.assertEquals(0, size);
        }

        @Test
        @DisplayName("Should have size one when appending to new list")
        void shouldHaveSizeOneWhenAppendingToNewList() {
            list.append('A');
            long size = list.size();
            Assertions.assertEquals(1, size);
        }

        @Test
        @DisplayName("Should increment size when appending a second same symbol")
        void shouldIncrementSizeWhenAppendingASecondSameSymbol() {
            list.append('A');
            long sizeBefore = list.size();

            list.append('A');
            long sizeAfter = list.size();

            long diff = sizeAfter - sizeBefore;
            Assertions.assertEquals(1, diff);
        }

        @Test
        @DisplayName("Should increment size when appending a second different symbol")
        void shouldIncrementSizeWhenAppendingASecondDifferentSymbol() {
            list.append('A');
            long sizeBefore = list.size();

            list.append('B');
            long sizeAfter = list.size();

            long diff = sizeAfter - sizeBefore;
            Assertions.assertEquals(1, diff);
        }

        @Test
        @DisplayName("Should increment size when appending same symbol")
        void shouldIncrementSizeWhenAppendingSameSymbol() {
            list.append('A');
            list.append('B');
            long sizeBefore = list.size();

            list.append('B');
            long sizeAfter = list.size();

            long diff = sizeAfter - sizeBefore;
            Assertions.assertEquals(1, diff);
        }

        @Test
        @DisplayName("Should increment size when appending a different symbol")
        void shouldIncrementSizeWhenAppendingADifferentSymbol() {
            list.append('A');
            list.append('B');
            long sizeBefore = list.size();

            list.append('C');
            long sizeAfter = list.size();

            long diff = sizeAfter - sizeBefore;
            Assertions.assertEquals(1, diff);
        }

        @Test
        @DisplayName("Should increment size when inserting same symbol into a streak")
        void shouldIncrementSizeWhenInsertingSameSymbolIntoAStreak() {
            appendAll('A', 'B', 'B', 'B', 'C');
            long sizeBefore = list.size();

            list.insert('B', 2);
            long sizeAfter = list.size();

            long diff = sizeAfter - sizeBefore;
            Assertions.assertEquals(1, diff);
        }

        @Test
        @DisplayName("Should increment size when inserting different symbol into a streak")
        void shouldIncrementSizeWhenInsertingDifferentSymbolIntoAStreak() {
            appendAll('A', 'B', 'B', 'B', 'C');
            long sizeBefore = list.size();

            list.insert('X', 2);
            long sizeAfter = list.size();

            long diff = sizeAfter - sizeBefore;
            Assertions.assertEquals(1, diff);
        }

        @Test
        @DisplayName("Should have size zero when removing the last element")
        void shouldHaveSizeZeroWhenRemovingTheLastElement() {
            list.append('A');
            list.remove(0);
            long size = list.size();
            Assertions.assertEquals(0, size);
        }
    }

    @Nested
    @DisplayName("insert()")
    class Insert {
        @Test
        @DisplayName("Should throw IndexOutOfBoundsException when inserting at a negative index")
        void shouldThrowIndexOutOfBoundsExceptionWhenInsertingAtANegativeIndex() {
            list.append('A');
            Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
                list.insert('B', -1);
            });
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException when inserting into an empty list")
        void shouldThrowIndexOutOfBoundsExceptionWhenInsertingIntoAnEmptyList() {
            Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
                list.insert('B', 0);
            });
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException when inserting at a nonexistent index")
        void shouldThrowIndexOutOfBoundsExceptionWhenInsertingAtANonexistentIndex() {
            list.append('A');
            Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
                list.insert('B', 1);
            });
        }

        @Test
        @DisplayName("Should insert symbol before same symbol streak")
        void shouldInsertSymbolBeforeSameSymbolStreak() {
            appendAll('A', 'B', 'B', 'C');
            list.insert('B', 1);
            Assertions.assertEquals("ABBBC", list.toString());
        }

        @Test
        @DisplayName("Should insert symbol to beginning of list of different symbols")
        void shouldInsertSymbolToBeginningOfListOfDifferentSymbols() {
            appendAll('A', 'B');
            list.insert('X', 0);
            Assertions.assertEquals("XAB", list.toString());
        }

        @Test
        @DisplayName("Should insert symbol in between a streak of the same symbol")
        void shouldInsertSymbolInBetweenAStreakOfTheSameSymbol() {
            appendAll('A', 'B', 'B', 'C');
            list.insert('B', 2);
            Assertions.assertEquals("ABBBC", list.toString());
        }

        @Test
        @DisplayName("Should insert symbol after a streak of the same symbol")
        void shouldInsertSymbolAfterAStreakOfTheSameSymbol() {
            appendAll('A', 'B', 'B', 'C');
            list.insert('B', 3);
            Assertions.assertEquals("ABBBC", list.toString());
        }

        @Test
        @DisplayName("Should insert symbol before different symbol streak")
        void shouldInsertSymbolBeforeDifferentSymbolStreak() {
            appendAll('A', 'B', 'B', 'C');
            list.insert('X', 1);
            Assertions.assertEquals("AXBBC", list.toString());
        }

        @Test
        @DisplayName("Should insert symbol in between a streak of a different symbol")
        void shouldInsertSymbolInBetweenAStreakOfADifferentSymbol() {
            appendAll('A', 'B', 'B', 'C');
            list.insert('X', 2);
            Assertions.assertEquals("ABXBC", list.toString());
        }

        @Test
        @DisplayName("Should insert symbol after a streak of a different symbol")
        void shouldInsertSymbolAfterAStreakOfADifferentSymbol() {
            appendAll('A', 'B', 'B', 'C');
            list.insert('X', 3);
            Assertions.assertEquals("ABBXC", list.toString());
        }
    }

    @Nested
    @DisplayName("remove()")
    class Remove {
        @Test
        @DisplayName("Should throw IndexOutOfBoundsException when removing a negative index")
        void shouldThrowIndexOutOfBoundsExceptionWhenRemovingFromANegativeIndex() {
            list.append('A');
            Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
                list.remove(-1);
            });
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException when removing from an empty list")
        void shouldThrowIndexOutOfBoundsExceptionWhenRemovingFromAnEmptyList() {
            Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
                list.remove(0);
            });
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException when removing from a nonexistent index")
        void shouldThrowIndexOutOfBoundsExceptionWhenRemovingFromANonexistentIndex() {
            list.append('A');
            Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
                list.remove(1);
            });
        }

        @Test
        @DisplayName("Should remove the symbol from a list of size one")
        void shouldRemoveTheSymbolFromAListOfSizeOne() {
            list.append('A');
            int symbol = list.remove(0);
            Assertions.assertEquals(0, list.size());
            Assertions.assertEquals('A', symbol);
        }

        @Test
        @DisplayName("Should remove the first symbol from a list of same symbols")
        void shouldRemoveTheFirstSymbolFromAListOfSameSymbols() {
            appendAll('A', 'A', 'A');
            int symbol = list.remove(0);
            Assertions.assertEquals("AA", list.toString());
            Assertions.assertEquals('A', symbol);
        }

        @Test
        @DisplayName("Should remove the first symbol from a list that starts with the same repeated symbol")
        void shouldRemoveTheFirstSymbolFromAListThatStartsWithTheSameRepeatedSymbol() {
            appendAll('A', 'A', 'A', 'B', 'B', 'C');
            int symbol = list.remove(0);
            Assertions.assertEquals("AABBC", list.toString());
            Assertions.assertEquals('A', symbol);
        }

        @Test
        @DisplayName("Should remove the first symbol from a list of different symbols")
        void shouldRemoveTheFirstSymbolFromAListOfDifferentSymbols() {
            appendAll('A', 'B', 'C', 'D');
            int symbol = list.remove(0);
            Assertions.assertEquals("BCD", list.toString());
            Assertions.assertEquals('A', symbol);
        }


        @Test
        @DisplayName("Should remove the last symbol from a list of same symbols")
        void shouldRemoveTheLastSymbolFromAListOfSameSymbols() {
            appendAll('A', 'A', 'A');
            int symbol = list.remove(2);
            Assertions.assertEquals("AA", list.toString());
            Assertions.assertEquals('A', symbol);
        }

        @Test
        @DisplayName("Should remove the first symbol from a list that ends with the same repeated symbol")
        void shouldRemoveTheLastSymbolFromAListThatStartsWithTheSameRepeatedSymbol() {
            appendAll('A', 'B', 'B', 'C', 'C', 'C');
            int symbol = list.remove(5);
            Assertions.assertEquals("ABBCC", list.toString());
            Assertions.assertEquals('C', symbol);
        }

        @Test
        @DisplayName("Should remove the last symbol from a list that ends with the same repeated symbol")
        void shouldRemoveTheLastSymbolFromAListThatEndsWithTheSameRepeatedSymbol() {
            appendAll('A', 'B', 'C', 'D');
            int symbol = list.remove(3);
            Assertions.assertEquals("ABC", list.toString());
            Assertions.assertEquals('D', symbol);
        }

        @Test
        @DisplayName("Should remove a symbol at the beginning of a streak")
        void shouldRemoveASymbolAtTheBeginningOfAStreak() {
            appendAll('A', 'B', 'C', 'C', 'C', 'D');
            int symbol = list.remove(2);
            Assertions.assertEquals("ABCCD", list.toString());
            Assertions.assertEquals('C', symbol);
        }

        @Test
        @DisplayName("Should remove a symbol at the end of a streak")
        void shouldRemoveASymbolAtTheEndOfAStreak() {
            appendAll('A', 'B', 'C', 'C', 'C', 'D');
            int symbol = list.remove(4);
            Assertions.assertEquals("ABCCD", list.toString());
            Assertions.assertEquals('C', symbol);
        }

        @Test
        @DisplayName("Should remove a symbol in the middle of a streak")
        void shouldRemoveASymbolInTheMiddleOfAStreak() {
            appendAll('A', 'B', 'C', 'C', 'C', 'D');
            int symbol = list.remove(3);
            Assertions.assertEquals("ABCCD", list.toString());
            Assertions.assertEquals('C', symbol);
        }

        @Test
        @DisplayName("Should remove a symbol that is not part of a streak and is surrounded by different symbols")
        void shouldRemoveASymbolThatIsNotPartOfAStreakAndIsSurroundedByDifferentSymbols() {
            appendAll('A', 'B', 'C', 'D', 'E');
            int symbol = list.remove(2);
            Assertions.assertEquals("ABDE", list.toString());
            Assertions.assertEquals('C', symbol);
        }

        @Test
        @DisplayName("Should remove a symbol that is not part of a streak and is surrounded by the same symbol")
        void shouldRemoveASymbolThatIsNotPartOfAStreakAndIsSurroundedByTheSameSymbol() {
            appendAll('A', 'B', 'C', 'B', 'E');
            int symbol = list.remove(2);
            Assertions.assertEquals("ABBE", list.toString());
            Assertions.assertEquals('C', symbol);
        }
    }
}