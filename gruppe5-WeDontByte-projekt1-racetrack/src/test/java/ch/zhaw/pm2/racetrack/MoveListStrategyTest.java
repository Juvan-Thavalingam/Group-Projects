package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.MoveListStrategy;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class tests the MoveListStrategy class.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class MoveListStrategyTest {

    /**
     * This method tests the movelist for car a.
     */
    @Test
    void testMoveListCarA(){
        File moveFile = new File("src/moves/challenge-car-a.txt");
        assertThrows(FileNotFoundException.class, () -> new MoveListStrategy(moveFile));
    }

    /**
     * This method tests the movelist for car b.
     */
    @Test
    void testMoveListCarB(){
        File moveFile = new File("src/moves/challenge-car-b.txt");
        assertThrows(FileNotFoundException.class, () -> new MoveListStrategy(moveFile));
    }
}
