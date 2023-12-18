package ch.zhaw.catan;

/**
 * This class contains all methods for the road object and its data.
 */
public class Road {

    private Config.Faction faction;

    /**
     * Constructs a road object with its correspoding faction.
     *
     * @param faction faction, which the road should be assigned to.
     */
    public Road(Config.Faction faction){
        this.faction = faction;
    }

    /**
     * This method returns the enum value.
     *
     * @return the enum value as a string.
     */
    @Override
    public String toString(){
        return faction.toString().substring(1);
    }

}
