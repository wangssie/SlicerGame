import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

public abstract class MovingObject implements Updateable, Drawable {
    // ATTRIBUTES
    private Point currentLocation;
    private Vector2 velocity;
    private Point destination;
    private Image objectImage;

    /** Constructor for moving objects
     * @param currentLocation: where the moving object is first located
     * @param destination: where the object will be headed to*/
    public MovingObject(Point currentLocation, Point destination, Image objectImage) {
        // initialised for all moving objects
        this.velocity = new Vector2(0,0);
        // assigned by subclass
        this.destination = destination;
        this.currentLocation = currentLocation;
        this.objectImage = objectImage;
    }

    // SETTERS
    /** Set the current location for the moving object
     * @param currentLocation: the new location we want to set the current location to*/
    public void setCurrentLocation(Point currentLocation) {
        this.currentLocation = currentLocation;
    }

    /** Set the destination point for the moving object
     * @param destination: the new location we want to set the destination to*/
    public void setDestination(Point destination) {
        this.destination = destination;
    }

    /** Set the velocity of the moving object
     * @param velocity: the new velocity vector we want to set the velocity to*/
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    // GETTERS
    /** Gets the current location of moving object */
    public Point getCurrentLocation() {
        return currentLocation;
    }

    /** Gets the destination point for the moving object */
    public Point getDestination() {
        return destination;
    }

    /** Get velocity of moving object*/
    public Vector2 getVelocity() {
        return velocity;
    }

    /** Gets the image of the moving object*/
    public Image getObjectImage() {
        return objectImage;
    }


    // METHODS
    /** Calculates and returns the radian of rotation */
    public double getRotation() {
        // get x and y values of velocity vector
        double x = this.velocity.x;
        double y = this.velocity.y;

        // avoids 0 denominator division
        if (x==0) {
            return (y>0)?Math.PI/2:-Math.PI/2;
        }

        // calculates base angle
        double angle = Math.atan(y/x);

        // 1st or 4th quadrant
        if (x>0) {
            return angle;
        }
        // 2nd or 3rd quadrant
        else {
            return Math.PI+angle;
        }
    }

    // ABSTRACT METHODS
    /** Return where the object should move to next*/
    public abstract Point newLocation();

    /** Check if object has a location to go to or has finished it's journey */
    public abstract boolean hasNewLocation();

    @Override
    public void drawToGame(Point position) {
        DrawOptions rotation = new DrawOptions().setRotation(this.getRotation());
        objectImage.draw(position.x, position.y, rotation);
    }

    @Override
    public void update(Input input) {
        Point newLocation = this.newLocation();
        drawToGame(newLocation);
    }
}
