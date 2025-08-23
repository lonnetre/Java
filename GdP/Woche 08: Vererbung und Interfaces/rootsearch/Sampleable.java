package rootsearch;

public interface Sampleable_L {

    /**
     * Evaluates the function at the position x and returns the value.
     * @param x the sample position
     * @return the function value at x
     */
    public double evaluateAt(double x);

    /**
     * Evaluates the derivative of the function at the position x and returns the value.
     * @param x the sample position
     * @return the derivative at x
     */
    public double evaluateDerivativeAt(double x);
}
