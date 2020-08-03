import bagel.Image;

public class SuperSlicer extends Slicer {
    // STATIC VARIABLES (can be changed)
    public static final int health = RegularSlicer.health;
    public static final double speed = ((double)3/4)*RegularSlicer.speed;
    public static final int reward = 15;
    public static final int penalty = RegularSlicer.penalty*2;
    public static final Image superSlicerImage = new Image("res/images/superslicer.png");
    public static final int childSpawnNumber=2;

    // CONSTRUCTOR
    /** Constructor for when Super Slicer is spawned at the start of the beginning of the path
     * @param game: the game object*/
    public SuperSlicer(ShadowDefend game) {
        super(health, speed, reward, penalty, superSlicerImage, game);
    }

    /** Constructor for when Super Slicer is spawned from the death of another Slicer
     * @param parentSlicer: the parent slicer that the slicer is spawned from
     * @param game: the game object */
    public SuperSlicer(Slicer parentSlicer, ShadowDefend game) {
        super(parentSlicer, health, speed, reward, penalty,childSpawnNumber, superSlicerImage, game);
    }

    /** Super slicer spawns 2 regular slicer */
    @Override
    public void spawnChildren() {
        // add two regular slicers to the level
        for (int i=0; i<childSpawnNumber; i++) {
            super.getGame().getCurrentLevel().getSlicerList().add(new RegularSlicer(this, super.getGame()));
        }
    }

}
