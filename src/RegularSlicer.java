import bagel.Image;

public class RegularSlicer extends Slicer {
    // STATIC VARIABLES (can change)
    public static final int health = 1;
    public static final double speed = 2.0;
    public static final int reward = 2;
    public static final int penalty = 1;
    public static final Image regularSlicerImage = new Image("res/images/slicer.png");
    public static final int childSpawnNumber = 0;

    // CONSTRUCTOR
    /** Constructor for when Regular Slicer is spawned at the start of the beginning of the path
     * @param game: the game object*/
    public RegularSlicer(ShadowDefend game) {
        super(health, speed, reward, penalty, regularSlicerImage, game);
    }

    /** Constructor for when Regular Slicer is spawned from the death of another Slicer
     * @param parentSlicer: the parent slicer that the regular slicer is spawned from
     * @param game: the game object */
    public RegularSlicer(Slicer parentSlicer, ShadowDefend game) {
        super(parentSlicer, health, speed, reward, penalty,childSpawnNumber, regularSlicerImage, game);
    }

    /** Regular slicer spawns 0 children */
    @Override
    public void spawnChildren() {
    }

}
