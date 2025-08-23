public class Circle_L {

    // Compiler error 3: Removed 'static'
    static final String[] AVAILABLE_COLORS = { "RED", "GREEN", "BLUE" };
    static int activeColor = 0;

    int radius;
    double posX;
    double posY;

    Circle_L(int r, double x, double y) {
        this.radius = r;
        this.posX = x;
        this.posY = y;
    }

    // Compiler error 1: Added 'static'
    double computeArea() {
        return this.radius * this.radius * Math.PI;
    }

    double computeCircumference() {
        return 2 * this.radius * Math.PI;
    }

    static String getActiveColor() {
        return Circle_L.AVAILABLE_COLORS[Circle_L.activeColor];
    }

    static void switchToNextColor() {
        Circle_L.activeColor = (Circle_L.activeColor + 1) % AVAILABLE_COLORS.length;
    }

    public static void main(String[] args) {
        Circle_L circle = new Circle_L(10, 5, 5);

        // Compiler error 2: changed 'circle' to 'Circle'
        System.out.println("Circumference: " + circle.computeCircumference());
    }
}
