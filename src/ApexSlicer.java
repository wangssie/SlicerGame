import bagel.Image;
import bagel.util.Point;

import java.util.Arrays;
import java.util.List;

public class ApexSlicer extends Slicer {
    // STATIC ATTRIBUTES (CAN BE CHANGED)
    public static final int health = RegularSlicer.health*25;
    public static final double speed = ((double)1/2)*MegaSlicer.speed;
    public static final int reward = 150;
    public static final int penalty = MegaSlicer.penalty*4;
    public static final Image apexSlicerImage = new Image("res/images/apexslicer.png");
    public static final int childSpawnNumber = 4;

    // CONSTRUCTOR
    /** Constructor for when Apex Slicer is spawned at the start of the beginning of the path
     * @param game: the game object*/
    public ApexSlicer(ShadowDefend game) {
        super(health, speed, reward, penalty, apexSlicerImage, game);
    }

    /** Constructor for when Apex Slicer is spawned from the death of another Slicer
     * @param parentSlicer: the parent slicer from which the slicer was spawned from*/
    public ApexSlicer(Slicer parentSlicer, ShadowDefend game) {
        super(parentSlicer, health, speed, reward, penalty,childSpawnNumber, apexSlicerImage, game);
    }

    @Override
    /** Apex slicer spawns 4 mega slicers */
    public void spawnChildren() {
        for (int i=0; i<childSpawnNumber; i++) {
            super.getGame().getCurrentLevel().getSlicerList().add(new MegaSlicer(this, super.getGame()));
        }
    }

}
