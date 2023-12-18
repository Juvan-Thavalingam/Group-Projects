package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * This class tests the DoNotMoveStrategy class.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class DoNotMoveStrategyTest {

    /**
     * This test tests if the next Move has the direction NONE.
     */
    @Test
    void testNextMoveNONE(){
        DoNotMoveStrategy strategy = new DoNotMoveStrategy();
        assertEquals(PositionVector.Direction.NONE, strategy.nextMove());
    }
}
