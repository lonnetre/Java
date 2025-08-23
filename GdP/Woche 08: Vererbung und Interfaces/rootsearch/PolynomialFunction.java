package rootsearch;

public class PolynomialFunction_L implements Sampleable_L {
	
	private double[] coefficients;

	/**
	 * Constructs a polynomial function from the given coefficients of the form:
	 * f(x) = a_0 + a_1 * x + ... +  a_{n-2}x^{n-2} + a_{n-1}x^{n-1} + a_n x^n
	 * @param coefficients the polynomial coefficients, where the first
	 *                     array entry is a_0, the second a_1 and so on.
	 */
	public PolynomialFunction_L(double[] coefficients){
		this.coefficients = coefficients;
	}


	@Override
	public double evaluateAt(double x) {
		double sum = 0;
		for(int i = 0; i < coefficients.length; i++){
			sum += coefficients[i] * Math.pow(x, i);
		}
		return sum;
	}


	@Override
	public double evaluateDerivativeAt(double x) {
		double sum = 0;
		for(int i = 1; i < coefficients.length; i++){
			sum += i * coefficients[i] * Math.pow(x, i-1);
		}
		return sum;
	}

}
