package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification.*;
import ch.zhaw.pm2.racetrack.given.TrackSpecification;

import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Arrays;

/**
 * This class represents the racetrack board.
 *
 * <p>The racetrack board consists of a rectangular grid of 'width' columns and 'height' rows.
 * The zero point of he grid is at the top left. The x-axis points to the right and the y-axis points downwards.</p>
 * <p>Positions on the track grid are specified using {@link PositionVector} objects. These are vectors containing an
 * x/y coordinate pair, pointing from the zero-point (top-left) to the addressed space in the grid.</p>
 *
 * <p>Each position in the grid represents a space which can hold an enum object of type {@link Config.SpaceType}.<br>
 * Possible Space types are:
 * <ul>
 *  <li>WALL : road boundary or off track space</li>
 *  <li>TRACK: road or open track space</li>
 *  <li>FINISH_LEFT, FINISH_RIGHT, FINISH_UP, FINISH_DOWN :  finish line spaces which have to be crossed
 *      in the indicated direction to winn the race.</li>
 * </ul>
 * <p>Beside the board the track contains the list of cars, with their current state (position, velocity, crashed,...)</p>
 *
 * <p>At initialization the track grid data is read from the given track file. The track data must be a
 * rectangular block of text. Empty lines at the start are ignored. Processing stops at the first empty line
 * following a non-empty line, or at the end of the file.</p>
 * <p>Characters in the line represent SpaceTypes. The mapping of the Characters is as follows:</p>
 * <ul>
 *   <li>WALL : '#'</li>
 *   <li>TRACK: ' '</li>
 *   <li>FINISH_LEFT : '&lt;'</li>
 *   <li>FINISH_RIGHT: '&gt;'</li>
 *   <li>FINISH_UP   : '^;'</li>
 *   <li>FINISH_DOWN: 'v'</li>
 *   <li>Any other character indicates the starting position of a car.<br>
 *       The character acts as the id for the car and must be unique.<br>
 *       There are 1 to {@link Config#MAX_CARS} allowed. </li>
 * </ul>
 *
 * <p>All lines must have the same length, used to initialize the grid width).
 * Beginning empty lines are skipped.
 * The the tracks ends with the first empty line or the file end.<br>
 * An {@link InvalidTrackFormatException} is thrown, if
 * <ul>
 *   <li>not all track lines have the same length</li>
 *   <li>the file contains no track lines (grid height is 0)</li>
 *   <li>the file contains more than {@link Config#MAX_CARS} cars</li>
 * </ul>
 *
 * <p>The Track can return a String representing the current state of the race (including car positons)</p>
 */
public class Track implements TrackSpecification {

    public static final char CRASH_INDICATOR = 'X';
    private Map<PositionVector, Character> trackMap = new HashMap<>();
    private List<Car> carList = new ArrayList<>();

    /**
     * Initialize a Track from the given track file.
     *
     * @param trackFile Reference to a file containing the track data
     * @throws FileNotFoundException       if the given track file could not be found
     * @throws InvalidTrackFormatException if the track file contains invalid data (no tracklines, ...)
     */
    public Track(File trackFile) throws FileNotFoundException, InvalidTrackFormatException {
        try {
            initMap(trackFile);
        } catch (IOException exception) {
            throw new InvalidTrackFormatException("IOException");
        }
    }

    /**
     * This method initialize the racetrack in a hashmap.
     *
     * @param trackFile is the desired track.
     * @throws IOException                 when corrupted or some error occurred during reading the data
     * @throws InvalidTrackFormatException when the track is not Valid for the game
     */
    private void initMap(File trackFile) throws IOException, InvalidTrackFormatException {
        if (!trackFile.exists()) {
            throw new FileNotFoundException();
        }

        createMap(scanFile(trackFile));
        createCarsForGame(getCarsOfTrack());
        removeCarsFromMap();

        try {
            checkValidTrack();
        } catch (NullPointerException exception) {
            throw new InvalidTrackFormatException("NullPointerException");
        }
    }

    /**
     * This method reads the trackFile and return a list.
     *
     * @param trackFile is the chosen track
     * @return returns every line of the file as a String in a list.
     * @throws IOException when corrupted or some error occurred during reading the data
     */
    private List<String> scanFile(File trackFile) throws IOException {
        List<String> trackList = new ArrayList<>();
        Scanner scan = new Scanner(trackFile, StandardCharsets.UTF_8);

        while (scan.hasNextLine()) {
            trackList.add(scan.nextLine());
        }
        scan.close();
        return trackList;
    }

    /**
     * This method creates a hashmap of the Arraylist, which contains the track.
     * The hashmap contains the column & line, which will combined in a key.
     * The character of the SpaceType is the value.
     * @param trackList contains the trackFile
     */
    private void createMap(List<String> trackList) {
        String lineText;
        for (int line = 0; line < trackList.size(); line++) {
            lineText = trackList.get(line);
            for (int column = 0; column < lineText.length(); column++) {
                trackMap.put(new PositionVector(column, line), lineText.charAt(column));
            }
        }
    }

    /**
     * This method creates the cars and put them in the carList.
     *
     * @param carInfos is a map with the informations for the creation of the cars.
     */
    private void createCarsForGame(Map<Character, PositionVector> carInfos) {
        for (Map.Entry<Character, PositionVector> entry : carInfos.entrySet()) {
            carList.add(new Car(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * This method check, if the racetrack is valid.
     *
     * @throws InvalidTrackFormatException if the track ist not valid.
     */
    private void checkValidTrack() throws InvalidTrackFormatException {
        int localMaxX;

        // Case 1: Check if all lines have the same length.
        for (int row = 0; row < getMaxMapY(); row++) {
            localMaxX = 0;
            for (int column = 0; column <= getMaxMapX(); column++) {
                localMaxX = column;
            }

            if (localMaxX != getMaxMapX()) {
                throw new InvalidTrackFormatException("At least one line does not correspond to the remaining lines.");
            }
        }
        checkBorderOfTrack();

        // Case 2: Check if count of Cars is more than allowed.
        if (getCarCount() > Config.MAX_CARS) {
            throw new InvalidTrackFormatException("The track contains " + getCarsOfTrack().size() + " cars, although the maximum number of cars is " + Config.MAX_CARS + ".");
        }

        // Case 3: Check if all the Finish-lines are the same.
        for (Map.Entry<PositionVector, Character> entry : trackMap.entrySet()) {
            if (entry.getValue() != getEntryFinishLine().getValue() &&
                (Arrays.asList(FINISH_DOWN.getValue(), FINISH_LEFT.getValue(), FINISH_UP.getValue(), FINISH_RIGHT.getValue()).contains(entry.getValue()))) {
                throw new InvalidTrackFormatException("The track contains a contradictory finish line.");
            }
        }
    }

    /**
     * This method checks the border of the track.
     *
     * @throws InvalidTrackFormatException is thrown if the border is not correct.
     */
    private void checkBorderOfTrack() throws InvalidTrackFormatException {
        // Case 1: Check if the first line of the track does only contain Wall.
        for (int index = 0; index <= getMaxMapX(); index++) {
            if (trackMap.get(new PositionVector(index, 0)) != "#".charAt(0)) {
                throw new InvalidTrackFormatException("The first line of the track does not only consist of # (Problem at the " + index + ". position).");
            }
        }

        // Case 2: Check if the last line of the track does only contain Wall.
        for (int index = 0; index <= getMaxMapX(); index++) {
            if (trackMap.get(new PositionVector(index, getMaxMapY())) != "#".charAt(0)) {
                throw new InvalidTrackFormatException("Last line of the track does not only consist of # (Problem at the " + index + ". position).");
            }
        }

        // Case 3: Check if the first row of the track does only contain Wall.
        for (int index = 0; index <= getMaxMapY(); index++) {
            if (trackMap.get(new PositionVector(0, index)) != "#".charAt(0)) {
                throw new InvalidTrackFormatException("First character per line of the track is not only # (Problem with line " + index + ").");
            }
        }

        // Case 4: Check if the last row of the track does only contain Wall.
        for (int index = 0; index <= getMaxMapY(); index++) {
            if (trackMap.get(new PositionVector(getMaxMapX(), index)) != "#".charAt(0)) {
                throw new InvalidTrackFormatException("Last character per line of the track does not only consist of #. (Problem with line " + index + ").");
            }
        }
    }

    /**
     * This method remove the Cars from the Hashmap, where the racetrack is initialized.
     */
    private void removeCarsFromMap() {
        for (int indexLine = 0; indexLine < getMaxMapY(); indexLine++) {
            for (int indexColumn = 0; indexColumn < getMaxMapX(); indexColumn++) {
                if (!(trackMap.get(new PositionVector(indexColumn, indexLine)).toString().matches("[#^>v<\s]"))) {
                    trackMap.put(new PositionVector(indexColumn, indexLine), ' ');
                }
            }
        }
    }

    /**
     * This method get the cars of a Hashmap, where the racetrack is initialized and put it in a new Hashmap.
     *
     * @return carMap, where only cars were added.
     */
    public Map<Character, PositionVector> getCarsOfTrack() {
        Map<Character, PositionVector> carMap = new HashMap<>();
        for (int indexLine = 0; indexLine < getMaxMapY(); indexLine++) {
            for (int indexColumn = 0; indexColumn < getMaxMapX(); indexColumn++) {
                if (!(trackMap.get(new PositionVector(indexColumn, indexLine)).toString().matches("[#^>v<\s]"))) {
                    carMap.put(trackMap.get(new PositionVector(indexColumn, indexLine)), new PositionVector(indexColumn, indexLine));
                }
            }
        }
        return carMap;
    }

    /**
     * Return the type of space at the given position.
     * If the location is outside the track bounds, it is considered a wall.
     *
     * @param position The coordinates of the position to examine
     * @return The type of track position at the given location
     */
    @Override
    public SpaceType getSpaceType(PositionVector position) {
        return switch (trackMap.get(position)) {
            case 'v' -> FINISH_DOWN;
            case '<' -> FINISH_LEFT;
            case '>' -> FINISH_RIGHT;
            case '^' -> FINISH_UP;
            case '#' -> WALL;
            default -> TRACK;
        };
    }

    /**
     * Return a list with cars.
     *
     * @return all cars.
     */
    public List<Car> getCarList() {
        return carList;
    }

    /**
     * Return the number of cars.
     *
     * @return Number of cars
     */
    @Override
    public int getCarCount() {
        return carList.size();
    }

    /**
     * Get instance of specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return The car instance at the given index
     */
    @Override
    public Car getCar(int carIndex) {
        return carList.get(carIndex);
    }

    /**
     * Get the id of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A char containing the id of the car
     */
    @Override
    public char getCarId(int carIndex) {
        return getCar(carIndex).getCarID();
    }

    /**
     * Get the position of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current position
     */
    @Override
    public PositionVector getCarPos(int carIndex) {
        return carList.get(carIndex).getPosition();
    }

    /**
     * Get the velocity of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current velocity
     */
    @Override
    public PositionVector getCarVelocity(int carIndex) {
        return carList.get(carIndex).getCurrentVelocity();
    }

    /**
     * Gets character at the given position.
     * If there is a crashed car at the position, {@link #CRASH_INDICATOR} is returned.
     *
     * @param y            position Y-value
     * @param x            position X-vlaue
     * @param currentSpace char to return if no car is at position (x,y)
     * @return character representing position (x,y) on the track
     */
    @Override
    public char getCharAtPosition(int y, int x, SpaceType currentSpace) {
        char returnChar = ' ';
        for (int index = 0; index < carList.size(); index++) {
            if (trackMap.get(new PositionVector(x, y)).equals(getCarId(index))) {
                if (getCar(index).isCrashed()) {
                    returnChar = CRASH_INDICATOR;
                } else {
                    returnChar = getCarId(index);
                }
            } else {
                returnChar = currentSpace.getValue();
            }
        }
        return returnChar;
    }

    /**
     * Return a String representation of the track, including the car locations.
     *
     * @return A String representation of the track
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        boolean carPrinted;

        int width = getMaxMapX();
        int high = getMaxMapY();

        for (int y = 0; y <= high; y++) {
            for (int x = 0; x <= width; x++) {

                carPrinted = false;

                for (Car car : carList) {
                    if (!createCrashCarList(carList).contains(car)) {
                        if (car.getPosition().getX() == x && car.getPosition().getY() == y) {
                            if (car.isCrashed()) {
                                output.append(CRASH_INDICATOR);
                            } else {
                                output.append(car.getCarID());
                            }

                            carPrinted = true;
                        }
                    }
                }
                if (!carPrinted) {
                    output.append(trackMap.get(new PositionVector(x, y)));
                }
                if (x == width) {
                    output.append("\n");
                }
            }
        }
        return output.toString();
    }

    /**
     * This method creates a list of cars which are crashed.
     *
     * @param carList is the list of cars which are checked if they are crashed.
     * @return crash car list
     */
    private List<Car> createCrashCarList(List<Car> carList) {
        List<Car> crashCarList = new ArrayList<>();

        for (Car car : carList) {
            for (Car otherCar : carList) {
                if (car != otherCar) {
                    if (car.getPosition().equals(otherCar.getPosition()) && car.isCrashed()) {
                        crashCarList.add(car);
                    }
                }
            }
        }
        return crashCarList;
    }

    /**
     * Get the max X of the trackMap.
     *
     * @return the max X as int.
     */
    private int getMaxMapX() {
        int maxX = 0;

        for (Map.Entry<PositionVector, Character> entry : trackMap.entrySet()) {
            if (entry.getKey().getX() > maxX) {
                maxX = entry.getKey().getX();
            }
        }
        return maxX;
    }

    /**
     * Get the max Y of the trackMap.
     *
     * @return the max Y as int.
     */
    private int getMaxMapY() {
        int maxY = 0;

        for (Map.Entry<PositionVector, Character> entry : trackMap.entrySet()) {
            if (entry.getKey().getY() > maxY) {
                maxY = entry.getKey().getY();
            }
        }
        return maxY;
    }

    /**
     * Get the direction of the finish line.
     *
     * @return SpaceType of the finish line.
     */
    public SpaceType getEntryFinishLine() {
        String trackAsString = trackMap.toString();
        SpaceType spaceType = null;
        char type;

        for (int i = 0; i < trackAsString.length(); i++) {
            type = trackAsString.charAt(i);

            if (type == FINISH_UP.getValue()) {
                spaceType = FINISH_UP;
            } else if (type == FINISH_DOWN.getValue()) {
                spaceType = FINISH_DOWN;
            } else if (type == FINISH_LEFT.getValue()) {
                spaceType = FINISH_LEFT;
            } else if (type == FINISH_RIGHT.getValue()) {
                spaceType = FINISH_RIGHT;
            }

            if (spaceType != null) {
                break;
            }
        }
        return spaceType;
    }
}

