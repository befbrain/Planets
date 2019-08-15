import processing.core.PApplet; // Import processing stuff
import java.util.ArrayList; // Import the ArrayList class
import java.util.Scanner;  // Import the Scanner class

// Programs main class
public class Main extends PApplet {

    // Create the ArrayList that will store all the objects
    public ArrayList<Object> objs = new ArrayList<Object>(); // Array of all the objects in the "universe".

    public double accuracy = 10000; // The accuracy of the simulation. Every move will be multiplied 1/accuracy increase the correctness of the simulation
    public double size_factor = 1; // What to multiply the diameter of the object by when being drawn. Has no effect on how the objects will work
    public int frameRate = 240; // The simulation frame rate
    public int maxMass = 1; // The maximum mass of the objects. Used when getting there color
    public float scaleAmount = 1f;
    public int changeAmount;
    public int newObjMass;

    // A bunch of verbose variables for debugging
    public boolean showNextPos = false; // Show the next position of all the objects
    public boolean showObjInx = false; // Show the index of the objects
    public boolean showDist = false; // Show the distances between all the objects
    public boolean showForce = true; // Show the force of the objects

    public boolean askForChangeAmount = false;

    public boolean simulationMode = true;
    public int newObjIndx = -1;
    public int newObjStep = -1;

    // Main method
    public static void main(String[] args) {
        // Initialize processing
        PApplet.main("Main"); // Initialize Processing to use Main as the main class
    }

    // Settings for the canvas
    public void settings() {
        // set the canvas size to 800 by 800 px
        size(800, 800); // Set the size of the canvas to 800 by 800 px
    }

    // Setup the simulation
    public void setup() {

        if(askForChangeAmount) {
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object

            System.out.print("Enter Change Amount: ");
            changeAmount = myObj.nextInt();  // Read user input
        } else {
            changeAmount = 1;
        }

        // Set the frame rate
        frameRate(frameRate);
    }

    public void keyReleased() {
        if (key == ' ') {
            System.out.println("Pause / Resume");
            simulationMode = !simulationMode;

            if(simulationMode) {
                objs.remove(newObjIndx);
                newObjIndx = -1;
                newObjStep = -1;
            }
        }
    }

    public void mouseClicked() {
        if(!simulationMode) {
            if(newObjStep == 1) {
                newObjStep += 1;
                objs.get(newObjIndx).size = 0;
            } else if(newObjStep == 2) {
                newObjStep += 1;
                objs.get(newObjIndx).mass = 0;
            } else if(newObjStep == 3) {
                newObjStep += 1;
            } else if(newObjStep == 4) {
                objs.get(newObjIndx).force.x *= objs.get(newObjIndx).mass * 4;
                objs.get(newObjIndx).force.y *= objs.get(newObjIndx).mass * 4;
                objs.get(newObjIndx).shouldShow = true;
                newObjStep = -1;
                newObjIndx = -1;
            }
        }
    }

    public void draw() {

        scale(scaleAmount);

        background(255); // Clear, and set the background to white

        if(simulationMode) {
            drawObjs(); // Draw the objects
            updateVel(); // Update the velocity of all the objects
            updatePos(); // Update the position of the objects
        } else {
            drawObjs(); // Draw the objects

            if(newObjIndx == -1) {
                objs.add(new Object((double) changeAmount / 2, 0, 0, 20, new Vector( 0, 0) ));
                newObjIndx = objs.size() - 1;
                newObjStep = 1;
            }

            if(newObjStep == 1) {
                objs.get(newObjIndx).x = mouseX;
                objs.get(newObjIndx).y = mouseY;
            } else if(newObjStep == 2) {
                objs.get(newObjIndx).size = getDist(objs.get(newObjIndx).x, objs.get(newObjIndx).y, mouseX, mouseY);
            } else if(newObjStep == 3) {
                objs.get(newObjIndx).mass = Math.ceil( (double) changeAmount / 400) * getDist(objs.get(newObjIndx).x, objs.get(newObjIndx).y, mouseX, mouseY);
                addText(0, (float) objs.get(newObjIndx).x, (float) objs.get(newObjIndx).y, Double.toString(Math.round(objs.get(newObjIndx).mass * 100) / 100));
            } else if(newObjStep == 4) {
                objs.get(newObjIndx).force.x = (  mouseX - objs.get(newObjIndx).x );
                objs.get(newObjIndx).force.y = (  mouseY - objs.get(newObjIndx).y );
                stroke(getMassColorGrad(objs.get(newObjIndx).mass), 100);
                line((float) objs.get(newObjIndx).x, (float) objs.get(newObjIndx).y, (float) (objs.get(newObjIndx).force.x + objs.get(newObjIndx).x), (float) ( objs.get(newObjIndx).force.y + objs.get(newObjIndx).y) );
            }

            drawObj(newObjIndx, 150);
        }
    }

    private void drawObjs() {
        // Set there to be no border
        noStroke();

        for(int i = 0; i < objs.size(); i++) { // For every object
            if(objs.get(i).shouldShow)
                drawObj(i, 255);
        }
    }

    private void drawObj(int i, int alpha) {
        fill(getMassColorGrad(objs.get(i).mass), alpha); // Set the fill color to the returned value of the getMassColorGrad (see below)
        circle((float) objs.get(i).x, (float) objs.get(i).y, (float) ( objs.get(i).size * size_factor) ); // Create a circle in the correct location with the correct size

        // Show the objects index for debugging
        if(showObjInx) {
            addText(0, (float) objs.get(i).x, (float) objs.get(i).y + 4, Integer.toString(i));
        }
    }

    private void updatePos() {

        // Set there to be no border
        noStroke();

        for(int a = 0; a < objs.size(); a++) { // For every object
            // set the objects new position to its force/mass ( /accuracy ) in the x and y direction
            objs.get(a).x += objs.get(a).force.x * ( 1 / accuracy ) / objs.get(a).mass;
            objs.get(a).y += objs.get(a).force.y * ( 1 / accuracy ) / objs.get(a).mass;

            // If should, show the next position of the object
            if(showNextPos) {
                fill(getMassColorGrad(objs.get(a).mass), 100);
                circle((float) objs.get(a).x, (float) objs.get(a).y, (float) objs.get(a).size);
            }
        }
    }

    private void updateVel() {
        double G = 10; // Set the gravitational constant to 10
        for(int a = 0; a < objs.size(); a++) { // For every object
            Vector newVels = new Vector(0, 0);
            for (int b = 0; b < objs.size(); b++) { // For every object
                if (a != b) { // If object a, and object b are not the same object

                    // Get the difference in the x, and in the y direction
                    double xd = objs.get(b).x - objs.get(a).x;
                    double yd = objs.get(b).y - objs.get(a).y;

                    if(showDist) { // Show the x and y distances between the object a and b
                        stroke(getMassColorGrad(objs.get(a).mass), 100);
                        line((float) objs.get(a).x, (float) objs.get(a).y, (float) (objs.get(a).x + xd), (float) objs.get(a).y);
                        addText(0, (float) (objs.get(a).x + 0.5 * xd), (float) objs.get(a).y, "~" + Double.toString(Math.round(xd)));
                        line((float) (objs.get(a).x + xd), (float) objs.get(a).y, (float) (objs.get(a).x + xd), (float) (objs.get(a).y + yd));
                        addText((float) Math.PI / 2, (float) (objs.get(a).x + xd), (float) (objs.get(a).y + 0.5 * yd), "~" + Double.toString(Math.round(yd)));
                    }

                    // Get the distance between the two objects
                    double d = getDist(objs.get(a).x, objs.get(a).y, objs.get(b).x, objs.get(b).y);

                    // Get the angle betwen the two objects
                    double angle = Math.atan(xd / yd);

                    // Show the direct distance between the two objects
                    if(showDist) {
                        stroke(getMassColorGrad(objs.get(a).mass), 100);
                        line( (float) objs.get(a).x, (float) objs.get(a).y, (float) objs.get(b).x, (float) objs.get(b).y  );
                        addText((float) ( Math.PI/2 - angle ), (float) (objs.get(a).x + 0.5 * xd), (float) (objs.get(a).y + 0.5 * yd), "~" + Double.toString(Math.round(d)) + "@ " + Double.toString(Math.round(angle * 180 / Math.PI)) + "°");
                    }

                    // Calculate the net force
                    double Fn = ( G * objs.get(a).mass * objs.get(b).mass / d );

                    System.out.println(Fn);

                    // Variables that will be used to make up for the lost negatives in the distance equation
                    double x_mult = 1;
                    double y_mult = 1;

                    if(objs.get(a).x > objs.get(b).x && objs.get(a).y > objs.get(b).y) {
                        x_mult *= -1.0;
                        y_mult *= -1.0;
                    } else if(objs.get(a).x < objs.get(b).x && objs.get(a).y > objs.get(b).y) {
                        x_mult *= -1.0;
                        y_mult *= -1.0;
                    }

                    // Claculate the net force in the x and y direction
                    double Fx = Math.sin(angle) * Fn * x_mult;
                    double Fy = Math.cos(angle) * Fn * y_mult;

                    if(showForce) { // Show the force
                        stroke(getMassColorGrad(objs.get(a).mass), 100);
                        line( (float) objs.get(a).x, (float) objs.get(a).y, (float) ( objs.get(a).x + Fx ), (float) ( objs.get(a).y + Fy ) );
                    }

                    // Set those forces to be added to the object after the termination of the current loop
                    newVels.x += Fx;
                    newVels.y += Fy;
                }
            }

            // Update the objects force
            objs.get(a).force.x += newVels.x;
            objs.get(a).force.y += newVels.y;
        }
    }

    // ========== Helper Functions ==========

    private double getDist(double x1, double y1, double x2, double y2) {
        // Simply run the distance equation
        return Math.sqrt(
                Math.pow( x2 - x1, 2 ) + Math.pow( y2 - y1, 2 )
        );
    }

    private int getMassColorGrad(double mass) {

        updateMaxMass();
        // Use processings lerpColor function to get some color to represent the objects mass
        return lerpColor(color(0, 0, 255), color(255, 0, 0), (float) mass / maxMass);
    }

    private void updateMaxMass() {
        for(int i = 0; i < objs.size(); i++) {
            maxMass = (objs.get(i).mass > maxMass) ? (int) objs.get(i).mass : maxMass;
        }
    }

    private void addText(float radians, float x, float y, String text) {
        // Add text to the canvas at some given location, and rotation
        fill(0);
        pushMatrix();
        textAlign(CENTER);
        translate(x,y);
        rotate( radians );
        text(text,0,0);
        popMatrix();
    }
}
