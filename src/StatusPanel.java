import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;

public class StatusPanel extends Panel{
    // GAME DEPENDENCY
    private ShadowDefend game;
    // STATUSES (can be changed)
    private static final String status1 = "Winner!";
    private static final String status2 = "Placing";
    private static final String status3 = "Wave In Progress";
    private static final String status4 = "Awaiting Start";
    private static final String status5 = "Paused";
    private static final String statusUnknown = "Unknown";
    // FONTS
    private static Font texts;
    // DRAWING VARIABLES (can be changed)
    private static final int verticalGap = (int) ((new Image("res/levels/1.png")).getHeight()-5);
    private static final int horizontalWaveGap = 5;
    private static final int horizontalTimeGap = 200;
    private static final int horizontalStatusGap = 440;
    private static final int horizontalLivesGap = 920;

    // CONSTRUCTOR
    /** Constructor of the status panel
     * @param game: the game object*/
    public StatusPanel(ShadowDefend game) {
        // initialise background
        super("res/images/statuspanel.png");
        // initialise font for status panel texts
        StatusPanel.texts = new Font("res/fonts/DejaVuSans-Bold.ttf", 18);
        // set up dependency on shadow defender class
        this.game = game;
    }

    @Override
    public void drawToGame(Point position) {
        // background
        super.background.drawFromTopLeft(0,(new Image("res/levels/1.png")).getHeight()-super.background.getHeight());
        // wave number
        texts.drawString("Wave: "+Math.max((game.getCurrentLevel().getCurrentWaveNumber()+1),1), horizontalWaveGap, verticalGap);
        // time scale
        texts.drawString("Time Scale: "+Timer.getScale()+".0", horizontalTimeGap, verticalGap);
        // status
        String status = getStatus();
        texts.drawString("Status: "+status, horizontalStatusGap, verticalGap);
        // lives
        texts.drawString("Lives: "+ game.getPlayer().getLives(), horizontalLivesGap, verticalGap);
    }

    @Override
    public void update(Input input) {
        drawToGame(null);
    }

    /** Returns if player has successfully completed the current level */
    private boolean isWinner() {
        if (this.game.getCurrentLevel().isLevelComplete() && this.game.getPlayer().isAlive() && this.game.isLastLevel()) {
            return true;
        }
        return false;
    }

    /** Returns if player is currently placing a tower onto map */
    private boolean isPlacing() {
        return game.getBuyPanel().isSelecting();
    }

    /** Returns if a wave is currently in progress */
    private boolean isWaveInProgress() {
        if (!this.game.getCurrentLevel().isLevelComplete() && this.game.getCurrentLevel().isStarted()) {
            return true;
        }
        else {
            return false;
        }
    }

    /** Returns if game is waiting for wave to begin */
    private boolean isAwaitingStart() {
        if (!this.game.getCurrentLevel().isStarted()) {
            return true;
        }
        else {
            return false;
        }
    }

    /** Returns if game is paused*/
    private boolean isPaused() {
        return game.isPause();
    }

    /** Returns the string that represents the current status of the game */
    private String getStatus() {
        String status;
        // if statements in order of priority of statuses
        if (isWinner()) {
            status = status1;
        }
        else if (isPaused()) {
            status = status5;
        }
        else if (isPlacing()) {
            status = status2;
        }
        else if (isWaveInProgress()) {
            status = status3;
        }
        else if (isAwaitingStart()) {
            status = status4;
        }
        else {
            status = statusUnknown;
        }
        return status;
    }

}
