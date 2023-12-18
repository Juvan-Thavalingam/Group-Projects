package ch.zhaw.pm2.racetrack.strategy;

import static ch.zhaw.pm2.racetrack.PositionVector.Direction;

/**
 * Do not accelerate in any direction.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class DoNotMoveStrategy implements MoveStrategy {

    /**
     * This method returns the direction for the next move (always NONE).
     * @return the direction none.
     */
    @Override
    public Direction nextMove() {
        return Direction.NONE;
    }

    /**
     * This method returns as toString DO_NOT_MOVE for the detection of the strategy.
     * @return DO_NOT_MOVE
     */
    @Override
    public String toString() {
        return "DO_NOT_MOVE";
    }
}
