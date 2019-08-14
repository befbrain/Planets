
public class Object {

    public double mass;
    public Vector force;
    public double x;
    public double y;
    public double size;

    public Object(double mass_param, double x_pos, double y_pos, double size_param, Vector force_param) {
        mass = mass_param;
        force = force_param;
        x = x_pos;
        y = y_pos;
        size = size_param;
    }
}