package ch.zhaw.catan;

import java.awt.*;

/**
 * This class contains all methods for the thief and his position
 */
public class Thief {

    private Point position;

    /**
     * Constructs a thief object, which contains a default position on the map.
     */
    public Thief() {
        position = new Point(7, 11);
    }

    public Point getThiefPosition() {
        return position;
    }

    public void setThiefPosition(Point field) {
        position = field;
    }

}
