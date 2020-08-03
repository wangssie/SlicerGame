public abstract class WaveEvents implements Updateable {
    // ATTRIBUTES
    private int waveNumber;
    private boolean active;
    private Timer timer;
    // SHADOW DEFEND DEPENDENCY
    private ShadowDefend game;

    /** Constructor to create a wave event - spawn or delay
     * @param waveNumber: the wave number the wave event is a part of
     * @param game: the game object
     * @param timer: the level's timer */
    protected WaveEvents(int waveNumber, ShadowDefend game, Timer timer) {
        this.waveNumber = waveNumber;
        active = false;
        this.game = game;
        this.timer = timer;
    }

    // SETTER
    /** Set active status of wave event */
    public void setActive(boolean active) {
        this.active = active;
    }

    // GETTER
    /** Returns if wave event is ongoing or not */
    protected boolean isActive() {
        return active;
    }

    /** Get the timer for the particular wave*/
    public Timer getTimer() {
        return timer;
    }

    /** Starts the event */
    public void deploy() {
        this.active = true;
    }

    // ABSTRACT METHODS
    /** Checks if wave event is complete or not */
    public abstract boolean isComplete();

   }
