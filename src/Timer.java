import bagel.Input;
import java.util.ArrayList;

public class Timer implements Updateable{
    // GAME DEPENDENCY
    private ShadowDefend game;
    // ATTRIBUTES
    private int frames; // how many frames have occurred
    private double millisecond; // tracks number of milliseconds that have passed
    /* NOTE: This interpretation of FPS is that number of frames equivalent to 1 second in the unscaled time */
    /* i.e. currentFPS = 30 -> 30 frames equate to 1 second in the unscaled time (but 0.5 seconds in real time) */
    private int millisecondIncrement; // number of milliseconds to be reached
    // STATIC VARIABLES
    private static final int FPS = 60; // original rate of frames per second
    private static int currentFPS; // the current rate of frames per second
    private static int scale = 1; // the current timescale
    private static final float MILLISECONDPERSECOND = 1000;
    public static final int DEFAULTmsINCREMENT = 100;
    // Array of all the timers in the game
    private static ArrayList<Timer> timerList = new ArrayList<Timer>();

    // CONSTRUCTOR
    public Timer(ShadowDefend game, int millisecondIncrement) {
        this.frames = 0;
        this.millisecond = 0;
        Timer.currentFPS = Timer.FPS/scale;
        this.game = game;
        if (millisecondIncrement==0) {
            this.millisecondIncrement = DEFAULTmsINCREMENT;
        }
        else {
            this.millisecondIncrement = millisecondIncrement;
        }
        Timer.timerList.add(this);
    }
    // SETTER
    /** Change the frames value when scale has changed for every timer
     * @param frames: the number of frames we want to set the timer to*/
    public void setFrames(int frames) {
        this.frames = frames;
    }


    // GETTER

    /** Gets the value of frames*/
    public int getFrames() {
        return frames;
    }

    /** Returns the number of milliseconds that have passed
     * @return -1 if time passed is in between the millisecond increment assigned for the timer */
    public double getMilliseconds() {
        if (frames==0) {
            return millisecond;
        }
        return -1;
    }

    /** Returns the scale of the timer  */
    public static int getScale() {
        return scale;
    }

    // METHODS

    /** Increases frame count by one when an update as occurred */
    public void addFrame() {
        frames++;
        // minimum frame increment can be is 1
        int frameIncrement = Math.max(1, (int)(((float)millisecondIncrement/MILLISECONDPERSECOND)*currentFPS));
        // milliSecondincrement amount of time has passed
        if (frames==frameIncrement) {
            // reset the frame count back to 0
            frames = 0;
            millisecond+=millisecondIncrement;
        }
    }

    /** Increases timescale by one */
    public void increaseScale() {
        scale+=1;
        int k = scale;
        // update current FPS to scaled version of FPS
        currentFPS = FPS/k;
        // scale the frame count by value k for each timer in game
        for (Timer timer : timerList) {
            timer.setFrames(timer.getFrames() * (k - 1) / k);
        }
    }

    /** Decrease timescale by one if possible */
    public void decreaseScale() {
        // timescale is already at minimum and cannot be scaled
        if (scale == 1) {
            return;
        }
        scale-=1;
        int k = scale;
        // update current FPS to scaled version of FPS
        currentFPS = FPS/k;
        // scale the frame count by value k for each timer in game
        for (Timer timer : timerList) {
            timer.setFrames(timer.getFrames() * (k + 1) / k);
        }
    }

    /** Updates the timer*/
    @Override
    public void update(Input input) {
        // adds a frame to the timer
        this.addFrame();
        // time scale is increased
        if (input.wasPressed(ShadowDefend.increaseTimeScaleKey)) {
            increaseScale();
        }
        // time scale is decreased
        if (input.wasPressed(ShadowDefend.decreaseTimeScaleKey)) {
            decreaseScale();
        }
    }
}
