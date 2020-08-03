import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

public class Projectile extends MovingObject {
    // STATIC ATTRIBUTE (can be changed)
    private static final int speed = 10;
    // PARAMETERS
    private int damage;
    private boolean active;
    private Slicer target;

    // CONSTRUCTOR
    /** Constructor of a projectile
     * @param damage: amount of damage caused by projectile
     * @param projectileImage: the image of the projectile
     * @param spawnLocation: the location where the projectile is being fired from
     * @param target: the slicer that the projectile is targeted to hit*/
    public Projectile(Point spawnLocation, Slicer target, Image projectileImage, int damage) {
        super(spawnLocation, target.getCurrentLocation(), projectileImage);
        this.damage = damage;
        this.target = target;
        // projectile is active (has not hit slicer yet)
        this.active = true;
    }

    // GETTER

    /** Returns if projectile is still active or has reached the end */
    public boolean isActive() {
        return active;
    }

    // METHODS

    /** Set the initial orientation of the projectile so tower can get rotation */
    public void setInitialRotation() {
        Vector2 directionVector = target.getCurrentLocation().asVector().sub(super.getCurrentLocation().asVector());
        super.setVelocity(directionVector);
    }

    /** Returns if projectile has new place to move to or has already intersected with target Slicer*/
    @Override
    public boolean hasNewLocation() {
        // check if projectile bounding box has intersected with slicer's bounding box
        if (target.getBoundary().intersects(super.getCurrentLocation())) {
            return false;
        }
        return true;
    }

    /** Get the new location of projectile and assign the new location to the projectile's current location*/
    @Override
    public Point newLocation() {
        // get new destination aka slicer's new position
        Point slicerNewPosition = target.getCurrentLocation();
        // set new vector for projectile
        Vector2 newVelocity = slicerNewPosition.asVector().sub(super.getCurrentLocation().asVector());
        newVelocity = newVelocity.div(newVelocity.length()).mul(speed*Timer.getScale());
        // update current location
        Point oldLocation = super.getCurrentLocation();
        Point newLocation = new Point(oldLocation.x + newVelocity.x, oldLocation.y + newVelocity.y);
        super.setCurrentLocation(newLocation);
        // return the new location aka current location
        return newLocation;
    }

    @Override
    public void update(Input input) {
        // projectile has not hit slicer
        if (hasNewLocation()) {
            super.update(input);
        }
        // projectile has hit slicer
        else {
            // deal damage to slicer
            target.loseHealth(damage);
            // remove projectile from list of projectiles from active tower
            this.active = false;
        }
    }

}
