package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.userinterface.TextInterface;

/**
 * this class let the user decide the next move.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class UserMoveStrategy implements MoveStrategy {

    private TextInterface textInterface;

    /**
     * This constructor creates the UserMoveStrategy and initialises the textInterface.
     * @param textInterface is the textInterface for the prompting of the direction.
     */
    public UserMoveStrategy(TextInterface textInterface){
        this.textInterface = textInterface;
    }

    /**
     * This method prompts the user to select the next move.
     * @return the selected direction.
     */
    @Override
    public Direction nextMove() {
        return textInterface.selectDirection();
    }
}
