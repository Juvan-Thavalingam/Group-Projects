package ch.zhaw.catan;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all methods for the player object and its data.
 */
public class Player {
    private Map<Config.Resource, Integer> playerResource;
    private Road road;

    private Settlement settlement;
    private int WinPoints = 0;
    private Config.Faction faction;

    private int numberOfRoadAvailable;
    private int numberOfSettlementsAvailable;
    private int numberOfCitiesAvailable;

    /**
     * Constructs a player object, which initializes the road, settlement and faction,
     * as well as their corresponding max building values.
     *
     * @param faction the faction, which the player object will be assigned to
     */
    public Player(Config.Faction faction){
        playerResource = new HashMap<>();
        this.faction = faction;
        fillPlayerResource();
        road = new Road(faction);
        settlement = new Settlement(faction);
        numberOfSettlementsAvailable = 5;
        numberOfCitiesAvailable = 4;
        numberOfRoadAvailable = 15;
    }

    public int getWinPoints() { return WinPoints; }

    public Config.Faction getFaction(){
        return faction;
    }

    public Road getRoad() {
        return road;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public City getCity(){
        City city = new City(faction);
        return city;
    }

    private void fillPlayerResource(){
        for(Config.Resource resource : Config.Resource.values()){
            playerResource.put(resource, 0);
        }
    }

    public Map<Config.Resource, Integer> getPlayerResource(){
        return playerResource;
    }

    /**
     * Adds one resource from the playerResource map during gameplay.
     *
     * @param resource the resource to be added
     */
    public void addResource(Config.Resource resource){
        int addValue = playerResource.get(resource);
        playerResource.put(resource, ++addValue);
    }

    /**
     * Removes one resource from the playerResource map during gameplay.
     *
     * @param resource the resource to be removed.
     * @return true, if removal of a resource was successful.
     */
    public boolean removeResource(Config.Resource resource) {
        int removeValue = playerResource.get(resource);
        if (removeValue-- >= 0) {
            playerResource.put(resource, removeValue);
            return true;
        }

        return false;
    }

    /**
     * Adds the specified victory points to the player.
     *
     * @param number the amount of victory points to be added.
     */
    public void addWinPoint(int number){
        WinPoints += number;
    }

    /**
     * This method removes one available settlement from the player.
     * @return true, if a settlement is available and removed.
     */
    public boolean checkAvailableSettlement(){
        if(numberOfSettlementsAvailable > 0){
            numberOfSettlementsAvailable -= 1;
            return true;
        }
        return false;
    }

    /**
     * This method removes one available road from the player.
     * @return true, if a road is available and removed.
     */
    public boolean checkAvailableRoads(){
        if(numberOfRoadAvailable > 0){
            numberOfRoadAvailable -= 1;
            return true;
        }
        return false;
    }

    /**
     * This method removes one available city from the player.
     * @return true, if a city is available and removed.
     */
    public boolean checkAvailableCities(){
        if(numberOfCitiesAvailable > 0){
            numberOfCitiesAvailable -= 1;
            return true;
        }
        return false;
    }
}

