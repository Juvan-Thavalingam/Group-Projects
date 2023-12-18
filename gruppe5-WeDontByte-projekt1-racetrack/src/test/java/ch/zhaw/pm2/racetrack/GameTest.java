package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the game class.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
class GameTest {

    private final static String SRC_TEST_TRACK_ONE_CAR = "src/test/resources/TestTrack_1C.txt";
    private final static String SRC_TEST_TRACK_TWO_CAR = "src/test/resources/TestTrack_2C.txt";
    private final static String SRC_TEST_TRACK_THREE_CAR = "src/test/resources/TestTrack_3C.txt";
    private Track track1C;
    private Track track2C;
    private Track track3C;
    private Game game1C;
    private Game game2C;
    private Game game3C;

    /**
     * This method defines the setting for the tests.
     */
    @BeforeEach
    void setUp() throws InvalidTrackFormatException, FileNotFoundException {
        try {
            track1C = new Track(new File(SRC_TEST_TRACK_ONE_CAR));
            track2C = new Track(new File(SRC_TEST_TRACK_TWO_CAR));
            track3C = new Track(new File(SRC_TEST_TRACK_THREE_CAR));

            game1C = new Game(track1C);
            game2C = new Game(track2C);
            game3C = new Game(track3C);
        } catch (InvalidTrackFormatException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Testcases for possible car crash.
     * - no crash
     * - crash
     */
    @Test
    void willCarCrash() {
        assertTrue(game2C.willCarCrash(0, new PositionVector(7, 0)));
        assertFalse(game2C.willCarCrash(0, new PositionVector(1, 1)));
        assertThrows(NullPointerException.class, () -> {
            game2C.willCarCrash(0, null);
        });
    }

    /**
     * Testcases for the Bresenham-Algorithmus
     * - Test in X direction
     * - Test in Y direction
     */
    @Test
    void calculatePath() {

        // Test in X direction
        PositionVector startXPosition = new PositionVector(35, 2);
        PositionVector endXPosition = new PositionVector(39, 2);

        List<PositionVector> theoreticalXPositionList = new ArrayList<>();
        theoreticalXPositionList.add(new PositionVector(36, 2));
        theoreticalXPositionList.add(new PositionVector(37, 2));
        theoreticalXPositionList.add(new PositionVector(38, 2));
        theoreticalXPositionList.add(new PositionVector(39, 2));

        assertEquals(theoreticalXPositionList, game1C.calculatePath(startXPosition, endXPosition));

        // Test in Y direction
        PositionVector startYPosition = new PositionVector(35, 1);
        PositionVector endYPosition = new PositionVector(35, 3);

        List<PositionVector> theoreticalYPositionList = new ArrayList<>();
        theoreticalYPositionList.add(new PositionVector(35, 2));
        theoreticalYPositionList.add(new PositionVector(35, 3));

        assertEquals(theoreticalYPositionList, game1C.calculatePath(startYPosition, endYPosition));


    }

    /**
     * This test tests if the current car is the right one after switchToNextCar.
     * - "normal" change, increase the index by 1.
     * - "special" change, reset the index to 0.
     *
     * @throws InvalidTrackFormatException
     * @throws IOException
     */
    @Test
    void switchToNextCar() throws InvalidTrackFormatException, IOException {

        // Test with one car
        game1C.switchToNextActiveCar();
        assertEquals(0, game1C.getCurrentCarIndex());

        game1C.switchToNextActiveCar();
        assertEquals(0, game1C.getCurrentCarIndex());


        // Test with two car
        game2C.switchToNextActiveCar();
        assertEquals(1, game2C.getCurrentCarIndex());

        game2C.switchToNextActiveCar();
        assertEquals(0, game2C.getCurrentCarIndex());


        // Test with three car
        game3C.switchToNextActiveCar();
        assertEquals(1, game3C.getCurrentCarIndex());

        game3C.switchToNextActiveCar();
        assertEquals(2, game3C.getCurrentCarIndex());

        game3C.switchToNextActiveCar();
        assertEquals(0, game3C.getCurrentCarIndex());
    }

    /**
     * This test tests if the correct car won.
     * - Win after crossing the finish line.
     * - Win for beeing the last one on track.
     */
    @Test
    void getWinner() throws InvalidTrackFormatException, IOException {

        // Winning by crossing the finish line
        track1C.getCar(0).setVelocity(new PositionVector(4, 0));
        assertEquals(0, game1C.getWinner());

        // Win after a crash

        // Two cars

        assertEquals(-1, game2C.getWinner());
        track2C.getCarList().get(0).crash();
        assertEquals(1, game2C.getWinner());

        // Three cars

        assertEquals(-1, game3C.getWinner());
        track3C.getCarList().get(0).crash();
        assertEquals(-1, game3C.getWinner());
        track3C.getCarList().get(1).crash();
        assertEquals(2, game3C.getWinner());

    }

    /**
     * This test tests if the doCarTurn is correct.
     */
    @Test
    void doCarTurn() {
        PositionVector currentPosition = track2C.getCarPos(game2C.getCurrentCarIndex());

        // Before doCarTurn
        assertEquals(0, game2C.getCurrentCarIndex());
        game2C.doCarTurn(PositionVector.Direction.LEFT);

        // After doCarTurn
        assertEquals(-1, game2C.getWinner());
        assertFalse(game2C.willCarCrash(game2C.getCurrentCarIndex(), new PositionVector(5, 1)));
        assertEquals(0, game2C.getCurrentCarIndex());
        assertNotEquals(track2C.getCarPos(game2C.getCurrentCarIndex()), currentPosition);
    }
}
