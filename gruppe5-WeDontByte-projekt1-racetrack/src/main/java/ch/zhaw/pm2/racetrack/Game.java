package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification.*;
import ch.zhaw.pm2.racetrack.given.GameSpecification;

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.pm2.racetrack.PositionVector.Direction;

/**
 * Game controller class, performing all actions to modify the game state.
 * It contains the logic to move the cars, detect if they are crashed
 * and if we have a winner.
 */
public class Game implements GameSpecification {

    private static final int FIRST_CAR_IN_LIST = 0;
    public static final int NO_WINNER = -1;
    private Car currentCar;
    private Track track;
    private SpaceType entryFinishLine;

    /**
     * This constructor creates a Game object.
     * The track is also initialised.
     * @param track the track on which the game is played
     */
    public Game (Track track) {
        this.track = track;
        entryFinishLine = track.getEntryFinishLine();
        currentCar = track.getCarList().get(FIRST_CAR_IN_LIST);
    }

    /**
     * Return the index of the current active car.
     * Car indexes are zero-based, so the first car is 0, and the last car is getCarCount() - 1.
     * @return The zero-based number of the current car
     */
    @Override
    public int getCurrentCarIndex() {
        return track.getCarList().indexOf(currentCar);
    }

    /**
     * Get the id of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return A char containing the id of the car
     */
    @Override
    public char getCarId(int carIndex) {
        return track.getCarId(carIndex);
    }

    /**
     * Get the position of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current position
     */
    @Override
    public PositionVector getCarPosition(int carIndex) {
        return track.getCarPos(carIndex);
    }

    /**
     * Get the velocity of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current velocity
     */
    @Override
    public PositionVector getCarVelocity(int carIndex) {
        return track.getCarVelocity(carIndex);
    }

    /**
     * Return the winner of the game. If the game is still in progress, returns NO_WINNER.
     * @return The winning car's index (zero-based, see getCurrentCar()), or NO_WINNER if the game is still in progress
     */
    @Override
    public int getWinner() {

        List<Car> livingCarList = new ArrayList<>();
        int winnerNr = NO_WINNER;

        for (Car car : track.getCarList()) {

            // Checking whether the round was valid.
            if (car.getCompletedRound() == 1) {
                winnerNr = track.getCarList().indexOf(car);
                livingCarList.add(car);
            }

            // Check if the car is still alive.
            if (!car.isCrashed()) {
                livingCarList.add(car);
            }
        }

        // When only one car is still "alive".
        if (livingCarList.size() == 1) {
            winnerNr = track.getCarList().indexOf(livingCarList.get(FIRST_CAR_IN_LIST));
        }

        return winnerNr;

    }

    /**
     * Execute the next turn for the current active car.
     * <p>This method changes the current car's velocity and checks on the path to the next position,
     * if it crashes (car state to crashed) or passes the finish line in the right direction (set winner state).</p>
     * <p>The steps are as follows</p>
     * <ol>
     *   <li>Accelerate the current car</li>
     *   <li>Calculate the path from current (start) to next (end) position
     *       (see {@link Game#calculatePath(PositionVector, PositionVector)})</li>
     *   <li>Verify for each step what space type it hits:
     *      <ul>
     *          <li>TRACK: check for collision with other car (crashed &amp; don't continue), otherwise do nothing</li>
     *          <li>WALL: car did collide with the wall - crashed &amp; don't continue</li>
     *          <li>FINISH_*: car hits the finish line - wins only if it crosses the line in the correct direction</li>
     *      </ul>
     *   </li>
     *   <li>If the car crashed or wins, set its position to the crash/win coordinates</li>
     *   <li>If the car crashed, also detect if there is only one car remaining, remaining car is the winner</li>
     *   <li>Otherwise move the car to the end position</li>
     * </ol>
     * <p>The calling method must check the winner state and decide how to go on. If the winner is different
     * than {@link Game#NO_WINNER}, or the current car is already marked as crashed the method returns immediately.</p>
     *
     * @param acceleration A Direction containing the current cars acceleration vector (-1,0,1) in x and y direction
     *                     for this turn
     */
    @Override
    public void doCarTurn(Direction acceleration) {
        PositionVector currentPosition;
        PositionVector newPosition;

        currentPosition = currentCar.getPosition();
        currentCar.accelerate(acceleration);
        newPosition = currentCar.nextPosition();

        if (!currentCar.isCrashed()) {
            for (PositionVector position : calculatePath(currentPosition, newPosition)) {
                if (willCarCrash(getCurrentCarIndex(), position)) {
                    currentCar.crash();
                    currentCar.setPosition(position);
                    break;
                }

                crossFinishLine(position, currentPosition, newPosition);
            }
        }

        if (!currentCar.isCrashed()) {
            currentCar.move();
        }

    }

    /**
     * This method checks if the current car crossed the finish line.
     * @param position is the checked position.
     * @param currentPosition is the start position of the car.
     * @param newPosition is the next position of the car.
     */
    private void crossFinishLine(PositionVector position, PositionVector currentPosition, PositionVector newPosition) {
        boolean crossedFinishLine = false;

        if (track.getSpaceType(position) == entryFinishLine) {

            switch (entryFinishLine) {
                case FINISH_DOWN ->{ if (currentPosition.getY() < newPosition.getY()) { crossedFinishLine = true; } }
                case FINISH_LEFT -> { if (currentPosition.getX() > newPosition.getX()) { crossedFinishLine = true; } }
                case FINISH_RIGHT -> { if (currentPosition.getX() < newPosition.getX()) { crossedFinishLine = true; } }
                case FINISH_UP -> { if (currentPosition.getY() > newPosition.getY()) { crossedFinishLine = true; } }
                default -> crossedFinishLine = false;
            }

            if (crossedFinishLine) {
                currentCar.addCompletedRound(1);
            } else {
                currentCar.addCompletedRound(-1);
            }

        }
    }

    /**
     * Switches to the next car who is still in the game. Skips crashed cars.
     */
    @Override
    public void switchToNextActiveCar() {

        int nextCar = getCurrentCarIndex() + 1;
        boolean isNextCar = false;

        while (!isNextCar) {
            if (nextCar == track.getCarCount()) {
                nextCar = 0;
            }

            if (!track.getCarList().get(nextCar).isCrashed()) {
                currentCar = track.getCarList().get(nextCar);
                isNextCar = true;
            }

            nextCar++;
        }

    }

    /**
     * Returns all of the grid positions in the path between two positions, for use in determining line of sight.
     * Determine the 'pixels/positions' on a raster/grid using Bresenham's line algorithm.
     * (https://de.wikipedia.org/wiki/Bresenham-Algorithmus)
     * Basic steps are
     * - Detect which axis of the distance vector is longer (faster movement)
     * - for each pixel on the 'faster' axis calculate the position on the 'slower' axis.
     * Direction of the movement has to correctly considered
     * @param startPosition Starting position as a PositionVector
     * @param endPosition Ending position as a PositionVector
     * @return Intervening grid positions as a List of PositionVector's, including the starting and ending positions.
     */
    @Override
    public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {

        List<PositionVector> vectorList = new ArrayList<>();

        // Use Bresenham's algorithm to determine positions.
        // Relative Distance (x & y axis) between end- and starting position
        int diffX = endPosition.getX() - startPosition.getX();
        int diffY = endPosition.getY() - startPosition.getY();

        // Absolute distance (x & y axis) between end- and starting position
        int distX = Math.abs(diffX);
        int distY = Math.abs(diffY);

        // Direction of vector on x & y axis (-1: to left/down, 0: none, +1 : to right/up)
        int dirX = Integer.signum(diffX);
        int dirY = Integer.signum(diffY);

        // Determine which axis is the fast direction and set parallel/diagonal step values
        int parallelStepX, parallelStepY;
        int diagonalStepX, diagonalStepY;
        int distanceSlowAxis, distanceFastAxis;

        if (distX > distY) {
            // x axis is the 'fast' direction
            parallelStepX = dirX; parallelStepY = 0; // parallel step only moves in x direction
            diagonalStepX = dirX; diagonalStepY = dirY; // diagonal step moves in both directions
            distanceSlowAxis = distY;
            distanceFastAxis = distX;
        } else {
            // y axis is the 'fast' direction
            parallelStepX = 0; parallelStepY = dirY; // parallel step only moves in y direction
            diagonalStepX = dirX; diagonalStepY = dirY; // diagonal step moves in both directions
            distanceSlowAxis = distX;
            distanceFastAxis = distY;
        }

        // initialize path loop
        int x = startPosition.getX();
        int y = startPosition.getY();
        int error = distanceFastAxis/2; // set to half distance to get a good starting value

        // path loop:
        // by default step parallel to the fast axis.
        // if error value gets negative take a diagonal step
        // this happens approximately every (distanceFastAxis / distanceSlowAxis) steps
        for (int step = 0; step < distanceFastAxis; step++) {
            error -= distanceSlowAxis; // update error value

            if (error < 0) {
                error += distanceFastAxis; // correct error value to be positive again

                // step into slow direction; diagonal step
                x += diagonalStepX;
                y += diagonalStepY;

            } else {
                // step into fast direction; parallel step
                x += parallelStepX;
                y += parallelStepY;
            }
            // Print position (add to the list)
            vectorList.add(new PositionVector(x, y));
        }
        return vectorList;
    }

    /**
     * Does indicate if a car would have a crash with a WALL space or another car at the given position.
     * @param carIndex The zero-based carIndex number
     * @param position A PositionVector of the possible crash position
     * @return A boolean indicator if the car would crash with a WALL or another car.
     */
    @Override
    public boolean willCarCrash(int carIndex, PositionVector position) {
        boolean willCrash = track.getSpaceType(position) == SpaceType.WALL;

        for (Car car : track.getCarList()) {
            if (car.getPosition().equals(position) && track.getCarList().indexOf(car) != carIndex) {
                willCrash = true;
            }
        }
        return willCrash;
    }

}
