import bagel.Input;

public class SpawnEvent extends WaveEvents {
    // ATTRIBUTES
    private int spawnAmount;
    private int spawnDelay;
    private int deployedNumber;
    // INSTANCE OF SLICER
    private String slicerType;
    // STATIC VARIABLES (can be changed if name of slicer has changed)
    private final static String regularSlicerType = "slicer";
    private final static String superSlicerType = "superslicer";
    private final static String megaSlicerType = "megaslicer";
    private final static String apexSlicerType = "apexslicer";
    // SHADOW DEFEND DEPENDENCY
    private ShadowDefend game;

    /** Constructor of a spawn event
     * @param slicerType: type of slicer in the spawn event
     * @param spawnAmount: number of slicers being spawned
     * @param spawnDelay: the delay between spawning of each slicer (in ms)
     * @param waveNumber: the wavenumber the spawn event is a part of
     * @param game: the game object */
    public SpawnEvent(int waveNumber, int spawnAmount, int spawnDelay, String slicerType, ShadowDefend game) {
        super(waveNumber, game, new Timer(game, spawnDelay));
        this.spawnAmount = spawnAmount;
        this.spawnDelay = spawnDelay;
        this.slicerType = slicerType;
        this.game = game;
        this.deployedNumber = 0;

    }

    // METHODS

    /** Deploy a slicer to the game*/
    private void deploySlicer() {
        game.getCurrentLevel().getSlicerList().add(getSlicer(slicerType));
        deployedNumber++;
    }

    /** Start the spawn event */
    @Override
    public void deploy() {
        // change status of waveevent to active
        super.deploy();
        // deploy the first slicer
        deploySlicer();
    }

    /** Returns an instance of a slicer for the particular wave */
    private Slicer getSlicer(String slicer) {
        if (slicer.equals(regularSlicerType)) {
            return new RegularSlicer(game);
        }
        else if (slicer.equals(superSlicerType)) {
            return new SuperSlicer(game);
        }
        else if (slicer.equals(megaSlicerType)) {
            return new MegaSlicer(game);
        }
        else if (slicer.equals(apexSlicerType)) {
            return new ApexSlicer(game);
        }
        // if slicer is not what is required, return null
        else {
            return null;
        }
    }

    /** Checks if the spawn event deployment is complete*/
    @Override
    public boolean isComplete() {
        // if spawn event has finished deploying all required slicers, then spawn event is finished = non-active
        if (deployedNumber==spawnAmount) {
            return true;
        }
        // spawn event is still active
        return false;
    }

    @Override
    public void update(Input input) {
        // update the timer
        super.getTimer().addFrame();
        // deployed new slicer if delayAmount time has passed
        if (super.getTimer().getMilliseconds()%spawnDelay==0 && deployedNumber<spawnAmount) {
            deploySlicer();
        }
        // if spawn event is complete, set status to non-active
        if (this.isComplete()) {
            super.setActive(false);
            game.getCurrentLevel().deployNextWaveEvent();
        }
    }
}