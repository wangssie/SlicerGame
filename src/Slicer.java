import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;

public abstract class Slicer extends MovingObject {
    // ATTRIBUTES
    private int health;
    private double speed;
    private int reward;
    private int penalty;
    private List<Point> path;
    private Rectangle boundary;
    private int childSpawnNumber;
    // DEPENDENCY ON GAME
    private ShadowDefend game;
    // STATIC VARIABLES (can be changed to allow higher time scales)
    private static final double MARGIN = 1.5; // Determines when the Slicer has "reached" a point

    // CONSTRUCTOR
    /** Constructor for slicer when it is spawned at the start of the map
     * @param health: health number of a slicer
     * @param objectImage: image of the slicer
     * @param penalty: amount of lives player will lose if slicer completes map without dying
     * @param reward: amount of money rewarded if player kills slicers
     * @param speed: speed that slicer travels
     * @param game: the game object */
    public Slicer(int health, double speed, int reward, int penalty, Image objectImage, ShadowDefend game) {
        // current location and destination can't be initialised until after path is established
        super(null, null, objectImage);
        // parameters of slicer
        this.health = health;
        this.speed = speed;
        this.reward = reward;
        this.penalty = penalty;
        this.game = game;
        // path that slicer will travel
        this.path = new ArrayList<Point>();
        path.addAll(game.getCurrentLevel().getMap().getAllPolylines().get(0));
        // initialise current location and destination
        super.setCurrentLocation(path.get(0));
        super.setDestination(path.get(0));
        // boundary of slicer
        updateBoundary();
    }

    /** Constructor for when the slicer is spawned as a child, in the middle of the map
     * @param parentSlicer: parent slicer that slicer is spawned from
     * @param health: health of slicer
     * @param speed: speed of slicer
     * @param reward: reward player is given if slicer is killed
     * @param penalty: number of lives player loses if slicer survives
     * @param childSpawnNumber: number of children slicer the slicer spawns when it dies
     * @param objectImage: image of the slicer
     * @param game: the game object
     */
    public Slicer(Slicer parentSlicer, int health, double speed, int reward, int penalty, int childSpawnNumber,Image objectImage, ShadowDefend game) {
        // current location can be initialised
        super(parentSlicer.getCurrentLocation(), parentSlicer.getDestination(), objectImage);
        super.setVelocity(parentSlicer.getVelocity());
        // parameters of slicer
        this.health = health;
        this.speed = speed;
        this.reward = reward;
        this.penalty = penalty;
        this.game = game;
        this.childSpawnNumber = childSpawnNumber;
        // path that slicer will travel
        this.path = new ArrayList<Point>();
        path.addAll(parentSlicer.getPath());
        // initialise destination
        // boundary of slicer
        updateBoundary();
    }

    // GETTER
    /** Get the Slicer's reward*/
    public int getReward() {
        return reward;
    }

    /** Get the Slicer's penalty */
    public int getPenalty() {
        return penalty;
    }

    /** Get the boundary for the slicer */
    public Rectangle getBoundary() {
        return boundary;
    }

    /** Get path of the slicer*/
    public List<Point> getPath() {
        return path;
    }

    /** Get the game */
    public ShadowDefend getGame() {
        return game;
    }

    /** Get the number of child slicer's spawned*/
    public int getChildSpawnNumber() {
        return childSpawnNumber;
    }

    // METHODS

    /** Returns whether slicer is dead (AKA has 0 health) or not */
    public boolean isDead() {
        if (health<=0) {
            return true;
        }
        return false;
    }

    /** Returns if slicer passed the map without being killed */
    public boolean isSurvived() {
        if (!this.hasNewLocation() && health>0) {
            return true;
        }
        return false;
    }

    /** Decrease slicer's health by damage caused
     * @param damage: amount of damage done to the slicer*/
    public void loseHealth(int damage) {
        this.health -= damage;
    }

    /** Returns true if the Slicer still has points to move to and false otherwise*/
    public boolean hasNewLocation() {
        if (path.isEmpty()) {
            return false;
        }
        return true;
    }

    /** Returns the Slicer's new location in the next frame */
    public Point newLocation() {
        // Slicer has approximately reached destination point (with a leeway of MARGIN*speedScale)
        if (Math.abs(super.getCurrentLocation().x-super.getDestination().x)<Timer.getScale()*MARGIN*speed && Math.abs(super.getCurrentLocation().y-super.getDestination().y)<Timer.getScale()*MARGIN*speed) {
            // update destination point to next point in path
            path.remove(0);
            // no more points in path, therefore no movement
            // return old location
            if (path.isEmpty()) {
                return super.getCurrentLocation();
            }
            super.setDestination(path.get(0));

            // update Slicer's velocity unit vector
            // velocity vector = unit vector (destination - current location)
            super.setVelocity(super.getDestination().asVector().sub(super.getCurrentLocation().asVector()));
            super.setVelocity(super.getVelocity().div(super.getVelocity().length()));
        }
        Point newLocation = new Point(super.getCurrentLocation().x+super.getVelocity().x*Timer.getScale()*speed, super.getCurrentLocation().y+super.getVelocity().y*Timer.getScale()*speed);
        // update the slicer's new location using the velocity unit vector, scaled by the speedScale value
        super.setCurrentLocation(newLocation);
        // return new location
        return super.getCurrentLocation();
    }

    /** Updates the slicers boundary rectangle to the slicer's current location*/
    private void updateBoundary() {
        Point point = super.getCurrentLocation();
        Image slicer = super.getObjectImage();
        this.boundary = slicer.getBoundingBoxAt(point);
    }

    /** Procedure for spawning children slicer when slicer dies */
    public abstract void spawnChildren();

    @Override
    public void update(Input input) {
        // draw slicer
        super.update(input);
        // update slicer's boundary
        updateBoundary();
    }
}
