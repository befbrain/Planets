import processing.core.PApplet;
import java.util.ArrayList;

public class Main extends PApplet {

    public ArrayList<Object> objs = new ArrayList<Object>(); // Array of all the objects in the "universe".

    public double accuracy = 1000; // The accuracy of the simulation
    public double size_factor = 1; // What to multiply the diameter of the object by when being drawn.
    public int frameRate = 120; // The simulation frame rate
    public int maxMass = 100;

    public boolean showNextPos = false;
    public boolean showObjInx = false;
    public boolean showDist = false;
    public boolean showForce = false;

    // Main method
    public static void main(String[] args) {
        PApplet.main("Main"); // Initialize Processing to use Main as the main class
    }

    // Settings for the canvas
    public void settings() {
        size(800, 800); // Set the size of the canvas to 800 by 800 px
    }

    // Setup the simulation
    public void setup() {
        // Set the frame rate
        frameRate(frameRate);

        // Create the objects for the simulation
        objs.add(new Object(81, width/2, height/2, 20, new Vector(0, 0) ));
        objs.add(new Object(1, width/2 + 238.9, height/2, 3, new Vector( 0, -900.3) ));
    }

    public void draw() {
        background(255); // Clear, and set the background to white

        drawObjs(); // Draw the objects

        updateVel(); // Update the velocity of all the objects
        updatePos(); // Update the position of the objects
    }

    private void drawObjs() {
        noStroke();
        for(int i = 0; i < objs.size(); i++) {
            fill(getMassColorGrad(objs.get(i).mass));
            circle((float) objs.get(i).x, (float) objs.get(i).y, (float) objs.get(i).size);

            if(showObjInx) {
                addText(0, (float) objs.get(i).x, (float) objs.get(i).y + 4, Integer.toString(i));
            }
        }
    }

    private void updatePos() {
        noStroke();
        fill(100);
        for(int a = 0; a < objs.size(); a++) {
            objs.get(a).x += objs.get(a).force.x * ( 1 / accuracy ) / objs.get(a).mass;
            objs.get(a).y += objs.get(a).force.y * ( 1 / accuracy ) / objs.get(a).mass;
            if(showNextPos) {
                fill(getMassColorGrad(objs.get(a).mass), 100);
                circle((float) objs.get(a).x, (float) objs.get(a).y, (float) objs.get(a).size);
            }
        }
    }

    private void updateVel() {
        double G = 10;
//        ArrayList<Vector> newVels = new ArrayList<Vector>();
        for(int a = 0; a < objs.size(); a++) { // For every object
            Vector newVels = new Vector(0, 0);
            for (int b = 0; b < objs.size(); b++) { // For every object
                if (a != b) { // If object a, and object b are not the same object

                    // Get the difference in the x, and in the y direction
                    double xd = objs.get(b).x - objs.get(a).x;
                    double yd = objs.get(b).y - objs.get(a).y;

                    if(showDist) {
                        stroke(getMassColorGrad(objs.get(a).mass), 100);
                        line((float) objs.get(a).x, (float) objs.get(a).y, (float) (objs.get(a).x + xd), (float) objs.get(a).y);
                        addText(0, (float) (objs.get(a).x + 0.5 * xd), (float) objs.get(a).y, "~" + Double.toString(Math.round(xd)));
                        line((float) (objs.get(a).x + xd), (float) objs.get(a).y, (float) (objs.get(a).x + xd), (float) (objs.get(a).y + yd));
                        addText((float) Math.PI / 2, (float) (objs.get(a).x + xd), (float) (objs.get(a).y + 0.5 * yd), "~" + Double.toString(Math.round(yd)));
                    }

                    double d = getDist(objs.get(a).x, objs.get(a).y, objs.get(b).x, objs.get(b).y);
                    double angle = Math.atan(xd / yd);
                    if(showDist) {
                        stroke(getMassColorGrad(objs.get(a).mass), 100);
                        line( (float) objs.get(a).x, (float) objs.get(a).y, (float) objs.get(b).x, (float) objs.get(b).y  );
                        addText((float) ( Math.PI/2 - angle ), (float) (objs.get(a).x + 0.5 * xd), (float) (objs.get(a).y + 0.5 * yd), "~" + Double.toString(Math.round(d)) + "@ " + Double.toString(Math.round(angle * 180 / Math.PI)) + "°");
                    }

                    double Fn = ( G * objs.get(a).mass * objs.get(b).mass / d );

                    double x_mult = 1;
                    double y_mult = 1;

                    if(objs.get(a).x > objs.get(b).x && objs.get(a).y > objs.get(b).y) {
                        x_mult *= -1.0;
                        y_mult *= -1.0;
                    } else if(objs.get(a).x < objs.get(b).x && objs.get(a).y > objs.get(b).y) {
                        x_mult *= -1.0;
                        y_mult *= -1.0;
                    }

                    double Fx = Math.sin(angle) * Fn * x_mult;
                    double Fy = Math.cos(angle) * Fn * y_mult;

                    if(showForce) {
                        stroke(getMassColorGrad(objs.get(a).mass), 100);
                        line( (float) objs.get(a).x, (float) objs.get(a).y, (float) ( objs.get(a).x + Fx ), (float) ( objs.get(a).y + Fy ) );
                    }

                    newVels.x += Fx;
                    newVels.y += Fy;
                }
            }

            objs.get(a).force.x += newVels.x;
            objs.get(a).force.y += newVels.y;
        }
    }

    // ========== Helper Functions ==========

    private double getDist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(
                Math.pow( x2 - x1, 2 ) + Math.pow( y2 - y1, 2 )
        );
    }

    private int getMassColorGrad(double mass) {
        return lerpColor(color(0, 0, 255), color(255, 0, 0), (float) mass / maxMass);
    }

    private void addText(float radians, float x, float y, String text) {
        fill(0);
        pushMatrix();
        textAlign(CENTER);
        translate(x,y);
        rotate( radians );
        text(text,0,0);
        popMatrix();
    }
}
