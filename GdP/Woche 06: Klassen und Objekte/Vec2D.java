public class Vec2D_L {
    double x;
    double y;

    Vec2D_L(double x, double y){
        this.x = x;
        this.y = y;
    }

    Vec2D_L(){
        this(0.0,0.0);
    }

    double length(){
        return Math.sqrt( x * x + y * y);
    }

    public static void main(String [] args){
        Vec2D_L a = new Vec2D_L();
        Vec2D_L b = new Vec2D_L(3.0,4.0);

        System.out.println("Vector a: [" + a.x + ", " + a.y + "] has length " + a.length());
        System.out.println("Vector b: [" + b.x + ", " + b.y + "] has length " + b.length());
    }
}
