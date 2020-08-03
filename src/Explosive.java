import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class Explosive implements Updateable, Drawable {
    // ATTRIBUTES
    private Point location;
    private int damage;
    private Timer timer;
    private ArrayList<Slicer> slicerInRangeList;
    private Rectangle range;
    private boolean active;
    // STATIC ATTRIBUTES (can be changed)
    public static final double detonationTime = 2000;
    public static final Image explosiveImage = new Image("res/images/explosive.png");
    // DEPENDENCY
    private ShadowDefend game;

    // CONSTRUCTOR
    /** Constructor of an explosive
     * @param airplane: the airplane which the explosive was deployed from
     * @param game: the game object */
    public Explosive (Airplane airplane, ShadowDefend game) {
        this.damage = Airplane.damage;
        this.location = airplane.getCurrentLocation();
        this.range = new Rectangle(location.x-Airplane.radius, location.y-Airplane.radius, 2*Airplane.radius, 2*Airplane.radius);
        this.timer = new Timer(game, 0);
        this.slicerInRangeList = new ArrayList<Slicer>();
        this.active = true;
        this.game = game;
    }

    // GETTER
    /** Returns whether the explosive has exploded (false) or awaiting detonation (true)*/
    public boolean isActive() {
        return active;
    }

    /** Find all the slicers on the game map that are within the range of the explosion*/
    private void findSlicers() {
        ArrayList<Slicer> slicerList = game.getCurrentLevel().getSlicerList();
        // iterate through game's slicer list
        for (Slicer slicer : slicerList) {
            // add slicer if within explosion range
            if (range.intersects(slicer.getCurrentLocation())) {
                slicerInRangeList.add(slicer);
            }
        }
    }

    /** Deal damage on every slicer in game that is within range of explosion*/
    private void dealDamage() {
        // iterate through each slicer within range of explosion
        for (Slicer slicer : slicerInRangeList) {
            // deal damage
            slicer.loseHealth(damage);
        }
    }

    /** Return true if explosion has reached time to explode */
    private boolean isReadyToExplode() {
        if (timer.getMilliseconds() == detonationTime) {
            return true;
        }
        return false;
    }

    /** Detonate the explosion and deal damage to all slicers in range*/
    private void detonate() {
        findSlicers();
        dealDamage();
        this.active = false;
    }

    @Override
    public void update(Input input) {
        timer.addFrame();
        // explosive has reached detonation time
        if (isReadyToExplode()) {
            // detonate
            detonate();
        }
        // explosive has not been detonated, drawn on map
        else {
            drawToGame(null);
        }
    }

    @Override
    public void drawToGame(Point position) {
        // draw explosive onto game
        explosiveImage.draw(this.location.x, this.location.y);
    }
}
