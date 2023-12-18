package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the Car class.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class CarTest {
    private Car newCar;

    /**
     * This method defines the setting for the tests.
     */
    @BeforeEach
    void setup() {
        newCar = new Car('a', new PositionVector(5, 3));
    }

    /**
     * This test calculates the next position and checks it.
     */
    @Test
    void nextPosition() {
        PositionVector startingPosition = new PositionVector(newCar.getPosition());
        PositionVector currentPosition = new PositionVector(5, 3);
        PositionVector currentAcceleration = new PositionVector(PositionVector.Direction.DOWN_LEFT.vector);

        newCar.accelerate(PositionVector.Direction.DOWN_LEFT);
        PositionVector expectedResult = new PositionVector(
            currentPosition.getX() + currentAcceleration.getX(),
            currentPosition.getY() + currentAcceleration.getY());
        PositionVector calculatedResult = newCar.nextPosition();
        PositionVector endingPosition = new PositionVector(newCar.getPosition());

        assertEquals(expectedResult, calculatedResult);
        assertEquals(startingPosition, endingPosition);
    }

    /**
     * This test tests if the acceleration is okay.
     */
    @Test
    void accelerate() {
        PositionVector startingVelocity = new PositionVector(newCar.getCurrentVelocity());
        PositionVector currentVelocity = new PositionVector(0, 0);
        PositionVector currentAcceleration = new PositionVector(PositionVector.Direction.UP_LEFT.vector);
        PositionVector expectedResult = new PositionVector(PositionVector.add(currentVelocity, currentAcceleration));

        newCar.accelerate(PositionVector.Direction.UP_LEFT);

        PositionVector calculatedResult = newCar.getCurrentVelocity();
        PositionVector endingVelocity = new PositionVector(newCar.getCurrentVelocity());
        PositionVector expectedVelocity = new PositionVector(PositionVector.add(startingVelocity, currentAcceleration));

        assertEquals(expectedResult, calculatedResult);
        assertEquals(expectedVelocity, endingVelocity);
    }

    /**
     * This method tests if the car can move.
     */
    @Test
    void testIfCarMove() {
        PositionVector endPosition = new PositionVector(4, 4);
        newCar.accelerate(PositionVector.Direction.DOWN_LEFT);
        newCar.move();

        PositionVector actualEndposition = newCar.getPosition();
        assertEquals(endPosition, actualEndposition);
    }

    /**
     * This test tests if the new setted position is now the current position.
     */
    @Test
    void testSetPosition() {
        PositionVector newPosition = new PositionVector(3, 4);
        newCar.setPosition(newPosition);
        Assertions.assertEquals(newPosition, newCar.getPosition());
    }

    /**
     * This test tests if the car crashed correct.
     */
    @Test
    void testCrash() {
        assertFalse(newCar.isCrashed());
        newCar.crash();
        Assertions.assertTrue(newCar.isCrashed());
    }

    /**
     * This test tests if the car can move after a crash.
     */
    @Test
    void testCarDontMoveAfterCrash() {
        new PositionVector(0, 0);
        newCar.crash();
        newCar.accelerate(PositionVector.Direction.DOWN_LEFT);
        newCar.move();
        PositionVector downLeftPosition = new PositionVector(1, 1);
        boolean isNewPosition = (new PositionVector(0, 0).equals(downLeftPosition));
        assertFalse(isNewPosition);
    }

    /**
     * This test test if the velocity is correct.
     */
    @Test
    void carVelocityTest() {
        new PositionVector(4, 4);
        PositionVector expectedVelocity = new PositionVector(0, 0);
        PositionVector resultVelocity = newCar.getCurrentVelocity();

        assertEquals(expectedVelocity, resultVelocity);
    }

    /**
     * This test tests if the car has the right velocity after the change.
     */
    @Test
    void carChangeVelocityTest() {
        new PositionVector(4, 4);
        newCar.accelerate(PositionVector.Direction.RIGHT);

        PositionVector expectedVelocity = new PositionVector(1, 0);
        PositionVector resultVelocity = newCar.getCurrentVelocity();

        assertEquals(expectedVelocity, resultVelocity);
    }
}
