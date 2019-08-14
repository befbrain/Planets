public class Vector {

    public double x;
    public double y;

    public Vector(double x_pos, double y_pos) {
        x = x_pos;
        y = y_pos;
    }

    public void println() {
        System.out.print(" [ ");
        System.out.print(x);
        System.out.print(", ");
        System.out.print(y);
        System.out.print(" ] \n");
    }
}
