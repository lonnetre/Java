package linkedlist_generic;

class RunLengthEncodedNode_L<T> {

    // Payload
    private final T SYMBOL;
    private int amount;

    // Reference to next node
    private RunLengthEncodedNode_L next;

    public RunLengthEncodedNode_L(T symbol) {
        this.SYMBOL = symbol;
        this.amount = 1;
    }

    public int increment() {
        this.amount += 1;
        return this.amount;
    }

    public int decrement() {
        this.amount -= 1;
        return this.amount;
    }

    public T getSymbol() {
        return this.SYMBOL;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public RunLengthEncodedNode_L getNext() {
        return next;
    }

    public void setNext(RunLengthEncodedNode_L next) {
        this.next = next;
    }
}
