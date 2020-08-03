import bagel.Image;
import bagel.util.Point;

public class SuperTank extends ActiveTower {
    // STATIC VARIABLES (can be changed)
    public static final int radius =150;
    public static final int damage = 3*Tank.damage;
    public static final int coolDown = 500;
    public static final int price = 600;
    public static final Image superTank = new Image("res/images/supertank.png");
    public static final Image superTankProjectile = new Image("res/images/supertank_projectile.png");

    // CONSTRUCTOR

    /** Constructor of a super tank
     * @param location: location of where the supertank is placed
     * @param game: the game object
     */
    public SuperTank(Point location, ShadowDefend game) {
        super(superTank, superTankProjectile, radius, damage, price, coolDown, location, game);
    }


}
