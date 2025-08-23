package rootsearch;

public abstract class RootFindingAlgorithm_L {

	/**
	 * The precision of how close a function value must be to 0 to be considered a root
	 */
	protected final double EPSILON = 0.001;


	/**
	 * Finds the root of a given function.
	 * @param function the function to find the root of
	 * @param xMin the smallest x (inclusive) to look for a root
	 * @param xMax the largest x (exclusive) to look for a root
	 * @return the x position where the root was found
	 */
	public abstract double findRoot(Sampleable_L function, double xMin, double xMax);
}
