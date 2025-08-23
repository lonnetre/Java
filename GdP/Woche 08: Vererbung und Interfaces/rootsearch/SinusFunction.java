package rootsearch;

public class SinusFunction_L implements Sampleable_L {

    @Override
    public double evaluateAt(double x) {
        return Math.sin(x);
    }


    @Override
    public double evaluateDerivativeAt(double x) {
        return Math.cos(x);
    }
}
