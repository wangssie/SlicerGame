import bagel.Input;

public class DelayEvent extends WaveEvents {
    // ATTRIBUTES
    private int delayTime;
    // SHADOW DEFEND DEPENDENCY
    private ShadowDefend game;

    // CONSTRUCTOR
    /** Constructor to create a delay event
     * @param delayTime: the amount of delaying time for the event
     * @param waveNumber: the wave the delay event is a part of
     * @param game: the game object*/
    public DelayEvent(int waveNumber, int delayTime, ShadowDefend game) {
        super(waveNumber, game, new Timer(game, delayTime));
        this.delayTime = delayTime;
        this.game = game;
    }

    // METHODS
    @Override
    /** Returns if the delay event has concluded/finished */
    public boolean isComplete() {
        // if delay event has been active for the required delay time, event is complete
        if (super.getTimer().getMilliseconds()>=delayTime) {
            return true;
        }
        // delay event is still active
        return false;
    }

    @Override
    public void update(Input input) {
        // update the timer
        super.getTimer().addFrame();
        // if delay event is complete, set status to non-active
        if (this.isComplete()) {
            super.setActive(false);
            game.getCurrentLevel().deployNextWaveEvent();
        }
    }
}

