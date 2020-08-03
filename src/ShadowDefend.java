import bagel.*;
import bagel.util.Point;
import java.util.ArrayList;

/** The game class */
public class ShadowDefend extends AbstractGame {
    // ATTRIBUTES
    private Timer timer;
    private ArrayList<Level> levels;
    private int currentLevel;
    private BuyPanel buyPanel;
    private StatusPanel statusPanel;
    private Player player;
    private boolean pause;
    private Point windowCenter;

    // STATIC ATTRIBUTES (can change)
    public static final Keys startWaveKey = Keys.S;
    public static final Keys increaseTimeScaleKey = Keys.L;
    public static final Keys decreaseTimeScaleKey = Keys.K;
    public static final Keys exitKey = Keys.ESCAPE;
    public static final Keys pauseKey = Keys.P;
    public static final Keys restartKey = Keys.R;
    private static Image pauseImage;
    private static Image gameOverScreen;
    private static Image winnerScreen;
    // LEVEL INFORMATION
    private static final String level1tmx = "res/levels/1.tmx";
    private static final String level2tmx = "res/levels/2.tmx";
    private static final String waveFile = "res/levels/waves.txt";

    /** Runs the game */
    public static void main(String[] args) {
        new ShadowDefend().run();
    }

    /** Constructor of the game */
    public ShadowDefend(){
        // create timer and levels array
        this.timer = new Timer(this, 0);
        levels = new ArrayList<Level>();
        currentLevel = 0;
        // Create the levels and add to lists of levels (UPDATED DEPENDING ON HOW MANY LEVELS EXIST)
        Level level1 = new Level(level1tmx,waveFile, this);
        Level level2 = new Level(level2tmx,waveFile, this);
        levels.add(level1);
        levels.add(level2);
        // Create the panels
        this.buyPanel = new BuyPanel(this);
        this.statusPanel = new StatusPanel(this);
        // Create player
        this.player = new Player(this);
        // pause attribute is false
        this.pause = false;
        // initialise other images
        ShadowDefend.pauseImage = new Image("res/images/pause.png");
        ShadowDefend.gameOverScreen = new Image("res/images/gameover.png");
        ShadowDefend.winnerScreen = new Image("res/images/winner2.png");
        // initialise center of windows
        this.windowCenter = new Point(getCurrentLevel().getMap().getWidth()/2, getCurrentLevel().getMap().getHeight()/2);
    }

    /** Updates the game 60 times per second */
    @Override
    protected void update(Input input) {
        // exit game
        if (input.wasPressed(exitKey)) {
            Window.close();
        }
        // restart game
        if (input.wasPressed(restartKey)) {
            restart();
            return;
        }
        // player died
        if (!player.isAlive()) {
            gameOver();
            return;
        }
        // player won
        if (player.isWinner()) {
            winner();
            return;
        }
        // game paused
        updatePause(input);
        if (pause) {
            pause();
            return;
        }
        // update timer (time scale)
        timer.update(input);
        // update level
        levels.get(currentLevel).update(input);
        // update buy panel
        buyPanel.update(input);
        // update status panel
        statusPanel.update(input);

    }

    // GETTERS

    /** Gets the instance of the player*/
    public Player getPlayer() {
        return player;
    }

    /** Gets the instance of the current level */
    public Level getCurrentLevel() {
        return levels.get(currentLevel);
    }

    /** Gets the instance of the buy panel*/
    public BuyPanel getBuyPanel() {
        return buyPanel;
    }

    /** Gets the pause status of the game*/
    public boolean isPause() {
        return pause;
    }

    // METHODS

    /** Returns whether the current level is the last level of the game */
    public boolean isLastLevel() {
        if (currentLevel==levels.size()-1) {
            return true;
        }
        return false;
    }

    /** Increments the current level to the next one */
    public void levelUp() {
        if (!isLastLevel()) {
            this.currentLevel++;
        }
        else {
            player.playerWon();
        }
    }

    /** Procedure once player has lost */
    private void gameOver() {
        drawFrozenGame();
        gameOverScreen.draw(windowCenter.x,windowCenter.y);
    }

    /** Procedure once player has won the game */
    private void winner() {
        drawFrozenGame();
        winnerScreen.draw(windowCenter.x, windowCenter.y);
    }

    /** Pause the game and functions */
    private void pause() {
        drawFrozenGame();
        pauseImage.draw(windowCenter.x, windowCenter.y);
    }

    /** Returns if game has been paused or unpaused
     * @param input: input of the game (mouse or keyboard)*/
    private void updatePause(Input input) {
        if (input.wasPressed(pauseKey)) {
            // pause the game
            if (!this.pause) {
                this.pause = true;
                pause();
            }
            // unpause
            else {
                this.pause = false;
            }
        }
    }

    /** Draw the elements of the game onto windows without any functions working (frozen) */
    private void drawFrozenGame() {
        getCurrentLevel().drawSnapShot();
        this.buyPanel.drawToGame(null);
        this.statusPanel.drawToGame(null);
    }

    /** Restarts the entire game back to level 1*/
    private void restart() {
        // restart the settings
        // create timer and levels array
        this.timer = new Timer(this, 0);
        levels = new ArrayList<Level>();
        currentLevel = 0;
        // Create the levels and add to lists of levels
        Level level1 = new Level(level1tmx,waveFile, this);
        Level level2 = new Level(level2tmx,waveFile, this);
        levels.add(level1);
        levels.add(level2);
        // Create the panels
        this.buyPanel = new BuyPanel(this);
        this.statusPanel = new StatusPanel(this);
        // Create player
        this.player = new Player(this);
        // pause attribute is false
        this.pause = false;
    }

}
