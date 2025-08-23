package rootsearch;

public class RootSearchEvaluation_L {

    public static void logRootFinding(Sampleable_L function, RootFindingAlgorithm_L rootFinder, double xMin, double xMax) {
        long startTime = System.currentTimeMillis();

        double x = rootFinder.findRoot(function, xMin, xMax);
        double y = (Double.isNaN(x)) ? Double.NaN : function.evaluateAt(x);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println(rootFinder.getClass().getSimpleName() + ":");
        System.out.println("- Duration: " + duration + "ms");
        System.out.print("- Precision: ");

        if (Double.isNaN(x)) {
            System.out.println("Did not find root");
        } else if (y == 0) {
            System.out.println("Perfect! -> f(" + x + ") = 0");
        } else {
            System.out.println("Numeric error of " + Math.abs(y) + " -> f(" + x + ") = " + y);
        }
    }

    public static void main(String[] args) {
        Sampleable_L[] functions = new Sampleable_L[]{
                new SinusFunction_L(),    // f(x) = sin(x)
                new PolynomialFunction_L(new double[] { 0, 1 }),  // f(x) = x
                new PolynomialFunction_L(new double[] { 5 }),  // f(x) = 5
                new PolynomialFunction_L(new double[] { -1, 0, 1 }),  // f(x) = x^2 - 1
                new PolynomialFunction_L(new double[] { -2, 0, 1 })  // f(x) = x^2 - 2
        };

        RootFindingAlgorithm_L[] algorithms = new RootFindingAlgorithm_L[]{
                new StepwiseSearch_L(),
                new NewtonsMethod_L()
        };

        for (int i = 0; i < functions.length; i++) {
            System.out.println(">>> Function Nr. " + (i+1));
            Sampleable_L function = functions[i];

            for (int j = 0; j < algorithms.length; j++) {
                RootFindingAlgorithm_L algorithm = algorithms[j];
                RootSearchEvaluation_L.logRootFinding(function, algorithm, -10_000, 50_000);
            }
        }
    }
}
