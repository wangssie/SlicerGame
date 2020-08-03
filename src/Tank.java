import bagel.Image;
import bagel.util.Point;

public class Tank extends ActiveTower {
    // STATIC VARIABLES (can change)
    public static final int radius =100;
    public static final int damage = 1;
    public static final int coolDown = 1000;
    public static final int price = 250;
    public static final Image tank = new Image("res/images/tank.png");
    public static final Image tankProjectile = new Image("res/images/tank_projectile.png");

    /** Constructor of a tank
     * @param location: location of where tank will be placed
     * @param game: the game object*/
    public Tank(Point location, ShadowDefend game) {
        super(tank, tankProjectile, radius, damage, price, coolDown,location, game);
    }


}
