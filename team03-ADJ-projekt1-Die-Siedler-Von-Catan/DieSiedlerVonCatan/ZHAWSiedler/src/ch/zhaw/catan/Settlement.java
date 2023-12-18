package ch.zhaw.catan;

/**
 * This class represents Settlement.
 */
public class Settlement {
    private Config.Faction faction;
    private static final int SETTLEMENT_VICTORY_POINTS = 1;

    /**
     * This constructor defines the faction of this Settlement, to know, to which player this settlement belongs.
     * @param faction is the faction of this settlement.
     */
    public Settlement(Config.Faction faction){
        this.faction = faction;
    }

    /**
     * This method overrides the toString method of the super class object.
     * This method is important for the hexboard, because this asks for a String.
     * @return the String value of faction.
     */
    @Override
    public String toString(){
        return faction.toString();
    }

    public int getVictoryPoints(){
        return SETTLEMENT_VICTORY_POINTS;
    }
    public Config.Faction getFaction(){
        return faction;
    }

}
