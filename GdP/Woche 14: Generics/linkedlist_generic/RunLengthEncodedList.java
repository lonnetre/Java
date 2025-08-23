package linkedlist_generic;

public class RunLengthEncodedList_L<T> implements GenericList_L<T> {

    private RunLengthEncodedNode_L<T> head;
    private long size;


    @Override
    public void append(T symbol) {

        // If the list is empty, have head point to a new node
        if (this.size() == 0) {
            this.head = new RunLengthEncodedNode_L<T>(symbol);
            this.size = 1;
            return;
        }

        // Iterate over the list and remember the current node
        RunLengthEncodedNode_L<T> current = this.head;
        while(current.getNext() != null) {
            // Go to the next node
            current = current.getNext();
        }

        if (current.getSymbol().equals(symbol)) {
            // If the last list node stores the same symbol, increase its amount
            current.increment();
        } else {
            // Otherwise append a new node to the list
            current.setNext(new RunLengthEncodedNode_L<T>(symbol));
        }

        this.size += 1;
    }


    @Override
    public String toString() {

        // If a String is composed of multiple parts that are
        // added one after another, a StringBuilder is more efficient
        // than a common String, since they are immutable
        StringBuilder builder = new StringBuilder();

        // Iterate over the list and remember the current node
        RunLengthEncodedNode_L current = this.head;

        while(current != null) {
            // Get the symbol and amount
            String symbol = current.getSymbol().toString();
            int symbolCount = current.getAmount();

            // Make a String out of the symbol that repeats the symbol amount-times and add to the StringBuilder
            String repeatedString = symbol.repeat(symbolCount);
            builder.append(repeatedString);

            // Go to the next node
            current = current.getNext();
        }

        return builder.toString();
    }


    @Override
    public T get(long index) {

        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }

        // Iterate over the list and remember the current node
        // and count the number of seen symbols
        RunLengthEncodedNode_L<T> current = this.head;
        long symbolCount = 0;

        T result = null;
        while(current != null) {
            // Count the number of symbols
            symbolCount += current.getAmount();

            if (symbolCount > index) {
                // Found the node that covers the requested index.
                // Remember the symbol and stop searching.
                result = current.getSymbol();
                break;
            }

            // Go to the next node
            current = current.getNext();
        }

        return result;
    }


    @Override
    public long size() {
        // The size is stored in a member variable, so that the size() call
        // is fast. A drawback to this solution is, that one has to be careful
        // to not forget updating the size at all relevant places in the code.
        // An alternative but slower solution would be to iterate over the
        // whole list and count the number of symbols.
        return this.size;
    }


    @Override
    public void insert(T symbol, long index) {

        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }

        this.size += 1;

        if (index == 0 && !symbol.equals(this.head.getSymbol())) {
            // Special case: Adds a new node to the beginning of the list
            RunLengthEncodedNode_L<T> newSymbolNode = new RunLengthEncodedNode_L<T>(symbol);
            newSymbolNode.setNext(this.head);
            this.head = newSymbolNode;
            return;
        }

        // Iterate over the list and remember the current node
        // and count the number of total symbols
        RunLengthEncodedNode_L<T> current = this.head;
        long numPreviousSymbols = 0;

        while(current != null) {

            boolean indexContainedByCurrentNode = index < numPreviousSymbols + current.getAmount();
            boolean indexDirectlyAfterCurrentNode = index == numPreviousSymbols + current.getAmount();

            if ((indexContainedByCurrentNode || indexDirectlyAfterCurrentNode) && symbol.equals(current.getSymbol())) {
                // The symbol adds to the current node
                current.increment();
                break;

            } else if (indexDirectlyAfterCurrentNode) {
                if (symbol.equals(current.getNext().getSymbol())) {
                    // The symbol adds to the next node
                    current.getNext().increment();

                } else {
                    // Adds a new node between the current and the next node
                    RunLengthEncodedNode_L<T> newSymbolNode = new RunLengthEncodedNode_L<T>(symbol);
                    newSymbolNode.setNext(current.getNext());
                    current.setNext(newSymbolNode);
                }
                break;

            } else if (indexContainedByCurrentNode) {
                // The symbol (B) interrupts the current node's streak (AAA): AA -> B -> A

                // The second part of the interrupted streak (A) requires a new node
                RunLengthEncodedNode_L<T> nextNode = new RunLengthEncodedNode_L<T>(current.getSymbol());
                nextNode.setAmount((int) (numPreviousSymbols + current.getAmount() - index));
                nextNode.setNext(current.getNext());

                RunLengthEncodedNode_L<T> newSymbolNode = new RunLengthEncodedNode_L<T>(symbol);
                newSymbolNode.setNext(nextNode);

                current.setAmount((int) (index - numPreviousSymbols));
                current.setNext(newSymbolNode);

                break;
            }

            // Go to the next node
            numPreviousSymbols += current.getAmount();
            current = current.getNext();
        }
    }


    @Override
    public T remove(long index) {

        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }

        this.size -= 1;

        // Special case: Check if the first node is affected
        if (index < this.head.getAmount()) {
            T result = this.head.getSymbol();
            this.head.decrement();

            // If this node is empty now, remove it from the list
            if (this.head.getAmount() == 0) {
                this.head = this.head.getNext();
            }

            return result;
        }

        // Iterate over the list; start at second node. Remember the current node and the previous node.
        // Count the number of total symbols
        RunLengthEncodedNode_L<T> previous = this.head;
        RunLengthEncodedNode_L<T> current = this.head.getNext();
        long numPreviousSymbols = this.head.getAmount();

        T result = null;

        while(current != null) {
            boolean indexContainedByCurrentNode = index < numPreviousSymbols + current.getAmount();

            if (indexContainedByCurrentNode) {
                result = current.getSymbol();
                current.decrement();

                // If this node is empty now, remove it from the list
                if (current.getAmount() == 0) {
                    previous.setNext(current.getNext());

                    // If the previous and next node share the same symbol, merge them
                    RunLengthEncodedNode_L<T> next = previous.getNext();
                    if (next != null && previous.getSymbol() == next.getSymbol()) {
                        previous.setAmount(previous.getAmount() + next.getAmount());
                        previous.setNext(next.getNext());
                    }
                }

                break;
            }

            // Go to the next node
            numPreviousSymbols += current.getAmount();
            previous = current;
            current = current.getNext();
        }

        return result;
    }
}
