import bagel.Image;

public class MegaSlicer extends Slicer {
    // STATIC ATTRIBUTES (can change)
    public static final int health = SuperSlicer.health*2;
    public static final double speed = SuperSlicer.speed;
    public static final int reward = 10;
    public static final int penalty = SuperSlicer.penalty*2;
    public static final Image megaSlicerImage = new Image("res/images/megaslicer.png");
    public static final int childSpawnNumber = 2;

    // CONSTRUCTOR
    /** Constructor for when Mega Slicer is spawned at the start of the beginning of the path
     * @param game: the game object */
    public MegaSlicer(ShadowDefend game) {
        super(health, speed, reward, penalty, megaSlicerImage, game);
    }

    /** Constructor for when Mega Slicer is spawned from the death of another Slicer
     * @param parentSlicer: the parent slicer which the mega slicer is spawned from
     * @param game: the game object*/
    public MegaSlicer(Slicer parentSlicer, ShadowDefend game) {
        super(parentSlicer, health, speed, reward, penalty, childSpawnNumber,megaSlicerImage, game);
    }

    /** Mega slicer spawns 2 super slicer */
    @Override
    public void spawnChildren() {
        // add two regular slicers to the level
        for (int i=0; i<childSpawnNumber; i++) {
            super.getGame().getCurrentLevel().getSlicerList().add(new SuperSlicer(this, super.getGame()));
        }
    }

}
