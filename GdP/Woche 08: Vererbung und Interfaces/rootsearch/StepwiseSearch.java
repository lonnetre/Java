package rootsearch;

public class StepwiseSearch_L extends RootFindingAlgorithm_L {
	
	private final double STEP_SIZE;
	
	public StepwiseSearch_L(double stepSize){
		this.STEP_SIZE = stepSize;
	}

	public StepwiseSearch_L(){
		this(0.25);
	}
	
	@Override
	public double findRoot(Sampleable_L function, double xMin, double xMax) {

		// Step through the function with step size STEP_SIZE
		for (double x = xMin; x < xMax; x += STEP_SIZE) {
			double val = function.evaluateAt(x);

			// If the function value is close enough to 0, return the corresponding x
			if (Math.abs(val) < EPSILON) {
				return x;
			}
		}

		// Return Not a Number if no root was found
		return Double.NaN;
	}

}
