package ch.zhaw.catan;

/**
 * City is a sub class of Settlement.
 */
public class City extends Settlement{

    private static final int CITY_VICTORY_POINTS = 2;

    /**
     * This constructor defines the faction.
     * @param faction to set the faction.
     */
    public City(Config.Faction faction){
        super(faction);
    }

    /**
     * This method overrides the victoryPoints of Settlement.
     * Instead of one points, it returns two points.
     * @return an int value of two.
     */
    @Override
    public int getVictoryPoints() {
        return CITY_VICTORY_POINTS;
    }

    /**
     * This method overrides the toString method of Settlement.
     * @return the uppercase String value of the faction.
     */
    @Override
    public String toString(){
        return super.getFaction().toString().toUpperCase();
    }
}
