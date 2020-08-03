import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Tower implements Updateable, Drawable{
    // ATTRIBUTES
    private Image towerImage;
    private int radius;
    private int damage;
    private int price;
    private Point location;
    private Rectangle range;
    private Rectangle boundary;
    // DEPENDENCY
    private ShadowDefend game;

    // CONSTRUCTOR

    /** Constructor for a tower
     * @param towerImage: image of the tower
     * @param radius: the range that the tower can attack from
     * @param damage: the damage the tower can inflict on enemies
     * @param price: the cost to purchase the tower
     * @param location: the location of where the tower will be placed
     * @param game: the game object
     */
    public Tower(Image towerImage, int radius, int damage, int price, Point location, ShadowDefend game) {
        this.towerImage = towerImage;
        this.radius = radius;
        this.damage = damage;
        this.price = price;
        this.location= location;
        this.game = game;
        if (location!=null) {
            this.boundary = new Rectangle(location.x - towerImage.getWidth() / 2, location.y - towerImage.getHeight() / 2, towerImage.getWidth(), towerImage.getHeight());
            this.range = new Rectangle(location.x - towerImage.getWidth() / 2 - radius, location.y - towerImage.getHeight() / 2 - radius, towerImage.getWidth() + 2 * radius, towerImage.getHeight() + 2 * radius);
        }
    }

    // GETTERS
    /** Get the image of the tower*/
    public Image getTowerImage() {
        return towerImage;
    }

    /** Get the radius of the tower*/
    public int getRadius() {
        return radius;
    }
    /** Get the damage amount of the tower*/
    public int getDamage() {
        return damage;
    }
    /** Get the price of the tower*/
    public int getPrice() {
        return price;
    }

    /** Get the game of the tower*/
    public ShadowDefend getGame() {
        return game;
    }

    /** Get where the placement location of the tower */
    public Point getLocation() {
        return location;
    }

    /** Get the boundary of the tower */
    public Rectangle getBoundary() {
        return boundary;
    }

    // METHODS
    /** Places tower onto the map and initialises its location
     * @param location: where the tower will be placed  */
    public void placeTower(Point location) {
        this.location = location;
        createBoundaries();
        // add airplane to airplane list
        if (this instanceof Airplane) {
            game.getCurrentLevel().getAirplaneList().add(new Airplane(location, game));
            Airplane.changeDirection();
        }
        // add active tower to tower list
        else {
            game.getCurrentLevel().getTowerList().add((ActiveTower)this);
        }
        // deduct cost of tower from player's money
        purchaseTower(this);
    }

    /** Creates the boundary of the tower and the range boundary */
    private void createBoundaries() {
        if (location!=null) {
            this.boundary = towerImage.getBoundingBoxAt(location);
            this.range = new Rectangle(location.x - towerImage.getWidth() / 2 - radius, location.y - towerImage.getHeight() / 2 - radius, towerImage.getWidth() + 2 * radius, towerImage.getHeight() + 2 * radius);
        }
    }

    /** Deduct cost of tower from player's money
     * @param tower: tower that we are purchasing*/
    private void purchaseTower(Tower tower) {
        game.getPlayer().updateMoney(-tower.price);
    }

}
