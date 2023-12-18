package ch.zhaw.catan;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoard;

import java.awt.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class extends the hexboard with Land, Settlement, Road And String.
 */
public class SiedlerBoard extends HexBoard<Land, Settlement, Road, String> {

    /**
     * Returns the fields associated with the specified dice value.
     *
     * @param dice the dice value
     * @return the fields associated with the dice value
     */
    public List<Point> getFieldsForDiceValue(int dice) {
        List<Point>fieldCoordinates = new ArrayList<>();

        for (Map.Entry<Point, Integer> entry : Config.getStandardDiceNumberPlacement().entrySet()){
            if (entry.getValue() == dice){
                fieldCoordinates.add(entry.getKey());
            }
        }
        return fieldCoordinates;
    }

    /**
     * Creates a new SiedlerBoard with the given size
     */
    public SiedlerBoard(){
        // Karte erstellen mit Länder
        for (Map.Entry<Point, Config.Land> landValue: Config.getStandardLandPlacement().entrySet()) {
            addField(landValue.getKey(), landValue.getValue());
        }
    }


    /**
     * Returns the {@link Land}s adjacent to the specified corner.
     *
     * @param corner the corner
     * @return the list with the adjacent {@link Land}s
     */
    public List<Land> getLandsForCorner(Point corner) {
        return getFields(corner);
    }
}
