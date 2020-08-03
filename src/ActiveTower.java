import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class ActiveTower extends Tower {
    // ATTRIBUTES
    private int coolDown;
    private double lastFired;
    private Timer timer;
    private double rotationAngle;
    private ArrayList<Projectile> projectileList;
    private Image projectileImage;
    // DEPENDENCY
    private ShadowDefend game;

    // CONSTRUCTOR
    public ActiveTower(Image towerImage, Image projectileImage, int radius, int damage, int price, int coolDown, Point location, ShadowDefend game) {
        super(towerImage, radius, damage, price, location, game);
        this.timer = new Timer(game, coolDown);
        this.coolDown = coolDown;
        this.game = game;
        this.projectileList = new ArrayList<Projectile>();
        this.projectileImage = projectileImage;
        this.lastFired = 0.0;
    }

    /** Returns the first enemy that is within the tower's radius
     * @Return: the slicer that is within radius OR null if no slicer is within radius*/
    private Slicer findEnemy() {
        // get list of slicers that are in the game
        List<Slicer> slicerList = game.getCurrentLevel().getSlicerList();
        // iterate through all possible slicers
        for (Slicer slicer : slicerList) {
            double distance = slicer.getCurrentLocation().distanceTo(super.getLocation());
            // distance is within radius
            if (distance < super.getRadius()) {
                return slicer;
            }
        }
        return null;
    }

    /** Fire the projectile towards the assigned Slicer
     * @param: slicer that projectile is aimed towards */
    private void fireProjectile(Slicer slicer) {
        // deploy projectile from tank location
        Projectile projectile = new Projectile(super.getLocation(), slicer, this.projectileImage, super.getDamage());
        projectileList.add(projectile);
        // rotate tank to face towards direction of projectile
        projectile.setInitialRotation();
        rotationAngle = projectile.getRotation()+Math.PI/2;
    }

    /** Returns whether tower has completed cool-down time and can fire new projectile */
    private boolean isReadyToFire() {
        if (timer.getMilliseconds()- lastFired >=coolDown || lastFired == 0.0) {
            return true;
        }
        return false;
    }

    @Override
    public void update(Input input) {
        timer.addFrame();
        // checks if it can fire projectile
        if (isReadyToFire()) {
            // linear search for the closest slicer that is within radius (findEnemy)
            Slicer target = findEnemy();
            // once it has chosen, fire projectile so that it follows the slicer (fireProjectile)
            if (target!=null) {
                fireProjectile(target);
                lastFired = timer.getMilliseconds();
            }
        }
        // clear projectile of any completed projectiles
        removeCompletedProjectiles();
        drawToGame(super.getLocation());
    }

    @Override
    public void drawToGame(Point position) {
        // draw the active tower
        // draw all the projectiles fired from this active tower
        for (Projectile projectile : projectileList) {
            projectile.update(null);
        }
        super.getTowerImage().draw(position.x, position.y, (new DrawOptions()).setRotation(rotationAngle));
    }

    /** Removes all projectiles fired from this active tower that are finished */
    private void removeCompletedProjectiles() {
        int i=0, size = projectileList.size();
        // iterate through all projectiles
        while (i<size) {
            // go to next projectile if current projectile is active
            if (projectileList.get(i).isActive()) {
                i++;
            }
            // remove projectile and decrease size of array if projectile is finished
            else {
                projectileList.remove(i);
                size--;
            }
        }
    }

    /** Draws the active towers and their projectiles in a frozen state*/
    public void drawSnapshot() {
        for (Projectile projectile : projectileList) {
            projectile.drawToGame(projectile.getCurrentLocation());
        }
        super.getTowerImage().draw(super.getLocation().x, super.getLocation().y, (new DrawOptions()).setRotation(rotationAngle));
    }

}
