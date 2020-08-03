import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

/* If enough time, use singleton design pattern to ensure only one buy panel is created */

public class BuyPanel extends Panel{
    // ATTRIBUTES
    private boolean selecting;
    private Tower selectingInstance;
    private ShadowDefend game;
    // IMAGE OF BUY PANEL
    private static final String panelImage = "res/images/buypanel.png";
    // IMAGES OF TOWERS
    private final Image tank = new Image("res/images/tank.png");
    private final Image superTank = new Image("res/images/supertank.png");
    private final Image airplane = new Image("res/images/airsupport.png");
    // RECTANGLE BORDERS OF IMAGES
    private Rectangle tankBorder;
    private Rectangle superTankBorder;
    private Rectangle airplaneBorder;
    // DRAWING VARIABLES
    private final int towerHorizontalOffset = 64;
    private final int towerGap = 120;
    private final int towerVerticalGap = 10;
    private final int priceVerticalGap = 5;
    // LOCATION OF TOWERS (can be changed)
    private final Point tankPoint = new Point(towerHorizontalOffset, super.background.getHeight()/2- towerVerticalGap);
    private final Point superTankPoint = new Point(towerHorizontalOffset+towerGap, super.background.getHeight()/2 - towerVerticalGap);
    private final Point airplanePoint = new Point(towerHorizontalOffset+2*towerGap, super.background.getHeight()/2 - towerVerticalGap);
    // FONTS (can be changed)
    private static final Font towerPricesText = new Font("res/fonts/DejaVuSans-Bold.ttf", 20);
    private static final Font instructionText = new Font("res/fonts/DejaVuSans-Bold.ttf", 12);
    private static final Font moneyText = new Font("res/fonts/DejaVuSans-Bold.ttf", 50);
    private static DrawOptions setColour = new DrawOptions();
    // LOCATION OF FONTS (can be changed)
    private final Point tankPriceTextPoint = new Point(towerHorizontalOffset-tank.getWidth()/2,
            tank.getHeight()/2+super.background.getHeight()/2 + priceVerticalGap);
    private final Point superTankPriceTextPoint = new Point(towerHorizontalOffset+towerGap-superTank.getWidth()/2,
            tank.getHeight()/2+super.background.getHeight()/2 + priceVerticalGap);
    private final Point airplanePriceTextPoint = new Point(towerHorizontalOffset+2*towerGap - airplane.getWidth()/2,
            tank.getHeight()/2+super.background.getHeight()/2 + priceVerticalGap);
    private final Point instructionTextPoint = new Point (background.getWidth()/2-50, 2*towerVerticalGap );
    private final Point moneyTextPoint = new Point(background.getWidth()-200, background.getHeight()/2+towerVerticalGap);

    /** Constructor to build a the buy panel
     * @param game: the game object*/
    public BuyPanel(ShadowDefend game) {
        // initialise background of panel
        super(panelImage);
        // set selecting tower to false
        this.selecting = false;
        // initialise rectangles of the towers
        this.tankBorder = tank.getBoundingBoxAt(tankPoint);
        this.superTankBorder = superTank.getBoundingBoxAt(superTankPoint);
        this.airplaneBorder = airplane.getBoundingBoxAt(airplanePoint);
        // adding dependency of shadow defend
        this.game = game;
    }

    @Override
    public void drawToGame(Point position) {
        // background
        super.background.drawFromTopLeft(0,0);
        // towers
        tank.draw(tankPoint.x, tankPoint.y);
        superTank.draw(superTankPoint.x, superTankPoint.y);
        airplane.draw(airplanePoint.x, airplanePoint.y);
        // prices
        drawPriceFont(new Tank(null, game), tankPriceTextPoint);
        drawPriceFont(new SuperTank(null, game), superTankPriceTextPoint);
        drawPriceFont(new Airplane(null, game), airplanePriceTextPoint);
        // instructions
        instructionText.drawString("Key binds:\n  S - Start Wave\n  L - Increase Timescale\n  K - Decrease Timescale\n  P - Pause/Unpause\n  R - Restart Game\n  esc - Exit Game", instructionTextPoint.x, instructionTextPoint.y , setColour.setBlendColour(Colour.WHITE));
        // money
        moneyText.drawString("$"+game.getPlayer().getMoney(), moneyTextPoint.x,moneyTextPoint.y);
    }

    /** Checks to see if the player can afford to purchase the instance of a tower
     * @param tower: the tower we are seeking to purchase
     * @return: player can afford (true) or can't (false) */
    private boolean canAfford(Tower tower) {
        if (tower.getPrice() <= game.getPlayer().getMoney()) {
            return true;
        }
        return false;
    }

    /** Draws the fonts of the tower prices
     * Colour of font is green if player can afford or red otherwise
     * @param location: Where the font will be draw
     * @param tower: Which tower is the price for */
    private void drawPriceFont(Tower tower, Point location) {
        if (canAfford(tower)) {
            towerPricesText.drawString("$"+ tower.getPrice(),
                    location.x, location.y,
                    setColour.setBlendColour(Colour.GREEN));
        }
        else {
            towerPricesText.drawString("$"+ tower.getPrice(),
                    location.x, location.y,
                    setColour.setBlendColour(Colour.RED));
        }
    }
    @Override
    public void update(Input input) {
        drawToGame(null);
        mouseInputs(input);
    }

    /** Manages tasks that are done with mouse inputs involving selecting and placing towers
     * @param input: input in the game (keyboard or mouse) */
    private void mouseInputs(Input input) {
        placingTower(input);
        selectingTower(input);
    }

    /** Checks and notifies if a tower has been selected to purchase
     * @param input: input in the game (keyboard or mouse) */
    private void selectingTower(Input input) {
        // get the mouse position
        Point mousePosition = input.getMousePosition();
        // if mouse is over the tower picture, has clicked on it and player can afford tower
            // ley player select tower
        if (tankBorder.intersects(mousePosition) && input.wasPressed(MouseButtons.LEFT) && canAfford(new Tank(null, game))) {
            selecting = true;
            selectingInstance = new Tank(null, game);
        }
        else if (superTankBorder.intersects(mousePosition) && input.wasPressed(MouseButtons.LEFT)&& canAfford(new SuperTank(null, game))) {
            selecting = true;
            selectingInstance = new SuperTank(null, game);
        }
        else if (airplaneBorder.intersects(mousePosition) && input.wasPressed(MouseButtons.LEFT) && canAfford(new Airplane(null, game)) && game.getCurrentLevel().isStarted()) {
            selecting = true;
            selectingInstance = new Airplane(null, game);
        }

    }

    /** Displays tower when player is placing selected tower
     * @param input: input of game (keyboard or mouse) */
    private void placingTower(Input input) {
        // player currently has a selected tower
        if (selecting) {
            Point mousePosition = input.getMousePosition();
            // mousePosition is not on blocked tiles or in the same position as other towers
            if (isValidTowerLocation(mousePosition)) {
                selectingInstance.getTowerImage().draw(mousePosition.x, mousePosition.y);
                // player chooses valid position to place tower
                if (input.wasPressed(MouseButtons.LEFT)) {
                    selectingInstance.placeTower(mousePosition);
                    deselectingTower();
                }
            }
            // player deselects their tower
            if (input.wasPressed(MouseButtons.RIGHT)) {
                deselectingTower();
            }
        }
        // player has not selected anything
    }

    /** Stops displaying tower when player has deselected tower */
    private void deselectingTower() {
        selecting=false;
        selectingInstance=null;
    }

    /** Checks if active tower can be placed on location point
     * @param point: the point that we are checking for validity*/
    private boolean isValidTowerLocation(Point point) {
        // airplane can be placed anywhere on map
        if (selectingInstance instanceof Airplane) {
            return true;
        }
        // valid tile on map
        if (!game.getCurrentLevel().getMap().getPropertyBoolean((int)point.x, (int)point.y, "blocked", false)) {
            // iterate through all towers
            // make sure the point does not intersect with any of the other tower's boundary
            ArrayList<ActiveTower> towerList = game.getCurrentLevel().getTowerList();
            for (Tower tower : towerList) {
                if (!(tower instanceof Airplane) && tower.getBoundary().intersects(point)) {
                    return false;
                }
            }
            return true;
        }
        // on an invalid tile from map
        return false;
    }

    /** Returns if player is currently selecting tower or not */
    public boolean isSelecting() {
        return selecting;
    }
}
