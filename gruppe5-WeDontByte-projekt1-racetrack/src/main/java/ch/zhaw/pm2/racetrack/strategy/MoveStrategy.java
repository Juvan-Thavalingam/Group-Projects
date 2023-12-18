package ch.zhaw.pm2.racetrack.strategy;

import static ch.zhaw.pm2.racetrack.PositionVector.Direction;

/**
 * This interface handles the MoveStrategies.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public interface MoveStrategy {
    Direction nextMove();
}
