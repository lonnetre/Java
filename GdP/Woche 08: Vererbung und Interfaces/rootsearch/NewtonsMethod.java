package rootsearch;

public class NewtonsMethod_L extends RootFindingAlgorithm_L {

	private final int MAX_ITERATIONS = 100_000;

	@Override
	public double findRoot(Sampleable_L function, double xMin, double xMax) {
		if (xMax <= xMin) {
			return Double.NaN;
		}

		// Start the search in the middle of the valid x values
		double x = (xMax + xMin) / 2;

		// Find the root with at most MAX_ITERATIONS iterations
		for(int i = 0; i < MAX_ITERATIONS; i++) {

			// Next Newton step
		    x = x - function.evaluateAt(x) / function.evaluateDerivativeAt(x);
		    double y = function.evaluateAt(x);

			// If the function value is close enough to 0, return the corresponding x
		    if (Math.abs(y) < EPSILON){
		        return x;
		    }
		}

		// Return Not a Number if no root was found with MAX_ITERATIONS iterations
		return Double.NaN;
	}
	
}
