import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Airplane extends Tower{
    // STATIC PARAMETERS (CAN BE CHANGED)
    public static final Image airplaneImage = new Image("res/images/airsupport.png");
    public static final int radius = 200;
    public final static int damage = 500;
    public final static int price = 500;
    public final static double speed = 3.0;
    private static Orientation direction = Orientation.HORIZONTAL;
    private final static double lowerBoundDeploymentTime = 1000;
    private final static double upperBoundDeploymentTime = 2000;
    // ATTRIBUTES
    private boolean active;
    private Vector2 velocity;
    private Point currentLocation;
    private Point destination;
    private double rotationAngle;
    private DrawOptions rotation;
    private ArrayList<Explosive> explosiveList;
    private double nextExplosiveTime;
    private Timer timer;
    // DEPENDENCY
    private ShadowDefend game;

    /** Constructor of airplane
     * @param spawnLocation: the point where the airplane was spawned
     * @param game: the game class */
    Airplane(Point spawnLocation, ShadowDefend game) {
        super(airplaneImage, radius, damage, price, spawnLocation, game);
        this.game = game;
        this.active = true;
        this.rotation = new DrawOptions();
        this.explosiveList = new ArrayList<Explosive>();
        this.nextExplosiveTime = 0.0;
        this.nextExplosiveTime = getNextExplosiveTime();
        this.timer = new Timer(game, 0);
        if (spawnLocation!=null) {
            setPath();
            setVelocityVector();
            setRotation();
        }
    }

    // GETTERS
    /** Returns if the airplane is still active */
    public boolean isActive() {
        return active;
    }

    /** Returns the airplane's current location in flight*/
    public Point getCurrentLocation() {
        return currentLocation;
    }

    // METHODS
    /** Initialise the velocity vector of the airplane*/
    private void setVelocityVector() {
        Vector2 velocityVector = destination.asVector().sub(currentLocation.asVector());
        velocityVector = velocityVector.div(velocityVector.length()).mul(speed);
        this.velocity = velocityVector;
    }

    /** Returns the new location that the airplane will travel to */
    private Point newLocation() {
        Point newLocation = new Point(currentLocation.x + velocity.x*Timer.getScale(), currentLocation.y+velocity.y*Timer.getScale());
        this.currentLocation = newLocation;
        return newLocation;
    }

    /** Returns whether the airplane still needs to travel or has completed its path */
    private boolean hasNextLocation() {
        // horizontal traversal
        if (direction == Orientation.HORIZONTAL) {
            if (currentLocation.x>=game.getCurrentLevel().getMap().getWidth()) {
                return false;
            }
        }
        // vertical traversal
        else {
            if (currentLocation.y>=game.getCurrentLevel().getMap().getHeight()) {
                return false;
            }
        }
        return true;
    }

    /** Calculates and sets the rotation (in radians) of the airplane */
    private void setRotation() {
        // rotate airplane 90 degrees to the right
        if (direction==Orientation.HORIZONTAL) {
            this.rotationAngle= Math.PI/2;
        }
        // keep rotation as it is
        else {
            this.rotationAngle = Math.PI;
        }
    }

    /** Initialises the start (Current location) and destination point of the airplane */
    private void setPath() {
        // check orientation
        // horizontal orientation
        if (direction == Orientation.HORIZONTAL) {
            this.currentLocation = new Point(0, super.getLocation().y);
            this.destination = new Point(game.getCurrentLevel().getMap().getWidth(), super.getLocation().y);
        }
        // vertical orientation
        else {
            this.currentLocation = new Point(super.getLocation().x,0);
            this.destination = new Point(super.getLocation().x, game.getCurrentLevel().getMap().getHeight());
        }
    }

    /** Deploys an explosive onto the map */
    private void deployExplosive() {
        explosiveList.add(new Explosive(this, game));
        // update when the last explosive time was made
        this.nextExplosiveTime = getNextExplosiveTime();
    }

    /** Returns the next explosive time
     * @return: the previous explosive time + a random time between 1000-2000 milliseconds inclusive, incremented by 100ms */
    private double getNextExplosiveTime() {
        Random rand = new Random();
        int range = (int)((upperBoundDeploymentTime-lowerBoundDeploymentTime)/Timer.DEFAULTmsINCREMENT) + 1;
        return nextExplosiveTime+lowerBoundDeploymentTime+rand.nextInt(range)*(double)Timer.DEFAULTmsINCREMENT;
    }

    @Override
    public void drawToGame(Point position) {
        // draw plane
        airplaneImage.draw(position.x, position.y, rotation.setRotation(rotationAngle));
        // draw explosives
        for (Explosive explosive : explosiveList) {
            explosive.update(null);
        }
    }

    @Override
    public void update(Input input) {
        // plane will continue travelling across map
        timer.addFrame();
        removeCompletedExplosives();
        if (hasNextLocation()) {
            drawToGame(this.newLocation());
            // if timer has reached random time between 1-2 secs
            if (timer.getMilliseconds()==nextExplosiveTime) {
                deployExplosive();
            }
        }
        // plane finished travelling but explosives still active
        else if (!explosiveList.isEmpty()) {
            drawToGame(this.newLocation());
        }
        // plane has reached/passed destination and explosives are complete
        else {
            this.active = false;
        }

    }

    /** Change the orientation of the next airplane being spawned */
    public static void changeDirection() {
        if (direction == Orientation.HORIZONTAL) {
            Airplane.direction = Orientation.VERTICAL;
        }
        else {
            Airplane.direction = Orientation.HORIZONTAL;
        }
    }

    /** Remove explosives that have already detonated from airplane's list of explosions*/
    private void removeCompletedExplosives() {
        int i=0, size = explosiveList.size();
        while (i<size) {
            if (explosiveList.get(i).isActive()) {
                i++;
            }
            else {
                explosiveList.remove(i);
                size--;
            }
        }
    }

    /** Draws the airplanes in a frozen state*/
    public void drawSnapshot() {
        // draw plane
        airplaneImage.draw(currentLocation.x, currentLocation.y, rotation.setRotation(rotationAngle));
        // draw explosives
        for (Explosive explosive : explosiveList) {
            explosive.drawToGame(null);
        }
    }
}
