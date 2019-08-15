
public class Object {

    public double mass;
    public Vector force;
    public double x;
    public double y;
    public double size;
    public boolean shouldShow = false;

    public Object(double mass_param, double x_pos, double y_pos, double size_param, Vector force_param) {
        initObj(mass_param, x_pos, y_pos, size_param, force_param);
    }

    public Object(double mass_param, double x_pos, double y_pos, double size_param, Vector force_param, boolean shouldShow_param) {
        initObj(mass_param, x_pos, y_pos, size_param, force_param);

        if(shouldShow_param) {
            shouldShow = shouldShow_param;
        }
    }

    public void initObj(double mass_param, double x_pos, double y_pos, double size_param, Vector force_param) {
        mass = mass_param;
        force = force_param;
        x = x_pos;
        y = y_pos;
        size = size_param;
    }
}