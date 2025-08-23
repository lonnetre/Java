package JUnitRectangle;

public class Rectangle_L {
    public static int computePerimeter(int width, int height) {
        if (width < 0 || height < 0) {
            throw new ArithmeticException("The perimeter cannot be computed from negative values!");
        }
        return (width + height) * 2;
    }

}