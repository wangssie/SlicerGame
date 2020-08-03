import bagel.util.Point;
/** A class that can be drawn onto the window of the game */
public interface Drawable {
    /** Draw the item onto the window og the game
     * @param position: the location of the drawn item */
    void drawToGame(Point position);
}
