import bagel.Image;

public abstract class Panel implements Updateable, Drawable{
    // ATTRIBUTES
    protected Image background; // background of the panel

    // CONSTRUCTOR
    /** Constructor of a panel in the game
     * @param backgroundFile: file of background image of the panel */
    public Panel(String backgroundFile) {
        this.background = new Image(backgroundFile);
    }

}

