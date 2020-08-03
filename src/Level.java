import bagel.map.TiledMap;
import bagel.util.Point;
import bagel.Input;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Level implements Updateable, Drawable {

    //ATTRIBUTES
    private TiledMap map;
    private String waveFile;
    private int waveEvents;
    private int currentWaveNumber;
    private ArrayList<ArrayList<WaveEvents>> waveList;
    private ArrayList<Slicer> slicerList;
    private ArrayList<ActiveTower> towerList;
    private ArrayList<Airplane> airplaneList;
    private boolean started;
    // SHADOW DEFEND DEPENDENCY
    private ShadowDefend game;
    //TYPES
    private static final String spawnType = "spawn";
    private static final String delayType = "delay";
    // STATIC ATTRIBUTES (can be changed)
    private static final int waveReward = 150;
    private static final int waveNumberBonusReward = 100;

    // CONSTRUCTOR
    /** Constructor of a level in the game
     * @param tmxFile: tmx file that the level depends on
     * @param waveFile: the wave text file that determines the waves in the level
     * @param game: the game object */
    public Level(String tmxFile, String waveFile, ShadowDefend game) {
        // create all the lists required for the level
        waveList = new ArrayList<ArrayList<WaveEvents>>();
        slicerList = new ArrayList<Slicer>();
        towerList = new ArrayList<ActiveTower>();
        airplaneList = new ArrayList<Airplane>();
        // map of the level
        map = new TiledMap(tmxFile);
        // initialise variables
        waveEvents = 0;
        currentWaveNumber = -1;
        this.started = false;
        // dependent initialisations
        this.waveFile = waveFile;
        this.game = game;
        // create the waves from the wave text file
        createWaves();
    }

    // GETTERS

    /** Returns the current wave number */
    public int getCurrentWaveNumber() {
        return currentWaveNumber;
    }

    /** Returns whether the level has started yet or not */
    public boolean isStarted() {
        return started;
    }

    /** Getter for the tiled map */
    public TiledMap getMap() {
        return map;
    }

    /** Getter for list of all slicers present in the game */
    public ArrayList<Slicer> getSlicerList() {
        return slicerList;
    }

    public ArrayList<ActiveTower> getTowerList() {
        return towerList;
    }

    public ArrayList<Airplane> getAirplaneList() {
        return airplaneList;
    }


    // METHODS

    /** Deploy the next wave event in the current wave if there is still any left*/
    public void deployNextWaveEvent() {
        if (waveEvents+1<waveList.get(currentWaveNumber).size()) {
            waveEvents++;
            waveList.get(currentWaveNumber).get(waveEvents).deploy();
        }
    }

    /** Create the list of waves in the wave text file */
    private void createWaves() {
        // Scan the contents of the wave file
        try (Scanner waveText = new Scanner(new FileReader(waveFile))) {
            while (waveText.hasNextLine()) {
                // get all the info on one line into an array
                String[] waveInfo = waveText.nextLine().split(",");
                int waveNumber = Integer.parseInt(waveInfo[0]);
                if (waveList.size() < waveNumber) {
                    waveList.add(new ArrayList<WaveEvents>());
                }
                int delay;
                String waveType = waveInfo[1];
                // wave is a spawn event
                if (waveType.equals(spawnType)) {
                    int spawnAmount = Integer.parseInt(waveInfo[2]);
                    delay = Integer.parseInt(waveInfo[4]);
                    // create and add spawn event to list of waves
                    // create an array for the new wavenumber
                    if (waveList.size() < waveNumber) {
                        waveList.add(new ArrayList<WaveEvents>());
                    }
                    waveList.get(waveNumber-1).add(new SpawnEvent(waveNumber, spawnAmount, delay, waveInfo[3], game));
                }
                // wave is a delay event
                else {
                    delay = Integer.parseInt(waveInfo[2]);
                    // create and add delay event to list of waves
                    waveList.get(waveNumber-1).add(new DelayEvent(waveNumber, delay, game));
                }
            }
        }
        // Can't read the text file
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** Give reward for completion of wave */
    private void giveLevelReward() {
        game.getPlayer().updateMoney(waveReward + (currentWaveNumber+1)*waveNumberBonusReward);
    }

    /** Begin the first wave event in the current wave */
    public void startWave() {
        this.started=true;
        currentWaveNumber++;
        waveEvents=0;
        waveList.get(currentWaveNumber).get(0).deploy();
    }

    /** Returns if the wave is complete i.e. all the waves events in wave are complete */
    public boolean isWaveComplete() {
        if (slicerList.size()==0 && waveEvents == waveList.get(currentWaveNumber).size()-1) {
            return true;
        }
        else {
            return false;
        }
    }

    /** Returns whether the level (i.e. all waves) is complete or not */
    public boolean isLevelComplete() {
        // no more slicers on map and we have completed the last wave
        if (slicerList.size() == 0 && currentWaveNumber==waveList.size()-1) {
            return true;
        }
        return false;
    }

    /** Remove all slicers from slicerlist that have either died or completed path */
    private void removeCompletedSlicers() {
        int i = 0, size = slicerList.size();
        while (i < size) {
            Slicer slicer = slicerList.get(i);
            if (slicer.isDead() || slicer.isSurvived()) {
                // if slicer beat the player
                if (slicer.isSurvived()) {
                    // player gets penalty
                    game.getPlayer().loseLife(slicer.getPenalty());
                }
                // if slicer was killed
                else {
                    // player gets reward from
                    game.getPlayer().updateMoney(slicer.getReward());
                    slicer.spawnChildren();
                    size+=slicer.getChildSpawnNumber();
                }
                // remove the slicer from the game
                slicerList.remove(i);
                size--;
            }
            else {
                i++;
            }
        }
    }

    /** Remove all airplanes from airplanelist that have finished flying*/
    private void removeCompletedAirplanes() {
        int i=0, size = airplaneList.size();
        // iterate through all airplanes
        while (i<size) {
            // go to next airplane is current airplane is active
            if (airplaneList.get(i).isActive()) {
                i++;
            }
            // remove airplane and decrease size of array if airplane is finished
            else {
                airplaneList.remove(i);
                size--;
            }
        }
    }

    /** Draws the map and slicers in their current position and does not update movements */
    public void drawSnapShot() {
        map.draw(0,0,0,0,map.getWidth(),map.getHeight());
        // draw all slicers that are alive on this level onto game
        for (Slicer slicer : slicerList) {
            slicer.drawToGame(slicer.getCurrentLocation());
        }
        for (ActiveTower activeTower : towerList) {
            activeTower.drawSnapshot();
        }
        for (Airplane airplane : airplaneList) {
            airplane.drawSnapshot();
        }
    }

    @Override
    public void drawToGame(Point position) {
        // draw the level map onto game
        map.draw(0,0,0,0,map.getWidth(),map.getHeight());
        // draw all slicers that are alive on this level onto game
        for (Slicer slicer : slicerList) {
            slicer.update(null);
        }
        for (ActiveTower activeTower : towerList) {
            activeTower.update(null);
        }
        for (Airplane airplane : airplaneList) {
            airplane.update(null);
        }
    }

    @Override
    public void update(Input input) {
        // if start wave key is pressed, the level begins
        if (input.wasPressed(ShadowDefend.startWaveKey) && !this.started) {
            this.started = true;
            startWave();
        }
        // complete all updates if level has begun
        if (started) {
            // update all waves in wave list that are active
            for (int i = 0; i <= waveEvents; i++) {
                if (waveList.get(currentWaveNumber).get(i).isActive()) {
                    waveList.get(currentWaveNumber).get(i).update(null);
                }
            }
            // remove all the slicers that are dead
            removeCompletedSlicers();
            // remove all airplanes that have completed deployment
            removeCompletedAirplanes();

            // if level is complete, level up and give reward for wave
            if (this.isWaveComplete()) {
                giveLevelReward();
                started = false;
            }

            if (this.isLevelComplete()) {
                game.levelUp();
            }
        }
        // draw level map and slicers to game
        drawToGame(null);
    }

}


