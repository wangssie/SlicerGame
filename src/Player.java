public class Player{
    // ATTRIBUTES
    private int lives;
    private int money;
    private boolean winner;
    // STATIC ATTRIBUTES (can change)
    private static final int startingLives = 25;
    private static final int startingMoney = 500;
    // DEPENDENCY
    private ShadowDefend game;

    // CONSTRUCTOR
    /** Constructor of a player in the game */
    public Player(ShadowDefend game) {
        // assign all attributes to default values
        this.lives = startingLives;
        this.money = startingMoney;
        this.winner = false;
        this.game = game;
    }

    // GETTERS
    /** Return the number of lives the player has*/
    public int getLives() {
        return lives;
    }

    /** Returns the amount of money the player has */
    public int getMoney() {
        return money;
    }

    /** Returns whether the player has won the game or not */
    public boolean isWinner() {
        return winner;
    }

    // METHODS

    /** Player loses one life
     * @param penalty: amount of lives the player loses*/
    public void loseLife(int penalty) {
        this.lives-=penalty;
    }

    /** Update the player's money balance
     * @param change: the amount of money being added (pos) or subtracted (neg)*/
    public void updateMoney(int change) {
        this.money += change;
    }

    /** Returns if the player is still alive AKA has enough lives to continue game */
    public boolean isAlive() {
        if (this.lives>0) {
            return true;
        }
        else {
            return false;
        }
    }

    /** Indicate that player has won */
    public void playerWon() {
        this.winner = true;
    }

}
